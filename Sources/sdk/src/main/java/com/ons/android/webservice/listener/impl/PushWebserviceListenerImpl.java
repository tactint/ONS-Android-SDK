package com.ons.android.webservice.listener.impl;

import com.ons.android.FailReason;
import com.ons.android.webservice.listener.PushWebserviceListener;

/**
 * Implementation of {@link PushWebserviceListener}
 *
 */
public class PushWebserviceListenerImpl implements PushWebserviceListener {

    @Override
    public void onSuccess() {
        // Currently nothing to do on success
    }

    @Override
    public void onError(FailReason reason) {
        // Currently nothing to do on error
    }
}
