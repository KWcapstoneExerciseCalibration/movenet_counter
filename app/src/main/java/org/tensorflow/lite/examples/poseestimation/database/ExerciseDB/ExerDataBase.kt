package org.tensorflow.lite.examples.poseestimation.database.ExerciseDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ExerSchema::class], version = 1)
abstract class ExerDataBase : RoomDatabase() {
    abstract fun exerDao(): ExerDao

    companion object{
        @Volatile
        private var instance: ExerDataBase? = null

        fun getInstance(context: Context): ExerDataBase =
            instance ?: synchronized(this){
                instance ?: buildDatabase(context).also {instance = it}
            }

        private fun buildDatabase(appContext: Context): ExerDataBase {
            val builder =
                Room.databaseBuilder(
                    appContext,
                    ExerDataBase::class.java,
                    "exercise_db"
                ).fallbackToDestructiveMigration()

            return builder.build()
        }

    }
}