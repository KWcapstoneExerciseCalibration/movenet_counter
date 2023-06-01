package org.tensorflow.lite.examples.poseestimation.ui.exercise

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.examples.poseestimation.R

class MenuActivity : AppCompatActivity(){

    private lateinit var btn_pushup: Button
    private lateinit var btn_squat: Button
    private lateinit var btn_shoulderpress: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        btn_pushup = findViewById(R.id.btn_pushup)
        btn_squat = findViewById(R.id.btn_squat)
        btn_shoulderpress = findViewById(R.id.btn_shoulderpress)

        var exercise: String

        btn_pushup.setOnClickListener {
            exercise = "PushUp"
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra("exercise",exercise)
            startActivity(intent)
        }

        btn_squat.setOnClickListener {
            exercise = "Squat"
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra("exercise",exercise)
            startActivity(intent)
        }

        btn_shoulderpress.setOnClickListener {
            exercise = "ShoulderPress"
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra("exercise",exercise)
            startActivity(intent)
        }
    }
}