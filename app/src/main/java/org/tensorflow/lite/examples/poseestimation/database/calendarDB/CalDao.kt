package org.tensorflow.lite.examples.poseestimation.database.calenderDB

import android.text.Editable
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

    @Query("SELECT note FROM table_calender WHERE date=:date")
    suspend fun getNote(date: Int): String

    @Query("SELECT exercise FROM table_calender WHERE date=:date")
    suspend fun getExer(date: Int): String

    @Query("DELETE FROM table_calender")
    suspend fun deleteAllUsers()

    @Query("UPDATE table_calender SET note=:note WHERE date=:date")
    suspend fun updateNote(note: String, date: Int)

}