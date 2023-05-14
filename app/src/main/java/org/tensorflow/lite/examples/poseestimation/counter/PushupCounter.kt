// Airysm

package org.tensorflow.lite.examples.poseestimation.counter

import android.util.Log
import android.widget.Toast
import org.tensorflow.lite.examples.poseestimation.data.BodyPart
import org.tensorflow.lite.examples.poseestimation.data.Person
import org.tensorflow.lite.examples.poseestimation.finder.zFinder

class PushupCounter : WorkoutCounter() {

    override var MIN_AMPLITUDE = 20

    var flag = 0

    override fun countAlgorithm(person: Person): Int {
        var human = zFinder().findZPerson(person, 2)

        Log.d("PushUpCounter", "PushUp Algorithm")

        if (human.keyPoints[BodyPart.LEFT_SHOULDER.ordinal].score >= MIN_CONFIDENCE &&
            human.keyPoints[BodyPart.RIGHT_SHOULDER.ordinal].score >= MIN_CONFIDENCE) {

            var angle = human.keyPoints[BodyPart.LEFT_ELBOW.ordinal].angle

            Log.d("left angle", angle.toString())

            if (flag == 0 && angle > 80.0 && angle < 100.0)
                flag++
            else if (flag == 1 && angle > 170.0 && angle < 190.0)
                flag++
            else if (flag == 2 && angle > 80.0 && angle < 100.0)
            {
                flag = 0
                count++
            }
        }

        Log.d("개수", "$count 개")


        return count
    }
}