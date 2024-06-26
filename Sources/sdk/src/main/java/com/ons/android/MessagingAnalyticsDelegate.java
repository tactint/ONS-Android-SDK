package com.ons.android;

import static com.ons.android.ONS.EventDispatcher.Type.MESSAGING_CLICK;
import static com.ons.android.ONS.EventDispatcher.Type.MESSAGING_CLOSE;
import static com.ons.android.ONS.EventDispatcher.Type.MESSAGING_WEBVIEW_CLICK;

import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ons.android.core.Logger;
import com.ons.android.di.providers.EventDispatcherModuleProvider;
import com.ons.android.di.providers.MessagingModuleProvider;
import com.ons.android.di.providers.TrackerModuleProvider;
import com.ons.android.eventdispatcher.MessagingEventPayload;
import com.ons.android.messaging.model.Action;
import com.ons.android.messaging.model.CTA;
import com.ons.android.messaging.model.Message;
import com.ons.android.messaging.model.MessagingError;
import com.ons.android.module.EventDispatcherModule;
import com.ons.android.module.MessagingModule;
import com.ons.android.module.TrackerModule;
import com.ons.android.processor.Module;
import com.ons.android.processor.Provide;
import java.util.ArrayList;

/**
 * Class that proxies the analytics call to the messaging module but ensures stuff like triggers only
 * occurring once.
 * It handles special cases such as In-App messages tracking an occurrence
 * <p>
 * Also makes it easily mockable
 *
 * @hide
 */
@Module
public class MessagingAnalyticsDelegate {

    private static final String STATE_KEY_CALLED_METHODS = "analyticsdelegate_called_methods";

    private MessagingModule messagingModule;
    private TrackerModule trackerModule;
    private EventDispatcherModule eventDispatcherModule;
    private Message message;
    private ONSMessage sourceMessage;
    final ArrayList<String> calledMethods = new ArrayList<>(6);

    MessagingAnalyticsDelegate(
        MessagingModule messagingModule,
        TrackerModule trackerModule,
        EventDispatcherModule eventDispatcherModule,
        Message message,
        ONSMessage sourceMessage
    ) {
        this.messagingModule = messagingModule;
        this.trackerModule = trackerModule;
        this.eventDispatcherModule = eventDispatcherModule;
        this.message = message;
        this.sourceMessage = sourceMessage;
    }

    @Provide
    public static MessagingAnalyticsDelegate provide(Message message, ONSMessage sourceMessage) {
        return new MessagingAnalyticsDelegate(
            MessagingModuleProvider.get(),
            TrackerModuleProvider.get(),
            EventDispatcherModuleProvider.get(),
            message,
            sourceMessage
        );
    }

    // Returns true if the method has already been ran once
    private boolean ensureOnce(String method) {
        synchronized (calledMethods) {
            if (calledMethods.contains(method)) {
                return true;
            } else {
                calledMethods.add(method);
                return false;
            }
        }
    }

    //region User interaction

    public void onGlobalTap(@NonNull Action action) {
        if (ensureOnce("globaltap")) {
            return;
        }
        messagingModule.onMessageGlobalTap(message, action);
        ONS.EventDispatcher.Type type = MESSAGING_CLICK;
        if (action.isDismissAction()) {
            // We trigger a close event when the global tap is a dismiss action
            type = MESSAGING_CLOSE;
        }
        eventDispatcherModule.dispatchEvent(
            type,
            new MessagingEventPayload(
                sourceMessage,
                sourceMessage.getJSON(),
                sourceMessage.getCustomPayloadInternal(),
                action
            )
        );
    }

    public void onCTAClicked(int ctaIndex, @NonNull CTA cta) {
        if (ensureOnce("ctaclicked")) {
            return;
        }
        messagingModule.onMessageCTAClicked(message, ctaIndex, cta);

        ONS.EventDispatcher.Type type = MESSAGING_CLICK;
        if (cta.isDismissAction()) {
            // We trigger a close event when the CTA is a dismiss action
            type = MESSAGING_CLOSE;
        }
        eventDispatcherModule.dispatchEvent(
            type,
            new MessagingEventPayload(
                sourceMessage,
                sourceMessage.getJSON(),
                sourceMessage.getCustomPayloadInternal(),
                cta
            )
        );
    }

    public void onWebViewClickTracked(@NonNull Action action, @Nullable String buttonAnalyticsId) {
        // This doesn't ensureOnce by design

        if (TextUtils.isEmpty(buttonAnalyticsId)) {
            buttonAnalyticsId = null;
        }
        if (buttonAnalyticsId != null && buttonAnalyticsId.length() > 30) {
            Logger.error(
                MessagingModule.TAG,
                "Could not track webview event: The analytics ID is invalid: it should be 30 characters or less. " +
                "The action will be tracked without an analytics ID, but will still be performed."
            );
            buttonAnalyticsId = null;
        }

        ONS.EventDispatcher.Type type = MESSAGING_WEBVIEW_CLICK;
        if (action.isDismissAction()) {
            // We trigger a close event when the CTA is a dismiss action
            type = MESSAGING_CLOSE;
        }

        messagingModule.onWebViewMessageClickTracked(message, action, buttonAnalyticsId);
        eventDispatcherModule.dispatchEvent(
            type,
            new MessagingEventPayload(
                sourceMessage,
                sourceMessage.getJSON(),
                sourceMessage.getCustomPayloadInternal(),
                action,
                buttonAnalyticsId
            )
        );
    }

    // Closed is when the user explicitly closes the message
    public void onClosed() {
        if (ensureOnce("closed")) {
            return;
        }
        messagingModule.onMessageClosed(message);
        eventDispatcherModule.dispatchEvent(
            ONS.EventDispatcher.Type.MESSAGING_CLOSE,
            new MessagingEventPayload(sourceMessage, sourceMessage.getJSON(), sourceMessage.getCustomPayloadInternal())
        );
    }

    public void onClosedError(@NonNull MessagingError cause) {
        if (ensureOnce("closederror")) {
            return;
        }
        messagingModule.onMessageClosedError(message, cause);
        eventDispatcherModule.dispatchEvent(
            ONS.EventDispatcher.Type.MESSAGING_CLOSE_ERROR,
            new MessagingEventPayload(sourceMessage, sourceMessage.getJSON(), sourceMessage.getCustomPayloadInternal())
        );
    }

    //endregion

    //region View lifecycle

    public void onAutoClosedAfterDelay() {
        if (ensureOnce("autoclosed")) {
            return;
        }
        messagingModule.onMessageAutoClosed(message);
        eventDispatcherModule.dispatchEvent(
            ONS.EventDispatcher.Type.MESSAGING_AUTO_CLOSE,
            new MessagingEventPayload(sourceMessage, sourceMessage.getJSON(), sourceMessage.getCustomPayloadInternal())
        );
    }

    public void onViewShown() {
        if (ensureOnce("viewshown")) {
            return;
        }
        messagingModule.onMessageShown(message);
        if (sourceMessage instanceof ONSInAppMessage) {
            ONSInAppMessage inAppMessage = (ONSInAppMessage) sourceMessage;
            trackerModule.trackCampaignView(inAppMessage.getCampaignId(), inAppMessage.getEventData());
        }

        eventDispatcherModule.dispatchEvent(
            ONS.EventDispatcher.Type.MESSAGING_SHOW,
            new MessagingEventPayload(sourceMessage, sourceMessage.getJSON(), sourceMessage.getCustomPayloadInternal())
        );
    }

    public void onViewDismissed() {
        if (ensureOnce("viewdismissed")) {
            return;
        }
        messagingModule.onMessageDismissed(message);
    }

    //endregion

    //region State saving

    public void restoreState(@Nullable Bundle inState) {
        if (inState != null) {
            ArrayList<String> stateCalledMethods = inState.getStringArrayList(STATE_KEY_CALLED_METHODS);
            if (stateCalledMethods != null) {
                calledMethods.addAll(stateCalledMethods);
            }
        }
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putStringArrayList(STATE_KEY_CALLED_METHODS, calledMethods);
    }
    //endregion
}
