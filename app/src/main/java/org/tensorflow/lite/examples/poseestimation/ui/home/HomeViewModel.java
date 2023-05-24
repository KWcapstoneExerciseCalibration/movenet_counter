package org.tensorflow.lite.examples.poseestimation.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Home fragment" +
                "메인 화면 입니다");
    }

    public LiveData<String> getText() {
        return mText;
    }
}