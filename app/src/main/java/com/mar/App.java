package com.mar;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Initialize Utils Tool Library with Application Context
        Utils.init(getApplicationContext());
    }
}
