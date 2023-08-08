package org.tensorflow.lite.examples.poseestimation.database.StatisticDB

import androidx.room.*

@Dao
interface StatDao {
    @Insert
    suspend fun create(statSchema: StatSchema): Long

    @Update
    suspend fun update(statSchema: StatSchema)

    @Delete
    suspend fun delete(statSchema: StatSchema)

    @Query("SELECT * FROM table_statistic")
    suspend fun readAll(): List<StatSchema>

    @Query("DELETE FROM table_statistic")
    suspend fun deleteAll()
}