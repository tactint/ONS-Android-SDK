package com.ons.android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ons.android.annotation.PublicSDK;
import com.ons.android.messaging.model.WebViewMessage;

/**
 * Model for the content of an WebView message
 */
@PublicSDK
public class ONSWebViewContent implements ONSInAppMessage.Content {

    private final String url;

    ONSWebViewContent(@NonNull WebViewMessage from) {
        url = from.url;
    }

    @Nullable
    public String getURL() {
        return url;
    }
}
