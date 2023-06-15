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
import org.tensorflow.lite.examples.poseestimation.database.calenderDB.CalDao
import org.tensorflow.lite.examples.poseestimation.database.calenderDB.CalDataBase
import org.tensorflow.lite.examples.poseestimation.databinding.FragmentDailylogBinding
import java.util.*


class DailylogFragment : Fragment() {
    var datePickerDialog: DatePickerDialog? = null
    private var binding: FragmentDailylogBinding? = null
    private lateinit var dao: CalDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val dashboardViewModel = ViewModelProvider(this).get(
            DailylogViewModel::class.java
        )
        binding = FragmentDailylogBinding.inflate(inflater, container, false)
        val root = binding!!.root

        dao = CalDataBase.getInstance(requireContext()).calDao()

        val dateText = root.findViewById<TextView>(R.id.textToday)
        val dateNote = root.findViewById<TextView>(R.id.textView5)
        val dateExer = root.findViewById<TextView>(R.id.textView)
        val calendar = Calendar.getInstance()

        dateText.text = calendar.get(Calendar.DAY_OF_MONTH).toString()
        CoroutineScope(Dispatchers.IO).launch {
            dateNote.text = dao.getNote(calendar.get(Calendar.DAY_OF_MONTH))
            dateExer.text = dao.getExer(calendar.get(Calendar.DAY_OF_MONTH)) + " " + dao.getCount(calendar.get(Calendar.DAY_OF_MONTH)) + "회 " + dao.getScore(calendar.get(Calendar.DAY_OF_MONTH)) + "점"
        }
            dateText.setOnClickListener {

            datePickerDialog = DatePickerDialog(requireActivity(), { datePicker, year, month, day ->
                val date = "" + day
                dateText.text = date
                CoroutineScope(Dispatchers.IO).launch {
                    dateNote.text = dao.getNote(day)
                    dateExer.text = dao.getExer(day) + " " + dao.getCount(day) + "회 " + dao.getScore(day) + "점"

                }
            }, 2023, 5, calendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog!!.show()
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}