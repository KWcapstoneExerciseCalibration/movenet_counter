package org.tensorflow.lite.examples.poseestimation.ui.dailylog;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.tensorflow.lite.examples.poseestimation.R;
import org.tensorflow.lite.examples.poseestimation.databinding.FragmentDailylogBinding;
import org.tensorflow.lite.examples.poseestimation.ui.dailylog.DailylogViewModel;

import java.util.Calendar;

public class DailylogFragment extends Fragment {
    DatePickerDialog datePickerDialog;
    TextView dateText;
    private FragmentDailylogBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DailylogViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DailylogViewModel.class);

        binding = FragmentDailylogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        dateText = root.findViewById(R.id.textToday);
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calender = Calendar.getInstance();
                int pDate = 1; //from database

                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String date = "" + day;
                        dateText.setText(date);
                    }
                }, 2023, 6, calender.get(pDate));
                datePickerDialog.show();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}