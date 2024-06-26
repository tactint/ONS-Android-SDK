package com.ons.android.post;

import com.ons.android.core.ByteArrayHelper;
import com.ons.android.core.Logger;
import com.ons.android.inbox.InboxCandidateNotificationInternal;
import com.ons.android.json.JSONArray;
import com.ons.android.json.JSONException;
import com.ons.android.json.JSONObject;
import java.util.Collection;

public class InboxSyncPostDataProvider implements PostDataProvider<JSONObject> {

    private static final String TAG = "InboxSyncPostDataProvider";
    private final JSONObject body;

    public InboxSyncPostDataProvider(Collection<InboxCandidateNotificationInternal> candidates) {
        this.body = new JSONObject();

        try {
            JSONArray notifications = new JSONArray();
            for (InboxCandidateNotificationInternal candidate : candidates) {
                JSONObject notification = new JSONObject();
                notification.put("notificationId", candidate.identifier);
                notification.put("read", !candidate.isUnread);
                notifications.put(notification);
            }

            this.body.put("notifications", notifications);
        } catch (JSONException e) {
            Logger.error(TAG, "Could not create post data", e);
        }
    }

    @Override
    public JSONObject getRawData() {
        return body;
    }

    @Override
    public byte[] getData() {
        return ByteArrayHelper.getUTF8Bytes(body.toString());
    }

    public boolean isEmpty() {
        return this.body.keySet().isEmpty();
    }

    @Override
    public String getContentType() {
        return "application/json";
    }
}
