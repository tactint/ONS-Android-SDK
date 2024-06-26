package com.ons.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.annotation.Nullable;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ONS.start("TEST_API_KEY");
        ONS.onCreate(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        ONS.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ONS.onStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ONS.onDestroy(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ONS.onNewIntent(this, intent);
    }
}
