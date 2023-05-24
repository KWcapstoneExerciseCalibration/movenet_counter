package org.tensorflow.lite.examples.poseestimation.ui.dailylog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.tensorflow.lite.examples.poseestimation.databinding.FragmentDailylogBinding;

public class DailylogFragment extends Fragment {

    private FragmentDailylogBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DailylogViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DailylogViewModel.class);

        binding = FragmentDailylogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}