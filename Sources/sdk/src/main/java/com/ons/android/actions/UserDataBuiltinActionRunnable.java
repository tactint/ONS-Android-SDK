package com.ons.android.actions;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ons.android.ONS;
import com.ons.android.ONSProfileAttributeEditor;
import com.ons.android.UserActionRunnable;
import com.ons.android.UserActionSource;
import com.ons.android.core.Logger;
import com.ons.android.json.JSONException;
import com.ons.android.json.JSONObject;
import com.ons.android.module.ActionModule;
import com.ons.android.user.InstallDataEditor;
import java.util.Locale;

public class UserDataBuiltinActionRunnable implements UserActionRunnable {

    private static final String TAG = "UserDataBuiltinAction";
    public static String IDENTIFIER = ActionModule.RESERVED_ACTION_IDENTIFIER_PREFIX + "user.tag";

    @Override
    public void performAction(
        @Nullable Context context,
        @NonNull String identifier,
        @NonNull JSONObject args,
        @Nullable UserActionSource source
    ) {
        try {
            JSONObject json = new JSONObject(args);

            String collection = json.getString("c");
            if (collection == null) {
                Logger.internal(TAG, "Could not perform tag edit action : collection's null");
                return;
            }

            if (collection.length() == 0) {
                Logger.internal(TAG, "Could not perform tag edit action : collection name is empty");
                return;
            }

            String tag = json.getString("t");
            if (tag == null) {
                Logger.internal(TAG, "Could not perform tag edit action : tag's null");
                return;
            }

            if (tag.length() == 0) {
                Logger.internal(TAG, "Could not perform tag edit action : tag name is empty");
                return;
            }

            String action = json.getString("a");
            if (action == null) {
                Logger.internal(TAG, "Could not perform tag edit action : action's null");
                return;
            }

            action = action.toLowerCase(Locale.US);

            if (action.equals("add")) {
                Logger.internal(TAG, "Adding tag " + tag + " to collection " + collection);
                ONSProfileAttributeEditor editor = ONS.Profile.editor();
                editor.addToArray(collection, tag);
                editor.save();
            } else if (action.equals("remove")) {
                Logger.internal(TAG, "Removing tag " + tag + " to collection " + collection);
                ONSProfileAttributeEditor editor = ONS.Profile.editor();
                editor.removeFromArray(collection, tag);
                editor.save();
            } else {
                Logger.internal(TAG, "Could not perform tag edit action: Unknown action");
            }
        } catch (JSONException e) {
            Logger.internal(TAG, "Json object failure : " + e.getLocalizedMessage());
        }
    }
}
