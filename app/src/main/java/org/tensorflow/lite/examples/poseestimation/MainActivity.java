package org.tensorflow.lite.examples.poseestimation;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.tensorflow.lite.examples.poseestimation.databinding.ActivityMainBinding;
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar) ;
        setSupportActionBar(tb) ;

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_dailylog, R.id.navigation_home, R.id.navigation_statistic)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public void onClick(View v){
        Intent intent = new Intent();

        ComponentName componentName = new ComponentName(
                "org.tensorflow.lite.examples.poseestimation",
                "org.tensorflow.lite.examples.poseestimation.ui.exercise.MenuActivity");
        intent.setComponent(componentName);

        startActivity(intent);
    }
}
