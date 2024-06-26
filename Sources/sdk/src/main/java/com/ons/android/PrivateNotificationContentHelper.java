package com.ons.android;

import com.ons.android.inbox.InboxNotificationContentInternal;

/**
 * Helper to extract a package local field from {@link ONSInboxNotificationContent}
 *
 * @hide
 */
public class PrivateNotificationContentHelper {

    private PrivateNotificationContentHelper() {}

    public static InboxNotificationContentInternal getInternalContent(ONSInboxNotificationContent publicContent) {
        return publicContent.internalContent;
    }

    public static ONSInboxNotificationContent getPublicContent(InboxNotificationContentInternal internalContent) {
        return new ONSInboxNotificationContent(internalContent);
    }
}
