package com.ons.android.module;

import android.content.Context;
import androidx.annotation.NonNull;
import com.ons.android.runtime.State;

/**
 * Abstract class of a ONS Module
 *
 */
public abstract class ONSModule {

    /**
     * ID of the module
     *
     * @return
     */
    public abstract String getId();

    /**
     * Should return the state of the module (usually 0 for deactivated, 1 for activated)
     *
     * @return
     */
    public abstract int getState();

    // ----------------------------------->

    /**
     * Called by ONS as soon as a context is available in the runtimeManager
     * For convenience, the application context is available as a parameter.
     * LocalBroadcastManager is also up.
     */
    public void onsContextBecameAvailable(@NonNull Context applicationContext) {
        // Override this method
    }

    /**
     * Called by ONS before ons start<br>
     * NB : Context & activity are already available from the runtimeManager
     */
    public void onsWillStart() {
        // Override this method
    }

    /**
     * Called by ONS right after ons start<br>
     * NB : Same context and activity that in willStart but with the new state {@link State#READY} set
     */
    public void onsDidStart() {
        // Override this method
    }

    /**
     * Called by ONS before switching to {@link State#FINISHING}<br>
     * NB : Context and activity are still available from the runtimeManager
     */
    public void onsIsFinishing() {
        // Override this method
    }

    /**
     * Called by ONS before switching to {@link State#OFF}<br>
     * NB : Context is still available from runtimeManager (not activity)
     */
    public void onsWillStop() {
        // Override this method
    }

    /**
     * Called by ONS right after ons stop<br>
     * NB : No context or activity are available
     */
    public void onsDidStop() {
        // Override this method
    }
}
