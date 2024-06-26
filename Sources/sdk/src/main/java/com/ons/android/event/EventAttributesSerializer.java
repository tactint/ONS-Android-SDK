package com.ons.android.event;

import com.ons.android.ONSEventAttributes;
import com.ons.android.json.JSONArray;
import com.ons.android.json.JSONException;
import com.ons.android.json.JSONObject;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EventAttributesSerializer {

    public static JSONObject serialize(ONSEventAttributes eventAttributes) throws JSONException {
        JSONObject obj = new JSONObject();
        JSONObject attributes = serializeObject(eventAttributes);
        obj.put("attributes", attributes);
        obj.put("label", eventAttributes.getLabel());
        if (eventAttributes.getTags() != null) {
            obj.put("tags", new JSONArray(eventAttributes.getTags()));
        }
        return obj;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static JSONObject serializeObject(ONSEventAttributes eventAttributes)
        throws JSONException, ClassCastException {
        JSONObject obj = new JSONObject();
        for (Map.Entry<String, EventTypedAttribute> entry : eventAttributes.getAttributes().entrySet()) {
            EventTypedAttribute attribute = entry.getValue();
            String prefixedKey = entry.getKey().toLowerCase(Locale.US) + "." + attribute.type.getTypeChar();

            switch (attribute.type) {
                case URL:
                    obj.put(prefixedKey, attribute.value.toString());
                    break;
                case OBJECT:
                    obj.put(prefixedKey, serializeObject((ONSEventAttributes) attribute.value));
                    break;
                case OBJECT_ARRAY:
                    obj.put(prefixedKey, serializeList((List<ONSEventAttributes>) attribute.value));
                    break;
                case STRING_ARRAY:
                    obj.put(prefixedKey, new JSONArray((Collection) attribute.value));
                    break;
                default:
                    obj.put(prefixedKey, attribute.value);
                    break;
            }
        }
        return obj;
    }

    public static JSONArray serializeList(List<ONSEventAttributes> eventAttributesList) throws JSONException {
        JSONArray array = new JSONArray();

        for (ONSEventAttributes eventData : eventAttributesList) {
            array.put(serializeObject(eventData));
        }
        return array;
    }
}
