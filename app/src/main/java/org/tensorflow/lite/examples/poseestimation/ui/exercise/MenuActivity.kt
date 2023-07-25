package org.tensorflow.lite.examples.poseestimation.ui.exercise


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.examples.poseestimation.R
import org.tensorflow.lite.examples.poseestimation.database.calenderDB.CalDao
import org.tensorflow.lite.examples.poseestimation.database.calenderDB.CalDataBase
import org.tensorflow.lite.examples.poseestimation.database.calenderDB.CalSchema
import java.util.Calendar



class MenuActivity : AppCompatActivity(){

    private lateinit var btn_pushup: Button
    private lateinit var btn_squat: Button
    private lateinit var btn_shoulderpress: Button

    private lateinit var dao: CalDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        btn_pushup = findViewById(R.id.btn_pushup)
        btn_squat = findViewById(R.id.btn_squat)
        btn_shoulderpress = findViewById(R.id.btn_shoulderpress)

        var exercise: String


        val calendar = Calendar.getInstance()
        dao = CalDataBase.getInstance(applicationContext).calDao()
        val calSchema = CalSchema(0, calendar.get(Calendar.DAY_OF_MONTH), "-", "-", 0, 0)
        CoroutineScope(Dispatchers.IO).launch {
            dao.create(calSchema)

        }


        fun pushtoDB (exercise: String){
            CoroutineScope(Dispatchers.IO).launch {
                dao.updateExer(exercise, calendar.get(Calendar.DAY_OF_MONTH),)

            }
        }




        btn_pushup.setOnClickListener {
            exercise = "PushUp"
            pushtoDB(exercise)

            val intent = Intent(this, GuideActivity::class.java)
            intent.putExtra("exercise",exercise)
            startActivity(intent)
            val intent_result = Intent(this, ResultActivity::class.java)
            intent_result.putExtra("exerciseName",exercise)
            startActivity(intent_result)
            finish()
        }

        btn_squat.setOnClickListener {
            exercise = "Squat"
            pushtoDB(exercise)

            val intent = Intent(this, GuideActivity::class.java)
            intent.putExtra("exercise",exercise)
            startActivity(intent)
            finish()
        }

        btn_shoulderpress.setOnClickListener {
            exercise = "ShoulderPress"
            pushtoDB(exercise)

            val intent = Intent(this, GuideActivity::class.java)
            intent.putExtra("exercise",exercise)
            startActivity(intent)
            finish()
        }


    }
}