package com.ons.android;

import androidx.annotation.Nullable;
import com.ons.android.annotation.PublicSDK;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

@PublicSDK
public class ONSUserAttribute {

    public Object value;
    public Type type;

    public ONSUserAttribute(Object value, Type type) {
        this.value = value;
        this.type = type;
    }

    @Nullable
    public Date getDateValue() {
        if (type == Type.DATE) {
            return (Date) value;
        }
        return null;
    }

    @Nullable
    public String getStringValue() {
        if (type == Type.STRING) {
            return (String) value;
        }
        return null;
    }

    @Nullable
    public Number getNumberValue() {
        if (type == Type.LONGLONG || type == Type.DOUBLE) {
            return (Number) value;
        }
        return null;
    }

    @Nullable
    public Boolean getBooleanValue() {
        if (type == Type.BOOL) {
            return (Boolean) value;
        }
        return null;
    }

    @Nullable
    public URI getUriValue() {
        if (type == Type.URL) {
            return (URI) value;
        }
        return null;
    }

    @PublicSDK
    public enum Type {
        STRING,
        LONGLONG,
        DOUBLE,
        BOOL,
        DATE,
        URL,
    }
}
