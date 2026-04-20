package com.ejabi.data.mapper
import com.ejabi.data.remote.DTo.CategoryDto
import com.ejabi.domain.model.Category

fun CategoryDto.toDomain(): Category {
    return Category(
        slug = slug,
        name = name,
        url = url
    )
}