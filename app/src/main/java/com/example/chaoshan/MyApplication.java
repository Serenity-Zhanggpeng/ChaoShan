package com.example.chaoshan;

import android.app.Application;



import org.litepal.LitePal;



public class MyApplication extends Application {
    private static MyApplication instance;
    public static synchronized MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        LitePal.initialize(instance);



    }






}
