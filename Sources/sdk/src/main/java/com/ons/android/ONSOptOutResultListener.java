package com.ons.android;

import com.ons.android.annotation.PublicSDK;

/**
 * Interface describing a listener for server-side Opt-Out/Opt-Out and wipe acknowledgment
 */
@PublicSDK
public interface ONSOptOutResultListener {
    void onSuccess();

    ErrorPolicy onError();

    @PublicSDK
    enum ErrorPolicy {
        /**
         * Ignore the error and proceed with the opt-out.
         */
        IGNORE,

        /**
         * Cancel the opt-out: please call the opt-out method again to retry.
         */
        CANCEL,
    }
}
