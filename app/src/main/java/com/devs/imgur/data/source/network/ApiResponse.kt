package com.devs.imgur.data.source.network

/**
 * Common class used by API responses.
 * @param <T> the type of the response object
</T> */
sealed class ApiResponse<out T>

/**
 * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
 */
object ApiEmptyResponse : ApiResponse<Nothing>()

data class ApiSuccessResponse<T>(val body: T) : ApiResponse<T>()

data class ApiErrorResponse<T>(val errorMessage: String) : ApiResponse<T>()

data class ApiNetworkError<T>(val errorMessage: String) : ApiResponse<T>()

object ApiTimeoutError : ApiResponse<Nothing>()
