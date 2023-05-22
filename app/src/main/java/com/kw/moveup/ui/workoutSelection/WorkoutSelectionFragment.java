package com.kw.moveup.ui.workoutSelection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.kw.moveup.databinding.FragmentWorkoutselectionBinding;

public class WorkoutSelectionFragment extends Fragment {

    private FragmentWorkoutselectionBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        WorkoutSelectionViewModel notificationsViewModel =
                new ViewModelProvider(this).get(WorkoutSelectionViewModel.class);

        binding = FragmentWorkoutselectionBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}