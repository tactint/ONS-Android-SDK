package com.ons.android;

import androidx.annotation.NonNull;
import com.ons.android.annotation.PublicSDK;

/**
 * Represents a ONSAction triggerable by a basic CTA messaging component
 */
@PublicSDK
public class ONSMessageCTA extends ONSMessageAction {

    private String label;

    /**
     * This is a private constructor
     *
     * @hide
     */
    public ONSMessageCTA(@NonNull com.ons.android.messaging.model.CTA from) {
        super(from);
        label = from.label;
    }

    @NonNull
    public String getLabel() {
        return label;
    }
}
