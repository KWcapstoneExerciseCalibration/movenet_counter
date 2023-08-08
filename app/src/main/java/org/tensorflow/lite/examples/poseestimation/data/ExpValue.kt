package org.tensorflow.lite.examples.poseestimation.data

object ExpValue {
    private val expNeedeed:List<Int> = listOf(0, 400, 900, 1500, 2300, 3100, 4100, 5100, 6300, 7600, 9000,   // 10
                                        10600, 12200, 14000, 15900, 17900, 20200, 22700, 25400, 28100, 31000, 	// 20
                                        34400, 38100, 42100, 46400, 51000, 55900, 61000, 66500, 72200, 78200)   // 30


    fun calculateExp(nowExp:Int): Pair<Int, Int> {
        var level:Int = 1
        var remainExp: Int = 0

        run breaker@{
            expNeedeed.forEachIndexed { index, expNeed ->
                if((nowExp-expNeed) > 0)  level = index
                else                      return@breaker
            }
        }
        remainExp = nowExp - expNeedeed[level]

        return Pair(level, remainExp)
    }

    fun levelNeedExp(level: Int) = expNeedeed[level+1] - expNeedeed[level]
}