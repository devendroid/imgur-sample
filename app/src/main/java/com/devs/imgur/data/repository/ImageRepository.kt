package com.devs.imgur.data.repository

import com.devs.imgur.data.repository.modal.Gallery
import com.devs.imgur.data.repository.resource.Resource

/**
 * Interface to the data layer.
 */
interface ImageRepository {

    suspend fun searchImage(
        query: String
    ): Resource<Gallery>
}
