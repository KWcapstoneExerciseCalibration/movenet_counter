package com.kw.moveup.ui.workoutSelection;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WorkoutSelectionViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public WorkoutSelectionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("workoutSelection fragment" +
                "운동 선택 화면 입니다");
    }

    public LiveData<String> getText() {
        return mText;
    }
}