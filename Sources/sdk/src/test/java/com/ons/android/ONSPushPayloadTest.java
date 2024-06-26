package com.ons.android;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.ons.android.core.InternalPushData;
import com.ons.android.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test {@link ONSPushPayload} model
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class ONSPushPayloadTest {

    private static final String DEEPLINK = "sdoifhsoif://oisdhf";
    private static final String LARGE_ICON_URL = "http://osdihsfoih.com/jqiopqj.png";
    private static final String BIG_PICTURE_URL = "http://oisdfhsof.com/sdfhsf.png";

    private Context appContext;

    @Before
    public void setUp() {
        appContext = ApplicationProvider.getApplicationContext();
    }

    /**
     * This test checks that wrong arguments throw the right errors
     *
     * @throws Exception
     */
    @Test
    public void testPreconditions() throws Exception {
        try {
            //noinspection ConstantConditions
            ONSPushPayload.payloadFromBundle(null);
            fail();
        } catch (IllegalArgumentException ignored) {}

        try {
            //noinspection ConstantConditions
            ONSPushPayload.payloadFromReceiverIntent(null);
            fail();
        } catch (IllegalArgumentException ignored) {}
    }

    /**
     * This test checks that a bundle that does not, at some point, contain "com.ons", will fail.
     * For performance reasons, there's no full integrity check done in ONSPushPayload
     *
     * @throws Exception
     */
    @Test
    public void testIncorrectData() throws Exception {
        final Bundle b = new Bundle();
        try {
            ONSPushPayload.payloadFromBundle(b);
        } catch (ONSPushPayload.ParsingException ignored) {}

        b.putBundle(ONS.Push.PAYLOAD_KEY, new Bundle());
        try {
            ONSPushPayload.payloadFromBundle(b);
        } catch (ONSPushPayload.ParsingException ignored) {}

        final Intent i = new Intent();
        try {
            ONSPushPayload.payloadFromReceiverIntent(i);
        } catch (IllegalArgumentException ignored) {}

        i.putExtra("foo", "bar");
        try {
            ONSPushPayload.payloadFromReceiverIntent(i);
        } catch (ONSPushPayload.ParsingException ignored) {}
    }

    /**
     * This test checks that a valid bundle is parsable and gets accurate information
     *
     * @throws Exception
     */
    @Test
    public void testValidDataForBundle() throws Exception {
        final Bundle payload = new Bundle();
        payload.putString("com.ons", getMockONSData());
        final Bundle b = new Bundle();
        b.putBundle(ONS.Push.PAYLOAD_KEY, payload);
        performSharedDataAssertions(ONSPushPayload.payloadFromBundle(b));
    }

    /**
     * This test checks that a valid bundle is parsable and gets accurate information
     *
     * @throws Exception
     */
    @Test
    public void testValidDataForReceiverIntent() throws Exception {
        final Intent i = new Intent();
        i.putExtra("com.ons", getMockONSData());
        performSharedDataAssertions(ONSPushPayload.payloadFromReceiverIntent(i));
    }

    private void performSharedDataAssertions(ONSPushPayload payload) throws Exception {
        assertTrue(payload.hasDeeplink());
        assertEquals(DEEPLINK, payload.getDeeplink());

        assertTrue(payload.hasCustomLargeIcon());
        assertEquals(LARGE_ICON_URL, payload.getCustomLargeIconURL(appContext));

        assertTrue(payload.hasBigPicture());
        assertEquals(BIG_PICTURE_URL, payload.getBigPictureURL(appContext));

        assertEquals(60L, payload.getInternalData().getReceiptMinDelay());
        assertEquals(3600L, payload.getInternalData().getReceiptMaxDelay());
        assertEquals(InternalPushData.ReceiptMode.DISPLAY, payload.getInternalData().getReceiptMode());

        assertTrue(payload.hasLandingMessage());

        final ONSMessage msg = payload.getLandingMessage();
        assertNotNull(msg);
        assertEquals("landing", msg.getKind());
    }

    /**
     * Get a mock ons push payload json. Should be put in com.ons
     *
     * @return
     */
    private static String getMockONSData() throws Exception {
        JSONObject onsData = new JSONObject();
        onsData.put("l", DEEPLINK);

        JSONObject largeIconObject = new JSONObject();
        largeIconObject.put("u", LARGE_ICON_URL);
        onsData.put("bi", largeIconObject);

        JSONObject bigPictureObject = new JSONObject();
        bigPictureObject.put("u", BIG_PICTURE_URL);
        onsData.put("bp", bigPictureObject);

        JSONObject receipt = new JSONObject();
        receipt.put("dmi", 60);
        receipt.put("dma", 3600);
        receipt.put("m", 1);
        onsData.put("r", receipt);

        String message =
            "{\"kind\":\"universal\",\"id\":\"webtest\",\"did\":\"webtest\",\"hero\":\"http://pfs.gdn/favicon.png\"," +
            "\"h1\":\"Hi\",\"h2\":\"Ho\",\"h3\":\"Subtitle\"," +
            "\"body\":\"Lorem ipsum.\",\"close\":true,\"cta\":[" +
            "{\"id\": \"okay\", \"label\": \"Okay!\", \"action\": \"callback\", \"actionString\": \"okaycallback\"}]," +
            "\"style\":\"#image-cnt {blur: 200;}\"}";
        onsData.put("ld", new JSONObject(message));

        return onsData.toString();
    }
}
