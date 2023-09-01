package org.tensorflow.lite.examples.poseestimation.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.examples.poseestimation.MainActivity
import org.tensorflow.lite.examples.poseestimation.R
import org.tensorflow.lite.examples.poseestimation.data.ExpValue
import org.tensorflow.lite.examples.poseestimation.database.UserDB.UserDataBase
import org.tensorflow.lite.examples.poseestimation.databinding.FragmentHomeBinding
import java.util.*
import kotlin.properties.Delegates

// 데이터베이스 관련 import 추가

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    // 데이터베이스 초기화 추가 필요

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val homeViewModel: HomeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        binding =
            FragmentHomeBinding.inflate(
                inflater,
                container,
                false
            )
        val root = binding!!.root

        // stateful하게 변경될 뷰
        val levelText = root.findViewById<TextView>(R.id.textLvl)
        val progressBar = root.findViewById<ProgressBar>(R.id.progressBarLvl)
        val expRemainText = root.findViewById<TextView>(R.id.textExp)

        // 데이터베이스
        // 1 데이터베이스로 현재 경험치를 불러옴
        // 2 경험치 계산 함수를 통한 현재 레벨, 다음 레벨까지 남은 경험치 계산
        // 3 뷰에 해당 값 반영
        var daoUser = UserDataBase.getInstance(MainActivity.getInstance()!!.applicationContext).userDao()

        CoroutineScope(Dispatchers.IO).launch {
            val exp = daoUser.readAll()[0].exp
            val (level, remain) = ExpValue.calculateExp(exp.toInt())

            levelText.text = "Lv. " + level
            progressBar.max = ExpValue.levelNeedExp(level)
            progressBar.progress = remain
            expRemainText.text =  remain.toString() +  " / " + ExpValue.levelNeedExp(level)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun onButtonClick(v: View?) {}
}