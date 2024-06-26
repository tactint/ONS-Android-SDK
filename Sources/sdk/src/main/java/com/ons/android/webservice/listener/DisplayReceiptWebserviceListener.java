package com.ons.android.webservice.listener;

import com.ons.android.core.Webservice;

/**
 * Listener for the display receipt webservice
 */
public interface DisplayReceiptWebserviceListener {
    /**
     * Called when a request succeed
     */
    void onSuccess();

    /**
     * Called when a request fail
     *
     * @param error webservice request error
     */
    void onFailure(Webservice.WebserviceError error);
}
