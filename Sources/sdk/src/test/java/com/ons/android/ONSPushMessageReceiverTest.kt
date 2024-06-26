package com.ons.android

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
@SmallTest
class ONSPushMessageReceiverTest {

    companion object {
        const val EXTRA_MSG_TYPE = "message_type"
    }

    val context: Context = ApplicationProvider.getApplicationContext()

    @After
    fun tearDown() {
        ONSPushMessageReceiver.resetHandledMessageIDs()
    }

    @Test
    fun testMessageDeduplication() {
        val receiver = ObservablePushMessageReceiver()

        // Test that unique message IDs are all handled
        for (i in 0..ONSPushMessageReceiver.MAX_HANDLED_MESSAGE_IDS_COUNT + 10) {
            receiver.reset()
            receiver.onReceive(context, makeMessageIntent(null))
            Assert.assertTrue(receiver.presentNotificationCalled)
        }

        // We looped more than MAX_HANDLED_MESSAGE_IDS_COUNT, test that the receiver isn't leaking
        // memory by storing infinite IDs
        Assert.assertEquals(ONSPushMessageReceiver.MAX_HANDLED_MESSAGE_IDS_COUNT,
                ONSPushMessageReceiver.getHandledMessageIDsSize())

        // Test that multiple pushes are deduplicated
        val duplicateMessageID = UUID.randomUUID().toString()
        receiver.reset()
        receiver.onReceive(context, makeMessageIntent(duplicateMessageID))
        Assert.assertTrue(receiver.presentNotificationCalled)
        receiver.reset()
        receiver.onReceive(context, makeMessageIntent(null))
        Assert.assertTrue(receiver.presentNotificationCalled)
        receiver.reset()
        receiver.onReceive(context, makeMessageIntent(duplicateMessageID))
        Assert.assertFalse(receiver.presentNotificationCalled)
    }

    @Test
    fun testIgnoresNonFCMMessages() {
        val receiver = ObservablePushMessageReceiver()
        val intent = Intent()

        receiver.onReceive(context, intent)
        Assert.assertTrue(receiver.presentNotificationCalled)

        receiver.reset()
        intent.putExtra(EXTRA_MSG_TYPE, "gcm")
        Assert.assertFalse(receiver.presentNotificationCalled)

        receiver.reset()
        intent.putExtra(EXTRA_MSG_TYPE, "data")
        Assert.assertFalse(receiver.presentNotificationCalled)
    }

    private fun makeMessageIntent(forceID: String?): Intent {
        val identifier = forceID ?: UUID.randomUUID().toString()

        return Intent().apply {
            putExtra("msg", "test message")
            putExtra("com.ons", "{}")
            putExtra(EXTRA_MSG_TYPE, "gcm")
            putExtra("google.message_id", identifier)
        }
    }
}

private class ObservablePushMessageReceiver: ONSPushMessageReceiver() {
    var presentNotificationCalled = false

    override fun presentNotification(context: Context, intent: Intent): Boolean {
        presentNotificationCalled = true
        return true
    }

    fun reset() {
        presentNotificationCalled = false
    }
}