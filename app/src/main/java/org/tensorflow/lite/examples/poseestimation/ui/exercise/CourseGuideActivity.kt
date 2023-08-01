package org.tensorflow.lite.examples.poseestimation.ui.exercise

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.examples.poseestimation.R

class CourseGuideActivity: AppCompatActivity() {

    private lateinit var btn_start: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courseguide)

        btn_start = findViewById(R.id.btn_start)

        btn_start.setOnClickListener {
            val intent_start = Intent(this, GuideActivity::class.java)
            intent_start.putExtra("exercise", intent.getStringExtra("exercise"))
            startActivity(intent_start)
            finish()
        }
    }
}