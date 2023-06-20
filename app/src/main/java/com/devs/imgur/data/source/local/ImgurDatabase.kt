package com.devs.imgur.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devs.imgur.data.source.local.entity.ImageEntity

/**
 * The Room Database that contains the Image table.
 *
 * Note that exportSchema should be true in production databases.
 */
@Database(entities = [ImageEntity::class], version = 1, exportSchema = false)
abstract class ImgurDatabase : RoomDatabase() {

    abstract fun imageDao(): ImageDao
}
