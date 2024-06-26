package com.ons.android.localcampaigns.signal;

import com.ons.android.localcampaigns.model.LocalCampaign;
import com.ons.android.localcampaigns.trigger.EventLocalCampaignTrigger;
import com.ons.android.localcampaigns.trigger.NextSessionTrigger;
import org.junit.Assert;
import org.junit.Test;

public class NewSessionSignalTest {

    @Test
    public void testSatisfiesTrigger() {
        Signal signal = new NewSessionSignal();

        Assert.assertTrue(signal.satisfiesTrigger(new NextSessionTrigger()));

        Assert.assertFalse(signal.satisfiesTrigger(new EventLocalCampaignTrigger("eventname", null)));
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
