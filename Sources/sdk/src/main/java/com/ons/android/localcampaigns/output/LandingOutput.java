package com.ons.android.localcampaigns.output;

import androidx.annotation.NonNull;
import com.ons.android.ONSInAppMessage;
import com.ons.android.core.Logger;
import com.ons.android.di.providers.MessagingModuleProvider;
import com.ons.android.json.JSONException;
import com.ons.android.json.JSONObject;
import com.ons.android.localcampaigns.model.LocalCampaign;
import com.ons.android.module.LocalCampaignsModule;
import com.ons.android.module.MessagingModule;
import com.ons.android.processor.Module;
import com.ons.android.processor.Provide;

@Module
public class LandingOutput extends LocalCampaign.Output {

    private MessagingModule messagingModule;

    public LandingOutput(MessagingModule messagingModule, @NonNull JSONObject payload) {
        super(payload);
        this.messagingModule = messagingModule;
    }

    @Provide
    public static LandingOutput provide(@NonNull JSONObject payload) {
        return new LandingOutput(MessagingModuleProvider.get(), payload);
    }

    @Override
    public boolean displayMessage(LocalCampaign campaign) {
        try {
            // Copy event data before making the ONSInAppMessage
            JSONObject mergedPayload = new JSONObject(payload);
            mergedPayload.put("ed", campaign.eventData);
            JSONObject customPayload = new JSONObject(
                campaign.customPayload != null ? new JSONObject(campaign.customPayload) : new JSONObject()
            );

            ONSInAppMessage message = new ONSInAppMessage(
                campaign.publicToken,
                campaign.id,
                campaign.eventData,
                mergedPayload,
                customPayload
            );

            messagingModule.displayInAppMessage(message);
            return true;
        } catch (JSONException e) {
            Logger.internal(LocalCampaignsModule.TAG, "Landing Output: Could not copy custom payload", e);
        }
        return false;
    }
}
