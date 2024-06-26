package com.ons.android;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import android.content.Intent;
import android.os.Bundle;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.ons.android.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link ONSMessage}
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class ONSMessageTest {

    /**
     * Get a mock ons push payload json. Should be put in com.ons
     *
     * @return
     */
    private static Bundle getMockONSLandingMessageBundle() throws Exception {
        String message =
            "{\"ld\":{\"kind\":\"universal\",\"id\":\"webtest\",\"did\":\"webtest\",\"hero\":\"http://pfs.gdn/favicon.png\"," +
            "\"h1\":\"Hi\",\"h2\":\"Ho\",\"h3\":\"Subtitle\"," +
            "\"body\":\"Lorem ipsum.\",\"close\":true,\"cta\":[" +
            "{\"id\": \"okay\", \"label\": \"Okay!\", \"action\": \"callback\", \"actionString\": \"okaycallback\"}]," +
            "\"style\":\"#image-cnt {blur: 200;}\"}}";
        final Bundle onsDataBundle = new Bundle();
        onsDataBundle.putString("com.ons", message);
        onsDataBundle.putString("custom", "payload");

        final Bundle pushPayloadBundle = new Bundle();
        pushPayloadBundle.putBundle(ONS.Push.PAYLOAD_KEY, onsDataBundle);

        final Bundle messageBundle = new Bundle();
        messageBundle.putBundle("data", pushPayloadBundle);
        messageBundle.putString("kind", ONSLandingMessage.KIND);

        final Bundle rootBundle = new Bundle();
        rootBundle.putBundle("com.ons.messaging.payload", messageBundle);
        return rootBundle;
    }

    @Test
    public void testPreconditions() throws Exception {
        try {
            //noinspection ConstantConditions
            ONSMessage.getMessageForBundle(null);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    @Test
    public void testWrongBundles() throws Exception {
        final Bundle b = new Bundle();

        try {
            ONSMessage.getMessageForBundle(b);
            fail();
        } catch (ONSPushPayload.ParsingException ignored) {}

        final Bundle message = new Bundle();
        message.putBundle("data", new Bundle());
        message.putString("kind", "invalid");
        b.putBundle("com.ons.messaging.payload", message);
        try {
            ONSMessage.getMessageForBundle(b);
            fail();
        } catch (ONSPushPayload.ParsingException ignored) {}
    }

    @Test
    public void testLandingMessageFromBundle() throws Exception {
        ONSMessage message = ONSMessage.getMessageForBundle(getMockONSLandingMessageBundle());

        if (!(message instanceof ONSLandingMessage)) {
            fail();
        }
    }

    @Test
    public void testLandingMessageBundleSerialization() throws Exception {
        ONSLandingMessage msg = (ONSLandingMessage) ONSMessage.getMessageForBundle(
            getMockONSLandingMessageBundle()
        );

        JSONObject customPayload = msg.getCustomPayloadInternal();
        assertEquals("payload", customPayload.getString("custom"));

        final Bundle b = new Bundle();
        msg.writeToBundle(b);
        checkIfBundleIsLanding(b);

        final Intent i = new Intent();
        msg.writeToIntent(i);
        checkIfBundleIsLanding(i.getExtras());
    }

    @Test
    public void testLandingMessageDeserialization() throws Exception {
        ONSMessage msg = ONSMessage.getMessageForBundle(getMockONSLandingMessageBundle());
        JSONObject customPayload = msg.getCustomPayloadInternal();
        assertEquals("payload", customPayload.getString("custom"));

        final Bundle b = new Bundle();
        msg.writeToBundle(b);

        assertNotNull(ONSMessage.getMessageForBundle(b));

        final Intent i = new Intent();
        msg.writeToIntent(i);

        assertNotNull(ONSMessage.getMessageForBundle(i.getExtras()));
    }

    private void checkIfBundleIsLanding(Bundle bundle) throws Exception {
        final Bundle msgBundle = bundle.getBundle("com.ons.messaging.payload");

        assertNotNull(msgBundle);
        final Bundle data = msgBundle.getBundle("data");
        assertNotNull(data);
        final Bundle pushData = data.getBundle(ONS.Push.PAYLOAD_KEY);
        assertNotNull(pushData);
        assertNotNull(pushData.getString("com.ons"));
        assertEquals("landing", msgBundle.getString("kind"));
    }
}
