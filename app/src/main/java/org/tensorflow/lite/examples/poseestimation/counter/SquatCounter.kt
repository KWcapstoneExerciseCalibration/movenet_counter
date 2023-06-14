package org.tensorflow.lite.examples.poseestimation.counter

import android.util.Log
import org.tensorflow.lite.examples.poseestimation.data.BodyPart
import org.tensorflow.lite.examples.poseestimation.data.Person
import org.tensorflow.lite.examples.poseestimation.data.zFinder
import org.tensorflow.lite.examples.poseestimation.ui.exercise.CameraActivity

// 스쿼트 counter class
class SquatCounter : WorkoutCounter() {

    // 왼쪽 허리 각도
    var leftbackAngle = 0
    // 왼쪽 무릎 각도
    var leftkneeAngle = 0
    // 오른쪽 허리 각도
    var rightbackAngle = 0
    // 오른쪽 무릎 각도
    var rightkneeAngle = 0
    // 코 y 좌표
    var noseYaxis = 0.0F
    // 코 y 좌표(flag)
    var nosePosition = false
    // 스쿼트 자세가 위인지(flag)
    var upPosition = false
    // 스쿼트 자세가 아래인지(flag)
    var downPosition = false
    // 틀린 자세인지(flag)
    var wrongPosition = false

    override fun countAlgorithm(person: Person): Int {
        Log.d("SquatCounter", "Squat Algorithm")

        var human = zFinder().findZPerson(person, 1)

        // 사용하는 신체 좌표 값들이 최소 정확도를 넘는지(넘지 않는다면 사람 형태가 카메라에 잡히지 않는 것)
        if (   human.keyPoints[BodyPart.LEFT_SHOULDER.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.LEFT_HIP.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.LEFT_KNEE.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.LEFT_ANKLE.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.RIGHT_SHOULDER.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.RIGHT_HIP.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.RIGHT_KNEE.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.RIGHT_ANKLE.ordinal].score >= MIN_CONFIDENCE
            && human.keyPoints[BodyPart.NOSE.ordinal].score >= MIN_CONFIDENCE) {

            leftbackAngle = human.keyPoints[BodyPart.LEFT_ELBOW.ordinal].angle
            // Log.d("leftbackAngle", leftbackAngle.toString())
            leftkneeAngle = human.keyPoints[BodyPart.LEFT_KNEE.ordinal].angle
            // Log.d("leftkneeAngle", leftkneeAngle.toString())
            rightbackAngle = human.keyPoints[BodyPart.RIGHT_ELBOW.ordinal].angle
            // Log.d("rightbackAngle", rightbackAngle.toString())
            rightkneeAngle = human.keyPoints[BodyPart.RIGHT_KNEE.ordinal].angle
            // Log.d("rightkneeAngle", rightkneeAngle.toString())

            // 코 y좌표 조건문
            if (noseYaxis > person.keyPoints[BodyPart.NOSE.ordinal].coordinate.y)
                nosePosition = true
            else if (noseYaxis < person.keyPoints[BodyPart.NOSE.ordinal].coordinate.y)
                nosePosition = false

            noseYaxis = human.keyPoints[BodyPart.NOSE.ordinal].coordinate.y

            // wrongPosition: 다리 각도가 30도 이하라면 잘못된 자세
            // 좀 더 각도 체크해서 조정하자
            if ((leftkneeAngle <= 40) && (leftkneeAngle >= 1))
                wrongPosition = true

            // upPosition: 왼쪽 다리가 펴진 상태, 스쿼트를 했다면 count + 1
            if ((leftkneeAngle >= 160 || leftkneeAngle == 0) && nosePosition) {
                if (downPosition == true) {
                    correct += 5
                    count++
                    CameraActivity.getInstance()?.ttsSpeak("$count 개")
                    Log.d("wrongPo", wrongPosition.toString())
                    if (wrongPosition == true)
                        wrongPosition = false
                    else
                        correct += 5
                }

                upPosition = true
                downPosition = false
            }

            Log.d("leftkneeAngle", leftkneeAngle.toString())

            // downPosition: 왼쪽 다리 각도가 100도라면 스쿼트를 하는 자세인 것
            if ((leftkneeAngle >= 80) && (leftkneeAngle <= 120)) {
                downPosition = true
                upPosition = false
            }

            Log.d("개수", "$count 개")
        }

        return count
    }
}