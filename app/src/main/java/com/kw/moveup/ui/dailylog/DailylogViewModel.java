package com.kw.moveup.ui.dailylog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DailylogViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DailylogViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Dailylog fragment" +
                "일정 화면 입니다");
    }

    public LiveData<String> getText() {
        return mText;
    }
}