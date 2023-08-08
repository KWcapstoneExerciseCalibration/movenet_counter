package org.tensorflow.lite.examples.poseestimation.data

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Random

object QuestData {
    // 했는지 여부를 따로 만든 이유는 퀘스트 완료 즉시 갱신을 위해서
    var dailyMax:Int = 10
    var daily1:String = " "
    var dailyCount1:Int = 0
    var dailyCheck1:Boolean = false
    var daily2:String = " "
    var dailyCount2:Int = 0
    var dailyCheck2:Boolean = false
    var quest2Max:Int = 10
    var quest2:String = " "
    var questCount2:Int = 0
    var questCheck2:Boolean = false


    @RequiresApi(Build.VERSION_CODES.O)
    fun todayQuest() {
        val now = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val random = Random(now.toLong())
        val questNum = random.nextInt(3)

        when(questNum){
            0->{daily1 = "팔굽혀펴기 " + dailyMax +"회"
                daily2 = "스쿼트 " + dailyMax +"회"
                quest2 = "숄더프레스 " + quest2Max +"회"}

            1->{daily1 = "팔굽혀펴기 " + dailyMax +"회"
                daily2 = "숄더프레스 " + dailyMax +"회"
                quest2 = "스쿼트 " + quest2Max +"회"}

            2->{daily1 = "스쿼트 " + dailyMax +"회"
                daily2 = "숄더프레스 " + dailyMax +"회"
                quest2 =  "팔굽혀펴기 " + quest2Max +"회"}
        }
    }
}