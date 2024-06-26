package com.ons.android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ons.android.annotation.PublicSDK;
import com.ons.android.json.JSONException;
import com.ons.android.json.JSONObject;

/**
 * Represents a ONSAction triggerable by a messaging component
 */
@PublicSDK
public class ONSMessageAction {

    private String action;

    private JSONObject args;

    /**
     * This is a private constructor
     *
     * @hide
     */
    public ONSMessageAction(@NonNull com.ons.android.messaging.model.Action from) {
        action = from.action;
        if (from.args != null) {
            try {
                args = new JSONObject(from.args);
            } catch (JSONException e) {
                args = new JSONObject();
            }
        }
    }

    @Nullable
    public String getAction() {
        return action;
    }

    @Nullable
    public JSONObject getArgs() {
        return args;
    }

    public boolean isDismissAction() {
        return action == null;
    }
}
