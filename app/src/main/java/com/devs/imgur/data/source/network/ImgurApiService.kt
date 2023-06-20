package com.devs.imgur.data.source.network

import com.devs.imgur.data.source.network.dto.GalleryDto

/**
 * Main entry point for accessing data from the network.
 *
 */
interface ImgurApiService {

    suspend fun searchImage(
        query: String
    ): ApiResponse<GalleryDto>
}
