package com.ons.android.messaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ons.android.ONSMessagingWebViewJavascriptBridge;
import com.ons.android.json.JSONObject;
import com.ons.android.messaging.model.MessagingError;

public interface WebViewActionListener {
    void onCloseAction();

    void onDismissAction(@Nullable String analyticsID);

    void onErrorAction(
        @NonNull ONSMessagingWebViewJavascriptBridge.DevelopmentErrorCause developmentCause,
        @NonNull MessagingError messagingCause,
        @Nullable String description
    );

    void onOpenDeeplinkAction(@NonNull String url, @Nullable Boolean openInAppOverride, @Nullable String analyticsID);

    void onPerformAction(@NonNull String action, @NonNull JSONObject args, @Nullable String analyticsID);
}
