package org.tensorflow.lite.examples.poseestimation.counter

import android.util.Log
import org.tensorflow.lite.examples.poseestimation.data.BodyPart
import org.tensorflow.lite.examples.poseestimation.data.Person
import org.tensorflow.lite.examples.poseestimation.data.zFinder
import org.tensorflow.lite.examples.poseestimation.ui.exercise.CameraActivity
import kotlin.math.max
import kotlin.math.min

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
    // 틀린지 확인하는 정수
    var wrongValue = 0f
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

            // wrongPosition
            // 좀 더 각도 체크해서 조정하자
            wrongValue = person.keyPoints[BodyPart.RIGHT_ELBOW.ordinal].coordinate.y - person.keyPoints[BodyPart.LEFT_ELBOW.ordinal].coordinate.y

            // upPosition: 왼쪽 다리가 펴진 상태, 스쿼트를 했다면 count + 1
            if ((leftkneeAngle >= 160 || leftkneeAngle == 0)) {
                if (wrongValue >= 40f || wrongValue <= -40f)
                    wrongPosition = true
                if (downPosition) {
                    score += 5
                    count++
                    CameraActivity.getInstance()?.ttsSpeak("$count 개")
                    Log.d("wrongPo", wrongPosition.toString())
                    if (wrongPosition == true) {
                        // wrongValue를 통해 tts로 잘못된 부분 알려주자
                        wrongPosition = false
                    }
                    else
                        score += 5
                }

                upPosition = true
                downPosition = false
            }

            Log.d("leftkneeAngle", leftkneeAngle.toString())

            // downPosition: 왼쪽 다리 각도가 100도라면 스쿼트를 하는 자세인 것
            if ((leftkneeAngle >= 75) && (leftkneeAngle <= 125)) {
                if (wrongValue >= 40f || wrongValue <= -40f)
                    wrongPosition = true
                downPosition = true
                upPosition = false
            }

            Log.d("개수", "$count 개")
        }

        // 현재 프로그래스 바 값 조정
        // 펴진거 판정 최저값(160), 접힌거 판정 최저값(80)
        if(leftkneeAngle == 0)
            now_progress = 80
        else
            now_progress = max(leftkneeAngle-80, 0)

        Log.d("now_progress", now_progress.toString())

        return count
    }
}