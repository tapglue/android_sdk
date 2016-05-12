# Tapglue Android SDK

[![Download](https://api.bintray.com/packages/tapglue-com/maven/tapglue-android-sdk/images/download.svg)](https://bintray.com/tapglue-com/maven/tapglue-android-sdk/_latestVersion)

## Get started

To start using the Tapglue API you need an `APP_TOKEN`. Visit our [Dashboard](https://dashboard.tapglue.com)
and login with your credentials or create a new account.

Before diving into the specifics of our SDK we recommend to check out the [Sample app](https://github.com/tapglue/android_sample).
It covers most of the concepts in our SDK and is a great template to use.

## Installing with Gradle

To use the SDK in Android we recommend using Android Studio which comes by default enabled with
Gradle Wrapper support.

We distribute our SDK via [jCenter](https://bintray.com/bintray/jcenter) or [Maven](http://search.maven.org/).

To install the current stable version add this dependency to your `build.gradle`:

```gradle
repositories {
    jcenter()
    // or mavenCentral()
}

dependencies {
    compile 'com.tapglue.android:tapglue-android-sdk:1.0.0'
}
```

## Adding Permissions

Our SDK requires `ACCESS_NETWORK_STATE` and `INTERNET` permissions. Ensure the following permissions
are requested in your `AndroidManifest.xml` file:

```xml
<manifest package="com.tapglue.exampleapp"
          xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>

    <application
        android:name=".ExampleApplication">
        <!--
        More configuration here
        -->
    </application>
</manifest>
```

## Initialise the library

To start using Tapglue, you must initialise our SDK with your app token first. You can find your app
token in the [Tapglue dashboard](https://dashboard.tapglue.com).

To initialise the library you need to import `import com.tapglue.android;` and initialise the SDK in
the `onCreate()` method.

```java
import com.tapglue.android;


public class ExampleApplication extends Application {

    @Override
    public void onCreate() {
        Tapglue.initialize(this, "YOUR_APP_KEY");
    }
}
```

- `setApiBaseUrl`
- `setDebugMode`
- `setFlushInterval (in seconds)`
- `setAnalytics`

Simply call `Tapglue.initialize` and define a config like in the following:

```java
import com.tapglue.android;


public class ExampleApplication extends Application {

    @Override
    public void onCreate() {

        Tapglue.TGConfiguration config = new Tapglue.TGConfiguration()
            .setToken("YOUR_APP_KEY")
            .setDebugMode(true);
        Tapglue.initialize(this, config);
    }
}
```

In most cases, it makes sense to do this in `onCreate()` method of your main `Application`.

## Compatibility

Tapglue SDK currently supports Android `4.0.3` (`API 15`) and above as a deployment target.

# Create users

After installing the Tapglue SDK into your Android app, creating users is usually the first thing
you need to do, to build the basis for your news feed.

## Create and login users

Our SDK provides three convenient ways to create users. Creating users will automatically resolve in
a login afterwards as you would not ask the users to login again after they registered. Do achieve
this, you can call the `Tapglue.user().createAndLoginUser()` method.

```java
TGUser user = new TGUser()
    .setUserName("username")
    .setPassword("password");

Tapglue.user().createAndLoginUser(user, new TGRequestCallback<Boolean>() {
    @Override
    public boolean callbackIsEnabled() {
        return false;
    }

    @Override
    public void onRequestError(TGRequestErrorType tgRequestErrorType) {
        // Handle error
    }

    @Override
    public void onRequestFinished(Boolean success, boolean liveChange) {
        // Handle success
    }
});
```

In most cases, it makes sense to call this after a sign-up or login screen and read the data from
the values entered in the text fields.

## Login users

Even though `createAndLogin` will automatically login existing users, it's better to call the login
only if you are just showing a login screen for example. You can do it with the following call:

```java
Tapglue.user().login("username", "password", new TGRequestCallback<Boolean>() {
    @Override
    public boolean callbackIsEnabled() {
        return false;
    }

    @Override
    public void onRequestError(TGRequestErrorType tgRequestErrorType) {
        // Handle error
    }

    @Override
    public void onRequestFinished(Boolean success, boolean liveChange) {
        // Handle success
    }
});
```

## Logout users

To logout users you can simply call `Tapglue.user().logout()`.

```java
Tapglue.user().logout(new TGRequestCallback<Boolean>() {
    @Override
    public boolean callbackIsEnabled() {
        return false;
    }

    @Override
    public void onRequestError(TGRequestErrorType tgRequestErrorType) {
        // Handle error
    }

    @Override
    public void onRequestFinished(Boolean success, boolean liveChange) {
        // Handle success
    }
});
```

## Current User

When you successfully create or login a user, we store it as the `currentUser` by default. To
access the currentUser simply do the following:

```java
TGUser user = Tapglue.user().getCurrentUser();
```

If the currentUser exists (is not null), you can access its properties from any place in your
project.

```java
user.getUserName();
```

## Update Current User

If users want to update their profile information you can update it with the
`Tapglue.user().saveChangesToCurrentUser()` method.

```java
Tapglue.user().saveChangesToCurrentUser(user, new TGRequestCallback<Boolean>() {
    @Override
    public boolean callbackIsEnabled() {
        return false;
    }

    @Override
    public void onRequestError(TGRequestErrorType tgRequestErrorType) {
        // Handle error
    }

    @Override
    public void onRequestFinished(Boolean success, boolean liveChange) {
        // Handle success
    }
});
```

## Delete Current User

To delete the current user you can perform `Tapglue.user().deleteCurrentUser()`.

```java
Tapglue.user().deleteCurrentUser(new TGRequestCallback<Boolean>() {
    @Override
    public boolean callbackIsEnabled() {
        return false;
    }

    @Override
    public void onRequestError(TGRequestErrorType tgRequestErrorType) {
        // Handle error
    }

    @Override
    public void onRequestFinished(Boolean success, boolean liveChange) {
        // Handle success
    }
});
```

Connecting users and building a social graph is one of the most challenging parts of building a
social experience. We provide three simple ways to help you get started.

## Search users

One way to create connections between users within your app is to do a search. This can be achieved
with the method `Tapglue.user().search()`.

```java
Tapglue.user().search("searchString", new TGRequestCallback<Boolean>() {
    @Override
    public boolean callbackIsEnabled() {
        return false;
    }

    @Override
    public void onRequestError(TGRequestErrorType tgRequestErrorType) {
        // Handle error
    }

    @Override
    public void onRequestFinished(Boolean success, boolean liveChange) {
        // Handle success
    }
});
```

## Follow or Friend a user

The simplest way to create a connection is to either follow or friend a user. To do so you can use
the methods:

- `Tapglue.connection().followUser`
- `Tapglue.connection().friendUser`

```java
Tapglue.connections().friendUser(user.getID(), new TGRequestCallback<Boolean>()  {
    @Override
    public boolean callbackIsEnabled() {
        return false;
    }

    @Override
    public void onRequestError(TGRequestErrorType tgRequestErrorType) {
        // Handle error
    }

    @Override
    public void onRequestFinished(Boolean success, boolean liveChange) {
        // Handle success
    }
});
```

To unfriend a user simply call the `Tapglue.connections().unFriendUser()` method.

# Send Events

Once you have users and connections in place, events are the last thing you need to send before
retrieving a production-ready activity feed.

## Create Events

There are three ways to create events in the SDK.

- `Tapglue.event().createEvent()`

The first method is for convenience and works best if the only thing you have to specify when
triggering an event is the `type` and an `objectId`.

If you use the second option you are able to attach a rich object with multiple attributes to an
event.

In the last example you create a `TGEvent` object first that creates all information you can attach
to an event. In the following example we will use this option:

```java
TGEvent event = new TGEvent()
    .setType("like")
    .setLanguage("en")
    .setLocation("berlin")
    .setVisibility(TGEvent.TGEventVisibility.Connections);

Tapglue.event().createEvent(event, new TGRequestCallback<Boolean>()  {
    @Override
    public boolean callbackIsEnabled() {
        return false;
    }

    @Override
    public void onRequestError(TGRequestErrorType tgRequestErrorType) {
        // Handle error
    }

    @Override
    public void onRequestFinished(Boolean success, boolean liveChange) {
        // Handle success
    }
});
```

Events will be queued and eventually flushed to send them to Tapglue. That way you can also track
events when offline and send them at once when online again. In the [first section](doc:android) of
the Android Guide you can learn more about configuring the flush settings.

You can learn more about [updating event](doc:update-event), [deleting events](doc:delete-event) etc.
in the reference documentation below.

# Display News Feed

## News feed

The last thing we need to do is retrieve the news feed and display the data. To achieve that we can
simply call `Tapglue.feed().retrieveFeedForCurrentUser()`.

```java
Tapglue.feed().retrieveFeedForCurrentUser(new TGRequestCallback<Boolean>()  {
    @Override
    public boolean callbackIsEnabled() {
        return false;
    }

    @Override
    public void onRequestError(TGRequestErrorType tgRequestErrorType) {
        // Handle error
    }

    @Override
    public void onRequestFinished(Boolean success, boolean liveChange) {
        // Handle success
    }
});
```

## Unread news feed

To retrieve only the latest events that have not been read by the user you can call
`Tapglue.feed().retrieveUnreadFeedForCurrentUser()` to save bandwidth.

```java
Tapglue.feed().retrieveUnreadFeedForCurrentUser(new TGRequestCallback<Boolean>()  {
    @Override
    public boolean callbackIsEnabled() {
        return false;
    }

    @Override
    public void onRequestError(TGRequestErrorType tgRequestErrorType) {
        // Handle error
    }

    @Override
    public void onRequestFinished(Boolean success, boolean liveChange) {
        // Handle success
    }
});
```

## Count of unread feed

Sometimes you just want to know how many new events exists to display for a user. You can use that
information and display in a badge over an icon. You can retrieve the count with
`Tapglue.feed().retrieveUnreadCountForCurrentUser()`.

```java
Tapglue.feed().retrieveUnreadCountForCurrentUser(new TGRequestCallback<Boolean>()  {
    @Override
    public boolean callbackIsEnabled() {
        return false;
    }

    @Override
    public void onRequestError(TGRequestErrorType tgRequestErrorType) {
        // Handle error
    }

    @Override
    public void onRequestFinished(Boolean success, boolean liveChange) {
        // Handle success
    }
});
```

## User feed

You can also retrieve the events of a single user and display them under a profile screen for
example:

```java
Tapglue.feed().retrieveEventsForCurrentUser(new TGRequestCallback<Boolean>()  {
    @Override
    public boolean callbackIsEnabled() {
        return false;
    }

    @Override
    public void onRequestError(TGRequestErrorType tgRequestErrorType) {
        // Handle error
    }

    @Override
    public void onRequestFinished(Boolean success, boolean liveChange) {
        // Handle success
    }
});
```

# Friends and Follower Lists

You might want to show friends, follower and following lists to user in your app. Our SDK provides
three methods to do so:

- `Tapglue.feed().retrieveFollowersForCurrentUser`
- `Tapglue.feed().retrieveFollowsForCurrentUser`
- `Tapglue.feed().retrieveFriendsForCurrentUser`

These methods can also be applied to other users with:

- `Tapglue.feed().retrieveFollowersForUser`
- `Tapglue.feed().retrieveFollowsForUser`
- `Tapglue.feed().retrieveFriendsForUser`

## Retrieve Follower

Here is an example to retrieve all follower of the currentUser:

```java
Tapglue.feed().retrieveFollowersForCurrentUser(new TGRequestCallback<Boolean>()  {
    @Override
    public boolean callbackIsEnabled() {
        return false;
    }

    @Override
    public void onRequestError(TGRequestErrorType tgRequestErrorType) {
        // Handle error
    }

    @Override
    public void onRequestFinished(Boolean success, boolean liveChange) {
        // Handle success
    }
});
```

# Debugging and Logging

You can turn on Tapglue logging by initialising the SDK with a custom configuration and setting
enabling the debug mode there.

```java
Tapglue.TGConfiguration config = new Tapglue.TGConfiguration()
    .setDebugMode(true);
```

Setting `.setDebugMode(true)` will cause the Tapglue library to log the users, queueing, and
creating of events, and other fine-grained info that's useful for understanding what the library is
doing.

# Error handling

Error handling is an important area when building apps. To always provide the best user-experience
to your users we defined custom errors that might happen when implementing Tapglue.

Most methods will provide you either a value or an error. We recommend to always check the `success`
or value first and handle errors in case they occur. Each error will contain a `code` and a `message`.
You can use the codes do define the behaviour on certain errors.

# License

This SDK is provided under Apache 2.0 license. For the full license, please see the [LICENSE](LICENSE)
file that ships with this SDK.
