package com.example.flytoyou.srmanager.Util;

import android.app.Application;

import com.example.flytoyou.srmanager.Bean.User;

/**
 * Created by flytoyou on 2017/3/1.
 */

public class App extends Application {
    public static User user;
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
