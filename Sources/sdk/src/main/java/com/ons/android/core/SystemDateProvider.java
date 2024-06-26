package com.ons.android.core;

import com.ons.android.date.ONSDate;
import com.ons.android.date.UTCDate;

public class SystemDateProvider implements DateProvider {

    @Override
    public ONSDate getCurrentDate() {
        return new UTCDate();
    }
}
