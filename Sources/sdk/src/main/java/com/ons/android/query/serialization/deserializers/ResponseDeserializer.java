package com.ons.android.query.serialization.deserializers;

import com.ons.android.json.JSONException;
import com.ons.android.json.JSONObject;
import com.ons.android.query.response.Response;

/**
 * Abstract deserializer class for {@link Response}
 */
public abstract class ResponseDeserializer {

    /**
     * Initial json response
     */
    protected final JSONObject json;

    /**
     * Constructor
     *
     * @param json json response
     */
    protected ResponseDeserializer(JSONObject json) {
        this.json = json;
    }

    /**
     * Get the response id
     *
     * @return Response identifier
     * @throws JSONException parsing exception
     */
    protected String getId() throws JSONException {
        return json.getString("id");
    }

    /**
     * Deserialize method to implement in the child class
     *
     * @return The child response class deserialized
     * @throws JSONException parsing exception
     */
    public abstract Response deserialize() throws JSONException;
}
