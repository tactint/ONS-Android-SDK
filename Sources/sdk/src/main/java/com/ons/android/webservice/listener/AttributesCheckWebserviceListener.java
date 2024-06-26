package com.ons.android.webservice.listener;

import com.ons.android.FailReason;
import com.ons.android.query.response.AttributesCheckResponse;

/**
 * Listener for AttributesCheckWebservice
 *
 */
public interface AttributesCheckWebserviceListener {
    /**
     * Called on success
     */
    void onSuccess(AttributesCheckResponse response);

    /**
     * Called on error
     *
     * @param reason
     */
    void onError(FailReason reason);
}
