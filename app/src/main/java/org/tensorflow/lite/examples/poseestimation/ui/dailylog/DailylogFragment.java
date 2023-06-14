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
import androidx.room.Room;

import org.tensorflow.lite.examples.poseestimation.R;
import org.tensorflow.lite.examples.poseestimation.database.calenderDB.CalDataBase;
import org.tensorflow.lite.examples.poseestimation.database.calenderDB.CalSchema;
import org.tensorflow.lite.examples.poseestimation.databinding.FragmentDailylogBinding;
import org.tensorflow.lite.examples.poseestimation.ui.dailylog.DailylogViewModel;

import java.util.Calendar;

public class DailylogFragment extends Fragment {
    DatePickerDialog datePickerDialog;
    private FragmentDailylogBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DailylogViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DailylogViewModel.class);

        binding = FragmentDailylogBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final CalDataBase database = Room.databaseBuilder(getActivity(), CalDataBase.class, "calenderDB")
                .allowMainThreadQueries()
                .build();

        TextView dateText = root.findViewById(R.id.textToday);
        TextView dateNote = root.findViewById(R.id.textView5);
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calender = Calendar.getInstance();

                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String date = "" + day;
                        dateText.setText(date);
                        dateNote.setText(date);
                    }
                }, 2023, 5, calender.get(Calendar.DAY_OF_MONTH));
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