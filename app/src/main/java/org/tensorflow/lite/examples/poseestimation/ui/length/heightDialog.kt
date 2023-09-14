package org.tensorflow.lite.examples.poseestimation.ui.length

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import org.tensorflow.lite.examples.poseestimation.R

class heightDialog(context: Context) {
    var firstAccess = false

    private val dialog = Dialog(context).apply {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_height)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
    }

    fun show(height:Float) {
        val numPick1 = dialog.findViewById<NumberPicker>(R.id.numPick1)
        val numPick2 = dialog.findViewById<NumberPicker>(R.id.numPick2)
        val okBtn = dialog.findViewById<Button>(R.id.okBtn)
        val cancelBtn = dialog.findViewById<Button>(R.id.cancelBtn)
        val comment = dialog.findViewById<TextView>(R.id.comment)

        numPick1.minValue = 0
        numPick1.maxValue = 250
        numPick1.value = height.toInt()

        numPick2.minValue = 0
        numPick2.maxValue = 9
        numPick2.value = ((height*10) % 10).toInt()

        if(!firstAccess){
            comment.text = ""
            comment.height = 0
        }

        cancelBtn.setOnClickListener { dialog.dismiss() }
        okBtn.setOnClickListener {
            onClickedListener.onClicked(numPick1.value, numPick2.value)
            dialog.dismiss()
        }

        dialog.show()
    }

    interface ButtonClickListener {
        fun onClicked(height1 : Int, height2 : Int)
    }

    private lateinit var onClickedListener: ButtonClickListener

    fun setOnClickedListener(listener: ButtonClickListener) {
        onClickedListener = listener
    }
}


