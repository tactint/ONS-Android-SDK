package com.ons.android;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.ons.android.annotation.PublicSDK;
import com.ons.android.json.JSONException;
import com.ons.android.json.JSONObject;
import java.util.Set;

/**
 * A subclass of ONSMessage that represents a push landing message
 */
@PublicSDK
public class ONSLandingMessage extends ONSMessage implements PushUserActionSource {

    public static final String KIND = "landing";

    private static final String GOOGLE_PREFIX = "google.";

    private Bundle payload;
    private JSONObject landing;

    /**
     * Flag indication whether this landing message has been triggered from an inbox notification.
     * Default: false (meaning the display has been triggered from a push notification )
     */
    private boolean isDisplayedFromInbox = false;

    protected ONSLandingMessage(@NonNull Bundle rawPayload, @NonNull JSONObject parsedLanding) {
        payload = cleanBundle(rawPayload);
        landing = parsedLanding;
    }

    /**
     * Remove reserved firebase's keys from a bundle
     *
     * @param bundle bundle to clean
     * @return A cleaned bundle without google's keys
     */
    private Bundle cleanBundle(Bundle bundle) {
        Bundle copy = new Bundle(bundle);
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            if (key != null && key.startsWith(GOOGLE_PREFIX)) {
                copy.remove(key);
            }
        }
        return copy;
    }

    @Override
    protected JSONObject getJSON() {
        return landing;
    }

    /**
     * @hide
     */
    @Override
    protected JSONObject getCustomPayloadInternal() {
        JSONObject json = new JSONObject();
        Set<String> keys = payload.keySet();
        for (String key : keys) {
            Object value = payload.get(key);
            if (value instanceof String) {
                try {
                    json.put(key, value.toString());
                } catch (JSONException e) {
                    //ignore
                }
            }
        }
        return json;
    }

    @Override
    protected String getKind() {
        return KIND;
    }

    @Override
    protected Bundle getBundleRepresentation() {
        // This is way less costly than creating a ONSPushPayload to get its serialization method
        // But if ONSPushPayload's changes, we'll need to change it here
        Bundle b = new Bundle();
        b.putBundle(ONS.Push.PAYLOAD_KEY, payload);
        return b;
    }

    public Bundle getPushBundle() {
        return new Bundle(payload);
    }

    /**
     * Whether this landing message has been triggered from an inbox notification
     * through the method {@link ONSInboxNotificationContent#displayLandingMessage}.
     *
     * @return true if displayed from inbox, false if from a push notification.
     */
    public boolean isDisplayedFromInbox() {
        return isDisplayedFromInbox;
    }

    void setIsDisplayedFromInbox(boolean triggeredFromInbox) {
        isDisplayedFromInbox = triggeredFromInbox;
    }
}
