package org.tensorflow.lite.examples.poseestimation.ui.statistic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.examples.poseestimation.database.calenderDB.CalDao
import org.tensorflow.lite.examples.poseestimation.database.calenderDB.CalDataBase
import org.tensorflow.lite.examples.poseestimation.databinding.FragmentStatisticsBinding

class StatisticFragment : Fragment() {
    private var binding: FragmentStatisticsBinding? = null
    private lateinit var dao: CalDao
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val notificationsViewModel =
            ViewModelProvider(this).get(
                StatisticViewModel::class.java
            )
        dao = CalDataBase.getInstance(requireContext()).calDao()
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAllUsers()
        }
        binding =
            FragmentStatisticsBinding.inflate(
                inflater,
                container,
                false
            )
        return binding!!.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}