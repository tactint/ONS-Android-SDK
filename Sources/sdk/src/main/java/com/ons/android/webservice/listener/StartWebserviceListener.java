package com.ons.android.webservice.listener;

import com.ons.android.FailReason;

/**
 * Contains method to implement to receive start webservice responses
 *
 */
public interface StartWebserviceListener {
    /**
     * Called on success
     */
    void onSuccess();

    /**
     * Call on error
     *
     * @param reason
     */
    void onError(FailReason reason);
}
