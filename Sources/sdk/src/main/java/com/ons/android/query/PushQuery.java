package com.ons.android.query;

import android.content.Context;
import com.ons.android.ONSPushRegistration;
import com.ons.android.core.NotificationAuthorizationStatus;
import com.ons.android.di.providers.ONSNotificationChannelsManagerProvider;
import com.ons.android.json.JSONException;
import com.ons.android.json.JSONObject;

/**
 * Query to send push token to server
 *
 */
public class PushQuery extends Query {

    /**
     * Registration information
     */
    private ONSPushRegistration registration;

    // -------------------------------------------->

    public PushQuery(Context context, ONSPushRegistration registration) {
        super(context, QueryType.PUSH);
        if (registration == null) {
            throw new NullPointerException("registration==null");
        }

        this.registration = registration;
    }

    // -------------------------------------------->

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject obj = super.toJSON();
        obj.put("tok", registration.getToken());
        obj.put("provider", registration.getProvider());
        obj.put("senderid", registration.getSenderID() != null ? registration.getSenderID() : JSONObject.NULL);
        obj.put(
            "gcpprojectid",
            registration.getGcpProjectID() != null ? registration.getGcpProjectID() : JSONObject.NULL
        );
        obj.put("nty", getNotificationType());

        return obj;
    }

    /**
     * Get the current notification type
     *
     * @return
     */
    private int getNotificationType() {
        // 15 = alert + sound + vibrate + lights
        return NotificationAuthorizationStatus.canAppShowNotifications(
                getContext(),
                ONSNotificationChannelsManagerProvider.get()
            )
            ? 15
            : 0;
    }
}
