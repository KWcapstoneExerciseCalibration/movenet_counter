package org.tensorflow.lite.examples.poseestimation.ui.statistic

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
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
import org.tensorflow.lite.examples.poseestimation.MainActivity
import org.tensorflow.lite.examples.poseestimation.R
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerDao
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerDataBase
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerSchema
import org.tensorflow.lite.examples.poseestimation.database.UserDB.UserDataBase
import org.tensorflow.lite.examples.poseestimation.database.UserDB.UserSchema
import org.tensorflow.lite.examples.poseestimation.database.calenderDB.CalDataBase
import org.tensorflow.lite.examples.poseestimation.databinding.FragmentStatisticsBinding
import org.tensorflow.lite.examples.poseestimation.ui.dailylog.ImpressionDialog
import org.tensorflow.lite.examples.poseestimation.ui.length.heightDialog
import java.util.*
import kotlin.math.*

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

        /*ExerciseDB 임시삭제 버튼
        val btn_reset = root.findViewById<Button>(R.id.btn_reset)
        btn_reset.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                dao.deleteAll()
                val initData = ExerSchema(0, "0", "0", "0", "0", 0, 0, 0, "0")
                dao.create(initData)
            }
        }
         */

        var monthList = listOf("7월", "8월", "9월", "10월")
        var adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, monthList)
        val spinner = root.findViewById<Spinner>(R.id.spinner)

        spinner.adapter = adapter

        //그래프 테스트 https://programmmingphil.tistory.com/16
        CoroutineScope(Dispatchers.Main).launch {

            suspend fun printGraph(dMonth: Int) {
                data class ExerData(val date: String, val score: Int)
                var dateArray = arrayOf("2023-0$dMonth-01")
                var dataList: List<ExerData>


                // 날짜 제어
                fun putMonth (){
                    for (i in 2..9){
                        dateArray += "2023-0$dMonth-0$i"
                    }
                    for (i in 10..30){
                        dateArray += "2023-0$dMonth-$i"
                    }
                    if (dMonth == 8){
                        dateArray += "2023-0$dMonth-31"
                    }
                }
                putMonth()

                // DB값 날짜에 매칭
                suspend fun convertDBtoGraph(data: Array<String>) = data.map {
                    ExerData(it, dao.getDateScore(it))
                }
                dataList = convertDBtoGraph(dateArray)


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

                //선 스타일 변경
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
                    valueTextSize = 15f
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
                    textSize = 15f
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


            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                val moAvg = root.findViewById<TextView>(R.id.moAvg)

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //아무것도 선택 안하면
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> {
                            CoroutineScope(Dispatchers.Main).launch(){
                                var dMonth = 7
                                printGraph(dMonth)
                                moAvg.text = dao.moAvg7().toString()
                            }
                        }
                        1 -> {
                            CoroutineScope(Dispatchers.Main).launch() {
                                var dMonth = 8
                                printGraph(dMonth)
                                moAvg.text = dao.moAvg8().toString()
                            }
                        }
                        2 -> {
                            CoroutineScope(Dispatchers.Main).launch() {
                                var dMonth = 9
                                printGraph(dMonth)
                                moAvg.text = dao.moAvg9().toString()
                            }
                        }
                        3 -> {
                            CoroutineScope(Dispatchers.Main).launch() {
                                var dMonth = 10
                                printGraph(dMonth)
                                moAvg.text = dao.moAvg10().toString()
                            }
                        }
                        else -> {
                            //일치하는게 없는 경우
                        }
                    }
                }
            }

        }

        // 체중, BMI 기록
        val btnHeight =root.findViewById<Button>(R.id.buttonHeight)
        val textHeight = root.findViewById<TextView>(R.id.textHeight)
        val btnWeight = root.findViewById<Button>(R.id.buttonWeight)
        val textWeight = root.findViewById<TextView>(R.id.textWeight)
        val textBMI = root.findViewById<TextView>(R.id.textBMI)
        var weight = 0.0F
        var height = 0.0F
        var bmi    = 0.0F

        var daoUser = UserDataBase.getInstance(requireContext()).userDao()

        CoroutineScope(Dispatchers.IO).launch {
            var userData = daoUser.readAll()

            // 비어있는 DB 처리는 main에서 해서 지웠습니다!
            weight = userData[0].weight
            textWeight.text = weight.toString()
            height = userData[0].height
            textHeight.text = height.toString()
            bmi    = userData[0].BMI
            textBMI.text = bmi.toString()
        }

        btnHeight.setOnClickListener{
            this.context?.let { it1 ->
                val dialog = heightDialog(it1)
                dialog.show(height)

                dialog.setOnClickedListener(object : heightDialog.ButtonClickListener {
                    override fun onClicked(height1 : Int, height2 : Int) {
                        height = height1 + height2 / 10.0f
                        textHeight.text = (height).toString()
                        bmi = calculateBMI(height/100, weight.toInt())
                        textBMI.text = bmi.toString()

                        CoroutineScope(Dispatchers.IO).launch {
                            var userData = daoUser.readAll()

                            userData[0].height = height
                            userData[0].BMI = bmi
                            daoUser.update(userData[0])
                        }
                    }
                })
            }
        }

        btnWeight.setOnClickListener{
            this.context?.let { it1 ->
                val dialog = weightDialog(it1)
                dialog.show(weight.toInt())

                dialog.setOnClickedListener(object : weightDialog.ButtonClickListener {
                    override fun onClicked(weight : Int) {
                        textWeight.text = weight.toString()
                        bmi = calculateBMI(height/100, weight)
                        textBMI.text = bmi.toString()

                        CoroutineScope(Dispatchers.IO).launch {
                            var userData = daoUser.readAll()

                            userData[0].weight = weight.toFloat()
                            userData[0].BMI = bmi
                            daoUser.update(userData[0])
                        }
                    }
                })
            }
        }

        return binding!!.root
    }

    fun calculateBMI(height:Float, weight:Int): Float{
        var bmi:Float

        if ((height.toDouble().pow(2.0)).toFloat() == 0.0F)
            bmi = 0.0F
        else {
            bmi = (weight / height.toDouble().pow(2.0)).toFloat()
            bmi = round((bmi*100))/100
            if (bmi > 55.0F || bmi < 0.0)
                bmi = 0.0F
        }
        return bmi
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}