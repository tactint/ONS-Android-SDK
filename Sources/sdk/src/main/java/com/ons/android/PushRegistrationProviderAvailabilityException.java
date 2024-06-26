package com.ons.android;

import com.ons.android.annotation.PublicSDK;

@PublicSDK
public class PushRegistrationProviderAvailabilityException extends Exception {

    public PushRegistrationProviderAvailabilityException(String message) {
        super(message);
    }
}
