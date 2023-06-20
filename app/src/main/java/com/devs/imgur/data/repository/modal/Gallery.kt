package com.devs.imgur.data.repository.modal

/**
 * External model exposed to other layers in the architecture.
 *
 * See ModelMappingExt.kt for mapping functions used to convert this model to other
 * models.
 */
data class Gallery (
    var data: List<Data>?,
    )

data class Data (
    val images: List<Image>?,
    val imagesCount: Int?,
    val title: String,
    val datetime: Long,
    )

data class Image (
    val link: String,
    val animated: Boolean,
)

