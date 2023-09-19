package org.tensorflow.lite.examples.poseestimation.ui.exercise

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.examples.poseestimation.MainActivity
import org.tensorflow.lite.examples.poseestimation.R

class CourseMenuActivity: AppCompatActivity() {

    private lateinit var btn_beginner: Button
    private lateinit var btn_intermediate: Button
    private lateinit var btn_advanced: Button
    private lateinit var btn_backward: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coursemenu)

        btn_beginner = findViewById(R.id.btn_beginner)
        btn_intermediate = findViewById(R.id.btn_intermediate)
        btn_advanced = findViewById(R.id.btn_advanced)
        btn_backward = findViewById(R.id.img_backward)

        var course : String
        val intentStart = Intent(this, CourseGuideActivity::class.java)
        val level = MainActivity.getInstance()?.levelGet()
        
        // 레벨에 따라 코스가 잠기고 자물쇠 이미지가 추가된다
        if (level!! >= 20) {
            btn_advanced.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            btn_advanced.isEnabled = true
            btn_intermediate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            btn_intermediate.isEnabled = true
        }
        else if (level >= 10) {
            btn_advanced.isEnabled = false
            btn_intermediate.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            btn_intermediate.isEnabled = true
        }
        else {
            btn_advanced.isEnabled = false
            btn_intermediate.isEnabled = false
        }

        btn_beginner.setOnClickListener {
            course = "Beginner"

            intentStart.putExtra("exercise", intent.getStringExtra("exercise"))
            intentStart.putExtra("course", course)
            startActivity(intentStart)
            finish()
        }

        btn_intermediate.setOnClickListener {
            course = "Intermediate"

            intentStart.putExtra("exercise", intent.getStringExtra("exercise"))
            intentStart.putExtra("course", course)
            startActivity(intentStart)
            finish()
        }

        btn_advanced.setOnClickListener {
            course = "Advanced"

            intentStart.putExtra("exercise", intent.getStringExtra("exercise"))
            intentStart.putExtra("course", course)
            startActivity(intentStart)
            finish()
        }

        btn_backward.setOnClickListener{
            startActivity(Intent(this, MenuActivity::class.java))
            finish()
        }
    }
}