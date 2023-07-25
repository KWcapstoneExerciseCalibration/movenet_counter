package org.tensorflow.lite.examples.poseestimation.ui.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
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
import org.tensorflow.lite.examples.poseestimation.databinding.FragmentDailylogBinding
import org.tensorflow.lite.examples.poseestimation.databinding.FragmentStatisticsBinding
import org.w3c.dom.Text

class StatisticFragment : Fragment() {
    private var binding: FragmentStatisticsBinding? = null
    private lateinit var dao: ExerDao
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val notificationsViewModel =
            ViewModelProvider(this).get(
                StatisticViewModel::class.java
            )
        binding =
            FragmentStatisticsBinding.inflate(
                inflater,
                container,
                false
            )
        val root = binding!!.root

        dao = ExerDataBase.getInstance(requireContext()).exerDao()

        val moAvg = root.findViewById<TextView>(R.id.moAvg)
        val yrAvg = root.findViewById<TextView>(R.id.yrAvg)

        CoroutineScope(Dispatchers.Main).launch {
            moAvg.text = dao.moAvg().toString()
        }




        //ExerciseDB 삭제 버튼

        val btn_reset = root.findViewById<Button>(R.id.btn_reset)
        btn_reset.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                dao.deleteAll()
            }
        }




        return binding!!.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}