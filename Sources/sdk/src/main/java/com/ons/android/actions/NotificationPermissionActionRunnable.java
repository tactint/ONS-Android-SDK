package com.ons.android.actions;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ons.android.UserActionRunnable;
import com.ons.android.UserActionSource;
import com.ons.android.core.Logger;
import com.ons.android.core.NotificationPermissionHelper;
import com.ons.android.json.JSONObject;
import com.ons.android.module.ActionModule;

public class NotificationPermissionActionRunnable implements UserActionRunnable {

    private static final String TAG = "NotificationPermissionAction";
    public static final String IDENTIFIER =
        ActionModule.RESERVED_ACTION_IDENTIFIER_PREFIX + "android_request_notifications";

    @Override
    public void performAction(
        @Nullable Context context,
        @NonNull String identifier,
        @NonNull JSONObject args,
        @Nullable UserActionSource source
    ) {
        if (context == null) {
            Logger.error(TAG, "Tried to perform a notif. permission request action, but no context was available");
            return;
        }
        final NotificationPermissionHelper notificationPermissionHelper = new NotificationPermissionHelper(null);
        notificationPermissionHelper.requestPermission(context, true, null);
    }
}
