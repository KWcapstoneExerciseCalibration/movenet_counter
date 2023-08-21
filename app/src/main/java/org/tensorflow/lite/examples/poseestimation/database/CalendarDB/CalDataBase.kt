package org.tensorflow.lite.examples.poseestimation.database.calenderDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [CalSchema::class], version = 4)
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

            return builder.addMigrations(MIGRATION_2_3).build()
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE users_new (date TEXT, note TEXT, intensity INTEGER, id INTEGER, PRIMARY KEY(id))");
                database.execSQL("DROP TABLE table_calender");
                database.execSQL("ALTER TABLE users_new RENAME TO table_calender");
            }
        }

    }
}