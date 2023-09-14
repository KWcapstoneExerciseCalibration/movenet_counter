package org.tensorflow.lite.examples.poseestimation.ui.dailylog

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.*
import org.tensorflow.lite.examples.poseestimation.MainActivity
import org.tensorflow.lite.examples.poseestimation.R
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerDao
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerDataBase
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

    private fun refresh(){
        var ft = parentFragmentManager.beginTransaction()
        ft.detach(this)
        ft.attach(this)
        ft.commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val dashboardViewModel = ViewModelProvider(this).get(
            DailylogViewModel::class.java
        )
        binding = FragmentDailylogBinding.inflate(inflater, container, false)
        val root = binding!!.root

        // 날짜 표시
        val dateText = root.findViewById<TextView>(R.id.textToday)
        val calendar = Calendar.getInstance()
        dateText.text = calendar.get(Calendar.DAY_OF_MONTH).toString()

        // 오늘의 운동 및 소감 표시
        val showPushup = root.findViewById<TextView>(R.id.textWorkout1)
        val showSquat = root.findViewById<TextView>(R.id.textWorkout2)
        val showShoulderPress = root.findViewById<TextView>(R.id.textWorkout3)
        val dateNote = root.findViewById<TextView>(R.id.textView5)

        exer_dao = ExerDataBase.getInstance(requireActivity()).exerDao()
        val today : Long = System.currentTimeMillis()
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        date.timeZone = TimeZone.getTimeZone("GMT+09:00")

        val text_1 = root.findViewById<TextView>(R.id.textWorkout1)
        val text_2 = root.findViewById<TextView>(R.id.textWorkout2)
        val text_3 = root.findViewById<TextView>(R.id.textWorkout3)


        CoroutineScope(Dispatchers.IO).launch {
            val (count, score) = scoreCal(date.format(today), "PushUp")
            val (count2, score2) = scoreCal(date.format(today), "Squat")
            val (count3, score3) = scoreCal(date.format(today), "ShoulderPress")

            val stringPU: String = { "팔굽혀펴기 " + count + "회 평균 " + score + "점" }.toString()
            val stringSq: String = { "스쿼트 " + count2 + "회 평균 " + score2 + "점" }.toString()
            val stringSP: String = { "숄더프레스 " + count3 + "회 평균 " + score3 + "점" }.toString()

            val countPU: Int = if (count == 0) 0 else 1
            val countSq: Int = if (count2== 0) 0 else 1
            val countSP: Int = if (count3== 0) 0 else 1
            val totalExercise: Int = countPU + countSq + countSP

            when(totalExercise){
                0 -> { text_1.setText("이 날 한 운동이 없습니다.") }

                1 -> { text_1.setText( if (countPU == 1)      stringPU
                                       else if (countSq == 1) stringSq
                                       else                   stringSP ) }

                2 -> { text_1.setText( if (countPU == 1)      stringPU
                                       else                   stringSq )
                       text_2.setText( if (countSP == 0)      stringSq
                                       else                   stringSP ) }

                3 -> { text_1.setText(stringPU)
                       text_2.setText(stringSq)
                       text_3.setText(stringSP) }
            }
        }

        // 최초 DB & 강도 및 소감 표시
        cal_dao = CalDataBase.getInstance(requireActivity()).calDao()
        CoroutineScope(Dispatchers.IO).launch {
            val calData = cal_dao.readAll()
            var todayPos = 	2147483647

            run breaker@{
                calData.forEachIndexed { index, calSchema ->
                    if(calSchema.date == date.format(today)){
                        todayPos = index
                        return@breaker
                    }
                }
            }
            if(todayPos == 2147483647) {
                val initData = CalSchema(date.format(today), "test", 0)
                dateNote.text = "오늘은 아직 소감을 적지 않았습니다!"
                cal_dao.create(initData)
            }
            else if(calData[todayPos].note == "test") {
                dateNote.text = "오늘은 아직 소감을 적지 않았습니다!"
                // intensity 처리
            }
            else {
                dateNote.text = calData[todayPos].note
                // intensity 처리
            }
        }

        val btn_edit = root.findViewById<ImageButton>(R.id.impressionBtn)
        btn_edit.setOnClickListener {
            this.context?.let { it1 ->
                val dialog = ImpressionDialog(it1)
                dialog.show(dateNote.text.toString())

                // 다이얼로그의 interface를 통해 데이터를 받아옴
                dialog.setOnClickedListener(object : ImpressionDialog.ButtonClickListener {
                    override fun onClicked(note: String) {
                        if(note.isBlank())  dateNote.text = "오늘은 아직 소감을 적지 않았습니다!"
                        else {
                            dateNote.text = note
                            updateDBNote(note)
                        }
                    }
                })
            }
        }

        val btn_1 = root.findViewById<Button>(R.id.button1)
        val btn_2 = root.findViewById<Button>(R.id.button2)
        val btn_3 = root.findViewById<Button>(R.id.button3)
        val btn_4 = root.findViewById<Button>(R.id.button4)
        val btn_5 = root.findViewById<Button>(R.id.button5)

        CoroutineScope(Dispatchers.IO).launch {

            val intensity = cal_dao.getIntens(date.format(today))

            when (intensity) {
                1 -> {  btn_1.setBackgroundColor(Color.LTGRAY)
                        btn_1.setTextColor(Color.BLACK)
                        btn_1.textSize = 14.0f
                     }
                2 -> {  btn_2.setBackgroundColor(Color.LTGRAY)
                        btn_2.setTextColor(Color.BLACK)
                        btn_2.textSize = 14.0f
                     }
                3 -> {  btn_3.setBackgroundColor(Color.LTGRAY)
                        btn_3.setTextColor(Color.BLACK)
                        btn_3.textSize = 14.0f
                     }
                4 -> {  btn_4.setBackgroundColor(Color.LTGRAY)
                        btn_4.setTextColor(Color.BLACK)
                        btn_4.textSize = 14.0f
                }
                5 -> {  btn_5.setBackgroundColor(Color.LTGRAY)
                        btn_5.setTextColor(Color.BLACK)
                        btn_5.textSize = 14.0f
                     }
            }

            // 더 나은 색 확정 시 Color.parseColor(Hexcode)로 반영
        }

        btn_1.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                cal_dao.upIntens(date.format(today), 1)
            }
            refresh()
        }
        btn_2.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                cal_dao.upIntens(date.format(today), 2)
            }
            refresh()
        }
        btn_3.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                cal_dao.upIntens(date.format(today), 3)
            }
            refresh()
        }
        btn_4.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                cal_dao.upIntens(date.format(today), 4)
            }
            refresh()
        }
        btn_5.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                cal_dao.upIntens(date.format(today), 5)
            }
            refresh()
        }

        dateText.setOnClickListener {
            datePickerDialog = DatePickerDialog(requireActivity(), { datePicker, year, month, day ->
                val date2 = "" + day
                dateText.text = date2
                CoroutineScope(Dispatchers.IO).launch {
                    if (exer_dao.readAll().size <= 1){
                        dateNote.text = "작성한 소감이 없습니다"
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

    private suspend fun scoreCal(date: String, exercise: String): Pair<Int, Int>{
        var count = exer_dao.getAllCount(date, exercise)
        var score = 0
        if(count!=0){
            exer_dao.getAllScore(date, exercise).forEach {
                score += it
            }
        }
        if(count != 0)
            score /= count

        return Pair(count, score)
    }

    private fun updateDBNote(note: String){
        val today : Long = System.currentTimeMillis()
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        cal_dao = CalDataBase.getInstance(requireActivity()).calDao()

        CoroutineScope(Dispatchers.IO).launch {
            cal_dao.upNote(date.format(today), note)
        }
    }
}