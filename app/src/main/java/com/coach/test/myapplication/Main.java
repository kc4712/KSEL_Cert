package com.coach.test.myapplication;

import com.coach.test.myapplication.util.MwApiWrapper;
import android.app.Application;


/**
 * 앱의 첫 실행 시, 초기화를 담당.
 *
 * @author user
 *
 */
public class Main extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        new MwApiWrapper(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}