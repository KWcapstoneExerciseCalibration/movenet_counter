package org.tensorflow.lite.examples.poseestimation

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
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

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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
}