package org.tensorflow.lite.examples.poseestimation.ui.exercise

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.examples.poseestimation.R
import org.tensorflow.lite.examples.poseestimation.counter.PushupCounter
import org.tensorflow.lite.examples.poseestimation.counter.ShoulderPressCounter
import org.tensorflow.lite.examples.poseestimation.counter.SquatCounter

class GuideActivity : AppCompatActivity() {

    private lateinit var img_exercise: ImageView
    private lateinit var tv_exercise: TextView
    private lateinit var btn_start: Button
    private lateinit var numPicker_exercise: NumberPicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)

        img_exercise = findViewById(R.id.ivExercise)
        tv_exercise = findViewById(R.id.tvExercise)
        btn_start = findViewById(R.id.btn_start)
        numPicker_exercise = findViewById(R.id.numPick)

        var exercise = intent.getStringExtra("exercise")

        // NumberPicker 설정
        numPicker_exercise.minValue = 1
        numPicker_exercise.maxValue = 100

        when(exercise) {
            // PushUp, ShoulderPress 이미지 바꿔주기
            "PushUp"                -> {img_exercise.setImageResource(R.drawable.pushup)
                tv_exercise.setText("팔굽혀펴기")}
            "Squat"                 -> {img_exercise.setImageResource(R.drawable.squat)
                tv_exercise.setText("스쿼트")}
            "ShoulderPress"         -> {img_exercise.setImageResource(R.drawable.shoulderpress)
                tv_exercise.setText("숄더프레스")}
            "CoursePushUp"          -> {img_exercise.setImageResource(R.drawable.pushup)
                tv_exercise.setText("팔굽혀펴기")}
            "CourseSquat"           -> {img_exercise.setImageResource(R.drawable.squat)
                tv_exercise.setText("스쿼트")}
            "CourseShoulderPress"   -> {img_exercise.setImageResource(R.drawable.shoulderpress)
                tv_exercise.setText("숄더프레스")}
            else                    -> Log.d("error", "운동 종류 선택 에러")
        }

        btn_start.setOnClickListener {
            val intent_start = Intent(this, CameraActivity::class.java)
            intent_start.putExtra("exercise",exercise)
            // 운동 횟수 변수 전달
            intent_start.putExtra("exercise_num", numPicker_exercise.value)
            // 코스 점수 변수 전달
            intent_start.putExtra("score", intent.getIntExtra("score", 0))
            startActivity(intent_start)
            finish()
        }
    }
}