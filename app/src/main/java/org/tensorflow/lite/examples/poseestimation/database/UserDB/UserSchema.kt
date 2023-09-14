package org.tensorflow.lite.examples.poseestimation.database.UserDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_user")
data class UserSchema(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val age: Int,
    var exp: Double,
    var weight: Float,
    var height: Float,
    var BMI: Float
)