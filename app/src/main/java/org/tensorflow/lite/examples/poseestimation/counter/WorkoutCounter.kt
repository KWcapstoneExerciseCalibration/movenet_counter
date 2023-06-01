package org.tensorflow.lite.examples.poseestimation.counter

import org.tensorflow.lite.examples.poseestimation.data.Person

// 운동 counter abstract class
abstract class WorkoutCounter() {

    // Movenet 신체 좌표의 최소 정확도 = 0.5
    open var MIN_CONFIDENCE = 0.5
    // 운동 한 개수
    var count = 0
    // 교정 점수
    var correct = 0
    // 운동 목표 개수(임의로 10 설정)
    var goal = 10

    abstract fun countAlgorithm(person : Person) : Int

    abstract fun correctAlgorithm(person: Person): Int

    // 운동 개수 초기화
    fun reset() {
        count = 0
    }
}