package com.ons.android.webservice.listener;

import com.ons.android.FailReason;
import com.ons.android.query.response.AttributesSendResponse;

/**
 * Listener for AttributesSendWebservice
 *
 */
public interface AttributesSendWebserviceListener {
    /**
     * Called on success
     */
    void onSuccess(AttributesSendResponse response);

    /**
     * Called on error
     *
     * @param reason
     */
    void onError(FailReason reason);
}
