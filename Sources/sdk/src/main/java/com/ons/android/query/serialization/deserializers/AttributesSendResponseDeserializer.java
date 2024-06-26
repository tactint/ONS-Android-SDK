package com.ons.android.query.serialization.deserializers;

import com.ons.android.json.JSONException;
import com.ons.android.json.JSONObject;
import com.ons.android.query.response.AttributesSendResponse;

/**
 * Deserializer class for {@link AttributesSendResponse}
 */
public class AttributesSendResponseDeserializer extends ResponseDeserializer {

    /**
     * Constructor
     *
     * @param json json response
     */
    public AttributesSendResponseDeserializer(JSONObject json) {
        super(json);
    }

    /**
     * Deserialize method
     *
     * @return AttributesSendResponse deserialized
     * @throws JSONException parsing exception
     */
    @Override
    public AttributesSendResponse deserialize() throws JSONException {
        AttributesSendResponse response = new AttributesSendResponse(getId());
        if (json.hasNonNull("trid")) {
            response.setTransactionID(json.getString("trid"));
        }
        if (json.hasNonNull("ver")) {
            response.setVersion(json.getLong("ver"));
        }
        if (json.hasNonNull("project_key")) {
            String projectKey = json.getString("project_key");
            if (!projectKey.isEmpty()) {
                response.setProjectKey(projectKey);
            }
        }
        return response;
    }
}
