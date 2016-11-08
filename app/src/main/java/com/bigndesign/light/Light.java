package com.bigndesign.light;

import android.app.Application;

import io.realm.Realm;

public class Light extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);


    }
}
