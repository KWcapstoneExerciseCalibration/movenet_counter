package org.tensorflow.lite.examples.poseestimation.counter

import android.util.Log
import org.tensorflow.lite.examples.poseestimation.data.BodyPart
import org.tensorflow.lite.examples.poseestimation.data.Person
import org.tensorflow.lite.examples.poseestimation.data.zFinder
import org.tensorflow.lite.examples.poseestimation.ui.exercise.CameraActivity

// 팔굽혀펴기 counter class
class PushupCounter : WorkoutCounter() {

    // 왼쪽 어깨 각도
    var leftelbowAngle = 0
    // 오른쪽 어깨 각도
    var rightelbowAngle = 0
    // 숄더프레스 자세가 위인지(flag)
    var upPosition = false
    // 숄더프레스 자세가 아래인지(flag)
    var downPosition = false
    // 틀린 자세인지(flag)
    var wrongPosition = false

    override fun countAlgorithm(person: Person): Int {
        Log.d("PushUpCounter", "PushUp count Algorithm")

        var human = zFinder().findZPerson(person, 1)

        // 오른쪽: 사용하는 신체 좌표 값들이 최소 정확도를 넘는지(넘지 않는다면 사람 형태가 카메라에 잡히지 않는 것)
        if ( true/*human.keyPoints[BodyPart.NOSE.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.RIGHT_WRIST.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.RIGHT_ELBOW.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.RIGHT_SHOULDER.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.LEFT_WRIST.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.LEFT_ELBOW.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.LEFT_SHOULDER.ordinal].score >= MIN_CONFIDENCE*/) {

            leftelbowAngle = human.keyPoints[BodyPart.LEFT_ELBOW.ordinal].angle
            rightelbowAngle = human.keyPoints[BodyPart.RIGHT_ELBOW.ordinal].angle

            // wrongPosition
            // 좀 더 각도 체크해서 조정하자

            // upPosition: 왼쪽 다리가 펴진 상태, 스쿼트를 했다면 count + 1
            if ((person.keyPoints[BodyPart.NOSE.ordinal].coordinate.y < person.keyPoints[BodyPart.LEFT_ELBOW.ordinal].coordinate.y)
            /* && ((leftelbowAngle >= 160 || leftelbowAngle == 0) || (rightelbowAngle >= 160 || rightelbowAngle == 0)) */) {
                if (downPosition == true) {
                    score += 5
                    count++
                    CameraActivity.getInstance()?.ttsSpeak("$count 개")
                    if (wrongPosition == true)
                        wrongPosition = false
                    else
                        score += 5
                }

                upPosition = true
                downPosition = false
            }

            // downPosition
            if ((person.keyPoints[BodyPart.NOSE.ordinal].coordinate.y > person.keyPoints[BodyPart.LEFT_ELBOW.ordinal].coordinate.y)
                && (person.keyPoints[BodyPart.NOSE.ordinal].coordinate.y > person.keyPoints[BodyPart.RIGHT_ELBOW.ordinal].coordinate.y)) {
                downPosition = true
                upPosition = false
            }

            Log.d("개수", "$count 개")
        }

        return count
    }
}