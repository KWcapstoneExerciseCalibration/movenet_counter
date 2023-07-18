package org.tensorflow.lite.examples.poseestimation.database.StatisticDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_statistic")
data class StatSchema(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val wkAvg: Double,
    val moAvg: Double,
    val yrAvg: Double,
    val weight: Double,
    val BMI: Double,
    val bestRecord: String,
    val foodMenu: String,
    val foodCal: Int
)