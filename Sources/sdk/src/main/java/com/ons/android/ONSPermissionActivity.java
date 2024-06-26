package com.ons.android;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.ons.android.annotation.PublicSDK;
import com.ons.android.core.GenericHelper;
import com.ons.android.core.Logger;
import com.ons.android.core.Parameters;
import com.ons.android.di.providers.LocalBroadcastManagerProvider;
import java.util.Date;

/**
 * Dummy activity that ask a runtime permission to the user
 */
@PublicSDK
public class ONSPermissionActivity extends Activity {

    private static final String TAG = "ONSPermissionActivity";

    // Delay when getting a denied permission is consider as auto, meaning permission has not been asked (in ms).
    private static final int DETECT_PERMISSION_ALREADY_DENIED_THRESHOLD = 650;

    // Intent action name broadcasted when permission result is received
    public static final String ACTION_PERMISSION_RESULT = Parameters.LIBRARY_BUNDLE + "activity.permission.result";

    // Intent extra key for getting the permission to request
    public static final String EXTRA_PERMISSION = "permission";

    // Intent extra key for getting the permission result
    public static final String EXTRA_RESULT = "result";

    // Intent extra key for redirect user to settings is permission has been already denied
    public static final String EXTRA_REDIRECT_SETTINGS = "should_redirect_settings";

    // ONS permission request code
    private static final int BATCH_PERMISSION_REQUEST_CODE = 51;

    // Permission to request (must be a permission name as the manifest)
    private String permission;

    // Timestamp when the request permission method has been called
    private long requestPermissionTimestamp = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ONS.onCreate(this);
        permission = getIntent().getStringExtra(EXTRA_PERMISSION);
        if (permission == null) {
            Logger.internal(TAG, "Cannot start permission activity without extra information");
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ONS.onStart(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermission();
        } else {
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        if (GenericHelper.checkPermission(permission, this)) {
            Logger.internal(TAG, "Permission " + permission + " is already granted, not requesting permission.");
            finish();
            return;
        }
        requestPermissionTimestamp = new Date().getTime();
        requestPermissions(new String[] { permission }, BATCH_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(
        int requestCode,
        @NonNull String[] permissions,
        @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == BATCH_PERMISSION_REQUEST_CODE) {
            long now = new Date().getTime();
            boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            Logger.internal(TAG, "Permission " + permission + ", granted: " + granted);

            // If denied, trying to detect if the permission has been really asked to the user or
            // the system auto deny it (meaning user has been already asked twice and denied it)
            boolean shouldRedirectToSettings = false;
            if (!granted) {
                // Checking if we should show a rationale (in this case, we can ask permission again)
                boolean shouldShowRationale = false;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    shouldShowRationale = shouldShowRequestPermissionRationale(permission);
                }
                // Evaluating if it could be a human response time or not
                boolean isNotHumanResponseTime =
                    (now - requestPermissionTimestamp) <= DETECT_PERMISSION_ALREADY_DENIED_THRESHOLD;
                shouldRedirectToSettings = isNotHumanResponseTime && !shouldShowRationale;
            }

            // Broadcasting result
            final Intent intent = new Intent(ACTION_PERMISSION_RESULT);
            intent.putExtra(EXTRA_PERMISSION, permission);
            intent.putExtra(EXTRA_RESULT, granted);
            intent.putExtra(EXTRA_REDIRECT_SETTINGS, shouldRedirectToSettings);
            LocalBroadcastManagerProvider.get(this).sendBroadcast(intent);
        }
        finish();
    }

    @Override
    protected void onStop() {
        ONS.onStop(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ONS.onDestroy(this);
        super.onDestroy();
    }
}
