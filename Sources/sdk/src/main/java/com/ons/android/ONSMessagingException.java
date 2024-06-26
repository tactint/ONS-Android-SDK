package com.ons.android;

import com.ons.android.annotation.PublicSDK;

/**
 * ONS Messaging Exception. Usually wraps another exception.
 */
@PublicSDK
public class ONSMessagingException extends Exception {

    public ONSMessagingException() {
        super();
    }

    public ONSMessagingException(String message) {
        super(message);
    }

    public ONSMessagingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ONSMessagingException(Throwable cause) {
        super(cause);
    }
}
