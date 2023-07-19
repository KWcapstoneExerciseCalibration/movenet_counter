package org.tensorflow.lite.examples.poseestimation.database.StatisticDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StatSchema::class], version = 1)
abstract class StatDataBase : RoomDatabase() {
    abstract fun statDao(): StatDao

    companion object{
        @Volatile
        private var instance: StatDataBase? = null

        fun getInstance(context: Context): StatDataBase =
            instance ?: synchronized(this){
                instance ?: buildDatabase(context).also {instance = it}
            }

        private fun buildDatabase(appContext: Context): StatDataBase {
            val builder =
                Room.databaseBuilder(
                    appContext,
                    StatDataBase::class.java,
                    "statistic_db"
                ).fallbackToDestructiveMigration()

            return builder.build()
        }

    }
}