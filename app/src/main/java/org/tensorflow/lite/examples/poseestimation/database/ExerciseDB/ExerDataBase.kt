package org.tensorflow.lite.examples.poseestimation.database.ExerciseDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.tensorflow.lite.examples.poseestimation.database.calenderDB.CalDataBase

@Database(entities = [ExerSchema::class], version = 2)
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

            return builder.addMigrations(ExerDataBase.MIGRATION_1_2).build()
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE 'table_exercise' ADD COLUMN 'date' TEXT NOT NULL default ''")
                database.execSQL("ALTER TABLE 'table_exercise' ADD COLUMN 'time' TEXT NOT NULL default ''")
            }
        }
    }
}