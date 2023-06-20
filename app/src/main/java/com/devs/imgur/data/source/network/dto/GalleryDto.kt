package com.devs.imgur.data.source.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Internal model used to represent a model obtained from the network. This is used inside the data
 * layer only.
 *
 * See ModelMappingExt.kt for mapping functions used to convert this model to other
 * models.
 */
@JsonClass(generateAdapter = true)
data class GalleryDto (
    var data: List<DataDto>?,
)

@JsonClass(generateAdapter = true)
data class DataDto (
    val images: List<ImageDto>?,
    @Json(name = "images_count")
    val imagesCount: Int?,
    val title: String,
    val datetime: Long,
)

@JsonClass(generateAdapter = true)
data class ImageDto (
    val link: String,
    val animated: Boolean,
)
