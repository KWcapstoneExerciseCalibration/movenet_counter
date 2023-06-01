package org.tensorflow.lite.examples.poseestimation
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
     *                        (왼/오른)손목, (왼/오른)엉덩이, (왼/오른)무름, (왼/오른)발목) **/

    fun colorChange(canvas: Canvas, person: Person, lineBool:List<Boolean>, jointBool: List<Boolean>){

        val paintCircle = Paint().apply {
            strokeWidth = ColorVisualization.CIRCLE_RADIUS
            color = Color.YELLOW
            style = Paint.Style.FILL
        }
        val paintLine = Paint().apply {
            strokeWidth = ColorVisualization.LINE_WIDTH
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
                    ColorVisualization.CIRCLE_RADIUS,
                    paintCircle
                )
            cache_count++
        }
    }
}