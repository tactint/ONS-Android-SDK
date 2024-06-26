package com.ons.android.webservice.listener.impl;

import android.content.Context;
import com.ons.android.FailReason;
import com.ons.android.core.ParameterKeys;
import com.ons.android.core.Parameters;
import com.ons.android.di.providers.ParametersProvider;
import com.ons.android.di.providers.RuntimeManagerProvider;
import com.ons.android.di.providers.UserModuleProvider;
import com.ons.android.query.response.AttributesSendResponse;
import com.ons.android.webservice.listener.AttributesSendWebserviceListener;

/**
 * Default implementation for attributes send webservice
 *
 */
public class AttributesSendWebserviceListenerImpl implements AttributesSendWebserviceListener {

    @Override
    public void onSuccess(AttributesSendResponse response) {
        UserModuleProvider.get().storeTransactionID(response.getTransactionID(), response.getVersion());

        // Detecting whether project has changed
        Context context = RuntimeManagerProvider.get().getContext();
        String projectKey = response.getProjectKey();
        if (projectKey != null && context != null) {
            Parameters parameters = ParametersProvider.get(context);
            String currentProjectKey = parameters.get(ParameterKeys.PROJECT_KEY);
            if (!projectKey.equals(currentProjectKey)) {
                // If we are here this mean we are running on a fresh V2 install and user has
                // just wrote some profile data.
                // So we save the project key to not trigger the profile data migration from the
                // next ATC response otherwise we would erase the data we just sent.
                parameters.set(ParameterKeys.PROJECT_KEY, projectKey, true);
            }
        }
    }

    @Override
    public void onError(FailReason reason) {
        UserModuleProvider.get().startSendWS(5000);
    }
}
