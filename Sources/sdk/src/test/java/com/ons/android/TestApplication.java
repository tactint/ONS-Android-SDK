package com.ons.android;

import android.app.Application;

public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ONS.start("FAKE_API_KEY");
    }
}
