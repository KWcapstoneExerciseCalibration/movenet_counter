package org.tensorflow.lite.examples.poseestimation.ui.home

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import org.tensorflow.lite.examples.poseestimation.MainActivity
import org.tensorflow.lite.examples.poseestimation.R
import org.tensorflow.lite.examples.poseestimation.data.QuestData

class QuestDialog(context: Context) {
    private val dialog = Dialog(context).apply {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_quest)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
    }

    private fun setQuest(name:TextView, count:TextView, success:CheckBox, quest: String, counting: Int, checked: Boolean){
        name.setQuest(quest)
        count.setQuest(counting)
        success.setQuest(checked)
    }

    private fun TextView.setQuest(quest: String) { text = quest }

    private fun TextView.setQuest(count: Int) { text = "$count 회" }

    private fun CheckBox.setQuest(checked: Boolean) { isChecked = checked }

    fun show() {
        val cancelBtn = dialog.findViewById<ImageButton>(R.id.cancelBtn)
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

        when (QuestData.questNum) {
            0 -> {
                setQuest(daily1Context, daily1Count, daily1Whether, QuestData.squat, QuestData.squatCount, QuestData.squatCheck)
                setQuest(daily2Context, daily2Count, daily2Whether, QuestData.shoulderPress, QuestData.shoulderPressCount, QuestData.shoulderPressCheck)
                setQuest(quest2Content, quest2Count, quest2Whether, QuestData.pushUp, QuestData.pushUpCount, QuestData.pushUpCheck)
            }
            1 -> {
                setQuest(daily1Context, daily1Count, daily1Whether, QuestData.pushUp, QuestData.pushUpCount, QuestData.pushUpCheck)
                setQuest(daily2Context, daily2Count, daily2Whether, QuestData.shoulderPress, QuestData.shoulderPressCount, QuestData.shoulderPressCheck)
                setQuest(quest2Content, quest2Count, quest2Whether, QuestData.squat, QuestData.squatCount, QuestData.squatCheck)
            }
            2 -> {
                setQuest(daily1Context, daily1Count, daily1Whether, QuestData.pushUp, QuestData.pushUpCount, QuestData.pushUpCheck)
                setQuest(daily2Context, daily2Count, daily2Whether, QuestData.squat, QuestData.squatCount, QuestData.squatCheck)
                setQuest(quest2Content, quest2Count, quest2Whether, QuestData.shoulderPress, QuestData.shoulderPressCount, QuestData.shoulderPressCheck)
            }
        }

        QuestData.changed = false

        dialog.show()
    }
}
