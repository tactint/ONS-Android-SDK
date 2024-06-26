package com.ons.android;

import com.ons.android.messaging.model.BannerMessage;

/**
 * Helper to access package private methods of {@link ONSBannerView}
 *
 * @hide
 */
public class ONSBannerViewPrivateHelper {

    public static ONSBannerView newInstance(
        ONSMessage rawMsg,
        BannerMessage msg,
        MessagingAnalyticsDelegate analyticsDelegate
    ) {
        return new ONSBannerView(rawMsg, msg, analyticsDelegate);
    }
}
