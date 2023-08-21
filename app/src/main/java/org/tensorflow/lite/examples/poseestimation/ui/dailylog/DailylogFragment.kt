package org.tensorflow.lite.examples.poseestimation.ui.dailylog

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.*
import org.tensorflow.lite.examples.poseestimation.R
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerDao
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerDataBase
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerSchema
import org.tensorflow.lite.examples.poseestimation.database.calenderDB.CalDao
import org.tensorflow.lite.examples.poseestimation.database.calenderDB.CalDataBase
import org.tensorflow.lite.examples.poseestimation.database.calenderDB.CalSchema
import org.tensorflow.lite.examples.poseestimation.databinding.FragmentDailylogBinding
import java.text.SimpleDateFormat
import java.util.*


class DailylogFragment : Fragment() {
    var datePickerDialog: DatePickerDialog? = null
    private var binding: FragmentDailylogBinding? = null
    private lateinit var exer_dao: ExerDao
    private lateinit var cal_dao: CalDao
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val dashboardViewModel = ViewModelProvider(this).get(
            DailylogViewModel::class.java
        )
        binding = FragmentDailylogBinding.inflate(inflater, container, false)
        val root = binding!!.root

        //날짜 표시
        val dateText = root.findViewById<TextView>(R.id.textToday)
        val calendar = Calendar.getInstance()
        dateText.text = calendar.get(Calendar.DAY_OF_MONTH).toString()

        //오늘의 운동 표시
        val showPushup = root.findViewById<TextView>(R.id.textWorkout1)
        val showSquat = root.findViewById<TextView>(R.id.textWorkout2)
        val showShoulderPress = root.findViewById<TextView>(R.id.textWorkout3)

        exer_dao = ExerDataBase.getInstance(requireActivity()).exerDao()
        val today : Long = System.currentTimeMillis()
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        date.timeZone = TimeZone.getTimeZone("GMT+09:00")

        CoroutineScope(Dispatchers.IO).launch {
            showPushup.text = "팔굽혀펴기 " + allCount(date.format(today), "PushUp") + "회 평균 " + allScore(date.format(today),"PushUp") + "점"
            showSquat.text = "스쿼트 " + allCount(date.format(today), "Squat") + "회 평균 " + allScore(date.format(today),"Squat") + "점"
            showShoulderPress.text = "숄더프레스 " + allCount(date.format(today), "ShoulderPress") + "회 평균 " + allScore(date.format(today),"ShoulderPress") + "점"
        }

        //강도&소감 표시

        //최초 DB -> 이전 필요
        cal_dao = CalDataBase.getInstance(requireActivity()).calDao()
        CoroutineScope(Dispatchers.IO).launch {
            if (cal_dao.readAll().isEmpty()){
                val initData = CalSchema(date.format(today), "test", 0)
                cal_dao.create(initData)
            }
        }

        val dateNote = root.findViewById<TextView>(R.id.textView5)
        dateNote.text = "test"

        val btn_1 = root.findViewById<Button>(R.id.button)
        val btn_2 = root.findViewById<Button>(R.id.button2)
        val btn_3 = root.findViewById<Button>(R.id.button3)
        val btn_4 = root.findViewById<Button>(R.id.button4)
        val btn_5 = root.findViewById<Button>(R.id.button5)
        btn_1.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                cal_dao.upIntens(date.format(today), 1)
            }
        }
        btn_2.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                cal_dao.upIntens(date.format(today), 2)
            }
        }
        btn_3.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                cal_dao.upIntens(date.format(today), 3)
            }
        }
        btn_4.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                cal_dao.upIntens(date.format(today), 4)
            }
        }
        btn_5.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                cal_dao.upIntens(date.format(today), 5)
            }
        }

        dateText.setOnClickListener {
            datePickerDialog = DatePickerDialog(requireActivity(), { datePicker, year, month, day ->
                val date2 = "" + day
                dateText.text = date2
                CoroutineScope(Dispatchers.IO).launch {
                    if (exer_dao.readAll().size <= 1){
                        dateNote.text = "현재 작성한 소감이 없습니다"
                        //dateExer.text = "현재 운동을 한번도 하지 않았습니다"
                    }
                    else{
                        //dateNote.text = dao.getNote(date.format(currentTime))
                        //dateExer.text = dao.getExer(date.format(currentTime)) + " " + dao.getCount(date.format(currentTime)) + "회 " + dao.getScore(date.format(currentTime)) + "점"
                    }
                }
            }, 2023, 7, calendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog!!.show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private suspend fun allCount(date: String, exercise: String): String = withContext(Dispatchers.IO) {
        return@withContext exer_dao.getAllCount(date, exercise).toString()
    }
    private suspend fun allScore(date: String, exercise: String): String = withContext(Dispatchers.IO) {
        return@withContext exer_dao.getAllScore(date, exercise).toString()
    }
}