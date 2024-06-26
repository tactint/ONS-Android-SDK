package com.ons.android;

import android.content.Context;
import com.ons.android.core.Logger;
import com.ons.android.core.MessagePackWebservice;
import com.ons.android.core.ParameterKeys;
import com.ons.android.core.Parameters;
import com.ons.android.core.TaskRunnable;
import com.ons.android.post.MetricPostDataProvider;
import com.ons.android.webservice.listener.MetricWebserviceListener;
import java.net.MalformedURLException;

class MetricWebservice extends MessagePackWebservice implements TaskRunnable {

    private static final String TAG = "MetricWebservice";

    private final MetricWebserviceListener listener;

    protected MetricWebservice(
        Context context,
        MetricWebserviceListener listener,
        MetricPostDataProvider dataProvider,
        String... parameters
    ) throws MalformedURLException {
        super(context, dataProvider, Parameters.METRIC_WS_URL, parameters);
        if (listener == null) {
            throw new NullPointerException("Listener is null");
        }
        this.listener = listener;
    }

    @Override
    public void run() {
        Logger.internal(TAG, "Webservice started");
        try {
            executeRequest();
            this.listener.onSuccess();
        } catch (WebserviceError error) {
            this.listener.onFailure(error);
        }
    }

    @Override
    public String getTaskIdentifier() {
        return "ONS/metricsws";
    }

    @Override
    protected String getSpecificRetryCountKey() {
        return ParameterKeys.METRIC_WS_RETRYCOUNT_KEY;
    }
}
