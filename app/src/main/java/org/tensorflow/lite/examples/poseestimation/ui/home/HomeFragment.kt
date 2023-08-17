package org.tensorflow.lite.examples.poseestimation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.tensorflow.lite.examples.poseestimation.R
import org.tensorflow.lite.examples.poseestimation.databinding.FragmentHomeBinding
import java.util.*
// 데이터베이스 관련 import 추가 필요

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null

    // 데이터베이스 초기화 추가 필요

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val homeViewModel = ViewModelProvider(this).get(
            HomeViewModel::class.java
        )
        binding =
            FragmentHomeBinding.inflate(
                inflater,
                container,
                false
            )
        val root = binding!!.root

        // stateful하게 변경될 뷰
        val levelText = root.findViewById<TextView>(R.id.textLvl)
        val expText = root.findViewById<TextView>(R.id.textExp)
        val levelProgressBar = root.findViewById<TextView>(R.id.progressBarLvl)

        // 데이터베이스
        // 1 데이터베이스로 현재 경험치를 불러옴
        // 2 경험치 계산 함수를 통한 현재 레벨, 다음 레벨까지 남은 경험치 계산
        // 3 뷰에 해당 값 반영

        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun onButtonClick(v: View?) {}
}