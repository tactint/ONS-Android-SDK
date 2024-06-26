package com.ons.android.webservice.listener.impl;

import com.ons.android.FailReason;
import com.ons.android.core.Logger;
import com.ons.android.di.providers.CampaignManagerProvider;
import com.ons.android.di.providers.LocalCampaignsModuleProvider;
import com.ons.android.localcampaigns.CampaignManager;
import com.ons.android.module.LocalCampaignsModule;
import com.ons.android.processor.Module;
import com.ons.android.processor.Provide;
import com.ons.android.query.response.LocalCampaignsResponse;
import com.ons.android.webservice.listener.LocalCampaignsWebserviceListener;
import java.util.List;

/**
 * Listener for the local campaigns webservice. It will redirect the campaigns to the right modules depending on their type
 */
@Module
public class LocalCampaignsWebserviceListenerImpl implements LocalCampaignsWebserviceListener {

    private LocalCampaignsModule localCampaignsModule;

    private CampaignManager campaignManager;

    private LocalCampaignsWebserviceListenerImpl(
        LocalCampaignsModule localCampaignsModule,
        CampaignManager campaignManager
    ) {
        this.localCampaignsModule = localCampaignsModule;
        this.campaignManager = campaignManager;
    }

    @Provide
    public static LocalCampaignsWebserviceListenerImpl provide() {
        return new LocalCampaignsWebserviceListenerImpl(
            LocalCampaignsModuleProvider.get(),
            CampaignManagerProvider.get()
        );
    }

    @Override
    public void onSuccess(List<LocalCampaignsResponse> responses) {
        for (LocalCampaignsResponse response : responses) {
            handleInAppResponse(response);
        }
    }

    @Override
    public void onError(FailReason reason) {
        Logger.internal(LocalCampaignsModule.TAG, "Error while refreshing local campaigns: " + reason.toString());
        localCampaignsModule.onLocalCampaignsWebserviceFinished();
    }

    private void handleInAppResponse(LocalCampaignsResponse response) {
        campaignManager.setCappings(response.getCappings());
        campaignManager.updateCampaignList(response.getCampaigns());
        localCampaignsModule.onLocalCampaignsWebserviceFinished();
    }
}
