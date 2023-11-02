package org.tensorflow.lite.examples.poseestimation.ui.dailylog

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.examples.poseestimation.R
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerDao
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerDataBase
import org.tensorflow.lite.examples.poseestimation.database.calenderDB.CalDao
import org.tensorflow.lite.examples.poseestimation.database.calenderDB.CalDataBase
import org.tensorflow.lite.examples.poseestimation.database.calenderDB.CalSchema
import org.tensorflow.lite.examples.poseestimation.databinding.FragmentDailylogBinding
import java.sql.Types.NULL
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
    ): View {
        val dashboardViewModel = ViewModelProvider(this).get(
            DailylogViewModel::class.java
        )
        binding = FragmentDailylogBinding.inflate(inflater, container, false)
        val root = binding!!.root

        // 날짜 표시
        val dateText = root.findViewById<TextView>(R.id.textToday)
        val timezone = TimeZone.getTimeZone("Asia/Seoul")
        val calendar = Calendar.getInstance(timezone)
        dateText.text = (calendar.get(Calendar.MONTH)+1).toString() + "월 " + calendar.get(Calendar.DAY_OF_MONTH).toString() + "일"

        // 오늘의 운동 및 소감 표시
        val dateNote = root.findViewById<TextView>(R.id.textView5)

        exer_dao = ExerDataBase.getInstance(requireActivity()).exerDao()
        val today : Long = System.currentTimeMillis()
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        date.timeZone = timezone

        val text_1 = root.findViewById<TextView>(R.id.textWorkout1)
        val text_2 = root.findViewById<TextView>(R.id.textWorkout2)
        val text_3 = root.findViewById<TextView>(R.id.textWorkout3)

        fun showExercise (dateToday : String){
            CoroutineScope(Dispatchers.IO).launch {
                val (count, score) = scoreCal(dateToday, "PushUp")
                val (count2, score2) = scoreCal(dateToday, "Squat")
                val (count3, score3) = scoreCal(dateToday, "ShoulderPress")

                val stringPU: CharSequence = ( "팔굽혀펴기 " + count + "회 평균 " + score + "점" )
                val stringSq: CharSequence = ( "스쿼트 " + count2 + "회 평균 " + score2 + "점" )
                val stringSP: CharSequence = ( "숄더프레스 " + count3 + "회 평균 " + score3 + "점" )

                val countPU: Int = if (count == 0) 0 else 1
                val countSq: Int = if (count2== 0) 0 else 1
                val countSP: Int = if (count3== 0) 0 else 1

                // 운동 개수 추가 시 수정 필요
                when(countPU + countSq + countSP){
                    0 -> {
                        text_1.text = "오늘 날 한 운동이 없습니다."
                        text_2.text = " "
                        text_3.text = " "
                    }

                    1 -> {
                        text_1.text = if (countPU == 1)      stringPU
                        else if (countSq == 1) stringSq
                        else                   stringSP
                        text_2.text = " "
                        text_3.text = " "
                    }

                    2 -> {
                        text_1.text = if (countPU == 1)      stringPU
                        else                   stringSq
                        text_2.text = if (countSP == 0)      stringSq
                        else                   stringSP
                        text_3.text = " "
                    }

                    3 -> {
                        text_1.text = stringPU
                        text_2.text = stringSq
                        text_3.text = stringSP
                    }
                }
            }
        }

        showExercise(date.format(today))

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
            }
            else {
                dateNote.text = calData[todayPos].note
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

        //운동 강도 관련
        val btn_1: Button = root.findViewById(R.id.button1)
        val btn_2: Button = root.findViewById(R.id.button2)
        val btn_3: Button = root.findViewById(R.id.button3)
        val btn_4: Button = root.findViewById(R.id.button4)
        val btn_5: Button = root.findViewById(R.id.button5)
        val btnList = listOf(btn_1, btn_2, btn_3, btn_4, btn_5)

        // 버튼의 배경색, 텍스트 색 및 크기 초기화 함수
        fun buttonInitialize (btn: Button) {
            btn.setBackgroundColor(Color.parseColor("#222222"))
            btn.setTextColor(Color.WHITE)
            btn.textSize = 12.0f

            val circle: Drawable? = this.context?.let { ContextCompat.getDrawable(it, R.drawable.white_circle) }
            btn.setCompoundDrawablesWithIntrinsicBounds(null, circle, null, null)
        }

        fun changeIntensityBox (intensity: Int?) {
            // 버튼 미선택 상태로 초기화
            for(btn in btnList)
                buttonInitialize(btn)

            when (intensity) {
                1 -> {
                    btn_1.setBackgroundColor(Color.LTGRAY)
                    btn_1.setTextColor(Color.BLACK)
                    btn_1.textSize = 13.0f
                    val emoji: Drawable? = this.context?.let { ContextCompat.getDrawable(it, R.drawable.emoji1) }
                    btn_1.setCompoundDrawablesWithIntrinsicBounds(null, emoji, null, null)
                }
                2 -> {
                    btn_2.setBackgroundColor(Color.LTGRAY)
                    btn_2.setTextColor(Color.BLACK)
                    btn_2.textSize = 13.0f
                    val emoji: Drawable? = this.context?.let { ContextCompat.getDrawable(it, R.drawable.emoji2) }
                    btn_2.setCompoundDrawablesWithIntrinsicBounds(null, emoji, null, null)
                }
                3 -> {
                    btn_3.setBackgroundColor(Color.LTGRAY)
                    btn_3.setTextColor(Color.BLACK)
                    btn_3.textSize = 13.0f
                    val emoji: Drawable? = this.context?.let { ContextCompat.getDrawable(it, R.drawable.emoji3) }
                    btn_3.setCompoundDrawablesWithIntrinsicBounds(null, emoji, null, null)
                }
                4 -> {
                    btn_4.setBackgroundColor(Color.LTGRAY)
                    btn_4.setTextColor(Color.BLACK)
                    btn_4.textSize = 13.0f
                    val emoji: Drawable? = this.context?.let { ContextCompat.getDrawable(it, R.drawable.emoji4) }
                    btn_4.setCompoundDrawablesWithIntrinsicBounds(null, emoji, null, null)
                }
                5 -> {
                    btn_5.setBackgroundColor(Color.LTGRAY)
                    btn_5.setTextColor(Color.BLACK)
                    btn_5.textSize = 13.0f
                    val emoji: Drawable? = this.context?.let { ContextCompat.getDrawable(it, R.drawable.emoji5) }
                    btn_5.setCompoundDrawablesWithIntrinsicBounds(null, emoji, null, null)
                }
                else -> {
                    //NULL 일 경우
                }

                // 더 나은 색 확정 시 Color.parseColor(Hexcode)로 반영
            }

            refresh()
        }

        CoroutineScope(Dispatchers.IO).launch {

            val intensity = cal_dao.getIntens(date.format(today))

            changeIntensityBox(intensity)

        }

        btn_1.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                cal_dao.upIntens(date.format(today), 1)
            }
            val intensity = 1
            changeIntensityBox(intensity)
        }
        btn_2.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                cal_dao.upIntens(date.format(today), 2)
            }
            val intensity = 2
            changeIntensityBox(intensity)
        }
        btn_3.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                cal_dao.upIntens(date.format(today), 3)
            }
            val intensity = 3
            changeIntensityBox(intensity)
        }
        btn_4.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                cal_dao.upIntens(date.format(today), 4)
            }
            val intensity = 4
            changeIntensityBox(intensity)
        }
        btn_5.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                cal_dao.upIntens(date.format(today), 5)
            }
            val intensity = 5
            changeIntensityBox(intensity)
        }

        dateText.setOnClickListener {
            datePickerDialog = DatePickerDialog(requireActivity(), { datePicker, year, month, day ->

                val showDate = (month+1).toString() + "월 " + day + "일"
                val actualMonth = month + 1
                val actualDate = "2023-" + "0$actualMonth-" + "$day"

                if (date.parse(actualDate) != date.parse(date.format(today))) {
                    btn_edit.isVisible = false
                    btn_1.isClickable = false
                    btn_2.isClickable = false
                    btn_3.isClickable = false
                    btn_4.isClickable = false
                    btn_5.isClickable = false
                }
                else {
                    btn_edit.isVisible = true
                    btn_1.isClickable = true
                    btn_2.isClickable = true
                    btn_3.isClickable = true
                    btn_4.isClickable = true
                    btn_5.isClickable = true
                }

                showExercise(actualDate)
                dateText.text = showDate
                CoroutineScope(Dispatchers.Main).launch {
                    dateNote.text = cal_dao.getNote(actualDate)
                    if(dateNote.text.isBlank())  {
                        dateNote.text = "소감을 적지 않았습니다!"
                    }

                    var intensity: Int? = cal_dao.getIntens(actualDate)

                    changeIntensityBox(intensity)
                }


            }, 2023, calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
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