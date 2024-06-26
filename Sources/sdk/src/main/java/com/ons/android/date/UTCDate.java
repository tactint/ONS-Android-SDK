package com.ons.android.date;

public class UTCDate extends ONSDate {

    public UTCDate() {
        super(System.currentTimeMillis());
    }

    public UTCDate(long timestamp) {
        super(timestamp);
    }
}
