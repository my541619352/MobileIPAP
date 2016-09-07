package com.tjsinfo.mobileipap;

import android.app.Application;
import android.content.Context;

/**
 * Created by zhanbo on 2016/9/5.
 */
public class App extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }

}
