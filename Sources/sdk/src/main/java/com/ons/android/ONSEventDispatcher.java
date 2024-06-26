package com.ons.android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ons.android.annotation.PublicSDK;

/**
 * Interface used when listening for event to dispatch.
 * See {@link ONS.EventDispatcher#addDispatcher(ONSEventDispatcher)} and {@link ONS.EventDispatcher#removeDispatcher(ONSEventDispatcher)}.
 */
@PublicSDK
public interface ONSEventDispatcher {
    /**
     * Get the name of the dispatcher
     * This information is only used for analytics
     *
     * /!\ `default` method can lead to an AbstractMethodError if java 8 language features are not available (api < 24).
     * Ensure catching the exception when using this method or use reflection check.
     *
     * @return the name of the dispatcher
     */
    @Nullable
    default String getName() {
        return null;
    }

    /**
     * Get the version of the dispatcher
     * This information is only used for analytics
     *
     * /!\ `default` method can lead to an AbstractMethodError if java 8 language features are not available (api < 24).
     * Ensure catching the exception when using this method or use reflection check.
     *
     * @return the version of the dispatcher
     */
    default int getVersion() {
        return 0;
    }

    /**
     * Callback when a new events just happened in the ONS SDK
     *
     * @param eventType
     * @param payload
     */
    void dispatchEvent(@NonNull ONS.EventDispatcher.Type eventType, @NonNull ONS.EventDispatcher.Payload payload);
}
