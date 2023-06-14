package org.tensorflow.lite.examples.poseestimation.ui.exercise

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.examples.poseestimation.MainActivity
import org.tensorflow.lite.examples.poseestimation.R

class ResultActivity : AppCompatActivity() {

    private lateinit var correct_text: TextView
    private lateinit var btn_close: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        correct_text = findViewById(R.id.correct_text)
        btn_close = findViewById(R.id.btnClose)

        correct_text.text = "점수 결과: " + intent.getIntExtra("score", 0).toString() + "점"

        btn_close.setOnClickListener {
            finish()
        }
    }
}