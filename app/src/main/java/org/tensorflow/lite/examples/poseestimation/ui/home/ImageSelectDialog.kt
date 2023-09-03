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
import org.tensorflow.lite.examples.poseestimation.database.UserDB.UserDataBase

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
        val cancelBtn = dialog.findViewById<ImageButton>(R.id.cancelBtn)

        val imageProfile = MainActivity.getInstance()?.findViewById<ImageButton>(R.id.imageProfile)
        val level = MainActivity.getInstance()?.levelGet()

        for (i: Int in numImgBtnId.indices)
            numImgBtn[i] = dialog.findViewById(numImgBtnId[i])

        for (i: Int in 3 until numImgBtnId.size) {
            val limit = (i - 2) * 5
            if (level!! < limit) {
                when (limit) {
                    5  -> numImgBtn[i]?.setImageResource(R.drawable.lock5)
                    10 -> numImgBtn[i]?.setImageResource(R.drawable.lock10)
                    15 -> numImgBtn[i]?.setImageResource(R.drawable.lock15)
                    20 -> numImgBtn[i]?.setImageResource(R.drawable.lock20)
                    25 -> numImgBtn[i]?.setImageResource(R.drawable.lock25)
                    30 -> numImgBtn[i]?.setImageResource(R.drawable.lock30)
                }
                numImgBtn[i]?.isEnabled = false
            }
            else {
                // 이미지 번호 별로 이미지를 바꿔 줘야 하는 기능 추가 해야 할 것
                when (limit) {
                    5  -> numImgBtn[i]?.setImageResource(R.drawable.ham)
                    10 -> numImgBtn[i]?.setImageResource(R.drawable.ham)
                    15 -> numImgBtn[i]?.setImageResource(R.drawable.ham)
                    20 -> numImgBtn[i]?.setImageResource(R.drawable.ham)
                    25 -> numImgBtn[i]?.setImageResource(R.drawable.ham)
                    30 -> numImgBtn[i]?.setImageResource(R.drawable.ham)
                }
                numImgBtn[i]?.isEnabled = true
            }
        }

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
