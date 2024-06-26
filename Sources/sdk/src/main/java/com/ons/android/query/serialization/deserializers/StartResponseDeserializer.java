package com.ons.android.query.serialization.deserializers;

import com.ons.android.json.JSONException;
import com.ons.android.json.JSONObject;
import com.ons.android.query.response.StartResponse;

/**
 * Deserializer class for {@link StartResponse}
 */
public class StartResponseDeserializer extends ResponseDeserializer {

    /**
     * Constructor
     *
     * @param json json response
     */
    public StartResponseDeserializer(JSONObject json) {
        super(json);
    }

    /**
     * Deserialize method
     *
     * @return StartResponse deserialized
     * @throws JSONException parsing exception
     */
    @Override
    public StartResponse deserialize() throws JSONException {
        return new StartResponse(getId());
    }
}
