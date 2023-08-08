package org.tensorflow.lite.examples.poseestimation.database.LengthDB

import androidx.room.*

@Dao
interface LengthDao {
    @Insert
    suspend fun create(lengthSchema: LengthSchema): Long

    @Update
    suspend fun update(lengthSchema: LengthSchema)

    @Delete
    suspend fun delete(lengthSchema: LengthSchema)

    @Query("SELECT * FROM table_length")
    suspend fun readAll(): List<LengthSchema>

    @Query("DELETE FROM table_length")
    suspend fun deleteAll()

}