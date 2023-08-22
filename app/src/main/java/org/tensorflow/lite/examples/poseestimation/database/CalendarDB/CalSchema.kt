package org.tensorflow.lite.examples.poseestimation.database.calenderDB
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_calender")
data class CalSchema(
    val date: String?,
    val note: String?,
    val intensity: Int?,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)