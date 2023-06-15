package org.tensorflow.lite.examples.poseestimation.ui.exercise

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.examples.poseestimation.R
import org.tensorflow.lite.examples.poseestimation.database.calenderDB.CalDao
import org.tensorflow.lite.examples.poseestimation.database.calenderDB.CalDataBase
import java.util.*

class ResultActivity : AppCompatActivity() {

    private lateinit var correct_text: TextView
    private lateinit var btn_close: Button
    private lateinit var dao: CalDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        correct_text = findViewById(R.id.correct_text)
        btn_close = findViewById(R.id.btnClose)

        correct_text.text = "점수 결과: " + intent.getIntExtra("score", 0).toString() + "점"

        dao = CalDataBase.getInstance(applicationContext).calDao()
        val calendar = Calendar.getInstance()
        val txt : EditText = findViewById(R.id.editTextNote)

        txt.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    CoroutineScope(Dispatchers.IO).launch {
                        dao.updateNote(txt.text.toString(), calendar.get(Calendar.DAY_OF_MONTH))
                        dao.updateScore(intent.getIntExtra("score", 0), calendar.get(Calendar.DAY_OF_MONTH))
                        dao.updateCount(intent.getIntExtra("count", 0), calendar.get(Calendar.DAY_OF_MONTH))
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
}