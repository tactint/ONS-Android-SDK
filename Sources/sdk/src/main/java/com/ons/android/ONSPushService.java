package com.ons.android;

import android.app.IntentService;
import android.content.Intent;
import com.ons.android.annotation.PublicSDK;
import com.ons.android.core.Logger;

/**
 * ONS's service for handling the push messages and show a notification
 * <p>
 * This can be used on Android O, if eligibility has been verified beforehand and startService
 * exceptions are handled.
 *
 */
@PublicSDK
public class ONSPushService extends IntentService {

    private static final String TAG = "ONSPushService";

    public ONSPushService() {
        super("ONSPushService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            if (intent == null) {
                Logger.internal(TAG, "Error while handling notification: null intent");
                return;
            }
            ONSPushNotificationPresenter.displayForPush(this, intent.getExtras());
        } catch (NotificationInterceptorRuntimeException nie) {
            throw nie.getWrappedRuntimeException();
        } catch (Exception e) {
            Logger.internal(TAG, "Error while handing notification", e);
        } finally {
            // Release the wake lock provided by the WakefulBroadcastReceiver.
            ONSPushMessageReceiver.completeWakefulIntent(intent);
        }
    }
}
