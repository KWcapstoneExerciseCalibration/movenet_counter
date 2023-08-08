package org.tensorflow.lite.examples.poseestimation.database.UserDB

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    suspend fun create(userSchema: UserSchema): Long

    @Update
    suspend fun update(userSchema: UserSchema)

    @Delete
    suspend fun delete(userSchema: UserSchema)

    @Query("SELECT * FROM table_user")
    suspend fun readAll(): List<UserSchema>

    @Query("DELETE FROM table_user")
    suspend fun deleteAll()
}