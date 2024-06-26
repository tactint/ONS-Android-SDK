package com.ons.android.webservice.listener;

import com.ons.android.FailReason;

/**
 * Listener for PushWebservice
 *
 */
public interface PushWebserviceListener {
    /**
     * Called on success
     */
    void onSuccess();

    /**
     * Called on error
     *
     * @param reason
     */
    void onError(FailReason reason);
}
