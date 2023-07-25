package org.tensorflow.lite.examples.poseestimation.database.ExerciseDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_exercise")
data class ExerSchema(
    @PrimaryKey val id: Long, //날짜 + 숫자(매일 0 부터 시작)
    val exercise: String?,
    val note: String,
    val intensity: Int,
    val count: Int,
    val score: Int,
    val correction: String
)