package com.ons.android.query.serialization.deserializers;

import com.ons.android.json.JSONException;
import com.ons.android.json.JSONObject;
import com.ons.android.query.response.TrackingResponse;

/**
 * Deserializer class for {@link TrackingResponse}
 */
public class TrackingResponseDeserializer extends ResponseDeserializer {

    /**
     * Constructor
     *
     * @param json json response
     */
    public TrackingResponseDeserializer(JSONObject json) {
        super(json);
    }

    /**
     * Deserialize method
     *
     * @return TrackingResponse deserialized
     * @throws JSONException parsing exception
     */
    @Override
    public TrackingResponse deserialize() throws JSONException {
        return new TrackingResponse(getId());
    }
}
