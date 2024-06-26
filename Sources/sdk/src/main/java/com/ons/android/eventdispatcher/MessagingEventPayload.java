package com.ons.android.eventdispatcher;

import static com.ons.android.core.InternalPushData.BATCH_BUNDLE_KEY;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ons.android.ONS;
import com.ons.android.ONSLandingMessage;
import com.ons.android.ONSMessage;
import com.ons.android.ONSPushPayload;
import com.ons.android.json.JSONObject;
import com.ons.android.messaging.model.Action;

/**
 * Payload accessor for a {@link ONS.EventDispatcher.Type#MESSAGING_SHOW} and other MESSAGING events.
 */
public class MessagingEventPayload implements ONS.EventDispatcher.Payload {

    private ONSMessage message;
    private JSONObject payload;
    private JSONObject customPayload;
    private Action action;
    private String buttonAnalyticsId;

    public MessagingEventPayload(ONSMessage message, JSONObject payload, JSONObject customPayload) {
        this(message, payload, customPayload, null);
    }

    public MessagingEventPayload(
        ONSMessage message,
        JSONObject payload,
        JSONObject customPayload,
        Action action,
        String buttonAnalyticsId
    ) {
        this.message = message;
        this.payload = payload;
        this.customPayload = customPayload;
        this.action = action;
        this.buttonAnalyticsId = buttonAnalyticsId;
    }

    public MessagingEventPayload(ONSMessage message, JSONObject payload, JSONObject customPayload, Action action) {
        this(message, payload, customPayload, action, null);
    }

    @Nullable
    @Override
    public String getTrackingId() {
        if (payload != null) {
            return payload.reallyOptString("did", null);
        }
        return null;
    }

    @Nullable
    @Override
    public String getWebViewAnalyticsID() {
        return buttonAnalyticsId;
    }

    @Nullable
    @Override
    public String getDeeplink() {
        if (action != null && "ons.deeplink".equals(action.action) && action.args != null) {
            return action.args.reallyOptString("l", null);
        }
        return null;
    }

    @Override
    public boolean isPositiveAction() {
        if (action != null && !action.isDismissAction()) {
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public String getCustomValue(@NonNull String key) {
        if (customPayload == null || BATCH_BUNDLE_KEY.equals(key)) {
            // Hide ons payload
            return null;
        }

        return customPayload.reallyOptString(key, null);
    }

    @Nullable
    @Override
    public ONSMessage getMessagingPayload() {
        return message;
    }

    @Nullable
    @Override
    public ONSPushPayload getPushPayload() {
        return null;
    }
}
