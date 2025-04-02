package com.yahorhous.bkctest.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.yahorhous.bkctest.domain.model.Resource
import com.yahorhous.bkctest.domain.model.cart.Purchase
import com.yahorhous.bkctest.domain.model.cart.PurchaseItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


interface AuthRepository {
    suspend fun signUp(name: String, email: String, password: String): Resource<Unit>
    suspend fun signIn(email: String, password: String): Resource<Unit>
    fun getCurrentUser(): Flow<FirebaseUser?>
    fun logout()
    fun getPurchaseHistory(userId: String): Flow<Resource<List<Purchase>>>
    fun getUserData(userId: String): Flow<Resource<String>>
}

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun signUp(name: String, email: String, password: String): Resource<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            saveUserName(name)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    private suspend fun saveUserName(name: String) {
        val user = auth.currentUser ?: return
        val userData = hashMapOf(
            "name" to name,
            "email" to user.email
        )
        firestore.collection("users").document(user.uid).set(userData).await()
    }

    override suspend fun signIn(email: String, password: String): Resource<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    override fun getCurrentUser(): Flow<FirebaseUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override fun logout() = auth.signOut()

    override fun getPurchaseHistory(userId: String): Flow<Resource<List<Purchase>>> = callbackFlow {
        val listenerRegistration = firestore.collection("purchases")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Ошибка загрузки истории"))
                    return@addSnapshotListener
                }

                val purchases = snapshot?.documents?.mapNotNull { doc ->
                    val items = doc.get("items") as? List<Map<String, Any>> ?: emptyList()
                    Purchase(
                        userId = userId,
                        timestamp = doc.getLong("timestamp") ?: 0,
                        items = items.map { item ->
                            PurchaseItem(
                                productName = item["name"] as? String ?: "",
                                quantity = (item["quantity"] as? Double) ?: 0.0,
                                price = (item["price"] as? Long)?.toInt() ?: 0,
                                barcode = item["barcode"] as? String ?: "",
                                unit = item["unit"] as? String ?: ""
                            )
                        },
                        total = (doc.getLong("total") ?: 0).toInt()
                    )
                } ?: emptyList()

                trySend(Resource.Success(purchases))
            }
        awaitClose { listenerRegistration.remove() }
    }.onStart {
        emit(Resource.Loading())
    }

    override fun getUserData(userId: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            val doc = firestore.collection("users").document(userId).get().await()
            emit(Resource.Success(doc.getString("name") ?: ""))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to load user"))
        }
    }


}