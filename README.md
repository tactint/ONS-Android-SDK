<img src="https://static.pfs.gdn/documentation/Readmes/logo_ons_full_178@2x.png" srcset="https://static.pfs.gdn/documentation/Readmes/logo_ons_full_178.png 1x" width="178" height="80" alt="ONS Logo" />

# ONS Android SDK

The ONS Android SDK allows you to build meaningful communication experience in your Android app through highly personalized push notifications & In-App messages.

Our [📕 setup documentation](https://doc.pfs.gdn/ios/prerequisites) details the steps to take for an easy and successful integration.

# Prerequisites

The ONS Android SDK requires Android 5.0 (Lollipop / API Level 21).

# Documentation

- [Setup guide](https://doc.pfs.gdn/android/prerequisites): start your implementation here!
- [Help center](https://help.pfs.gdn/en/): answers to most questions you may have during the integration
- [API reference](https://doc.pfs.gdn/android-api-reference/index.html): this documents each of the classes and methods in the ONS Android SDK

You may also find this guide useful to review after integration to make sure you're ready to go live: [How can I test the integration on Android?](https://help.pfs.gdn/en/articles/2672749-how-can-i-test-the-integration-on-android)

# Releases

ONS is available in Maven Central  
You can also integrate the ONS Android SDK manually by [downloading the `.aar`](https://doc.pfs.gdn/download/android).

# Building

Build instructions are detailed in [BUILDING.md](BUILDING.md).

# Contributing

Please refer to our [contributing guidelines](CONTRIBUTING.md).

# Tracking Events

The ONS Android SDK reports various tracking events to the server. These events are grouped by the service that reports them. Below is a list of all tracking events, grouped by each service, along with sample payloads and details about when the events are triggered.

## TrackerWebService

### Events

- `START`
- `STOP`
- `OPEN_FROM_PUSH`
- `MESSAGING`
- `LOCAL_CAMPAIGN_VIEWED`
- `NATIVE_DATA_CHANGED`
- `INSTALL_DATA_CHANGED`
- `PROFILE_DATA_CHANGED`
- `INSTALL_DATA_CHANGED_TRACK_FAILURE`
- `LOCATION_CHANGED`
- `NOTIFICATION_STATUS_CHANGE`
- `INBOX_MARK_AS_READ`
- `INBOX_MARK_AS_DELETED`
- `INBOX_MARK_ALL_AS_READ`
- `OPT_IN`
- `OPT_OUT`
- `OPT_OUT_AND_WIPE_DATA`
- `PROFILE_IDENTIFY`
- `FIND_MY_INSTALLATION`

### Sample Payload

```json
{
  "id": "event_id",
  "date": "2023-01-01T00:00:00Z",
  "name": "START",
  "params": {
    "key": "value"
  },
  "session": "session_id",
  "ts": 1234567890
}
```

### Trigger Details

- `START`: Triggered when the SDK is started.
- `STOP`: Triggered when the SDK is stopped.
- `OPEN_FROM_PUSH`: Triggered when the app is opened from a push notification.
- `MESSAGING`: Triggered when a messaging event occurs.
- `LOCAL_CAMPAIGN_VIEWED`: Triggered when a local campaign is viewed.
- `NATIVE_DATA_CHANGED`: Triggered when native data changes.
- `INSTALL_DATA_CHANGED`: Triggered when install data changes.
- `PROFILE_DATA_CHANGED`: Triggered when profile data changes.
- `INSTALL_DATA_CHANGED_TRACK_FAILURE`: Triggered when tracking install data changes fails.
- `LOCATION_CHANGED`: Triggered when the location changes.
- `NOTIFICATION_STATUS_CHANGE`: Triggered when the notification status changes.
- `INBOX_MARK_AS_READ`: Triggered when an inbox message is marked as read.
- `INBOX_MARK_AS_DELETED`: Triggered when an inbox message is marked as deleted.
- `INBOX_MARK_ALL_AS_READ`: Triggered when all inbox messages are marked as read.
- `OPT_IN`: Triggered when the user opts in.
- `OPT_OUT`: Triggered when the user opts out.
- `OPT_OUT_AND_WIPE_DATA`: Triggered when the user opts out and wipes data.
- `PROFILE_IDENTIFY`: Triggered when the user profile is identified.
- `FIND_MY_INSTALLATION`: Triggered when the find my installation event occurs.

## StartWebservice

### Events

- `START`

### Sample Payload

```json
{
  "id": "event_id",
  "date": "2023-01-01T00:00:00Z",
  "name": "START",
  "params": {
    "key": "value"
  },
  "session": "session_id",
  "ts": 1234567890
}
```

### Trigger Details

- `START`: Triggered when the SDK is started.

## PushWebservice

### Events

- `PUSH`

### Sample Payload

```json
{
  "id": "event_id",
  "date": "2023-01-01T00:00:00Z",
  "name": "PUSH",
  "params": {
    "key": "value"
  },
  "session": "session_id",
  "ts": 1234567890
}
```

### Trigger Details

- `PUSH`: Triggered when a push notification is sent.

## AttributesSendWebservice

### Events

- `ATTRIBUTES_SEND`

### Sample Payload

```json
{
  "id": "event_id",
  "date": "2023-01-01T00:00:00Z",
  "name": "ATTRIBUTES_SEND",
  "params": {
    "key": "value"
  },
  "session": "session_id",
  "ts": 1234567890
}
```

### Trigger Details

- `ATTRIBUTES_SEND`: Triggered when user attributes are sent.
