package com.ons.android.webservice.listener;

import com.ons.android.core.Webservice;
import java.util.List;

/**
 * Listener for LocalCampaignsJITWebservice
 */

public interface LocalCampaignsJITWebserviceListener {
    /**
     * Called on success
     */
    void onSuccess(List<String> eligibleCampaigns);

    /**
     * Called on error
     *
     * @param error webservice error
     */
    void onFailure(Webservice.WebserviceError error);
}
