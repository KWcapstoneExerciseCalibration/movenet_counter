/* Copyright 2021 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================
*/

package org.tensorflow.lite.examples.poseestimation.ui.exercise

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Process
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.examples.poseestimation.MainActivity
import org.tensorflow.lite.examples.poseestimation.R
import org.tensorflow.lite.examples.poseestimation.camera.CameraSource
import org.tensorflow.lite.examples.poseestimation.counter.PushupCounter
import org.tensorflow.lite.examples.poseestimation.counter.ShoulderPressCounter
import org.tensorflow.lite.examples.poseestimation.counter.SquatCounter
import org.tensorflow.lite.examples.poseestimation.counter.WorkoutCounter
import org.tensorflow.lite.examples.poseestimation.data.Device
import org.tensorflow.lite.examples.poseestimation.data.QuestData
import org.tensorflow.lite.examples.poseestimation.data.zFinder
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerDao
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerDataBase
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerSchema
import org.tensorflow.lite.examples.poseestimation.database.LengthDB.LengthDao
import org.tensorflow.lite.examples.poseestimation.database.LengthDB.LengthDataBase
import org.tensorflow.lite.examples.poseestimation.database.LengthDB.LengthSchema
import org.tensorflow.lite.examples.poseestimation.database.UserDB.UserDataBase
import org.tensorflow.lite.examples.poseestimation.database.UserDB.UserSchema
import org.tensorflow.lite.examples.poseestimation.ml.*
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : AppCompatActivity() {
    private lateinit var dao: ExerDao
    private lateinit var daoLen: LengthDao
    init{
        instance = this
    }
    companion object {
        private const val FRAGMENT_DIALOG = "dialog"

        @SuppressLint("StaticFieldLeak")
        private var instance: CameraActivity? = null

        fun getInstance(): CameraActivity? {
            return instance
        }

        // Start: 운동 숫자 count
        var workoutCounter : WorkoutCounter = PushupCounter()
        // End
    }

    /** A [SurfaceView] for camera preview.   */
    private lateinit var surfaceView: SurfaceView

    /** Default pose estimation model is 1 (MoveNet Thunder) **/
    private var modelPos = 1

    /** Default device is CPU */
    private var device = Device.CPU

    // Airysm: 운동 숫자 text
    private lateinit var count_text: TextView
    private lateinit var btn_stop: Button
    // End

    // Intent
    private lateinit var intent_result: Intent

    // progressbar
    private lateinit var progress:ProgressBar

    // tts
    private var tts: android.speech.tts.TextToSpeech? = null

    // AlertDialog가 띄워져 있는지
    private var alter: Boolean = false

    private lateinit var spnDevice: Spinner
    private lateinit var spnModel: Spinner
    private lateinit var spnTracker: Spinner
    private lateinit var vTrackerOption: View
    private lateinit var swClassification: SwitchCompat
    private lateinit var vClassificationOption: View
    private var cameraSource: CameraSource? = null
    private var isClassifyPose = false
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                openCamera()
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
                ErrorDialog.newInstance(getString(R.string.tfe_pe_request_permission))
                    .show(supportFragmentManager, FRAGMENT_DIALOG)
            }
        }
    private var changeModelListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
            // do nothing
        }

        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            changeModel(position)
        }
    }

    private var changeDeviceListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            // Do nothing
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            // do nothing
        }
    }

    private var changeTrackerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            // Do nothing
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            // do nothing
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        // keep screen on while app is running
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        daoLen = LengthDataBase.getInstance(applicationContext).lengthDao()

        // Start: 연동
        count_text = findViewById(R.id.count_tv)
        btn_stop = findViewById(R.id.btn_stop)
        progress = findViewById(R.id.progressbar)
        // End

        var exercise = intent.getStringExtra("exercise")

        spnModel = findViewById(R.id.spnModel)
        spnDevice = findViewById(R.id.spnDevice)
        spnTracker = findViewById(R.id.spnTracker)
        vTrackerOption = findViewById(R.id.vTrackerOption)
        surfaceView = findViewById(R.id.surfaceView)
        swClassification = findViewById(R.id.swPoseClassification)
        vClassificationOption = findViewById(R.id.vClassificationOption)
        initTTS()
        initSpinner()
        spnModel.setSelection(modelPos)
        if (!isCameraPermissionGranted()) {
            requestPermission()
        }

        when(exercise) {
            "PushUp"              -> {workoutCounter = PushupCounter()
                                  progress.max = 90}
            "Squat"               -> {workoutCounter = SquatCounter()
                                  progress.max = 80}
            "ShoulderPress"       -> {workoutCounter = ShoulderPressCounter()
                                  progress.max = 70}
            "CoursePushUp"        -> {workoutCounter = PushupCounter()
                                  progress.max = 90}
            "CourseSquat"         -> {workoutCounter = SquatCounter()
                                  progress.max = 80}
            "CourseShoulderPress" -> {workoutCounter = ShoulderPressCounter()
                                  progress.max = 70}
            else                  -> Log.d("error", "운동 종류 선택 에러")
        }

        if (exercise.equals("CoursePushUp") || exercise.equals("CourseSquat"))
            intent_result = Intent(this, GuideActivity::class.java)
        else
            intent_result = Intent(this, ResultActivity::class.java)


        // DB: 어깨 길이와 각 신체 길이 불러 오기
        CoroutineScope(Dispatchers.IO).launch {
            val userData = daoLen.readAll()

            if (userData.isEmpty()){
                val initData = LengthSchema(0,  56.8F, 47.8f, 47.8f, 57.7f, 57.7f, 66.2f, 66.2f, 37.9f, 37.9f, 38.0f, 38.0f)
                daoLen.create(initData)
            }
            else {
                val lastUserLen = userData[0]
                zFinder.shoulder = lastUserLen.shoulder
                zFinder.lastKeypointLength = mutableListOf(lastUserLen.calf_1, lastUserLen.calf_2, lastUserLen.thigh_1, lastUserLen.thigh_2, lastUserLen.body_1, lastUserLen.body_2,
                    lastUserLen.upperarm_1, lastUserLen.upperarm_2, lastUserLen.lowerarm_1, lastUserLen.lowerarm_2)
            }
        }

        // 운동 종료 버튼
        btn_stop.setOnClickListener{
            exitResultActivity(exercise)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        openCamera()
    }

    override fun onResume() {
        cameraSource?.resume()
        super.onResume()
    }

    override fun onPause() {
        cameraSource?.close()
        cameraSource = null
        super.onPause()
    }

    // check if permission is granted or not.
    private fun isCameraPermissionGranted(): Boolean {
        return checkPermission(
            Manifest.permission.CAMERA,
            Process.myPid(),
            Process.myUid()
        ) == PackageManager.PERMISSION_GRANTED
    }

    // open camera
    private fun openCamera() {
        if (isCameraPermissionGranted()) {
            if (cameraSource == null) {
                cameraSource =
                    CameraSource(surfaceView, object : CameraSource.CameraSourceListener {
                        override fun onFPSListener(fps: Int) {
                            // Do nothing
                        }

                        override fun onDetectedInfo(
                            personScore: Float?,
                            poseLabels: List<Pair<String, Float>>?
                        ) {
                            // Start: 목표 count 보여주는 textView
                            var cnt = workoutCounter.count
                            var goal = intent.getIntExtra("exercise_num", 0)

                            workoutCounter.set_goal(intent.getIntExtra("exercise_num", 0))
                            count_text.text = "목표\n$cnt / $goal"
                            // End

                            // 프로그래스바: 게이지 수정
                            progress.progress = workoutCounter.now_progress

                            // 분기: 목표와 현재의 갯수가 같아지면, 나갈지 더할지 선택
                            if(cnt == goal && alter == false && cameraSource != null) {
                                alter = true
                                ttsSpeak("빠밤")
                                var builder = AlertDialog.Builder(this@CameraActivity)
                                builder.setTitle("멋져요! 목표치에 도달하셨습니다!")
                                    .setMessage("운동을 더 하시겠습니까?")
                                    .setPositiveButton("네",
                                        DialogInterface.OnClickListener { dialog, id ->
                                            // Do nothing
                                        })
                                    .setNegativeButton("아니오",
                                        DialogInterface.OnClickListener { dialog, id ->
                                            exitResultActivity(intent.getStringExtra("exercise"))
                                        })
                                builder.show()
                            }
                        }

                    }).apply {
                        prepareCamera()
                    }
                isPoseClassifier()
                lifecycleScope.launch(Dispatchers.Main) {
                    cameraSource?.initCamera()
                }
            }
            createPoseEstimator()
        }
    }

    private fun exitResultActivity(exercise: String?){
        intent_result.putExtra("score", intent.getIntExtra("score", 0) + workoutCounter.score)
        intent_result.putExtra("count", workoutCounter.count)
        intent_result.putExtra("wrongArrayList", workoutCounter.wrongArray)

        when(exercise) {
            "CoursePushUp"  -> {intent_result.putExtra("exercise", "CourseSquat")}
            "CourseSquat"   -> {intent_result.putExtra("exercise", "CourseShoulderPress")}
            else            -> {intent_result.putExtra("exercise", exercise)}
        }

        // ExerciseDB 에 data 삽입
        dao = ExerDataBase.getInstance(applicationContext).exerDao()

            val currentTime : Long = System.currentTimeMillis()
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            date.timeZone = TimeZone.getTimeZone("GMT+09:00")
            val time = SimpleDateFormat("HH:mm:ss")
            time.timeZone = TimeZone.getTimeZone("GMT+09:00")
            val exerciseData = ExerSchema(currentTime, date.format(currentTime), time.format(currentTime), intent.getStringExtra("exercise"), "note", 0, workoutCounter.count, intent.getIntExtra("score", 0) + workoutCounter.score, "correction")

        CoroutineScope(Dispatchers.IO).launch {
            dao.create(exerciseData)
        }

        if(workoutCounter.count != 0)
            MainActivity.getInstance()?.expInput((workoutCounter.score * workoutCounter.count / 10.0))

        QuestData.questChanged(exercise)

        workoutCounter.reset()
        startActivity(intent_result)
        finish()
    }

    private fun isPoseClassifier() {
        cameraSource?.setClassifier(if (isClassifyPose) PoseClassifier.create(this) else null)
    }

    // Initialize spinners to let user select model/accelerator/tracker.
    private fun initSpinner() {
        ArrayAdapter.createFromResource(
            this,
            R.array.tfe_pe_models_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spnModel.adapter = adapter
            spnModel.onItemSelectedListener = changeModelListener
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.tfe_pe_device_name, android.R.layout.simple_spinner_item
        ).also { adaper ->
            adaper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spnDevice.adapter = adaper
            spnDevice.onItemSelectedListener = changeDeviceListener
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.tfe_pe_tracker_array, android.R.layout.simple_spinner_item
        ).also { adaper ->
            adaper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spnTracker.adapter = adaper
            spnTracker.onItemSelectedListener = changeTrackerListener
        }
    }

    private fun initTTS(){
        tts = android.speech.tts.TextToSpeech(this) {
            if (it == android.speech.tts.TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.KOREAN)
                if (result == android.speech.tts.TextToSpeech.LANG_MISSING_DATA || result == android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.d("TextToSpeech", "Language not supported")
                } else {
                    Log.d("TextToSpeech", "TTS setting succeed")
                }
            } else {
                Log.d("TextToSpeech", "TTS init failed")
            }
        }
    }

    fun ttsSpeak(strTTS:String){
        tts?.speak(strTTS, android.speech.tts.TextToSpeech.QUEUE_ADD, null, null)
    }

    // Change model when app is running
    private fun changeModel(position: Int) {
        if (modelPos == position) return
        modelPos = 1
        createPoseEstimator()
    }

    private fun createPoseEstimator() {
        // For MoveNet MultiPose, hide score and disable pose classifier as the model returns
        // multiple Person instances.
        val poseDetector = when (modelPos) {
            1 -> {
                // MoveNet Thunder (SinglePose)
                spnTracker.setSelection(0)
                MoveNet.create(this, device, ModelType.Thunder)
            }
            else -> {
                null
            }
        }
        poseDetector?.let { detector ->
            cameraSource?.setDetector(detector)
        }
    }

    private fun requestPermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) -> {
                // You can use the API that requires the permission.
                openCamera()
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
        }
    }

    /**
     * Shows an error message dialog.
     */
    class ErrorDialog : DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(activity)
                .setMessage(requireArguments().getString(ARG_MESSAGE))
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    // do nothing
                }
                .create()

        companion object {

            @JvmStatic
            private val ARG_MESSAGE = "message"

            @JvmStatic
            fun newInstance(message: String): ErrorDialog = ErrorDialog().apply {
                arguments = Bundle().apply { putString(ARG_MESSAGE, message) }
            }
        }
    }
}
