package com.ons.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.ons.android.annotation.PublicSDK;
import com.ons.android.core.Logger;
import com.ons.android.json.JSONObject;
import com.ons.android.messaging.PayloadParser;
import com.ons.android.messaging.model.AlertMessage;
import com.ons.android.messaging.model.BannerMessage;
import com.ons.android.messaging.model.ImageMessage;
import com.ons.android.messaging.model.Message;
import com.ons.android.messaging.model.ModalMessage;
import com.ons.android.messaging.model.UniversalMessage;
import com.ons.android.messaging.model.WebViewMessage;
import com.ons.android.module.MessagingModule;

/**
 * Model representing a ONS Messaging message.
 */
@PublicSDK
public abstract class ONSMessage implements UserActionSource {

    /**
     * Key to retrieve the messaging payload (if applicable) from an extra
     */
    public static final String MESSAGING_EXTRA_PAYLOAD_KEY = "com.ons.messaging.payload";

    private static final String KIND_KEY = "kind";

    private static final String DATA_KEY = "data";

    protected abstract JSONObject getJSON();

    /**
     * @hide
     */
    protected abstract JSONObject getCustomPayloadInternal();

    public void writeToBundle(@NonNull Bundle bundle) {
        //noinspection ConstantConditions
        if (bundle == null) {
            throw new IllegalArgumentException("bundle cannot be null");
        }

        final Bundle payloadBundle = new Bundle();
        payloadBundle.putString(KIND_KEY, getKind());
        payloadBundle.putBundle(DATA_KEY, getBundleRepresentation());

        bundle.putBundle(MESSAGING_EXTRA_PAYLOAD_KEY, payloadBundle);
    }

    public void writeToIntent(@NonNull Intent intent) {
        //noinspection ConstantConditions
        if (intent == null) {
            throw new IllegalArgumentException("intent cannot be null");
        }

        final Bundle payloadBundle = new Bundle();
        payloadBundle.putString(KIND_KEY, getKind());
        payloadBundle.putBundle(DATA_KEY, getBundleRepresentation());

        intent.putExtra(MESSAGING_EXTRA_PAYLOAD_KEY, payloadBundle);
    }

    public static ONSMessage getMessageForBundle(@NonNull Bundle bundle) throws ONSPushPayload.ParsingException {
        //noinspection ConstantConditions
        if (bundle == null) {
            throw new IllegalArgumentException("Bundle cannot be null");
        }

        final Bundle messageBundle = bundle.getBundle(MESSAGING_EXTRA_PAYLOAD_KEY);
        if (messageBundle == null) {
            throw new ONSPushPayload.ParsingException(
                "Bundle doesn't contain the required elements for reading ONSMessage"
            );
        }

        String kind = messageBundle.getString(KIND_KEY);

        if (ONSLandingMessage.KIND.equals(kind)) {
            final Bundle data = messageBundle.getBundle(DATA_KEY);
            if (data != null) {
                return ONSPushPayload.payloadFromBundle(data).getLandingMessage();
            }
        } else if (ONSInAppMessage.KIND.equals(kind)) {
            final Bundle data = messageBundle.getBundle(DATA_KEY);
            if (data != null) {
                return ONSInAppMessage.getInstanceFromBundle(data);
            }
        }

        throw new ONSPushPayload.ParsingException("Unknown ONSMessage kind");
    }

    /**
     * Returns the format of the displayable message, if any.
     * <p>
     * You should cache this result rather than access the getter multiple times, as it involves
     * some computation.
     * <p>
     * Note: This getter bypasses most of the checks of the message's internal representation.
     * Having a valid format returned here does not mean that other operations
     * (such as {@link ONS.Messaging#loadFragment(Context, ONSMessage)}) will succeed.
     *
     * @return the format of the displayable message, if any.
     */
    public Format getFormat() {
        try {
            Message msg = PayloadParser.parseBasePayload(getJSON());

            if (msg instanceof AlertMessage) {
                return Format.ALERT;
            } else if (msg instanceof UniversalMessage) {
                return Format.FULLSCREEN;
            } else if (msg instanceof BannerMessage) {
                return Format.BANNER;
            } else if (msg instanceof ModalMessage) {
                return Format.MODAL;
            } else if (msg instanceof ImageMessage) {
                return Format.IMAGE;
            } else if (msg instanceof WebViewMessage) {
                return Format.WEBVIEW;
            }
        } catch (Exception e) {
            Logger.internal(MessagingModule.TAG, "Could not read base payload from message", e);
        }
        return Format.UNKNOWN;
    }

    protected abstract String getKind();

    protected abstract Bundle getBundleRepresentation();

    /**
     * Formats that can be contained into a ONSMessage.
     * <p>
     * This list might evolve in the future
     */
    @PublicSDK
    public enum Format {
        /**
         * UNKNOWN means that the message is invalid and does not contain any displayable message,
         * or that the format is unknown to this version of the SDK, and might be available in a newer one.
         */
        UNKNOWN,
        /**
         * ALERT is simple a system alert
         */
        ALERT,
        /**
         * FULLSCREEN is the fullscreen format
         */
        FULLSCREEN,
        /**
         * BANNER is a banner that can be attached on top or bottom of your screen
         */
        BANNER,
        /**
         * BANNER is a popup that takes over the screen modally, like a system alert but with a custom style
         */
        MODAL,
        /**
         * IMAGE is a modal popup that simply shows an image in an alert (detached) or fullscreen (attached) style
         */
        IMAGE,
        /**
         * WEBVIEW is a fullscreen format that load an URL into a WebView
         */
        WEBVIEW,
    }
}
