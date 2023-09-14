package org.tensorflow.lite.examples.poseestimation.ui.statistic

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.ImageButton
import android.widget.NumberPicker
import androidx.navigation.fragment.findNavController
import org.tensorflow.lite.examples.poseestimation.MainActivity
import org.tensorflow.lite.examples.poseestimation.R

class weightDialog(context: Context) {

    private val dialog = Dialog(context).apply {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_weight)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
    }

    fun show(weight:Int) {
        val numPick = dialog.findViewById<NumberPicker>(R.id.numPick)
        val cancelBtn = dialog.findViewById<Button>(R.id.cancelBtn)
        val okBtn = dialog.findViewById<Button>(R.id.okBtn)

        numPick.minValue = 0
        numPick.maxValue = 200
        numPick.value = weight


        cancelBtn.setOnClickListener { dialog.dismiss() }
        okBtn.setOnClickListener {
            onClickedListener.onClicked(numPick.value)
            dialog.dismiss()
        }

        dialog.show()
    }

    interface ButtonClickListener {
        fun onClicked(weight : Int)
    }

    private lateinit var onClickedListener: ButtonClickListener

    fun setOnClickedListener(listener: ButtonClickListener) {
        onClickedListener = listener
    }
}


