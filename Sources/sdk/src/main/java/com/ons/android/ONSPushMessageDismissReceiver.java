package com.ons.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.ons.android.annotation.PublicSDK;
import com.ons.android.compat.WakefulBroadcastReceiver;
import com.ons.android.core.Logger;
import com.ons.android.di.providers.EventDispatcherModuleProvider;
import com.ons.android.eventdispatcher.PushEventPayload;

/**
 * ONS's implementation of dismiss intent of push notification
 *
 */
@PublicSDK
public class ONSPushMessageDismissReceiver extends WakefulBroadcastReceiver {

    private static final String TAG = "ONSPushMessageDismissReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            Logger.internal(TAG, "Null intent");
            return;
        }

        final Bundle extras = intent.getExtras();
        if (extras == null || extras.isEmpty()) {
            Logger.internal(TAG, "Intent extras were empty, stop dispatching event");
            return;
        }

        try {
            ONSPushPayload pushPayload = ONSPushPayload.payloadFromBundle(extras);
            ONS.EventDispatcher.Payload eventPayload = new PushEventPayload(pushPayload);

            // We may come from background, try to reload dispatchers from manifest
            EventDispatcherModuleProvider.get().loadDispatcherFromContext(context);
            EventDispatcherModuleProvider
                .get()
                .dispatchEvent(ONS.EventDispatcher.Type.NOTIFICATION_DISMISS, eventPayload);
        } catch (ONSPushPayload.ParsingException e) {
            Logger.internal(TAG, "Invalid payload, skip dispatchers");
        }
    }
}
