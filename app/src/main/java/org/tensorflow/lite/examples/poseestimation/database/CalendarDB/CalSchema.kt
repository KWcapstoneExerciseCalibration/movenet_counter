package org.tensorflow.lite.examples.poseestimation.database.calenderDB
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_calender")
data class CalSchema(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val date: Int,
    val exercise: String,
    val note: String,
    val count: Int,
    val score: Int
)