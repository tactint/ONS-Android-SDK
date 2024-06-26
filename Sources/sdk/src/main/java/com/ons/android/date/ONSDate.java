package com.ons.android.date;

import androidx.annotation.NonNull;

public abstract class ONSDate implements Comparable<ONSDate> {

    protected long timestamp;

    public ONSDate(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTime(long time) {
        timestamp = time;
    }

    public long getTime() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ONSDate onsDate = (ONSDate) o;

        return timestamp == onsDate.timestamp;
    }

    @Override
    public int hashCode() {
        return (int) (timestamp ^ (timestamp >>> 32));
    }

    @Override
    public int compareTo(@NonNull ONSDate otherDate) {
        long thisVal = getTime();
        long anotherVal = otherDate.getTime();
        //Suppress the inspection as it's not available on API 15 (IntelliJ believes it is though)
        //noinspection UseCompareMethod
        return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
    }
}
