package org.tensorflow.lite.examples.poseestimation.ui.dailylog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.EditText
import org.tensorflow.lite.examples.poseestimation.R

class ImpressionDialog(context: Context) {
    private val dialog = Dialog(context).apply {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_impression)
        setCancelable(true)
    }

    fun show(note: String) {
        val okBtn = dialog.findViewById<Button>(R.id.okBtn)
        val cancelBtn = dialog.findViewById<Button>(R.id.cancelBtn)
        val noteEditText = dialog.findViewById<EditText>(R.id.todayNote)

        okBtn.setOnClickListener {
            onClickedListener.onClicked(noteEditText.text.toString())
            dialog.dismiss()
        }
        cancelBtn.setOnClickListener {  dialog.dismiss()  }

        if (note == "오늘은 아직 소감을 적지 않았습니다!")    noteEditText.setText("")
        else                                                noteEditText.setText(note)
        dialog.show()
    }

    interface ButtonClickListener {
        fun onClicked(note: String)
    }

    private lateinit var onClickedListener: ButtonClickListener

    fun setOnClickedListener(listener: ButtonClickListener) {
        onClickedListener = listener
    }
}


