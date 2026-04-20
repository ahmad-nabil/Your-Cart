package com.ejabi.data.Repository
import com.ejabi.data.mapper.toDomain
import com.ejabi.data.remote.api.ProductApi
import com.ejabi.domain.model.Product
import com.ejabi.domain.repository.ProductRepository
import javax.inject.Inject


class ProductRepositoryImpl @Inject constructor(
    private val productApi: ProductApi
) : ProductRepository {

    override suspend fun getProducts(): List<Product> {
        return productApi.getProducts().products.map { it.toDomain() }
    }
}