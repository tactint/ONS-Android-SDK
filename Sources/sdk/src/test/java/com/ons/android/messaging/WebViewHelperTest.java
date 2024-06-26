package com.ons.android.messaging;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class WebViewHelperTest {

    @Test
    public void testAnalyticsID() {
        Assert.assertNull(WebViewHelper.getAnalyticsIDFromURL(""));
        Assert.assertNull(WebViewHelper.getAnalyticsIDFromURL("https://pfs.gdn"));
        Assert.assertNull(WebViewHelper.getAnalyticsIDFromURL("https://pfs.gdn/onsAnalyticsID=foo"));
        Assert.assertNull(WebViewHelper.getAnalyticsIDFromURL("https://pfs.gdn/?onsAnalyticsid=foo"));
        Assert.assertEquals("foo", WebViewHelper.getAnalyticsIDFromURL("https://pfs.gdn/?onsAnalyticsID=foo"));
        Assert.assertEquals(
            "foo",
            WebViewHelper.getAnalyticsIDFromURL("https://pfs.gdn/index.html?onsAnalyticsID=foo")
        );
        Assert.assertEquals(
            "foo",
            WebViewHelper.getAnalyticsIDFromURL("https://pfs.gdn/?test=test&onsAnalyticsID=foo")
        );
        Assert.assertEquals(
            "space example",
            WebViewHelper.getAnalyticsIDFromURL("https://pfs.gdn/?onsAnalyticsID=space%20example")
        );
    }
}
