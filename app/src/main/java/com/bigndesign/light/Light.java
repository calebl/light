package com.bigndesign.light;

import com.activeandroid.ActiveAndroid;

public class Light extends com.activeandroid.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);


    }
}
