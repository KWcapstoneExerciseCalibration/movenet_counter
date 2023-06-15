package org.tensorflow.lite.examples.poseestimation.database.calenderDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [CalSchema::class], version = 2)
abstract class CalDataBase: RoomDatabase() {
    abstract fun calDao(): CalDao

    companion object{
        @Volatile
        private var instance: CalDataBase? = null

        fun getInstance(context: Context): CalDataBase =
            instance ?: synchronized(this){
                instance ?: buildDatabase(context).also {instance = it}
            }

        private fun buildDatabase(appContext: Context): CalDataBase{
            val builder =
                Room.databaseBuilder(
                    appContext,
                    CalDataBase::class.java,
                    "calender_db"
                ).fallbackToDestructiveMigration()

            return builder.addMigrations(MIGRATION_1_2).build()
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE 'table_calender' ADD COLUMN 'count' INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE 'table_calender' ADD COLUMN 'score' INTEGER NOT NULL DEFAULT 0")
            }
        }

    }
}