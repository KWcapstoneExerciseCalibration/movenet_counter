package org.tensorflow.lite.examples.poseestimation.ui.home

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import org.tensorflow.lite.examples.poseestimation.MainActivity
import org.tensorflow.lite.examples.poseestimation.R

class ImageSelectDialog(context: Context) {

    private val dialog = Dialog(context).apply {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_change_image)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
    }

    fun show() {
        val numImgBtn = arrayOfNulls<ImageButton>(10)
        val numImgBtnId = arrayOf(R.id.img1, R.id.img2, R.id.img3, R.id.img4, R.id.img5,
                                R.id.img6, R.id.img7, R.id.img8, R.id.img9)
        val cancelBtn = dialog.findViewById<Button>(R.id.cancelBtn)

        val imageProfile = MainActivity.getInstance()?.findViewById<ImageButton>(R.id.imageProfile)

        for (i: Int in numImgBtnId.indices)
            numImgBtn[i] = dialog.findViewById(numImgBtnId[i])

        for (i: Int in numImgBtn.indices) {
            numImgBtn[i]?.setOnClickListener {
                imageProfile?.setImageDrawable(numImgBtn[i]?.drawable)
                dialog.dismiss()
            }
        }


        cancelBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
}
