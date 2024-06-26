package com.ons.android.messaging.model;

import com.ons.android.json.JSONObject;
import java.io.Serializable;

public class Action implements Serializable {

    private static final long serialVersionUID = 0L;

    public String action;
    public JSONObject args;

    public Action(String action, JSONObject args) {
        this.action = action;
        this.args = args;
    }

    public boolean isDismissAction() {
        return action == null || "ons.dismiss".equals(action);
    }
}
