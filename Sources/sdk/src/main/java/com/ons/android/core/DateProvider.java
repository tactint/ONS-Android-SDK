package com.ons.android.core;

import androidx.annotation.NonNull;
import com.ons.android.date.ONSDate;

/**
 * Simple interface for a mockable date provider
 */

public interface DateProvider {
    @NonNull
    ONSDate getCurrentDate();
}
