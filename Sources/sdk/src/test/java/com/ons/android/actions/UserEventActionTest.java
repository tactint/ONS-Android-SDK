package com.ons.android.actions;

import static org.mockito.ArgumentMatchers.eq;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;
import com.ons.android.ONSEventAttributes;
import com.ons.android.di.DITest;
import com.ons.android.di.DITestUtils;
import com.ons.android.di.providers.RuntimeManagerProvider;
import com.ons.android.json.JSONException;
import com.ons.android.json.JSONObject;
import com.ons.android.module.ActionModule;
import com.ons.android.module.ProfileModule;
import java.util.Date;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.robolectric.shadows.ShadowLog;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class UserEventActionTest extends DITest {

    private Context context;

    private ProfileModule profileModule;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;

        context = ApplicationProvider.getApplicationContext();

        RuntimeManagerProvider.get().setContext(context);

        profileModule = DITestUtils.mockSingletonDependency(ProfileModule.class, null);
    }

    @Test
    public void testTrackEventAction() throws JSONException {
        ActionModule actionModule = new ActionModule();

        PowerMockito
            .doNothing()
            .when(profileModule)
            .trackPublicEvent(Mockito.anyString(), Mockito.any(ONSEventAttributes.class));

        ArgumentCaptor<ONSEventAttributes> eventDataCaptor = ArgumentCaptor.forClass(ONSEventAttributes.class);

        String eventJSON = "{'e':'event_test', 'l':'label_test'}";
        actionModule.performAction(context, "ons.user.event", new JSONObject(eventJSON), null);
        Mockito.verify(profileModule).trackPublicEvent(eq("event_test"), eventDataCaptor.capture());

        ONSEventAttributes eventData = eventDataCaptor.getValue();
        Assert.assertNotNull(eventData);
        Assert.assertNotNull(eventData.getAttributes());
        Assert.assertNotNull(eventData.getLabel());
        Assert.assertEquals("label_test", eventData.getLabel());
        Assert.assertTrue(eventData.getAttributes().isEmpty());
        Assert.assertNull(eventData.getTags());
    }

    @Test
    public void testTrackEventWithoutLabelAction() throws JSONException {
        ActionModule actionModule = new ActionModule();

        PowerMockito
            .doNothing()
            .when(profileModule)
            .trackPublicEvent(Mockito.anyString(), Mockito.any(ONSEventAttributes.class));

        ArgumentCaptor<ONSEventAttributes> eventDataCaptor = ArgumentCaptor.forClass(ONSEventAttributes.class);

        String eventJSON = "{'e':'event_test'}";
        actionModule.performAction(context, "ons.user.event", new JSONObject(eventJSON), null);
        Mockito.verify(profileModule).trackPublicEvent(eq("event_test"), eventDataCaptor.capture());

        ONSEventAttributes eventData = eventDataCaptor.getValue();
        Assert.assertNotNull(eventData.getAttributes());
        Assert.assertNull(eventData.getLabel());
        Assert.assertNull(eventData.getTags());
    }

    @Test
    public void testTrackEventWithTagsAction() throws JSONException {
        ActionModule actionModule = new ActionModule();

        PowerMockito
            .doNothing()
            .when(profileModule)
            .trackPublicEvent(Mockito.anyString(), Mockito.any(ONSEventAttributes.class));

        ArgumentCaptor<ONSEventAttributes> eventDataCaptor = ArgumentCaptor.forClass(ONSEventAttributes.class);

        String eventJSON = "{'e':'event_test', 'l':'label_test', 't':['tag1', 'tag2', 'tag3']}";
        actionModule.performAction(context, "ons.user.event", new JSONObject(eventJSON), null);
        Mockito.verify(profileModule).trackPublicEvent(eq("event_test"), eventDataCaptor.capture());

        ONSEventAttributes eventData = eventDataCaptor.getValue();
        Assert.assertNotNull(eventData);
        Assert.assertNotNull(eventData.getAttributes());
        Assert.assertNotNull(eventData.getTags());
        Assert.assertEquals(3, eventData.getTags().size());
        Assert.assertEquals("label_test", eventData.getLabel());
        Assert.assertTrue(eventData.getTags().contains("tag1"));
        Assert.assertTrue(eventData.getTags().contains("tag2"));
        Assert.assertTrue(eventData.getTags().contains("tag3"));
    }

    @Test
    public void testTrackEventWithAttrAction() throws JSONException {
        ActionModule actionModule = new ActionModule();

        PowerMockito
            .doNothing()
            .when(profileModule)
            .trackPublicEvent(Mockito.anyString(), Mockito.any(ONSEventAttributes.class));

        ArgumentCaptor<ONSEventAttributes> eventDataCaptor = ArgumentCaptor.forClass(ONSEventAttributes.class);

        String eventJSON =
            "{'e':'event_test', 'l':'label_test', 'a':{'bool':true, 'int':64, 'double': 68987.256, 'string':'tototo', 'date': '2020-08-09T12:12:23.943Z'}}}";
        actionModule.performAction(context, "ons.user.event", new JSONObject(eventJSON), null);
        Mockito.verify(profileModule).trackPublicEvent(eq("event_test"), eventDataCaptor.capture());

        ONSEventAttributes eventData = eventDataCaptor.getValue();
        Assert.assertNotNull(eventData);

        Assert.assertNotNull(eventData.getAttributes());
        Assert.assertNotNull(eventData.getLabel());
        Assert.assertEquals("label_test", eventData.getLabel());
        Assert.assertEquals(5, eventData.getAttributes().size());
        Assert.assertTrue((Boolean) eventData.getAttributes().get("bool").value);
        Assert.assertEquals(64, eventData.getAttributes().get("int").value);
        Assert.assertEquals(68987.256, eventData.getAttributes().get("double").value);
        Assert.assertEquals("tototo", eventData.getAttributes().get("string").value);
        Assert.assertEquals(1596975143943L, eventData.getAttributes().get("date").value);

        Assert.assertNull(eventData.getTags());
    }
}
