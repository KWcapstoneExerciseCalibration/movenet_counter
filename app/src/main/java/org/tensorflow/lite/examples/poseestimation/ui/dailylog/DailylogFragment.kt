package org.tensorflow.lite.examples.poseestimation.ui.dailylog

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.examples.poseestimation.R
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerDao
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerDataBase
import org.tensorflow.lite.examples.poseestimation.databinding.FragmentDailylogBinding
import java.text.SimpleDateFormat
import java.util.*


class DailylogFragment : Fragment() {
    var datePickerDialog: DatePickerDialog? = null
    private var binding: FragmentDailylogBinding? = null
    private lateinit var dao: ExerDao
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val dashboardViewModel = ViewModelProvider(this).get(
            DailylogViewModel::class.java
        )
        binding = FragmentDailylogBinding.inflate(inflater, container, false)
        val root = binding!!.root

        val dateText = root.findViewById<TextView>(R.id.textToday)
        val dateNote = root.findViewById<TextView>(R.id.textView5)
        val dateExer = root.findViewById<TextView>(R.id.textWorkout1)
        val calendar = Calendar.getInstance()

        dao = ExerDataBase.getInstance(requireActivity()).exerDao()
        val currentTime : Long = System.currentTimeMillis()
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        date.timeZone = TimeZone.getTimeZone("GMT+09:00")

        dateText.text = calendar.get(Calendar.DAY_OF_MONTH).toString()

        CoroutineScope(Dispatchers.IO).launch {
            if (dao.readAll().size <= 1){
                dateNote.text = "현재 작성한 소감이 없습니다"
                dateExer.text = "현재 운동을 한번도 하지 않았습니다"
            }
            else{
                dateNote.text = dao.getNote(date.format(currentTime))
                dateExer.text = dao.getExer(date.format(currentTime)) + " " + dao.getCount(date.format(currentTime)) + "회 " + dao.getScore(date.format(currentTime)) + "점"
            }
        }

        dateText.setOnClickListener {
            datePickerDialog = DatePickerDialog(requireActivity(), { datePicker, year, month, day ->
                val date2 = "" + day
                dateText.text = date2
                CoroutineScope(Dispatchers.IO).launch {
                    if (dao.readAll().size <= 1){
                        dateNote.text = "현재 작성한 소감이 없습니다"
                        dateExer.text = "현재 운동을 한번도 하지 않았습니다"
                    }
                    else{
                        dateNote.text = dao.getNote(date.format(currentTime))
                        dateExer.text = dao.getExer(date.format(currentTime)) + " " + dao.getCount(date.format(currentTime)) + "회 " + dao.getScore(date.format(currentTime)) + "점"
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
}