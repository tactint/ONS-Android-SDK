package com.ons.android;

import android.os.Bundle;
import com.ons.android.annotation.PublicSDK;

/**
 * Represents a push user action source
 */
@PublicSDK
public interface PushUserActionSource extends UserActionSource {
    Bundle getPushBundle();
}
