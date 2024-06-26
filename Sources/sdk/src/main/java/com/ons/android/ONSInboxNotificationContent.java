package com.ons.android;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ons.android.annotation.PublicSDK;
import com.ons.android.core.Logger;
import com.ons.android.di.providers.MessagingModuleProvider;
import com.ons.android.di.providers.OptOutModuleProvider;
import com.ons.android.di.providers.RuntimeManagerProvider;
import com.ons.android.inbox.InboxNotificationContentInternal;
import com.ons.android.json.JSONObject;
import com.ons.android.module.MessagingModule;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ONSInboxNotificationContent is a model representing the content of an inbox notification
 */
@PublicSDK
public class ONSInboxNotificationContent {

    private static final String TAG = "ONSInboxNotificationContent";

    @NonNull
    InboxNotificationContentInternal internalContent;

    @Nullable
    private ONSPushPayload onsPushPayloadCache = null;

    /**
     * @param internalContent
     * @hide
     */
    protected ONSInboxNotificationContent(InboxNotificationContentInternal internalContent) {
        this.internalContent = internalContent;
    }

    /**
     * Unique identifier for this notification.
     *
     * @return The unique notification identifier. Do not make assumptions about its format: it can change at any time.
     */
    @NonNull
    public String getNotificationIdentifier() {
        return internalContent.identifiers.identifier;
    }

    @Nullable
    public String getTitle() {
        return internalContent.title;
    }

    @Nullable
    public String getBody() {
        return internalContent.body;
    }

    @NonNull
    public ONSNotificationSource getSource() {
        return internalContent.source;
    }

    public boolean isUnread() {
        return internalContent.isUnread;
    }

    @NonNull
    public Date getDate() {
        return (Date) internalContent.date.clone();
    }

    /**
     * Returns whether ONS considers this a silent notification.
     *
     * A silent notification is a notification with no title and message, which won't be displayed by
     * ONS SDK.
     * Warning: Other services listening to push messages might display it.
     */
    public boolean isSilent() {
        try {
            return internalContent.body == null || getPushPayload().getInternalData().isSilent();
        } catch (ONSPushPayload.ParsingException ignored) {
            return true;
        }
    }

    /**
     * Get the payload in its raw JSON form. This might differ from what you're used to in other classes
     * handling push payloads. If you want to simulate the push behaviour, call {@link ONSPushPayload#getPushBundle()} on the instance given by {@link #getPushPayload()} .
     */
    @NonNull
    public Map<String, String> getRawPayload() {
        return new HashMap<>(internalContent.payload);
    }

    /**
     * Get {@link ONSPushPayload} instance, property initialized with the notification's original push payload
     */
    @NonNull
    public synchronized ONSPushPayload getPushPayload() throws ONSPushPayload.ParsingException {
        // This kinds of get into a lot of hoops to work, but reworking all of these classes would need
        // a lot of refactoring, and probably require to break the public API
        if (onsPushPayloadCache == null) {
            onsPushPayloadCache = new ONSPushPayload(internalContent.getReceiverLikePayload());
        }

        return onsPushPayloadCache;
    }

    /**
     * Whether the notification content has a landing message attached
     * @return true if a landing message is attached
     */
    public boolean hasLandingMessage() {
        try {
            ONSPushPayload payload = this.getPushPayload();
            return payload.hasLandingMessage();
        } catch (ONSPushPayload.ParsingException e) {
            return false;
        }
    }

    /**
     * Display the landing message attached to a ONSInboxNotificationContent.
     * Do nothing if no message is attached.
     * <p>
     * Note that this method will work even if ONS is in do not disturb mode.
     * <p>
     * The given context should be an Activity instance to enable support for the banner format, as it
     * has to be attached to an activity.
     * @param context Your activity's context, Can't be null.
     */
    public void displayLandingMessage(@NonNull Context context) {
        if (context == null) {
            Logger.internal(TAG, "Context cannot be null.");
            return;
        }

        if (OptOutModuleProvider.get().isOptedOutSync(context)) {
            Logger.info(TAG, "Ignoring as ONS has been Opted Out from");
            return;
        }

        MessagingModule messagingModule = MessagingModuleProvider.get();

        if (!messagingModule.doesAppHaveRequiredLibraries(true)) {
            return;
        }

        if (!RuntimeManagerProvider.get().isApplicationInForeground()) {
            Logger.internal(TAG, "Trying to present landing message while application is in background.");
        }

        try {
            ONSPushPayload payload = this.getPushPayload();

            if (!payload.hasLandingMessage()) {
                Logger.internal(TAG, "No landing message present.");
                return;
            }

            JSONObject messageJSON = payload.getInternalData().getLandingMessage();
            ONSLandingMessage message = new ONSLandingMessage(payload.getPushBundle(), messageJSON);
            message.setIsDisplayedFromInbox(true);
            messagingModule.displayMessage(context, message, true);
        } catch (ONSPushPayload.ParsingException e) {
            Logger.internal("Parsing push payload has failed, cannot display landing message.");
        }
    }
}
