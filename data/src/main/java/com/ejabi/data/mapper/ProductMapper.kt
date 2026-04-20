package com.ejabi.data.mapper

import com.ejabi.data.remote.DTo.ProductDto
import com.ejabi.domain.model.Product

fun ProductDto.toDomain(): Product {
    return Product(
        id = id,
        title = title,
        description = description,
        category = category,
        price = price,
        thumbnail = thumbnail,
        images = images,
        rating = rating
    )
}