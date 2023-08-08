package org.tensorflow.lite.examples.poseestimation.database.UserDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_user")
data class UserSchema(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val age: Int,
    val exp: Double,
    val weight: Float,
    val height: Float,
    val BMI: Float
)