package com.ons.android.localcampaigns;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.ons.android.di.DI;
import com.ons.android.di.DITestUtils;
import com.ons.android.di.providers.CampaignManagerProvider;
import com.ons.android.di.providers.LandingOutputProvider;
import com.ons.android.di.providers.LocalCampaignsModuleProvider;
import com.ons.android.json.JSONObject;
import com.ons.android.localcampaigns.model.LocalCampaign;
import com.ons.android.localcampaigns.signal.NewSessionSignal;
import com.ons.android.localcampaigns.signal.Signal;
import com.ons.android.localcampaigns.trigger.NextSessionTrigger;
import com.ons.android.module.LocalCampaignsModule;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class LocalCampaignsModuleTests {

    @Before
    public void setup() {
        DI.reset();
        LocalCampaignsModule module = DITestUtils.mockSingletonDependency(LocalCampaignsModule.class, null);
        module.onsDidStart();
    }

    @After
    public void teardown() {
        DI.reset();
    }

    @Test
    public void testSignalQueue() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        LocalCampaignsModule module = LocalCampaignsModuleProvider.get();

        Field signalQueueField = LocalCampaignsModule.class.getDeclaredField("signalQueue");
        signalQueueField.setAccessible(true);
        LinkedList<Signal> signalQueue = (LinkedList<Signal>) signalQueueField.get(module);

        Field isReadyField = LocalCampaignsModule.class.getDeclaredField("isReady");
        isReadyField.setAccessible(true);
        AtomicBoolean isReady = (AtomicBoolean) isReadyField.get(module);

        assert signalQueue != null;
        assert isReady != null;

        Assert.assertFalse(isReady.get());
        Assert.assertEquals(0, signalQueue.size());

        module.sendSignal(new NewSessionSignal());
        Assert.assertEquals(1, signalQueue.size());

        // Simulate synchro is finished
        module.onLocalCampaignsWebserviceFinished();

        Assert.assertTrue(isReady.get());
        Assert.assertEquals(0, signalQueue.size());
    }
}
