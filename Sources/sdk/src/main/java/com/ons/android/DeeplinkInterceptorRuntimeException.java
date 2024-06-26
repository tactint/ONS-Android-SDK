package com.ons.android;

/**
 * Wraps an exception that happened in a {@link ONSDeeplinkInterceptor}
 * <p>
 * Not meant to be exposed directly into the public api, but to be rethrown
 * <p>
 * No cause string is supported as it's pretty explicit
 *
 * @hide
 */
public class DeeplinkInterceptorRuntimeException extends Exception {

    private RuntimeException wrappedRuntimeException;

    public DeeplinkInterceptorRuntimeException(RuntimeException cause) {
        super(cause);
        this.wrappedRuntimeException = cause;
    }

    public RuntimeException getWrappedRuntimeException() {
        return wrappedRuntimeException;
    }
}
