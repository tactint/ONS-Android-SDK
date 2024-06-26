package com.ons.android.inbox

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.ons.android.ONS
import com.ons.android.ONSInboxNotificationContent
import com.ons.android.ONSNotificationSource
import com.ons.android.PrivateNotificationContentHelper
import com.ons.android.json.JSONObject
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
@SmallTest
class InboxNotificationContentParsingTest {

    val now = Date()
    val notifID = UUID.randomUUID().toString()

    @Test
    fun testPayloadParsing() {
        // Test multiple combinations of payloads

        var payload = addMessageToPayload(makeBaseNotificationPayload(), "foo", "bar")
        var content: ONSInboxNotificationContent = makePublicNotification(payload)
        Assert.assertEquals("foo", content.body)
        Assert.assertEquals("bar", content.title)
        Assert.assertTrue(content.isUnread)
        Assert.assertFalse(content.isSilent)
        Assert.assertEquals("https://pfs.gdn", content.pushPayload.deeplink)
        Assert.assertEquals(now, content.date)
        Assert.assertEquals(notifID, content.notificationIdentifier)
        Assert.assertEquals(ONSNotificationSource.TRANSACTIONAL, content.source)

        payload = addMessageToPayload(makeBaseNotificationPayload(), "foo", null)
        payload.put("read", true)
        payload.getJSONObject("payload").getJSONObject("com.ons").put("t", "c")
        content = makePublicNotification(payload)
        Assert.assertEquals("foo", content.body)
        Assert.assertNull(content.title)
        Assert.assertFalse(content.isUnread)
        Assert.assertFalse(content.isSilent)
        Assert.assertEquals(ONSNotificationSource.CAMPAIGN, content.source)
    }

    @Test
    fun testSilentNotificationDetection() {
        // Test that a notification with no title and no body is silent

        var payload = addMessageToPayload(makeBaseNotificationPayload(), null, null)
        var content: ONSInboxNotificationContent = makePublicNotification(payload)
        Assert.assertTrue(content.isSilent)

        // Test that a notification with a title and no body is silent
        payload = addMessageToPayload(makeBaseNotificationPayload(), null, "bar")
        content = makePublicNotification(payload)
        Assert.assertTrue(content.isSilent)

        // Test that a notification with a title and body BUT with the "s" flag in com.ons is silent
        payload = addMessageToPayload(makeBaseNotificationPayload(), null, null)
        payload.getJSONObject("payload").getJSONObject("com.ons").put("s", true)
        content = makePublicNotification(payload)
        Assert.assertTrue(content.isSilent)

        // Test that a notification with a body is not silent
        payload = addMessageToPayload(makeBaseNotificationPayload(), "foo", null)
        content = makePublicNotification(payload)
        Assert.assertFalse(content.isSilent)
    }

    private fun makeBaseNotificationPayload(): JSONObject {
        return JSONObject().apply {
            put("notificationId", notifID)
            put("sendId", "abcdeff")
            put("notificationTime", now.time)
            put("payload", JSONObject().apply {
                put("com.ons", JSONObject().apply {
                    put("t", "t")
                    put("at", JSONObject().apply {
                        put("u", "https://pfs.gdn")
                    })
                    put("od", JSONObject().apply {
                        put("n", "5a3c93c0-7a3b-0000-0000-69f412b0000000")
                    })
                    put("l", "https://pfs.gdn")
                    put("i", "6y4g8guj-u1586420592376_000000")
                })
            })
        }
    }

    private fun addMessageToPayload(rootPayload: JSONObject, body: String?, title: String?): JSONObject {
        val payload = rootPayload.getJSONObject("payload")
        body?.let {
            payload.put(ONS.Push.BODY_KEY, body)
        }
        title?.let {
            payload.put(ONS.Push.TITLE_KEY, title)
        }
        return rootPayload
    }

    private fun makePublicNotification(payload: JSONObject): ONSInboxNotificationContent {
        return InboxFetchWebserviceClient.parseNotification(payload).let(PrivateNotificationContentHelper::getPublicContent)
    }
}