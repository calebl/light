package com.bigndesign.light;

/**
 * Created by caleb on 1/2/17.
 */
import com.facebook.stetho.Stetho;

public class DebugLight extends Light {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }
}
