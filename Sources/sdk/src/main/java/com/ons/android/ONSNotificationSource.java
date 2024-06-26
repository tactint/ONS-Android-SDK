package com.ons.android;

import com.ons.android.annotation.PublicSDK;

/**
 * ONSNotificationSource represents how the push was sent from ONS: via the Transactional API, or using a Push Campaign
 * The value might be unknown for forward compatibility, or if the information was missing.
 */
@PublicSDK
public enum ONSNotificationSource {
    UNKNOWN,
    CAMPAIGN,
    TRANSACTIONAL,
    TRIGGER,
}
