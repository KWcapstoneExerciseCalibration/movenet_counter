package org.tensorflow.lite.examples.poseestimation.database.calenderDB

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CalSchema::class], version = 1)
abstract class CalDataBase: RoomDatabase() {
    abstract fun calDao(): CalDao
}