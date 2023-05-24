package org.tensorflow.lite.examples.poseestimation;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.examples.poseestimation.databinding.ActivityHomeBinding;
public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void onClick(View v){
        Intent intent = new Intent();

        ComponentName componentName = new ComponentName(
                "org.tensorflow.lite.examples.poseestimation",
                "org.tensorflow.lite.examples.poseestimation.MainActivity");
        intent.setComponent(componentName);

        startActivity(intent);
    }
}
