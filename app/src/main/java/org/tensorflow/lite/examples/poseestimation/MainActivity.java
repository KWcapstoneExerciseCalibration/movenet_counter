package org.tensorflow.lite.examples.poseestimation;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.examples.poseestimation.databinding.ActivityMainBinding;
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void onClick(View v){
        Intent intent = new Intent();

        ComponentName componentName = new ComponentName(
                "org.tensorflow.lite.examples.poseestimation",
                "org.tensorflow.lite.examples.poseestimation.CameraActivity");
        intent.setComponent(componentName);

        startActivity(intent);
    }
}
