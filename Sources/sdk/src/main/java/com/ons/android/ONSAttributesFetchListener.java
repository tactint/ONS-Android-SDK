package com.ons.android;

import android.content.Context;
import androidx.annotation.NonNull;
import com.ons.android.annotation.PublicSDK;
import java.util.Map;

/**
 * Listener used when fetching attributes using {@link ONS.User#fetchAttributes(Context, ONSAttributesFetchListener)}.
 */
@PublicSDK
public interface ONSAttributesFetchListener {
    /**
     * @param attributes A map of attributes. The keys are the ones used when setting the attributes.
     *                   The values are of type {@link ONSUserAttribute}.
     */
    void onSuccess(@NonNull Map<String, ONSUserAttribute> attributes);

    void onError();
}
