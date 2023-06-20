package com.devs.imgur.data.source.network

import com.devs.imgur.data.source.network.dto.GalleryDto
import retrofit2.http.*


interface ImgurApiServiceImp : ImgurApiService {

    @GET("gallery/search/top/week/1")
    override suspend fun searchImage(
        @Query("q") query: String
    ): ApiResponse<GalleryDto>

}