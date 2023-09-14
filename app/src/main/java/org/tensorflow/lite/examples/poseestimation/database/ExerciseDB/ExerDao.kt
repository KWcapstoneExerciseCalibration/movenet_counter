package org.tensorflow.lite.examples.poseestimation.database.ExerciseDB

import androidx.room.*

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

    @Query("SELECT ifnull(AVG(score),0) FROM table_exercise WHERE date=:date")
    suspend fun getDateScore(date: String): Int

    @Query("SELECT ifnull(AVG(score),0) FROM table_exercise WHERE date >= '2023-08-01' and date <= '2023-08-30'")
    suspend fun moAvg8(): Double

    @Query("SELECT ifnull(AVG(score),0) FROM table_exercise WHERE date >= '2023-09-01' and date <= '2023-09-30'")
    suspend fun moAvg9(): Double

    @Query("SELECT ifnull(AVG(score),0) FROM table_exercise WHERE date >= '2023-07-01' and date <= '2023-07-30'")
    suspend fun moAvg7(): Double

    @Query("SELECT ifnull(AVG(score),0) FROM table_exercise WHERE date >= '2023-10-01' and date <= '2023-10-30'")
    suspend fun moAvg10(): Double

    @Query("SELECT ifnull(SUM(count),0) FROM table_exercise WHERE exercise=:exercise AND date=:date")
    suspend fun getAllCount(date: String, exercise: String): Int

    @Query("SELECT ifnull(count*score,0) FROM table_exercise WHERE exercise=:exercise AND date=:date")
    suspend fun getAllScore(date: String, exercise: String): List<Int>
}