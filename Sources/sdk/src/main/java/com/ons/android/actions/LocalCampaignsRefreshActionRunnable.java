package com.ons.android.actions;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ons.android.UserActionRunnable;
import com.ons.android.UserActionSource;
import com.ons.android.WebserviceLauncher;
import com.ons.android.core.Logger;
import com.ons.android.di.providers.RuntimeManagerProvider;
import com.ons.android.json.JSONObject;
import com.ons.android.module.ActionModule;
import com.ons.android.runtime.RuntimeManager;

public class LocalCampaignsRefreshActionRunnable implements UserActionRunnable {

    private static final String TAG = "LocalCampaignsRefreshAction";
    public static String IDENTIFIER = ActionModule.RESERVED_ACTION_IDENTIFIER_PREFIX + "refresh_lc";

    @Override
    public void performAction(
        @Nullable Context context,
        @NonNull String identifier,
        @NonNull JSONObject args,
        @Nullable UserActionSource source
    ) {
        RuntimeManager rm = RuntimeManagerProvider.get();
        if (rm != null) {
            WebserviceLauncher.launchLocalCampaignsWebservice(rm);
        } else {
            Logger.error(
                TAG,
                "Tried to perform a Local Campaigns Refresh action, but was unable to get a RuntimeManager instance."
            );
        }
    }
}
