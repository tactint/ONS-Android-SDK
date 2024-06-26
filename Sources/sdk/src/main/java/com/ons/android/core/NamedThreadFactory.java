package com.ons.android.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * ThreadFactory that adds ONS's name to the created thread.
 * No further configuration is changed.
 *
 */
public class NamedThreadFactory implements ThreadFactory {

    private static ThreadFactory defaultFactory = Executors.defaultThreadFactory();

    private String suffix = null;

    public NamedThreadFactory() {}

    public NamedThreadFactory(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public Thread newThread(Runnable r) {
        final Thread t = defaultFactory.newThread(r);
        if (suffix != null) {
            t.setName("com.ons.android." + suffix);
        } else {
            t.setName("com.ons.android");
        }
        return t;
    }
}
