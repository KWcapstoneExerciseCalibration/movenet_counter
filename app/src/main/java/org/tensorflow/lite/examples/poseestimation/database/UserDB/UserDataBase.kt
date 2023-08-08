package org.tensorflow.lite.examples.poseestimation.database.UserDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserSchema::class], version = 1)
abstract class UserDataBase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object{
        @Volatile
        private var instance: UserDataBase? = null

        fun getInstance(context: Context): UserDataBase =
            instance ?: synchronized(this){
                instance ?: buildDatabase(context).also {instance = it}
            }

        private fun buildDatabase(appContext: Context): UserDataBase {
            val builder =
                Room.databaseBuilder(
                    appContext,
                    UserDataBase::class.java,
                    "user_db"
                ).fallbackToDestructiveMigration()

            return builder.build()
        }

    }
}