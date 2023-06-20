package com.devs.imgur.di

import android.content.Context
import androidx.room.Room
import com.devs.imgur.data.repository.ImageRepository
import com.devs.imgur.data.repository.ImageRepositoryImp
import com.devs.imgur.data.source.local.ImageDao
import com.devs.imgur.data.source.local.ImgurDatabase
import com.devs.imgur.data.source.network.ImgurApiService
import com.devs.imgur.data.source.network.ImgurApiServiceImp
import com.devs.imgur.data.source.network.di.ApiServiceFactory
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindImageRepository(repository: ImageRepositoryImp): ImageRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun provideImgurApiService(@ApplicationContext context: Context): ImgurApiService {
        return ApiServiceFactory.createApiService(context)
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().build()
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): ImgurDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            ImgurDatabase::class.java,
            "Imgur.db"
        ).build()
    }

    @Provides
    fun provideImageDao(database: ImgurDatabase): ImageDao = database.imageDao()
}
