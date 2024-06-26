package com.ons.android.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.ons.android.ONS;
import com.ons.android.ONSUserAttribute;
import com.ons.android.MockONSAttributesFetchListener;
import com.ons.android.MockONSTagCollectionsFetchListener;
import com.ons.android.UserDataAccessor;
import com.ons.android.core.Promise;
import com.ons.android.di.DITest;
import com.ons.android.di.DITestUtils;
import com.ons.android.di.providers.RuntimeManagerProvider;
import com.ons.android.di.providers.UserModuleProvider;
import com.ons.android.module.UserModule;
import java.lang.reflect.Field;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class InstallDataEditorTest extends DITest {

    private InstallDataEditor editor;
    private Context context;

    @Before
    public void setUp() {
        super.setUp();
        context = ApplicationProvider.getApplicationContext();
        simulateONSStart(context);
        editor = ONS.Profile.editor();
    }

    @After
    public void tearDown() {
        super.tearDown();
        UserModuleProvider.get().clearInstallationData();
    }

    @Test
    public void testAttributesRead() throws Exception {
        editor.setAttribute("today", new Date());
        editor.setAttribute("float_value", 3.2);
        editor.setAttribute("int_value", 4);
        editor.setAttribute("url_value", new URI("ons://pfs.gdn"));
        editor.setAttribute("wrong_url_value", new URI("pfs.gdn"));
        editor.saveSync();

        MockONSAttributesFetchListener listener = new MockONSAttributesFetchListener();

        final Context ctx = RuntimeManagerProvider.get().getContext();
        UserDataAccessor.fetchAttributes(ctx, listener, false);

        Map<String, ONSUserAttribute> result = listener.getAttributes();

        assertTrue(listener.didFinish());
        assertFalse(listener.didFail());
        assertNotNull(result);
        assertEquals(4, listener.getAttributes().size()); // 3 attributes were set

        ONSUserAttribute dateValue = result.get("today");
        ONSUserAttribute urlValue = result.get("url_value");
        ONSUserAttribute wrongUrlValue = result.get("wrong_url_value");
        assertNotNull(dateValue);
        assertNull(dateValue.getStringValue());
        assertNull(dateValue.getNumberValue());
        assertNull(dateValue.getBooleanValue());
        assertNull(dateValue.getUriValue());
        assertNotNull(dateValue.getDateValue());

        assertNull(wrongUrlValue);
        assertNotNull(urlValue);
        assertNotNull(urlValue.getUriValue());
    }

    @Test
    public void testTagCollectionsRead() throws Exception {
        editor.saveSync();
        editor.addTag("collection_1", "tag_1");
        editor.addTag("collection_1", "tag_2");
        editor.addTag("collection_2", "tag_3");
        editor.addTag("collection_3", "TAG_4");
        editor.saveSync();

        MockONSTagCollectionsFetchListener listener = new MockONSTagCollectionsFetchListener();

        Context ctx = RuntimeManagerProvider.get().getContext();

        UserDataAccessor.fetchTagCollections(ctx, listener, false);

        Map<String, Set<String>> result = listener.getTagCollections();

        assertTrue(listener.didFinish());
        assertFalse(listener.didFail());
        assertNotNull(result);
        assertEquals(result.size(), 3); // 3 collections were set
        Set<String> collection1 = result.get("collection_1");
        assertTrue(collection1.contains("tag_2"));
        assertFalse(collection1.contains("tag_3"));
        Set<String> collection3 = result.get("collection_3");
        assertTrue(collection3.contains("tag_4")); // tags are set to lowercase when saved
    }

    @Test
    public void testCustomDataRead() throws Exception {
        // Inital test
        String initialRegion = ONS.User.getRegion(context);
        String initialLanguage = ONS.User.getLanguage(context);

        assertNull(initialRegion);
        assertNull(initialLanguage);

        editor.setRegion("az");
        editor.setLanguage("ba");

        Promise<Void> savePromise = editor.saveSync();

        // No error
        assertEquals(Promise.Status.RESOLVED, savePromise.getStatus());
        // Test reading
        assertEquals("az", ONS.User.getRegion(context));
        assertEquals("ba", ONS.User.getLanguage(context));

        // Clear custom data
        editor.setRegion(null);
        editor.setLanguage(null);

        savePromise = editor.saveSync();

        // No error
        assertEquals(Promise.Status.RESOLVED, savePromise.getStatus());

        // Test if clearing succeeded and that we're back to initial state.
        assertNull(ONS.User.getRegion(context));
        assertNull(ONS.User.getLanguage(context));
        assertEquals(ONS.User.getRegion(context), initialRegion);
        assertEquals(ONS.User.getLanguage(context), initialLanguage);
        assertNull(ONS.User.getIdentifier(context));
    }

    @Test
    public void testUserOperationsStacked() throws Exception {
        UserModule module = DITestUtils.mockSingletonDependency(UserModule.class, null);
        InstallDataEditor editor = ONS.Profile.editor();
        editor.setAttribute("dummy", "value").save();
        editor.setAttribute("dummy", "value").save();
        ONS.Profile.editor().setAttribute("dummy", "value").save();

        Field fieldQueues = UserModule.class.getDeclaredField("operationQueues");
        fieldQueues.setAccessible(true);
        List<UserOperationQueue> queues = (List<UserOperationQueue>) fieldQueues.get(module);

        // Ensure every editor instance has added its queue
        Assert.assertEquals(3, queues.size());
    }
}
