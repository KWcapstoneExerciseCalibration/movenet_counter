package org.tensorflow.lite.examples.poseestimation.ui.exercise

import CalDataBase
import CalSchema
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.examples.poseestimation.R
import java.util.Calendar


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
        /*
        val database = Room.inMemoryDatabaseBuilder(
            this,
            CalDataBase::class.java
        ).build()

        fun pushtoDB (exercise: String){
            val calSchema = CalSchema(0, Calendar.DAY_OF_MONTH, exercise, "")
            CoroutineScope(Dispatchers.IO).launch {
                database.calDao().create(calSchema)

                var dbCalSchema = database.calDao().readAll()[0]
                Log.d("logDB", "insert -> $dbCalSchema")
            }
        }1
        */


        btn_pushup.setOnClickListener {
            exercise = "PushUp"
            //pushtoDB(exercise)

            val intent = Intent(this, GuideActivity::class.java)
            intent.putExtra("exercise",exercise)
            startActivity(intent)
            finish()
        }

        btn_squat.setOnClickListener {
            exercise = "Squat"
            //pushtoDB(exercise)

            val intent = Intent(this, GuideActivity::class.java)
            intent.putExtra("exercise",exercise)
            startActivity(intent)
            finish()
        }

        btn_shoulderpress.setOnClickListener {
            exercise = "ShoulderPress"
            //pushtoDB(exercise)

            val intent = Intent(this, GuideActivity::class.java)
            intent.putExtra("exercise",exercise)
            startActivity(intent)
            finish()
        }


    }
}