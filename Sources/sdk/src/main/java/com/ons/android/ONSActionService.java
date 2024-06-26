package com.ons.android;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.core.app.NotificationManagerCompat;
import com.ons.android.annotation.PublicSDK;
import com.ons.android.core.Logger;
import com.ons.android.di.providers.ActionModuleProvider;
import com.ons.android.json.JSONException;
import com.ons.android.json.JSONObject;
import java.util.Locale;

/**
 * Service that ONS uses to respond to actions when not in an activity
 */
@PublicSDK
public final class ONSActionService extends IntentService {

    private static final String TAG = "ONSActionService";
    static final String INTENT_ACTION = "com.ons.android.action.exec";
    static final String ACTION_EXTRA_IDENTIFIER = "actionID";
    static final String ACTION_EXTRA_ARGS = "args";
    static final String ACTION_EXTRA_DISMISS_NOTIFICATION_ID = "dismissNotificationID";

    public ONSActionService() {
        super("ONSActionService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Logger.internal(TAG, "Handling intent " + intent);
        final Bundle extras = intent.getExtras();
        if (extras == null) {
            return;
        }

        // Dismiss the origin notification
        int notificationId = intent.getIntExtra(ACTION_EXTRA_DISMISS_NOTIFICATION_ID, 0);

        if (notificationId != 0) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.cancel(ONS.NOTIFICATION_TAG, notificationId);
        }

        final String actionIdentifier = intent.getStringExtra(ACTION_EXTRA_IDENTIFIER);

        if (TextUtils.isEmpty(actionIdentifier)) {
            Logger.error(TAG, "Empty or null action identifier, aborting");
            return;
        }

        JSONObject actionArgs = null;
        final String actionArgsString = intent.getStringExtra(ACTION_EXTRA_ARGS);
        if (actionArgsString != null) {
            try {
                actionArgs = new JSONObject(actionArgsString);
            } catch (JSONException e) {
                Logger.error(TAG, "Unexpected error while decoding json action arguments", e);
            }
        }

        if (actionArgs == null) {
            actionArgs = new JSONObject();
        }

        ONSPushPayload payload = null;

        try {
            payload = ONSPushPayload.payloadFromBundle(extras);
        } catch (ONSPushPayload.ParsingException e) {
            Logger.error(TAG, "Unexpected error while decoding ONSPushPayload", e);
        }

        if (
            ActionModuleProvider.get().performAction(this, actionIdentifier.toLowerCase(Locale.US), actionArgs, payload)
        ) {
            Logger.internal(TAG, "Action executed");
        }
    }
}
