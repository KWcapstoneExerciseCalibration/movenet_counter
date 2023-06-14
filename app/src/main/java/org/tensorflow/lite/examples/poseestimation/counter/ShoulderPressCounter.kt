package org.tensorflow.lite.examples.poseestimation.counter

import android.util.Log
import org.tensorflow.lite.examples.poseestimation.data.Person
import org.tensorflow.lite.examples.poseestimation.data.zFinder
import org.tensorflow.lite.examples.poseestimation.data.BodyPart
import org.tensorflow.lite.examples.poseestimation.ui.exercise.CameraActivity
import kotlin.math.acos

class ShoulderPressCounter: WorkoutCounter() {

    // 왼쪽 어깨 각도
    var leftelbowAngle = 0
    // 오른쪽 어깨 각도
    var rightelbowAngle = 0
    // 틀린지 확인하는 정수
    var wrongValue = 0f
    // 숄더프레스 자세가 위인지(flag)
    var upPosition = false
    // 숄더프레스 자세가 아래인지(flag)
    var downPosition = false
    // 틀린 자세인지(flag)
    var wrongPosition = false
    // 틀린 자세인지(flag)
    var wrongflag = false
    var wrongflag2 = false
    // 손이 팔보다 위에 있는지(flag)
    var wristPosition = false

    override fun countAlgorithm(person: Person): Int {
        Log.d("ShoulderPressCounter", "Shoulder Press count Algorithm")

        var human = zFinder().findZPerson(person, 1)

        // 조건 쓰기
        // 사용하는 신체 좌표 값들이 최소 정확도를 넘는지(넘지 않는다면 사람 형태가 카메라에 잡히지 않는 것)
        if (   human.keyPoints[BodyPart.RIGHT_WRIST.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.RIGHT_ELBOW.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.RIGHT_SHOULDER.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.LEFT_WRIST.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.LEFT_ELBOW.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.LEFT_SHOULDER.ordinal].score >= MIN_CONFIDENCE) {

            leftelbowAngle = human.keyPoints[BodyPart.LEFT_ELBOW.ordinal].angle
            rightelbowAngle = human.keyPoints[BodyPart.RIGHT_ELBOW.ordinal].angle

            // wrongPosition
            wrongValue = person.keyPoints[BodyPart.RIGHT_ELBOW.ordinal].coordinate.y - person.keyPoints[BodyPart.LEFT_ELBOW.ordinal].coordinate.y

            wristPosition = (person.keyPoints[BodyPart.RIGHT_ELBOW.ordinal].coordinate.y > person.keyPoints[BodyPart.RIGHT_WRIST.ordinal].coordinate.y)
                    && (person.keyPoints[BodyPart.LEFT_ELBOW.ordinal].coordinate.y > person.keyPoints[BodyPart.LEFT_WRIST.ordinal].coordinate.y)

            // upPosition: 왼쪽 다리가 펴진 상태, 스쿼트를 했다면 count + 1
            if (wristPosition && (leftelbowAngle >= 140 || leftelbowAngle == 0) && (rightelbowAngle >= 140 || rightelbowAngle == 0)) {
                if (wrongValue >= 40f || wrongValue <= -40f) {
                    wrongflag2 = true
                    wrongPosition = true
                }
                if (downPosition == true) {
                    score += 5
                    count++
                    CameraActivity.getInstance()?.ttsSpeak("$count 개")
                    if (wrongPosition == true) {
                        if (wrongflag)
                            CameraActivity.getInstance()?.ttsSpeak("팔을 직각으로 해주세요")
                        if (wrongflag2)
                            CameraActivity.getInstance()?.ttsSpeak("몸을 기울이지 마세요")
                        wrongPosition = false
                        wrongflag = false
                        wrongflag2 = false
                    }
                    else
                        score += 5
                }

                upPosition = true
                downPosition = false
            }
            // downPosition: 왼쪽 다리 각도가 100도라면 스쿼트를 하는 자세인 것
            if (wristPosition && (leftelbowAngle >= 70) && (leftelbowAngle <= 110) && (rightelbowAngle >= 70) && (rightelbowAngle <= 110)) {
                if (wrongValue >= 40f || wrongValue <= -40f)
                    wrongPosition = true
                downPosition = true
                upPosition = false
            }
            else if (wristPosition && (leftelbowAngle < 50 && leftelbowAngle > 0)) {
                wrongPosition = true
                wrongflag = true
            }

            Log.d("개수", "$count 개")
        }

        return count
    }
}