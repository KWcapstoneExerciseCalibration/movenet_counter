package org.tensorflow.lite.examples.poseestimation.database.LengthDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.tensorflow.lite.examples.poseestimation.data.zFinder

@Database(entities = [LengthSchema::class], version = 1)
abstract class LengthDataBase : RoomDatabase() {
    abstract fun lengthDao(): LengthDao

    companion object{
        @Volatile
        private var instance: LengthDataBase? = null

        fun getInstance(context: Context): LengthDataBase =
            instance ?: synchronized(this){
                instance ?: buildDatabase(context).also {instance = it}
            }

        private fun buildDatabase(appContext: Context): LengthDataBase {
            val builder =
                Room.databaseBuilder(
                    appContext,
                    LengthDataBase::class.java,
                    "length_db"
                ).fallbackToDestructiveMigration()

            return builder.build()
        }
    }
}