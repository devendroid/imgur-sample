package com.devs.imgur.data.repository

import com.devs.imgur.data.repository.modal.Data
import com.devs.imgur.data.repository.modal.Gallery
import com.devs.imgur.data.repository.modal.Image
import com.devs.imgur.data.source.network.dto.DataDto
import com.devs.imgur.data.source.network.dto.GalleryDto
import com.devs.imgur.data.source.network.dto.ImageDto

/**
 * Data model mapping extension functions. There are three model types:
 *
 * - Gallery: External model exposed to other layers in the architecture.
 * Obtained using `toExternal`.
 *
 * - GalleryDto: Internal model used to represent a image from the network. Obtained using
 * `toGalleryDto`.
 *
 * - GalleryEntity: Internal model used to represent a image stored locally in a database. Obtained
 * using `toGalleryEntity`.
 *
 */

// Network to External
fun GalleryDto.toExternal() = Gallery(
    data = arrayListOf<Data>().apply {
        data?.forEach {
            add(it.toData())
        }
    }
)

fun DataDto.toData() = Data(
    images = arrayListOf<Image>().apply {
        images?.forEach {
            add(it.toImage())
        }
    },
    title = title,
    datetime  = datetime,
    imagesCount = imagesCount
)

fun ImageDto.toImage() = Image(
    link = link,
    animated  = animated,
)
