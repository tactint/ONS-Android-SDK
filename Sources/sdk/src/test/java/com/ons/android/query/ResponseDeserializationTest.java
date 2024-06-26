package com.ons.android.query;

import com.ons.android.json.JSONException;
import com.ons.android.query.response.AttributesCheckResponse;
import com.ons.android.query.response.AttributesSendResponse;
import com.ons.android.query.response.PushResponse;
import com.ons.android.query.response.StartResponse;
import com.ons.android.query.response.TrackingResponse;
import com.ons.android.query.serialization.deserializers.AttributesCheckResponseDeserializer;
import com.ons.android.query.serialization.deserializers.AttributesSendResponseDeserializer;
import com.ons.android.query.serialization.deserializers.PushResponseDeserializer;
import com.ons.android.query.serialization.deserializers.StartResponseDeserializer;
import com.ons.android.query.serialization.deserializers.TrackingResponseDeserializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ResponseDeserializationTest {

    private ResponseFactory factory;

    @Before
    public void setUp() {
        factory = new ResponseFactory();
    }

    @Test
    public void testAttributesCheckResponseDeserializer() throws JSONException {
        AttributesCheckResponseDeserializer deserializer = new AttributesCheckResponseDeserializer(
            factory.createJsonAttributesCheckResponse()
        );
        AttributesCheckResponse response = deserializer.deserialize();
        Assert.assertEquals("dummy_id", response.getQueryID());
        Assert.assertEquals(AttributesCheckResponse.Action.RECHECK, response.getAction());
        Assert.assertEquals(Long.valueOf(1499960145L), response.getTime());
        Assert.assertEquals(1L, response.getVersion());
    }

    @Test
    public void testAttributesSendResponseDeserializer() throws JSONException {
        AttributesSendResponseDeserializer deserializer = new AttributesSendResponseDeserializer(
            factory.createJsonAttributesSendResponse()
        );
        AttributesSendResponse response = deserializer.deserialize();
        Assert.assertEquals("dummy_id", response.getQueryID());
        Assert.assertEquals("1234-1234-1234", response.getTransactionID());
        Assert.assertEquals(1L, response.getVersion());
    }

    @Test
    public void testPushResponseDeserializer() throws JSONException {
        PushResponseDeserializer deserializer = new PushResponseDeserializer(factory.createJsonPushResponse());
        PushResponse response = deserializer.deserialize();
        Assert.assertEquals("dummy_id", response.getQueryID());
    }

    @Test
    public void testStartResponseDeserializer() throws JSONException {
        StartResponseDeserializer deserializer = new StartResponseDeserializer(factory.createJsonStartResponse());
        StartResponse response = deserializer.deserialize();
        Assert.assertEquals("dummy_id", response.getQueryID());
    }

    @Test
    public void testTrackingResponseDeserializer() throws JSONException {
        TrackingResponseDeserializer deserializer = new TrackingResponseDeserializer(factory.createJsonTrackResponse());
        TrackingResponse response = deserializer.deserialize();
        Assert.assertEquals("dummy_id", response.getQueryID());
    }
}
