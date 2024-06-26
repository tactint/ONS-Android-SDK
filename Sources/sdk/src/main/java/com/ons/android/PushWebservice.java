package com.ons.android;

import android.content.Context;
import androidx.annotation.NonNull;
import com.ons.android.core.Logger;
import com.ons.android.core.ParameterKeys;
import com.ons.android.core.Parameters;
import com.ons.android.core.TaskRunnable;
import com.ons.android.json.JSONObject;
import com.ons.android.query.PushQuery;
import com.ons.android.query.Query;
import com.ons.android.query.QueryType;
import com.ons.android.query.response.PushResponse;
import com.ons.android.webservice.listener.PushWebserviceListener;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Webservice to send push token
 *
 * @hide
 */
class PushWebservice extends ONSQueryWebservice implements TaskRunnable {

    private static final String TAG = "PushWebservice";

    /**
     * The registration information
     */
    private final ONSPushRegistration registration;

    /**
     * Listener of this WS
     */
    private final PushWebserviceListener listener;

    // ------------------------------------------>

    protected PushWebservice(
        Context context,
        @NonNull ONSPushRegistration registration,
        PushWebserviceListener listener
    ) throws MalformedURLException {
        super(context, RequestType.POST, Parameters.PUSH_WS_URL);
        if (registration == null) {
            throw new NullPointerException("registration==null");
        }

        if (listener == null) {
            throw new NullPointerException("listener==null");
        }

        this.listener = listener;
        this.registration = registration;
    }

    // ------------------------------------------>

    @Override
    protected List<Query> getQueries() {
        List<Query> queries = new ArrayList<>(1);

        queries.add(new PushQuery(applicationContext, registration));

        return queries;
    }

    @Override
    public void run() {
        try {
            Logger.internal(TAG, "push webservice started");

            /*
             * Read response
             */
            JSONObject response = null;
            try {
                response = getStandardResponseBodyIfValid();
            } catch (WebserviceError error) {
                Logger.internal(TAG, "Error on PushWebservice : " + error.getReason().toString(), error.getCause());

                switch (error.getReason()) {
                    case NETWORK_ERROR:
                        listener.onError(FailReason.NETWORK_ERROR);
                        break;
                    case INVALID_API_KEY:
                        listener.onError(FailReason.INVALID_API_KEY);
                        break;
                    case DEACTIVATED_API_KEY:
                        listener.onError(FailReason.DEACTIVATED_API_KEY);
                        break;
                    default:
                        listener.onError(FailReason.UNEXPECTED_ERROR);
                        break;
                }

                return;
            }

            /*
             * Parse response to retrieve responses for queries, parameters and other stuffs
             */
            parseResponse(response);

            /*
             * Read resposne
             */
            PushResponse pushResponse = getResponseFor(PushResponse.class, QueryType.PUSH);
            if (pushResponse == null) {
                throw new NullPointerException("Missing push response");
            }

            Logger.internal(TAG, "push webservice ended");

            // Call the listener
            listener.onSuccess();
        } catch (Exception e) {
            Logger.internal(TAG, "Error while reading PushWebservice response", e);
            listener.onError(FailReason.UNEXPECTED_ERROR);
        }
    }

    @Override
    public String getTaskIdentifier() {
        return "ONS/pushws";
    }

    // ------------------------------------------>

    @Override
    protected String getPropertyParameterKey() {
        return ParameterKeys.PUSH_WS_PROPERTY_KEY;
    }

    @Override
    protected String getURLSorterPatternParameterKey() {
        return ParameterKeys.PUSH_WS_URLSORTER_PATTERN_KEY;
    }

    @Override
    protected String getCryptorTypeParameterKey() {
        return ParameterKeys.PUSH_WS_CRYPTORTYPE_KEY;
    }

    @Override
    protected String getCryptorModeParameterKey() {
        return ParameterKeys.PUSH_WS_CRYPTORMODE_KEY;
    }

    @Override
    protected String getPostCryptorTypeParameterKey() {
        return ParameterKeys.PUSH_WS_POST_CRYPTORTYPE_KEY;
    }

    @Override
    protected String getReadCryptorTypeParameterKey() {
        return ParameterKeys.PUSH_WS_READ_CRYPTORTYPE_KEY;
    }

    @Override
    protected String getSpecificConnectTimeoutKey() {
        return ParameterKeys.PUSH_WS_CONNECT_TIMEOUT_KEY;
    }

    @Override
    protected String getSpecificReadTimeoutKey() {
        return ParameterKeys.PUSH_WS_READ_TIMEOUT_KEY;
    }

    @Override
    protected String getSpecificRetryCountKey() {
        return ParameterKeys.PUSH_WS_RETRYCOUNT_KEY;
    }
}
