package org.tensorflow.lite.examples.poseestimation

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.AppBarConfiguration.Builder
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.tensorflow.lite.examples.poseestimation.databinding.ActivityMainBinding
import org.tensorflow.lite.examples.poseestimation.ui.length.LengthActivity
import org.tensorflow.lite.examples.poseestimation.ui.statistic.StatisticFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // 첫 접속 확인용
    // DB 연동 후 true로 바꿔주세요! && 50줄 주석도 해제(?) 부탁드립니다!
    // false 변경하는 거는 ui\length\LengthActivity.kt 175줄에 있습니당
    var firstAccess = false

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
        setupWithNavController(binding!!.navView, navController)

        val toolbarBodyTemplate = binding.toolbar
        setSupportActionBar(toolbarBodyTemplate)

        // if(firstAccess) measureOpen()
    }

    fun onClick(v: View?) {
        val intent = Intent()
        val componentName = ComponentName(
            "org.tensorflow.lite.examples.poseestimation",
            "org.tensorflow.lite.examples.poseestimation.ui.exercise.MenuActivity"
        )
        intent.component = componentName
        startActivity(intent)
    }

    // 툴바와 res/menu/toolbar_main을 연결
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
            R.menu.toolbar_main,
            menu
        )
        return true
    }

    // Toolbar 메뉴 클릭 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_remeasure -> {
                startActivity(Intent(this, LengthActivity::class.java))
            }
            R.id.menu_reset -> {
                StatisticFragment.getInstance()?.resetDB()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}