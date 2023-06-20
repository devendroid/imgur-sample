package com.devs.imgur.data.source.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.devs.imgur.data.source.local.entity.ImageEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the image table.
 */
@Dao
interface ImageDao {

    /**
     * Observes list of images.
     *
     * @return all images.
     */
    @Query("SELECT * FROM image")
    fun observeAll(): Flow<List<ImageEntity>>

    /**
     * Observes a single image.
     *
     * @param imageId the image id.
     * @return the image with imageId.
     */
    @Query("SELECT * FROM image WHERE id = :imageId")
    fun observeById(imageId: String): Flow<ImageEntity>

    /**
     * Select all images from the images table.
     *
     * @return all images.
     */
    @Query("SELECT * FROM image")
    suspend fun getAll(): List<ImageEntity>

    /**
     * Select a image by id.
     *
     * @param imageId the image id.
     * @return the image with imageId.
     */
    @Query("SELECT * FROM image WHERE id = :imageId")
    suspend fun getById(imageId: String): ImageEntity?

    /**
     * Insert or update a image in the database. If a image already exists, replace it.
     *
     * @param image the image to be inserted or updated.
     */
    @Upsert
    suspend fun upsert(image: ImageEntity)

    /**
     * Insert or update images in the database. If a image already exists, replace it.
     *
     * @param images the images to be inserted or updated.
     */
    @Upsert
    suspend fun upsertAll(images: List<ImageEntity>)

    /**
     * Update the complete status of a image
     *
     * @param imageId id of the image
     * @param completed status to be updated
     */
    @Query("UPDATE image SET isCompleted = :completed WHERE id = :imageId")
    suspend fun updateCompleted(imageId: String, completed: Boolean)

    /**
     * Delete a image by id.
     *
     * @return the number of images deleted. This should always be 1.
     */
    @Query("DELETE FROM image WHERE id = :imageId")
    suspend fun deleteById(imageId: String): Int

    /**
     * Delete all images.
     */
    @Query("DELETE FROM image")
    suspend fun deleteAll()

    /**
     * Delete all completed images from the table.
     *
     * @return the number of images deleted.
     */
    @Query("DELETE FROM image WHERE isCompleted = 1")
    suspend fun deleteCompleted(): Int
}
