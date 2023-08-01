package org.tensorflow.lite.examples.poseestimation
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi
import org.tensorflow.lite.examples.poseestimation.data.BodyPart
import org.tensorflow.lite.examples.poseestimation.data.Person


object ColorVisualization {
    /** Radius of circle used to draw keypoints.  */
    private const val CIRCLE_RADIUS = 6f

    /** Width of line used to connected two keypoints.  */
    private const val LINE_WIDTH = 4f

    /** Pair of keypoints to draw lines between.  */
    private val bodyJoints = listOf(
        Pair(BodyPart.NOSE, BodyPart.LEFT_EYE),
        Pair(BodyPart.NOSE, BodyPart.RIGHT_EYE),
        Pair(BodyPart.LEFT_EYE, BodyPart.LEFT_EAR),
        Pair(BodyPart.RIGHT_EYE, BodyPart.RIGHT_EAR),
        Pair(BodyPart.NOSE, BodyPart.LEFT_SHOULDER),
        Pair(BodyPart.NOSE, BodyPart.RIGHT_SHOULDER),
        Pair(BodyPart.LEFT_SHOULDER, BodyPart.LEFT_ELBOW),
        Pair(BodyPart.RIGHT_SHOULDER, BodyPart.RIGHT_ELBOW),
        Pair(BodyPart.LEFT_ELBOW, BodyPart.LEFT_WRIST),
        Pair(BodyPart.RIGHT_ELBOW, BodyPart.RIGHT_WRIST),
        Pair(BodyPart.LEFT_SHOULDER, BodyPart.RIGHT_SHOULDER),
        Pair(BodyPart.LEFT_SHOULDER, BodyPart.LEFT_HIP),
        Pair(BodyPart.RIGHT_SHOULDER, BodyPart.RIGHT_HIP),
        Pair(BodyPart.LEFT_HIP, BodyPart.RIGHT_HIP),
        Pair(BodyPart.LEFT_HIP, BodyPart.LEFT_KNEE),
        Pair(BodyPart.RIGHT_HIP, BodyPart.RIGHT_KNEE),
        Pair(BodyPart.LEFT_KNEE, BodyPart.LEFT_ANKLE),
        Pair(BodyPart.RIGHT_KNEE, BodyPart.RIGHT_ANKLE)
    )

    /** lineBool > 18개 / 눈-코(왼/오른), 눈-귀(왼/오른), 코-어깨(왼/오른),
     *                    어깨-팔꿈치(왼/오른), 팔꿈치-손목(왼/오른), 어깨-어깨, 어깨-엉덩이(왼/오른),
     *                    엉덩이-엉덩이, 엉덩이-무릎(왼/오른), 무릎-발목(왼/오른)
     *
     *  jointBool > 17개 / 코, (왼/오른)눈, (왼/오른)귀, (왼/오른)어깨, (왼/오른)팔꿈치
     *                        (왼/오른)손목, (왼/오른)엉덩이, (왼/오른)무릎, (왼/오른)발목) **/

    fun colorChange(canvas: Canvas, person: Person, wrong:Array<Boolean>){
        val lineBool:MutableList<Boolean> = mutableListOf(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false)
        val jointBool:MutableList<Boolean> = mutableListOf(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false)

        wrong.forEachIndexed { index, bool ->
            if(bool)
                when(index){
                    0-> { // 머리
                        for(i in 0..5)
                            lineBool[i] = true
                        for(i in 0..6)
                            jointBool[i] = true
                    }
                    1-> { // 상체(팔 포함)
                        for(i in 6..13)
                            lineBool[i] = true
                        for(i in 5..12)
                            jointBool[i] = true
                    }
                    2-> { // 하체
                        for(i in 13..17)
                            lineBool[i] = true
                        for(i in 11..16)
                            jointBool[i] = true
                    }
                    3-> { // 왼팔
                        lineBool[6] = true
                        lineBool[8] = true
                        jointBool[5] = true
                        jointBool[7] = true
                        jointBool[9] = true
                    }
                    4-> { // 오른팔
                        lineBool[7] = true
                        lineBool[9] = true
                        jointBool[6] = true
                        jointBool[8] = true
                        jointBool[10] = true
                    }
                    5-> { // 몸통
                        for(i in 10..13)
                            lineBool[i] = true
                        jointBool[5] = true
                        jointBool[6] = true
                        jointBool[11] = true
                        jointBool[12] = true
                    }
                    6-> { // 왼다리
                        lineBool[14] = true
                        lineBool[16] = true
                        jointBool[11] = true
                        jointBool[13] = true
                        jointBool[15] = true
                    }
                    7-> { // 오른다리
                        lineBool[15] = true
                        lineBool[17] = true
                        jointBool[12] = true
                        jointBool[14] = true
                        jointBool[16] = true
                    }
                }
        }

        val paintCircle = Paint().apply {
            strokeWidth = CIRCLE_RADIUS
            color = Color.YELLOW
            style = Paint.Style.FILL
        }
        val paintLine = Paint().apply {
            strokeWidth = LINE_WIDTH
            color = Color.YELLOW
            style = Paint.Style.STROKE
        }
        var cache_count = 0

        bodyJoints.forEach {
            val pointA = person.keyPoints[it.first.position].coordinate
            val pointB = person.keyPoints[it.second.position].coordinate
            if(lineBool[cache_count])
                canvas.drawLine(pointA.x, pointA.y, pointB.x, pointB.y, paintLine)
            cache_count++
        }

        cache_count = 0
        person.keyPoints.forEach { point ->
            if(jointBool[cache_count])
                canvas.drawCircle(
                    point.coordinate.x,
                    point.coordinate.y,
                    CIRCLE_RADIUS,
                    paintCircle
                )
            cache_count++
        }
    }
}