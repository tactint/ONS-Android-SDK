package com.ons.android.di;

import android.content.Context;
import com.ons.android.ONS;
import com.ons.android.di.providers.RuntimeManagerProvider;
import com.ons.android.runtime.State;
import org.junit.After;
import org.junit.Before;

/**
 * Superclass for every test that use the dependency graph
 */
public class DITest {

    @Before
    public void setUp() {
        DI.reset();
    }

    @After
    public void tearDown() {
        DI.reset();
    }

    protected void simulateONSStart(Context context) {
        ONS.start("FAKE_API_KEY");
        RuntimeManagerProvider.get().changeState((state, config) -> State.READY);
        RuntimeManagerProvider.get().setContext(context);
    }
}
