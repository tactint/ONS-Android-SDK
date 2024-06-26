package com.ons.android;

import android.content.Context;
import com.ons.android.core.Logger;
import com.ons.android.core.MessagePackWebservice;
import com.ons.android.core.ParameterKeys;
import com.ons.android.core.Parameters;
import com.ons.android.core.TaskRunnable;
import com.ons.android.post.DisplayReceiptPostDataProvider;
import com.ons.android.webservice.listener.DisplayReceiptWebserviceListener;
import java.net.MalformedURLException;

class DisplayReceiptWebservice extends MessagePackWebservice implements TaskRunnable {

    private static final String TAG = "DisplayReceiptWebservice";

    private final DisplayReceiptWebserviceListener listener;

    /**
     * @param context      Android context
     * @param listener
     * @param dataProvider
     * @param parameters
     * @throws MalformedURLException
     */
    protected DisplayReceiptWebservice(
        Context context,
        DisplayReceiptWebserviceListener listener,
        DisplayReceiptPostDataProvider dataProvider,
        String... parameters
    ) throws MalformedURLException {
        super(context, dataProvider, Parameters.DISPLAY_RECEIPT_WS_URL, addSchemaVersion(parameters));
        if (listener == null) {
            throw new NullPointerException("Listener is null");
        }
        this.listener = listener;
    }

    @Override
    public void run() {
        try {
            Logger.internal(TAG, "Webservice started");
            executeRequest();
            listener.onSuccess();
        } catch (WebserviceError error) {
            listener.onFailure(error);
        }
    }

    @Override
    public String getTaskIdentifier() {
        return "ONS/receiptws";
    }

    @Override
    protected String getCryptorTypeParameterKey() {
        return ParameterKeys.DISPLAY_RECEIPT_WS_CRYPTORTYPE_KEY;
    }

    @Override
    protected String getSpecificRetryCountKey() {
        return ParameterKeys.DISPLAY_RECEIPT_WS_RETRYCOUNT_KEY;
    }
}
