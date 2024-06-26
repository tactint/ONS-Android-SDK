package com.ons.android;

import android.content.Context;
import androidx.annotation.NonNull;

/**
 * @hide
 */
public class ONSNotificationChannelsManagerPrivateHelper {

    @NonNull
    public static String getChannelId(ONSNotificationChannelsManager manager) {
        return manager.getChannelId(null);
    }

    public static void registerONSChannelIfNeeded(ONSNotificationChannelsManager manager, Context context) {
        manager.registerONSChannelIfNeeded(context);
    }
}
