package com.ons.android.localcampaigns.signal;

import com.ons.android.localcampaigns.model.LocalCampaign;
import com.ons.android.localcampaigns.trigger.EventLocalCampaignTrigger;
import com.ons.android.localcampaigns.trigger.NextSessionTrigger;
import org.junit.Assert;
import org.junit.Test;

public class EventTrackedSignalTest {

    @Test
    public void testSatisfiesTrigger() {
        final String eventName = "my_event";
        Signal signal = new EventTrackedSignal(eventName, null);

        Assert.assertTrue(signal.satisfiesTrigger(new EventLocalCampaignTrigger(eventName, null)));

        Assert.assertFalse(signal.satisfiesTrigger(new NextSessionTrigger()));
        Assert.assertFalse(
            signal.satisfiesTrigger(
                new LocalCampaign.Trigger() {
                    @Override
                    public String getType() {
                        return null;
                    }
                }
            )
        );
    }
}
