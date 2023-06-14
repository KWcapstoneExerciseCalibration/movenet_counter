package org.tensorflow.lite.examples.poseestimation.counter

import android.util.Log
import org.tensorflow.lite.examples.poseestimation.data.BodyPart
import org.tensorflow.lite.examples.poseestimation.data.Person
import org.tensorflow.lite.examples.poseestimation.data.zFinder
import org.tensorflow.lite.examples.poseestimation.ui.exercise.CameraActivity

// 팔굽혀펴기 counter class
class PushupCounter : WorkoutCounter() {

    // 어깨 각도
    var elbowAngle = 0
    // 허리 각도
    var backAngle = 0
    // 팔굽혀펴기 자세가 위인지(flag)
    var upPosition = false
    // 팔굽혀펴기 자세가 아래인지(flag)
    var downPosition = false

    override fun countAlgorithm(person: Person): Int {
        Log.d("PushUpCounter", "PushUp count Algorithm")

        var human = zFinder().findZPerson(person, 2)

        // 오른쪽: 사용하는 신체 좌표 값들이 최소 정확도를 넘는지(넘지 않는다면 사람 형태가 카메라에 잡히지 않는 것)
        if (   human.keyPoints[BodyPart.RIGHT_WRIST.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.RIGHT_ELBOW.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.RIGHT_SHOULDER.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.RIGHT_HIP.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.RIGHT_KNEE.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.NOSE.ordinal].score >= MIN_CONFIDENCE) {

            elbowAngle = human.keyPoints[BodyPart.RIGHT_ELBOW.ordinal].angle
            Log.d("elbowAngle", elbowAngle.toString())
            backAngle = human.keyPoints[BodyPart.RIGHT_HIP.ordinal].angle
            Log.d("backAngle", backAngle.toString())

            if (backAngle < 20 || backAngle > 160) {
                // 허리가 펴지지 않았을 때, color red 코드
            }

            // upPosition: 어깨 각도가 펴진 상태일 때, 팔굽혀펴기를 한 후라면 count + 1
            if (elbowAngle >= 140 || elbowAngle == 0) {
                if (downPosition == true) {
                    correct += 5
                    count++
                    CameraActivity.getInstance()?.ttsSpeak("$count 개")
                }

                upPosition = true
                downPosition = false
            }

            // downPosition: 허리가 펴져 있고, 코 위치보다 어깨가 높으며, 어깨 각도가 100도라면 팔굽혀펴기를 하는 자세인 것
            if (/*elbowAboveNose && */((elbowAngle >= 80) && (elbowAngle <= 120))) {
                downPosition = true
                upPosition = false
            }
            Log.d("개수", "$count 개")
        }

        return count
    }
}