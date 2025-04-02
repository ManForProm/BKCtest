package com.yahorhous.bkctest.data.repository

import com.yahorhous.bkctest.domain.model.product.Product
import com.yahorhous.bkctest.data.local.dao.BarcodeDao
import com.yahorhous.bkctest.data.local.dao.PackDao
import com.yahorhous.bkctest.data.local.dao.PackPriceDao
import com.yahorhous.bkctest.data.local.dao.UnitDao
import com.yahorhous.bkctest.data.local.entity.PackPriceEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
}

class ProductRepositoryImpl @Inject constructor(
    private val packDao: PackDao,
    private val packPriceDao: PackPriceDao,
    private val unitDao: UnitDao,
    private val barcodeDao: BarcodeDao,
    private val dispatcher: CoroutineDispatcher
) : ProductRepository {

    override fun getAllProducts(): Flow<List<Product>> {
        return packDao.getAllPacks().combine(unitDao.getAllUnits()) { packs, units ->
            packs.map { pack ->
                val unit = units.firstOrNull { it.id == pack.unitId }
                val priceFlow = packPriceDao.getPricesForPack(pack.id)
                priceFlow.map { prices ->
                    val price = prices.firstOrNull() ?: PackPriceEntity(0, pack.id, 0, 0)
                    Product(
                        id = pack.id,
                        name = pack.name,
                        type = pack.type,
                        unit = unit?.name ?: "",
                        price = price.price,
                        bonus = price.bonus,
                        quant = pack.quant
                    )
                }
            }
        }.flatMapLatest { productFlows ->
            if (productFlows.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(productFlows) { it.toList() }
            }
        }.flowOn(dispatcher)
    }
}
