package org.tensorflow.lite.examples.poseestimation.ui.exercise

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide)

        img_exercise = findViewById(R.id.ivExercise)
        tv_exercise = findViewById(R.id.tvExercise)
        btn_start = findViewById(R.id.btn_start)

        var exercise = intent.getStringExtra("exercise")

        when(exercise) {
            // PushUp, ShoulderPress 이미지 바꿔주기
            "PushUp"        -> {img_exercise.setImageResource(R.drawable.pushup)
                                tv_exercise.setText("팔굽혀펴기")}
            "Squat"         -> {img_exercise.setImageResource(R.drawable.squat)
                                tv_exercise.setText("스쿼트")}
            "ShoulderPress" -> {img_exercise.setImageResource(R.drawable.shoulderpress)
                                tv_exercise.setText("숄더프레스")}
            else            -> Log.d("error", "운동 종류 선택 에러")
        }

        btn_start.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra("exercise",exercise)
            startActivity(intent)
        }
    }
}