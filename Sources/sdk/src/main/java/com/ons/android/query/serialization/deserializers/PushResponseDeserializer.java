package com.ons.android.query.serialization.deserializers;

import com.ons.android.json.JSONException;
import com.ons.android.json.JSONObject;
import com.ons.android.query.response.PushResponse;

/**
 * Deserializer class for {@link PushResponse}
 */
public class PushResponseDeserializer extends ResponseDeserializer {

    /**
     * Constructor
     *
     * @param json json response
     */
    public PushResponseDeserializer(JSONObject json) {
        super(json);
    }

    /**
     * Deserialize method
     *
     * @return PushResponse deserialized
     * @throws JSONException parsing exception
     */
    @Override
    public PushResponse deserialize() throws JSONException {
        return new PushResponse(getId());
    }
}
