package org.tensorflow.lite.examples.poseestimation.ui.home

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import org.tensorflow.lite.examples.poseestimation.R
import org.tensorflow.lite.examples.poseestimation.data.QuestData

class QuestDialog (context : Context) {
    private val dialog = Dialog(context)   //부모 액티비티의 context

    fun show() {
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_quest)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        val cancelBtn = dialog.findViewById<Button>(R.id.cancelBtn)
        val daily1Context = dialog.findViewById<TextView>(R.id.daily1Context)
        val daily1Count = dialog.findViewById<TextView>(R.id.daily1Count)
        val daily1Whether = dialog.findViewById<CheckBox>(R.id.daily1Whether)
        val daily2Context = dialog.findViewById<TextView>(R.id.daily2Context)
        val daily2Count = dialog.findViewById<TextView>(R.id.daily2Count)
        val daily2Whether = dialog.findViewById<CheckBox>(R.id.daily2Whether)
        val quest2Content = dialog.findViewById<TextView>(R.id.quest2Content)
        val quest2Count = dialog.findViewById<TextView>(R.id.quest2Count)
        val quest2Whether = dialog.findViewById<CheckBox>(R.id.quest2Whether)

        cancelBtn.setOnClickListener { dialog.dismiss() }
        daily1Context.text = QuestData.daily1
        daily1Count.text = QuestData.dailyCount1.toString() + "회"
        daily1Whether.isChecked = QuestData.dailyCheck1

        daily2Context.text = QuestData.daily2
        daily2Count.text = QuestData.dailyCount2.toString() + "회"
        daily2Whether.isChecked = QuestData.dailyCheck2

        quest2Content.text = QuestData.quest2
        quest2Count.text = QuestData.questCount2.toString() + "회"
        quest2Whether.isChecked = QuestData.questCheck2

        dialog.show()
    }
}