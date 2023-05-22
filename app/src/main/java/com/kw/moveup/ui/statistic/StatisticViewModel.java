package com.kw.moveup.ui.statistic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StatisticViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public StatisticViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Statistic fragment" +
                "통계 화면 입니다");
    }

    public LiveData<String> getText() {
        return mText;
    }
}