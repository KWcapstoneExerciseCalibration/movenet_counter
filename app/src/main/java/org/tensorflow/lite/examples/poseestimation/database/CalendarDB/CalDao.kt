package org.tensorflow.lite.examples.poseestimation.database.calenderDB

import androidx.room.*

@Dao
interface CalDao {
    @Insert
    suspend fun create(calSchema: CalSchema): Long

    @Update
    suspend fun update(calSchema: CalSchema)

    @Delete
    suspend fun delete(calSchema: CalSchema)

    @Query("SELECT * FROM table_calender")
    suspend fun readAll(): List<CalSchema>

    @Query("DELETE FROM table_calender")
    suspend fun deleteAllUsers()

    @Query("UPDATE table_calender SET intensity=:intensity WHERE date=:date")
    suspend fun upIntens(date: String, intensity: Int)

    @Query("SELECT ifnull (intensity, 0) FROM table_calender WHERE date=:date")
    suspend fun getIntens(date: String): Int?


    @Query("SELECT nullif(note, '') FROM table_calender WHERE date=:date")
    suspend fun getNote(date: String): String

    @Query("UPDATE table_calender SET note=:note WHERE date=:date")
    suspend fun upNote(date: String, note: String?)
}