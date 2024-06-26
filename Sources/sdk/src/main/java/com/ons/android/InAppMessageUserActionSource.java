package com.ons.android;

import androidx.annotation.NonNull;
import com.ons.android.annotation.PublicSDK;
import com.ons.android.json.JSONObject;

/**
 * Represents an In-App Message user action source.
 */
@PublicSDK
public interface InAppMessageUserActionSource extends UserActionSource {
    @NonNull
    JSONObject getCustomPayload();
}
