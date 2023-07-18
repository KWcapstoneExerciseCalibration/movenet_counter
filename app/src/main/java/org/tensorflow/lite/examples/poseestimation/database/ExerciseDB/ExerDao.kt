package org.tensorflow.lite.examples.poseestimation.database.ExerciseDB

import androidx.room.*
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerSchema

@Dao
interface ExerDao {
    @Insert
    suspend fun create(exerSchema: ExerSchema): Long

    @Update
    suspend fun update(exerSchema: ExerSchema)

    @Delete
    suspend fun delete(exerSchema: ExerSchema)

    @Query("SELECT * FROM table_exercise")
    suspend fun readAll(): List<ExerSchema>

    @Query("DELETE FROM table_exercise")
    suspend fun deleteAll()
}