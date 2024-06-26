package com.ons.android.webservice.listener;

import com.ons.android.core.Webservice;

/**
 * Listener for the metric webservice
 */
public interface MetricWebserviceListener {
    /**
     * Called when a request succeed
     */
    void onSuccess();

    /**
     * Called when a request fail
     *
     * @param error error
     */
    void onFailure(Webservice.WebserviceError error);
}
