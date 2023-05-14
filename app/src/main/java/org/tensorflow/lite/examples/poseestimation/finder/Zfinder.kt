package org.tensorflow.lite.examples.poseestimation.finder

import kotlin.Triple
import org.tensorflow.lite.examples.poseestimation.data.BodyPart
import org.tensorflow.lite.examples.poseestimation.data.Person
import kotlin.math.*


class zFinder {
    private val bodyJoints = listOf(
        Pair(BodyPart.LEFT_ANKLE, BodyPart.LEFT_KNEE),
        Pair(BodyPart.RIGHT_ANKLE, BodyPart.RIGHT_KNEE),
        Pair(BodyPart.LEFT_KNEE, BodyPart.LEFT_HIP),
        Pair(BodyPart.RIGHT_KNEE, BodyPart.RIGHT_HIP),
        Pair(BodyPart.LEFT_HIP, BodyPart.LEFT_SHOULDER),
        Pair(BodyPart.RIGHT_HIP, BodyPart.RIGHT_SHOULDER),
        Pair(BodyPart.LEFT_SHOULDER, BodyPart.LEFT_ELBOW),
        Pair(BodyPart.RIGHT_SHOULDER, BodyPart.RIGHT_ELBOW),
        Pair(BodyPart.LEFT_ELBOW, BodyPart.LEFT_WRIST),
        Pair(BodyPart.RIGHT_ELBOW, BodyPart.RIGHT_WRIST)
    )
    private val bodyAngleJoints = listOf(
        Triple(BodyPart.LEFT_ANKLE, BodyPart.LEFT_KNEE, BodyPart.LEFT_HIP),
        Triple(BodyPart.RIGHT_ANKLE, BodyPart.RIGHT_KNEE, BodyPart.RIGHT_HIP),
        Triple(BodyPart.LEFT_KNEE, BodyPart.LEFT_HIP, BodyPart.LEFT_SHOULDER),
        Triple(BodyPart.RIGHT_KNEE, BodyPart.RIGHT_HIP, BodyPart.RIGHT_SHOULDER),
        Triple(BodyPart.LEFT_HIP, BodyPart.LEFT_SHOULDER, BodyPart.LEFT_ELBOW),
        Triple(BodyPart.RIGHT_HIP, BodyPart.RIGHT_SHOULDER, BodyPart.RIGHT_ELBOW),
        Triple(BodyPart.LEFT_SHOULDER, BodyPart.LEFT_ELBOW, BodyPart.LEFT_WRIST),
        Triple(BodyPart.RIGHT_SHOULDER, BodyPart.RIGHT_ELBOW, BodyPart.RIGHT_WRIST)
    )

    fun findZPerson(person: Person, poseNum: Int): Person {
        val shoulder = 56.8F
        var lastKeypointLength = mutableListOf(47.8f, 47.8f, 57.7f, 57.7f, 66.2f, 66.2f, 37.9f, 37.9f, 38.0f, 38.0f)
        //  (종아리/0, 1), (허벅지/ 2 ,3), (상체/ 4, 5), (윗팔/ 6 ,7), (아래팔/ 8, 9)
        //  z값은 카메라와 가까울수록 -, 멀수록 +

        var nowOrder: Int = 0
        // person.keyPoints[].coordinate.x
        var shouldLen = sqrt((person.keyPoints[BodyPart.LEFT_SHOULDER.position].coordinate.x - person.keyPoints[BodyPart.RIGHT_SHOULDER.position].coordinate.x).pow(2)
                    + (person.keyPoints[BodyPart.LEFT_SHOULDER.position].coordinate.y - person.keyPoints[BodyPart.RIGHT_SHOULDER.position].coordinate.y).pow(2))

        shouldLen /= shoulder;

        for(i in 0..lastKeypointLength.size-1)
            lastKeypointLength[i] = lastKeypointLength[i]  * shouldLen

        bodyJoints.forEach {
            val pointA = Pair(person.keyPoints[it.first.position].coordinate.x, person.keyPoints[it.first.position].coordinate.y)
            val pointB = Pair(person.keyPoints[it.second.position].coordinate.x, person.keyPoints[it.second.position].coordinate.y)

            var zLength = lastKeypointLength[nowOrder]

            zLength = zLength.pow(2)
            zLength -= (pointA.first - pointB.first).pow(2)
            zLength -= (pointA.second - pointB.second).pow(2)
            zLength = sqrt(abs(zLength))

            person.keyPoints[it.second.position].z = zLength
            nowOrder += 1
        }

        when (poseNum){
            // 스쿼트
            1 -> {
                person.keyPoints[BodyPart.LEFT_KNEE.position].z = person.keyPoints[BodyPart.LEFT_ANKLE.position].z - person.keyPoints[BodyPart.LEFT_KNEE.position].z
                person.keyPoints[BodyPart.RIGHT_KNEE.position].z = person.keyPoints[BodyPart.RIGHT_ANKLE.position].z - person.keyPoints[BodyPart.RIGHT_KNEE.position].z
                person.keyPoints[BodyPart.LEFT_HIP.position].z = person.keyPoints[BodyPart.LEFT_KNEE.position].z + person.keyPoints[BodyPart.LEFT_HIP.position].z
                person.keyPoints[BodyPart.RIGHT_HIP.position].z = person.keyPoints[BodyPart.RIGHT_KNEE.position].z + person.keyPoints[BodyPart.RIGHT_HIP.position].z
                person.keyPoints[BodyPart.LEFT_SHOULDER.position].z = person.keyPoints[BodyPart.LEFT_HIP.position].z - person.keyPoints[BodyPart.LEFT_SHOULDER.position].z
                person.keyPoints[BodyPart.RIGHT_SHOULDER.position].z = person.keyPoints[BodyPart.RIGHT_HIP.position].z - person.keyPoints[BodyPart.RIGHT_SHOULDER.position].z
                person.keyPoints[BodyPart.LEFT_ELBOW.position].z = person.keyPoints[BodyPart.LEFT_SHOULDER.position].z - person.keyPoints[BodyPart.LEFT_ELBOW.position].z
                person.keyPoints[BodyPart.RIGHT_ELBOW.position].z = person.keyPoints[BodyPart.RIGHT_SHOULDER.position].z - person.keyPoints[BodyPart.RIGHT_ELBOW.position].z
                person.keyPoints[BodyPart.LEFT_WRIST.position].z = person.keyPoints[BodyPart.LEFT_ELBOW.position].z - person.keyPoints[BodyPart.LEFT_WRIST.position].z
                person.keyPoints[BodyPart.RIGHT_WRIST.position].z = person.keyPoints[BodyPart.RIGHT_ELBOW.position].z - person.keyPoints[BodyPart.RIGHT_WRIST.position].z
                }
            }

        nowOrder = 0
        bodyAngleJoints.forEach{
            var v1Len: Float = 0.0F;
            var v2Len: Float = 0.0F;
            var v1 = mutableListOf(person.keyPoints[it.first.position].coordinate.x - person.keyPoints[it.second.position].coordinate.x,
                                    person.keyPoints[it.first.position].coordinate.y - person.keyPoints[it.second.position].coordinate.y,
                                    person.keyPoints[it.first.position].z - person.keyPoints[it.second.position].z)
            var v2 = mutableListOf(person.keyPoints[it.third.position].coordinate.x - person.keyPoints[it.second.position].coordinate.x,
                                    person.keyPoints[it.third.position].coordinate.y - person.keyPoints[it.second.position].coordinate.y,
                                    person.keyPoints[it.third.position].z - person.keyPoints[it.second.position].z)

            var theta: Double = (v1[0]*v2[0] + v1[1]*v2[1] + v1[2]*v2[2]).toDouble()

            when(nowOrder) {
                0->{v1Len = lastKeypointLength[0]
                    v2Len = lastKeypointLength[2]}
                1-> {v1Len = lastKeypointLength[1]
                    v2Len = lastKeypointLength[3]}
                2->{v1Len = lastKeypointLength[2]
                    v2Len = lastKeypointLength[4]}
                3->{v1Len = lastKeypointLength[3]
                    v2Len = lastKeypointLength[5]}
                4->{v1Len = lastKeypointLength[4]
                    v2Len = lastKeypointLength[6]}
                5->{v1Len = lastKeypointLength[5]
                    v2Len = lastKeypointLength[7]}
                6->{v1Len = lastKeypointLength[6]
                    v2Len = lastKeypointLength[8]}
                7->{v1Len = lastKeypointLength[7]
                    v2Len = lastKeypointLength[9]}
            }
            theta /= (v1Len*v2Len)
            theta = Math.toDegrees(acos(theta))
            person.keyPoints[it.second.position].angle = theta.toInt()
            nowOrder++
        }

        return person
    }
}
