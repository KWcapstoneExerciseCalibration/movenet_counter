package org.tensorflow.lite.examples.poseestimation.ui.exercise

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.examples.poseestimation.R

class CourseGuideActivity: AppCompatActivity() {

    private lateinit var btn_start: Button
    private lateinit var tv_pushup : TextView
    private lateinit var tv_squat : TextView
    private lateinit var tv_shoulderpress : TextView
    private lateinit var btn_backward: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courseguide)

        btn_start = findViewById(R.id.btn_start)
        tv_pushup = findViewById(R.id.textViewPushUp)
        tv_squat = findViewById(R.id.textViewSquat)
        tv_shoulderpress = findViewById(R.id.textViewShoulderPress)
        btn_backward = findViewById(R.id.img_backward)

        var course = intent.getStringExtra("course")

        when (course) {
            "Beginner"     -> { tv_pushup.text        = "팔굽혀펴기\nX10"
                                tv_squat.text         = "스쿼트\nX10"
                                tv_shoulderpress.text = "숄더프레스\nX10"
            }
            "Intermediate" -> { tv_pushup.text        = "팔굽혀펴기\nX30"
                                tv_squat.text         = "스쿼트\nX30"
                                tv_shoulderpress.text = "숄더프레스\nX30"
            }
            "Advanced"     -> { tv_pushup.text        = "팔굽혀펴기\nX50"
                                tv_squat.text         = "스쿼트\nX50"
                                tv_shoulderpress.text = "숄더프레스\nX50"
            }
        }

        btn_start.setOnClickListener {
            val intentStart = Intent(this, GuideActivity::class.java)
            intentStart.putExtra("exercise", "CoursePushUp")
            intentStart.putExtra("course", intent.getStringExtra("course"))
            startActivity(intentStart)
            finish()
        }

        btn_backward.setOnClickListener{
            startActivity(Intent(this, CourseMenuActivity::class.java))
            finish()
        }
    }
}