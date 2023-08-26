package org.tensorflow.lite.examples.poseestimation.ui.statistic

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.tensorflow.lite.examples.poseestimation.R
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerDao
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerDataBase
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerSchema
import org.tensorflow.lite.examples.poseestimation.databinding.FragmentStatisticsBinding
import java.util.*

class StatisticFragment : Fragment() {
    private var binding: FragmentStatisticsBinding? = null
    private lateinit var dao: ExerDao

    // 다른 class에서 main함수 불러오기 용
    init{
        instance = this
    }
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: StatisticFragment? = null

        fun getInstance(): StatisticFragment? {
            return instance
        }
    }


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
                val initData = ExerSchema(0, "0", "0", "0", "0", 0, 0, 0, "0")
                dao.create(initData)
            }
        }

        //그래프 테스트 https://programmmingphil.tistory.com/16
        CoroutineScope(Dispatchers.Main).launch {
            data class ExerData(val date: String, val score: Int)
            val date1 = "2023-08-18"
            val date2 = "2023-08-19"
            val date3 = "2023-08-20"
            val date4 = "2023-08-21"
            val date5 = "2023-08-22"
            val dataList: List<ExerData> = listOf(
                ExerData(date1, dao.getDateScore(date1)),
                ExerData(date2, dao.getDateScore(date2)),
                ExerData(date3, dao.getDateScore(date3)),
                ExerData(date4, dao.getDateScore(date4)),
                ExerData(date5, dao.getDateScore(date5)),
            )

            fun changeDateText(dataList: List<ExerData>): List<String> {
                val dataTextList = ArrayList<String>()
                for (i in dataList.indices) {
                    val textSize = dataList[i].date.length
                    val dateText = dataList[i].date.substring(textSize - 2, textSize)
                    if (dateText == "01") {
                        dataTextList.add(dataList[i].date)
                    } else {
                        dataTextList.add(dateText)
                    }
                }
                return dataTextList
            }

            class XAxisCustomFormatter(val xAxisData: List<String>) : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return xAxisData[(value).toInt()]
                }

            }

            val linechart = root.findViewById<LineChart>(R.id.line_chart)
            val xAxis = linechart.xAxis

            //데이터 가공
            //y축
            val entries: MutableList<Entry> = mutableListOf()
            for (i in dataList.indices) {
                entries.add(Entry(i.toFloat(), dataList[i].score.toFloat()))
            }
            val lineDataSet = LineDataSet(entries, "entries")

            lineDataSet.apply {
                color = resources.getColor(R.color.black, null)
                circleRadius = 5f
                lineWidth = 3f
                setCircleColor(resources.getColor(R.color.purple_700, null))
                circleHoleColor = resources.getColor(R.color.purple_700, null)
                setDrawHighlightIndicators(false)
                setDrawValues(true) // 숫자표시
                valueTextColor = resources.getColor(R.color.black, null)
                valueFormatter = DefaultValueFormatter(0)  // 소숫점 자릿수 설정
                valueTextSize = 10f
            }

            //차트 전체 설정
            linechart.apply {
                axisRight.isEnabled = false   //y축 사용여부
                axisLeft.isEnabled = false
                legend.isEnabled = false    //legend 사용여부
                description.isEnabled = false //주석
                isDragXEnabled = true   // x 축 드래그 여부
                isScaleYEnabled = false //y축 줌 사용여부
                isScaleXEnabled = false //x축 줌 사용여부
            }

            //X축 설정
            xAxis.apply {
                setDrawGridLines(false)
                setDrawAxisLine(true)
                setDrawLabels(true)
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = XAxisCustomFormatter(changeDateText(dataList))
                textColor = resources.getColor(R.color.black, null)
                textSize = 10f
                labelRotationAngle = 0f
                setLabelCount(10, true)
            }

            val horizontalScrollView =
                root.findViewById<HorizontalScrollView>(R.id.horizontal_scroll_view)
            horizontalScrollView.post {
                horizontalScrollView.scrollTo(
                    linechart.width,
                    0
                )
            }

            linechart.apply {
                data = LineData(lineDataSet)
                notifyDataSetChanged() //데이터 갱신
                invalidate() // view갱신
            }
        }


        return binding!!.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}