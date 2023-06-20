package com.devs.imgur.data.repository

import com.devs.imgur.data.repository.modal.Gallery
import com.devs.imgur.data.repository.resource.NetworkError
import com.devs.imgur.data.repository.resource.Resource
import com.devs.imgur.data.source.local.ImageDao
import com.devs.imgur.data.source.network.*
import com.devs.imgur.di.ApplicationScope
import com.devs.imgur.di.DefaultDispatcher
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

/**
 * Default implementation of [ImageRepository]. Single entry point for managing images' data.
 *
 * @param networkDataSource - The network data source
 * @param localDataSource - The local data source
 * @param dispatcher - The dispatcher to be used for long running or complex operations, such as ID
 * generation or mapping many models.
 * @param scope - The coroutine scope used for deferred jobs where the result isn't important, such
 * as sending data to the network.
 */
@Singleton
class ImageRepositoryImp @Inject constructor(
    private val networkDataSource: ImgurApiService,
    private val localDataSource: ImageDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) : ImageRepository {

    override suspend fun searchImage(
        query: String
    ): Resource<Gallery> {
        return when (val apiResponse =
            networkDataSource.searchImage(query)) {
            is ApiSuccessResponse -> Resource.success(apiResponse.body.toExternal())
            is ApiTimeoutError -> Resource.error(null, networkError = NetworkError.TIMEOUT)
            is ApiNetworkError,
            is ApiEmptyResponse -> Resource.error(null)
            is ApiErrorResponse -> {
                Resource.error(null, apiResponse.errorMessage)
            }
        }
    }
}

