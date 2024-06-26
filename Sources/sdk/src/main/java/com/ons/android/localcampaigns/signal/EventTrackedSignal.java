package com.ons.android.localcampaigns.signal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.ons.android.json.JSONObject;
import com.ons.android.localcampaigns.model.LocalCampaign;
import com.ons.android.localcampaigns.trigger.EventLocalCampaignTrigger;

/**
 * Represents the event tracked signal for all events
 */
public class EventTrackedSignal implements Signal {

    @NonNull
    public String name;

    @Nullable
    public JSONObject parameters;

    public EventTrackedSignal(@NonNull String eventName, @Nullable JSONObject parameters) {
        this.name = eventName;
        this.parameters = parameters;
    }

    public boolean satisfiesTrigger(LocalCampaign.Trigger trigger) {
        return (
            trigger instanceof EventLocalCampaignTrigger &&
            ((EventLocalCampaignTrigger) trigger).isSatisfied(name, null)
        );
    }
}
