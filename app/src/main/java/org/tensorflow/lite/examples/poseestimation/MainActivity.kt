package org.tensorflow.lite.examples.poseestimation

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.AppBarConfiguration.Builder
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.examples.poseestimation.data.QuestData
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerDao
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerDataBase
import org.tensorflow.lite.examples.poseestimation.database.ExerciseDB.ExerSchema
import org.tensorflow.lite.examples.poseestimation.database.UserDB.UserDao
import org.tensorflow.lite.examples.poseestimation.database.UserDB.UserDataBase
import org.tensorflow.lite.examples.poseestimation.database.UserDB.UserSchema
import org.tensorflow.lite.examples.poseestimation.databinding.ActivityMainBinding
import org.tensorflow.lite.examples.poseestimation.ui.exercise.CameraActivity
import org.tensorflow.lite.examples.poseestimation.ui.home.QuestDialog
import org.tensorflow.lite.examples.poseestimation.ui.length.LengthActivity
import org.tensorflow.lite.examples.poseestimation.ui.statistic.StatisticFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dao: ExerDao
    private lateinit var daoUser: UserDao

    init{
        instance = this
    }
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: MainActivity? = null

        fun getInstance(): MainActivity? {
            return instance
        }
    }

    var firstAccess = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tb = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(tb)
        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
        val appBarConfiguration: AppBarConfiguration = Builder(
            R.id.navigation_dailylog, R.id.navigation_home, R.id.navigation_statistic
        )
            .build()
        val navController = findNavController(this, R.id.nav_host_fragment_activity_main)
        setupActionBarWithNavController(this, navController, appBarConfiguration)
        setupWithNavController(binding.navView, navController)

        val toolbarBodyTemplate = binding.toolbar
        setSupportActionBar(toolbarBodyTemplate)

        dao = ExerDataBase.getInstance(applicationContext).exerDao()
        CoroutineScope(Dispatchers.IO).launch {
            // ExerDB가 완전히 비어있다면 첫 접속
            if (dao.readAll().isEmpty()){
                val initData = ExerSchema(0, "0", "0", "0", "0", 0, 0, 0, "0")
                dao.create(initData)

                finishQuest(0.0)
                firstAccess = true
            }
        }

        if(firstAccess) measureOpen()

        // 접속시 DB의 운동 횟수 불러 오기
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onClick(v: View?) {
        val intent = Intent()
        val componentName = ComponentName(
            "org.tensorflow.lite.examples.poseestimation",
            "org.tensorflow.lite.examples.poseestimation.ui.exercise.MenuActivity"
        )
        intent.component = componentName
        QuestData.todayQuest()
        startActivity(intent)
    }

    // Toolbar 메뉴 클릭 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_remeasure -> {
                measureOpen()
            }
            R.id.menu_reset -> {
                StatisticFragment.getInstance()?.resetDB()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onClickQuest(v: View){
        val dialog = QuestDialog(this)
        QuestData.todayQuest()
        dialog.show()
    }

    // 툴바와 res/menu/toolbar_main을 연결
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
            R.menu.toolbar_main,
            menu
        )
        return true
    }

    private fun measureOpen(){
        startActivity(Intent(this, LengthActivity::class.java))
    }

    fun finishQuest(exp: Double){
        daoUser = UserDataBase.getInstance(applicationContext).userDao()

        CoroutineScope(Dispatchers.IO).launch {
            var userData = daoUser.readAll()

            if (userData.isEmpty()){
                val initData = UserSchema(0, 0, exp, 0F, 0F, 0F)
                daoUser.create(initData)
            }
            else {
                userData.get(0).exp += exp
                daoUser.update(userData.get(0))
            }
        }
    }
}