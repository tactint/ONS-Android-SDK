package com.ons.android.eventdispatcher;

import static com.ons.android.core.InternalPushData.BATCH_BUNDLE_KEY;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ons.android.ONS;
import com.ons.android.ONSMessage;
import com.ons.android.ONSPushPayload;

/**
 * Payload accessor for a {@link ONS.EventDispatcher.Type#NOTIFICATION_DISPLAY} or {@link ONS.EventDispatcher.Type#NOTIFICATION_OPEN}.
 */
public class PushEventPayload implements ONS.EventDispatcher.Payload {

    private ONSPushPayload payload;
    private boolean isOpening;

    public PushEventPayload(ONSPushPayload payload) {
        this(payload, false);
    }

    public PushEventPayload(ONSPushPayload payload, boolean isOpening) {
        this.payload = payload;
        this.isOpening = isOpening;
    }

    @Nullable
    @Override
    public String getTrackingId() {
        // No tracking ID in push campaign
        return null;
    }

    @Nullable
    @Override
    public String getWebViewAnalyticsID() {
        return null;
    }

    @Nullable
    @Override
    public String getDeeplink() {
        return payload.getDeeplink();
    }

    @Override
    public boolean isPositiveAction() {
        return isOpening;
    }

    @Nullable
    @Override
    public String getCustomValue(@NonNull String key) {
        if (BATCH_BUNDLE_KEY.equals(key)) {
            // Hide ons payload
            return null;
        }
        return payload.getPushBundle().getString(key);
    }

    @Nullable
    @Override
    public ONSMessage getMessagingPayload() {
        return null;
    }

    @Nullable
    @Override
    public ONSPushPayload getPushPayload() {
        return payload;
    }
}
