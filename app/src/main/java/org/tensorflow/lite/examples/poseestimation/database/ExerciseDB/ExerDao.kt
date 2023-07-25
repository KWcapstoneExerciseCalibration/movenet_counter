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

    @Query("SELECT note FROM table_exercise WHERE date=:date")
    suspend fun getNote(date: String): String

    @Query("SELECT exercise FROM table_exercise WHERE date=:date")
    suspend fun getExer(date: String): String

    @Query("SELECT score FROM table_exercise WHERE date=:date")
    suspend fun getScore(date: String): Int

    @Query("SELECT count FROM table_exercise WHERE date=:date")
    suspend fun getCount(date: String): Int

    @Query("SELECT AVG(score) FROM table_exercise")
    suspend fun moAvg(): Double
}