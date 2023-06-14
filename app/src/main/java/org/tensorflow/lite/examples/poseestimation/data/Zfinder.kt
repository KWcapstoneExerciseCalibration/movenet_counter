package org.tensorflow.lite.examples.poseestimation.data

import kotlin.Triple
import kotlin.math.*


class zFinder {

    data class BodyLen(var shoulder: Float, var arr: Array<Float>, var human: Person)

    // DB / Data Base / 지민님 : 어깨 길이와 각 신체길이 불러오기 (float 11개)
    val shoulder = 56.8F
    val lastKeypointLength = mutableListOf(47.8f, 47.8f, 57.7f, 57.7f, 66.2f, 66.2f, 37.9f, 37.9f, 38.0f, 38.0f)
    //  (종아리/0, 1), (허벅지/ 2 ,3), (상체/ 4, 5), (윗팔/ 6 ,7), (아래팔/ 8, 9)


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

    /** poseNum: 1(스쿼트), 2(팔굽혀펴기) **/
    // 사람 class와 자세 번호 입력시, z값과 각도 입력 후 return
    fun findZPerson(person: Person, poseNum: Int): Person {
        var nowOrder: Int = 0
        var lenRate: Float = 0.0f

        // 신체 비율 재조종
        when(poseNum) {
            1 -> {
                // 스쿼트의 경우, 어깨 길이를 기준으로 비율 조정
                lenRate = sqrt(
                    (person.keyPoints[BodyPart.LEFT_SHOULDER.position].coordinate.x - person.keyPoints[BodyPart.RIGHT_SHOULDER.position].coordinate.x).pow(
                        2
                    )
                            + (person.keyPoints[BodyPart.LEFT_SHOULDER.position].coordinate.y - person.keyPoints[BodyPart.RIGHT_SHOULDER.position].coordinate.y).pow(
                        2
                    )
                )

                lenRate /= shoulder;
            }
            2 -> {
                // 팔굽혀 펴기의 경우, 왼 종아리 길이를 기준으로 비율 조정
                lenRate = sqrt(
                    (person.keyPoints[BodyPart.LEFT_ANKLE.position].coordinate.x - person.keyPoints[BodyPart.LEFT_KNEE.position].coordinate.x).pow(
                        2
                    )
                            + (person.keyPoints[BodyPart.LEFT_ANKLE.position].coordinate.y - person.keyPoints[BodyPart.LEFT_KNEE.position].coordinate.y).pow(
                        2
                    )
                )

                lenRate /= lastKeypointLength[0]
            }
        }

        // 길이의 절댓값 계산 후 저장
        for(i in 0..lastKeypointLength.size-1)
            lastKeypointLength[i] = lastKeypointLength[i]  * lenRate

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

        // 계산된 절댓값에서 카메라와 가까울수록 -, 멀수록 +
        when (poseNum){
            1 -> {
                // 스쿼트
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

            2->{
                // 팔굽혀펴기
                person.keyPoints[BodyPart.LEFT_KNEE.position].z = person.keyPoints[BodyPart.LEFT_ANKLE.position].z - person.keyPoints[BodyPart.LEFT_KNEE.position].z
                person.keyPoints[BodyPart.RIGHT_KNEE.position].z = person.keyPoints[BodyPart.RIGHT_ANKLE.position].z + person.keyPoints[BodyPart.RIGHT_KNEE.position].z
                person.keyPoints[BodyPart.LEFT_HIP.position].z = person.keyPoints[BodyPart.LEFT_KNEE.position].z - person.keyPoints[BodyPart.LEFT_HIP.position].z
                person.keyPoints[BodyPart.RIGHT_HIP.position].z = person.keyPoints[BodyPart.RIGHT_KNEE.position].z + person.keyPoints[BodyPart.RIGHT_HIP.position].z
                person.keyPoints[BodyPart.LEFT_SHOULDER.position].z = person.keyPoints[BodyPart.LEFT_HIP.position].z - person.keyPoints[BodyPart.LEFT_SHOULDER.position].z
                person.keyPoints[BodyPart.RIGHT_SHOULDER.position].z = person.keyPoints[BodyPart.RIGHT_HIP.position].z + person.keyPoints[BodyPart.RIGHT_SHOULDER.position].z
                person.keyPoints[BodyPart.LEFT_ELBOW.position].z = person.keyPoints[BodyPart.LEFT_SHOULDER.position].z - person.keyPoints[BodyPart.LEFT_ELBOW.position].z
                person.keyPoints[BodyPart.RIGHT_ELBOW.position].z = person.keyPoints[BodyPart.RIGHT_SHOULDER.position].z + person.keyPoints[BodyPart.RIGHT_ELBOW.position].z
                person.keyPoints[BodyPart.LEFT_WRIST.position].z = person.keyPoints[BodyPart.LEFT_ELBOW.position].z - person.keyPoints[BodyPart.LEFT_WRIST.position].z
                person.keyPoints[BodyPart.RIGHT_WRIST.position].z = person.keyPoints[BodyPart.RIGHT_ELBOW.position].z + person.keyPoints[BodyPart.RIGHT_WRIST.position].z
            }
        }

        // 각 관절을 이루는 두 신체 부위를 기준으로, 벡터 생성
        // dot 연산을 통해 두 벡터 사이의 각을 계산 (길이 정보는 사전 저장된 값 활용)
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

    // person class 입력시 길이를 z값에 입력해 return
    fun findLengthPerson(
        person: Person
    ): Person {
        var lenBody = BodyLen(0.0f, emptyArray(), person)

        bodyJoints.forEach {
            val len: Float = sqrt(
                (person.keyPoints[it.first.position].coordinate.x - person.keyPoints[it.second.position].coordinate.x).pow(2)
                        + (person.keyPoints[it.first.position].coordinate.y - person.keyPoints[it.second.position].coordinate.y).pow(2)
            )
            lenBody.human.keyPoints[it.second.position].z = len            // for visualize
            lenBody.arr += len
        }

        lenBody.shoulder = sqrt((person.keyPoints[BodyPart.LEFT_SHOULDER.position].coordinate.x - person.keyPoints[BodyPart.RIGHT_SHOULDER.position].coordinate.x).pow(2)
                + (person.keyPoints[BodyPart.LEFT_SHOULDER.position].coordinate.y - person.keyPoints[BodyPart.RIGHT_SHOULDER.position].coordinate.y).pow(2))

        // DB / Data Base / 지민님 : 어깨 길이와 각 신체길이 입력하기 (float 11개)
        /* lenBody.shoulder와 lenBody.arr에 입력되어 있음 */
        
        return person
    }
}
