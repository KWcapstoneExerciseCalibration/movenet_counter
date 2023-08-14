package org.tensorflow.lite.examples.poseestimation.data

import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import org.tensorflow.lite.examples.poseestimation.MainActivity
import org.tensorflow.lite.examples.poseestimation.R
import org.tensorflow.lite.examples.poseestimation.ui.exercise.CameraActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Random
import kotlin.properties.Delegates

object QuestData {
    // 했는지 여부를 따로 만든 이유는 퀘스트 완료 즉시 갱신을 위해서
    var changed = false
    var dailyMax:Int = 10
    var pushUp:String = " "
    var pushUpCount:Int = 0
    var pushUpCheck:Boolean = false
    var squat:String = " "
    var squatCount:Int = 0
    var squatCheck:Boolean = false
    var quest2Max:Int = 10
    var shoulderPress:String = " "
    var shoulderPressCount:Int = 0
    var shoulderPressCheck:Boolean = false
    var questNum by Delegates.notNull<Int>()


    @RequiresApi(Build.VERSION_CODES.O)
    fun todayQuest() {
        val now = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        val random = Random(now.toLong())
        questNum = random.nextInt(3)

        when(questNum){
            0->{pushUp = "팔굽혀펴기 " + quest2Max +"회"
                squat = "스쿼트 " + dailyMax +"회"
                shoulderPress = "숄더프레스 " + dailyMax +"회"}

            1->{pushUp = "팔굽혀펴기 " + dailyMax +"회"
                squat = "스쿼트 " + quest2Max +"회"
                shoulderPress = "숄더프레스 " + dailyMax +"회"}

            2->{pushUp = "팔굽혀펴기 " + dailyMax +"회"
                squat = "스쿼트 " + dailyMax +"회"
                shoulderPress = "숄더프레스 " + quest2Max +"회"}
        }
    }

    fun questChanged(exercise: String?){
        if (exercise!!.contains("PushUp")){
            pushUpCount += CameraActivity.workoutCounter.count

            // 일퀘 번호가 0 (푸쉬업이 일퀘가 아닌 경우)
            if(questNum == 0 && quest2Max <= pushUpCount) {
                pushUpCheck = true
                changed = true
            }
            // 푸쉬업이 일퀘인 경우
            if (questNum != 0 && dailyMax <= pushUpCount) {
                pushUpCheck = true
                changed = true
            }
        }
        else if(exercise!!.contains("Squat")){
            squatCount += CameraActivity.workoutCounter.count

            // 일퀘 번호가 1 (스쿼트가 일퀘가 아닌 경우)
            if(questNum == 1 && quest2Max <= squatCount) {
                squatCheck = true
                changed = true
            }
            // 스쿼트가 일퀘인 경우
            if (questNum != 1 && dailyMax <= squatCount) {
                squatCheck = true
                changed = true
            }
        }
        else if(exercise!!.contains("ShoulderPress")){
            shoulderPressCount += CameraActivity.workoutCounter.count

            // 일퀘 번호가 2 (숄더프레스가 일퀘가 아닌 경우)
            if(questNum == 2 && quest2Max <= shoulderPressCount) {
                shoulderPressCheck = true
                changed = true
            }
            // 숄더프레스가 일퀘인 경우
            if (questNum != 2 && dailyMax <= shoulderPressCount) {
                shoulderPressCheck = true
                changed = true
            }
        }
        else    Log.d("error", "QuestData 입력 에러")

        if (changed){
            MainActivity.getInstance()?.findViewById<ImageView>(R.id.questChanged)?.visibility = android.view.View.VISIBLE
        }
    }
}