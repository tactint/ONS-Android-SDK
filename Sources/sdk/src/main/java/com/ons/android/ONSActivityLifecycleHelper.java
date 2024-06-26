package com.ons.android;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import com.ons.android.annotation.PublicSDK;

/**
 * Implementation of {@link android.app.Application.ActivityLifecycleCallbacks} for managing ONS's lifecycle
 * <p>
 * Important note: While this removes the need for most lifecycle activities, you still <b>MUST</b> add ONS.onNewIntent(this, intent) in all your activities
 *
 */
@SuppressWarnings("unused")
@PublicSDK
public class ONSActivityLifecycleHelper implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        ONS.onCreate(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ONS.onStart(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) {}

    @Override
    public void onActivityPaused(Activity activity) {}

    @Override
    public void onActivityStopped(Activity activity) {
        ONS.onStop(activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

    @Override
    public void onActivityDestroyed(Activity activity) {
        ONS.onDestroy(activity);
    }
}
