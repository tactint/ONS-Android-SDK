package com.ons.android;

import android.app.Application;
import com.google.firebase.FirebaseApp;

public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        ONS.start("FAKE_API_KEY");
    }
}
