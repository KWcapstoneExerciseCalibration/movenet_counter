package org.tensorflow.lite.examples.poseestimation.database.LengthDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_length")
data class LengthSchema(
    @PrimaryKey val id: Int, //날짜 + 숫자(매일 0 부터 시작)
    val shoulder: Float,
    val calf_1: Float,
    val calf_2: Float,
    val thigh_1: Float,
    val thigh_2: Float,
    val body_1: Float,
    val body_2: Float,
    val upperarm_1: Float,
    val upperarm_2: Float,
    val lowerarm_1: Float,
    val lowerarm_2: Float,
)