package com.bigndesign.light;

import android.app.Application;

import im.delight.android.ddp.MeteorSingleton;
import im.delight.android.ddp.db.memory.InMemoryDatabase;
import io.realm.Realm;

public class Light extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        String serverUrl = "wss://light-server-yrllrmcjbl.now.sh/websocket";

        //PRODUCTION
        MeteorSingleton.createInstance(this, serverUrl, new InMemoryDatabase());
    }
}
