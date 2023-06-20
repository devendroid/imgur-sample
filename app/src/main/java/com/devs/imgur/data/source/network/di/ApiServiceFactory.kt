package com.devs.imgur.data.source.network.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.devs.imgur.BuildConfig
import com.devs.imgur.ImgurApplication
import com.devs.imgur.data.source.network.ImgurApiServiceImp
import com.devs.imgur.data.source.network.adapters.ApiResponseAdapterFactory
import com.squareup.moshi.Moshi
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object ApiServiceFactory {

    var forceUpdate = false
    val cacheSize = (5 * 1024 * 1024).toLong()

    fun createApiService(
        context: Context,
        moshi: Moshi = Moshi.Builder().build()): ImgurApiServiceImp {
        val myCache = Cache(context.cacheDir, cacheSize)
        val httpClient = lazy {
            OkHttpClient.Builder().apply {
                if (ImgurApplication.LOGGER_ENABLE) {
                    val loggingInterceptor = HttpLoggingInterceptor()
                    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                    cache(myCache)
                    addInterceptor(loggingInterceptor)
                    addInterceptor(offlineInterceptor(context))
                    addInterceptor(oAuthInterceptor(BuildConfig.IMGUR_CLIENT_ID))
                    addNetworkInterceptor(networkInterceptor())
                }
            }.build()
        }

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addCallAdapterFactory(ApiResponseAdapterFactory())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .callFactory { request ->
                // OkHttpClient will be created lazily on the first network call and not at startup
                httpClient.value.newCall(request)
            }
            .build()
            .create<ImgurApiServiceImp>()
    }

    private fun offlineInterceptor(context: Context): Interceptor {
        return Interceptor { chain ->
            var request: Request = chain.request()

            if (hasNetwork(context).not()) {
                val cacheControl = CacheControl.Builder()
                    .maxStale(24, TimeUnit.HOURS)
                    .build()
                request = request.newBuilder()
                    .addHeader("language", "es")
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .cacheControl(cacheControl)
                    .build()
            } else if (forceUpdate) {
                request = request.newBuilder()
                    .addHeader("language", "es")
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build()
                forceUpdate = false
            }
            chain.proceed(request)
        }
    }

    private fun oAuthInterceptor(accessToken: String): Interceptor {
        return  Interceptor{ chain ->
            var request = chain.request()
            request = request.newBuilder().header("Authorization", "Client-ID $accessToken").build()
            chain.proceed(request)
        }
    }

    private fun networkInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response: Response = chain.proceed(chain.request())
            val cacheControl = CacheControl.Builder()
                .maxStale(24, TimeUnit.HOURS)
                .build()
            response.newBuilder()
                .addHeader("language", "es")
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", cacheControl.toString())
                .build()
        }
    }


    private fun hasNetwork(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }
        return result
    }
}
