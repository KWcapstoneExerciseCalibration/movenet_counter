package org.tensorflow.lite.examples.poseestimation.ui.exercise

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.examples.poseestimation.R
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerDao
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerDataBase
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerSchema
import java.text.SimpleDateFormat
import java.util.*

class ResultActivity : AppCompatActivity() {

    private lateinit var correct_text: TextView
    private lateinit var wrong_text: TextView
    private lateinit var btn_close: Button
    private lateinit var dao: ExerDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        correct_text = findViewById(R.id.correct_text)
        wrong_text = findViewById(R.id.wrong_tv)
        btn_close = findViewById(R.id.btnClose)

        var wrongArray = intent.getStringArrayListExtra("wrongArrayList")
        var wrongString : String = ""

        // ArrayList<String> wrongPosition 정보를 String 배열로 저장했습니다.
        wrongArray?.forEach { wrongString += (it + "\n") }

        correct_text.text = "점수 결과: " + intent.getIntExtra("score", 0).toString() + "점"
        wrong_text.text = wrongString

        dao = ExerDataBase.getInstance(applicationContext).exerDao()
        val calendar = Calendar.getInstance()
        val txt : EditText = findViewById(R.id.editTextNote)
        var exerciseName = intent.getStringExtra("exerciseName")

        txt.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    CoroutineScope(Dispatchers.IO).launch {
                        val currentTime : Long = System.currentTimeMillis()
                        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        date.timeZone = TimeZone.getTimeZone("GMT+09:00")
                        val time = SimpleDateFormat("HH:mm:ss")
                        time.timeZone = TimeZone.getTimeZone("GMT+09:00")
                        val exerciseData = ExerSchema(currentTime, date.format(currentTime), time.format(currentTime), exerciseName, txt.text.toString(), 0, intent.getIntExtra("count", 0), intent.getIntExtra("score", 0), "test")
                        dao.create(exerciseData)
                        /*
                        dao.updateNote(txt.text.toString(), calendar.get(Calendar.DAY_OF_MONTH))
                        dao.updateScore(intent.getIntExtra("score", 0), calendar.get(Calendar.DAY_OF_MONTH))
                        dao.updateCount(intent.getIntExtra("count", 0), calendar.get(Calendar.DAY_OF_MONTH))
                         */
                    }
                    return true
                }
                return false
            }
        })

        btn_close.setOnClickListener {
            finish()
        }
    }

    private fun getDB_ID(){

    }
}