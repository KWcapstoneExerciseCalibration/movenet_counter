package org.tensorflow.lite.examples.poseestimation.counter

import android.util.Log
import org.tensorflow.lite.examples.poseestimation.data.Person
import org.tensorflow.lite.examples.poseestimation.data.zFinder

class ShoulderPressCounter: WorkoutCounter() {

    // 왼쪽 어깨 각도
    var leftelbowAngle = 0
    // 오른쪽 어깨 각도
    var rightelbowAngle = 0
    // 팔굽혀펴기 자세가 위인지(flag)
    var upPosition = false
    // 팔굽혀펴기 자세가 아래인지(flag)
    var downPosition = false

    override fun countAlgorithm(person: Person): Int {
        Log.d("ShoulderPressCounter", "Shoulder Press count Algorithm")

        var human = zFinder().findZPerson(person, 1)
        
        // 조건 쓰기

        return count
    }
}