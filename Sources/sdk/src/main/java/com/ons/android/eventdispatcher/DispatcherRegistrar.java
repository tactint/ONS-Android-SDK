package com.ons.android.eventdispatcher;

import android.content.Context;
import androidx.annotation.Keep;
import com.ons.android.ONSEventDispatcher;

/**
 * Class used to init dispatcher from manifest meta-data
 */
@Keep
public interface DispatcherRegistrar {
    /**
     * Instantiate the dispatcher
     *
     * @param context
     * @return
     */
    ONSEventDispatcher getDispatcher(Context context);
}
