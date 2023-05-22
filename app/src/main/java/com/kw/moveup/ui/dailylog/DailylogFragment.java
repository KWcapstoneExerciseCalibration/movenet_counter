package com.kw.moveup.ui.dailylog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.kw.moveup.databinding.FragmentDailylogBinding;

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