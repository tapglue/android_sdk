# Tapglue Android SDK

This will help you get started with Tapglue on Android step by step guide.

A more detailed documentation can be found on our [documentation](http://developers.tapglue.com/docs/android) website.

## Get started

To start using the Tapglue API you need an `APP_TOKEN`. Visit our [Dashboard](https://dashboard.tapglue.com) and login with your credentials or create a new account.

## Quickstart

We created a template with Tapglue already installed for you. Before diving into the specifics of our SDK you can download the [Quickstart-Project](https://github.com/tapglue/ios_quickstart) and start using Tapglue immediately.

## Sample App

Our [Sample app](https://github.com/tapglue/android_sample) covers most of the concepts in our SDK and is a great showcase if you want to check implementation details.

# Installing the SDK

This page will help you get started with Tapglue on Android step by step guide.

## Installing with Gradle

To use the SDK in Android we recommend installing with Gradle. This will automatically install the necessary dependencies and pull the SDK binaries from the Maven Central repository.

To install the current stable version add this dependency to your `build.gradle`

```gradle
repositories {
    jcenter()
    // or mavenCentral()
}

dependencies {
    compile 'com.tapglue.android:tapglue-android-sdk:2.0.2'
}
```

That's it! Build the project try out our app.

## Adding Permissions

Our SDK requires `ACCESS_NETWORK_STATE` and `INTERNET` permissions. Ensure the following permissions are requested in your `AndroidManifest.xml` file:

```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.INTERNET" />
```

## Initialise the library

To start using Tapglue, you must initialise our SDK with your app token first. You can find your app token in the Tapglue dashboard.

To initialise the library you need to import `import com.tapglue.android`; and initialise the SDK in the `onCreate()` method.

```java
import com.tapglue.android;

String yourUrl = "https://api.tapglue.com";
Configuration configuration = new Configuration(yourUrl, "YOUR_APP_TOKEN");
Tapglue tapglue = new Tapglue(configuration, context);
```

## SDK Configuration

The `Configuration` class lets you set

- Your URL
- Your client token
- enable logging

To enable logging for example you would:

```java
Configuration configuration = new Configuration(yourUrl, "YOUR_APP_TOKEN");
configuration.setLogging(true);
```

For us to be able to track your performance we need you to create a instance of tapglue in your application `onCreate`

## Compatibility

Versions of Tapglue greater than 2.0 will work for a deployment target of Android 4.0.3 (API 15) and above.

# Pagination

Our SDK provides paginated endpoints. This means you potentially have to paginate through several pages to get all the information from an endpoint. This applies to endpoints that return data in a list form. The previous page represents content older than the current page.

For example when you get the followers of the current user you get a page with the first results. This page will also contain a pointer to get more results.

```java
RxPage<List<User>> firstPage = tapglue.retrieveFollowers().toBlocking().first();
RxPage<List<Users>> secondPage = firstPage.getPrevious().toBlocking().first();
```

# Users

After installing Tapglue into your Android app, creating users is usually the first thing you need to do, to build the basis for your news feed.

## Create users

To create a user you first need to create the `User` instance you would like, and then call

- `tapglue.createUser(user)`

Here is an example of creating a user with the username and password:

```java
Tapglue tapglue = new Tapglue(configuration, context);

User user = new User("username", "password");
user = tapglue.create(user);
```

This creates the user on tapglue. You should replace the values of your instance with the ones returned from the server.

## Login users

After creating a user you probably want to log that user in. There are two ways, with username and password or with email and password.

```java
Tapglue tapglue = new Tapglue(configuration, context);
tapglue.loginWithUsername("username", "password");
```

After login the user gets persisted as the current user, and can be fetched by doing:

```java
Tapglue tapglue = new Tapglue(configuration, context);
User user = tapglue.getCurrentUser();
````

To refresh the persistent current user you should:

```java
Tapglue tapglue = new Tapglue(configuration, context);
User user = tapglue.refreshCurrentUser();
```

This will query the current user on our API and persist the response.

## Logout users

To logout the current user:

```java
Tapglue tapglue = new Tapglue(configuration, context);
tapglue.logoutCurrentUser();
```

## Update Current User

```java
Tapglue tapglue = new Tapglue(configuration, context);
User user = tapglue.getCurrentUser();
//apply changes to user..
user = tapglue.updateCurrentUser(user);
````

## Delete Current User

```java
Tapglue tapglue = new Tapglue(configuration, context);
tapglue.deleteCurrentUser();
```

## Search users

Connecting users and building a social graph is one of the most challenging parts of building a social experience. We provide three simple ways to help you get started.

## Search single users

One way to create connections between users within your app is to do a search. This can be achieved with the following:

```java
Tapglue tapglue = new Tapglue(configuration, context);
List<User> result = tapglue.searchUsers(searchTerm);
```

This will search for the provided term in the `username`, `firstName`, `lastName` and `email`.

## E-Mails Search

If you want to search for multiple e-mails and get back a list of users. This is usually the case when you want to sync users from a source like the address-book. To do so use the following:

```java
Tapglue tapglue = new Tapglue(configuration, context);
List<String> emails = Arrays.asList("user@domain",...);
List<User> result = tapglue.searchUsersByEmails(emails);
```

## Social-IDs Search

A similar behaviour can be achieved if you want to sync users from another network like Facebook or Twitter.

```java
Tapglue tapglue = new Tapglue(configuration, context);
// Specify list of socialIds
List<String> socialIds = Arrays.asList("1234567","7654321");
String platform = "facebook";

List<User> result = tapglue.searchUsersBySocialIds(platform, socialIds);
```

# Connect Users

## Create friend connection

```java
Tapglue tapglue = new Tapglue(configuration, context);

tapglue.createConnection(new Connection(futureFriend, Type.FRIEND, State.PENDING);
````

## Create follow connection

```java
Tapglue tapglue = new Tapglue(configuration, context);

tapglue.createConnection(new Connection(futureFriend, Type.FOLLOW, State.CONFIRMED);
```

## Friend vs. Follow

We allow you to create follow or friend connections between your users. Just specify the type to `follow` or `friend` and let us handle the rest.

## Connection State

In some communities you want to give users the power to confirm or reject an incoming connection request. Especially when you're using the friends model, this is a very valid use-case. We allow you to specify a state when creating a connection. You can decide to create confirmed connections by default or pending ones first, that the other user has to confirm afterwards.

|State|Description|
|-----|-----------|
|`pending`|The connection was requested but is not yet activated.|
|`confirmed`|The connection is confirmed and established.|
|`rejected`|The connection has been refused.|

Behind the scenes our API is handling the business logic that the connection states imply. For example it is not possible to create another connection if there is an existing one that has been rejected.

## Delete a connection

To delete a connection to a user you only need that users Id.

```java
Tapglue tapglue = new Tapglue(configuration, context);

tapglue.deleteConnection(userId);
```

# Posts

Events are very powerful to build Notification centers or activity feeds. However, if you wan't to include user generated content to build a proper news feed we provide a much more powerful entity for you: `Posts`.

## Create Posts

The method to create a post is:

- `tapglue.createPost(post)`

To create a post you first need to instantiate a Post object and set all the attributes you want the post to have. A post requires two fields on construction, the attachments and the visibility. The attachments can be understood as the content of the post, the visibility has three possible values:

- `private` only visible to the user who creates the post
- `connection` only visible to the connections of the creator of the post
- `public` visible to everybody on the network

```java
//create attachment
Map<String, String> contents = new HashMap<>();
contents.add("en-US", "the content of the post");
Attachment attachment = new Attachment(contents, Type.TEXT, "myContent");
List<Attachment> attachments = Arrays.asList(attachment);

Post post = new Post(attachments, Visibility.CONNECTION);

Tapglue tapglue = new Tapglue(configuration, context);
post = tapglue.createPost(post);
```

The content key should be a BCP 47 compliant string.

## Attachments

Each post can have multiple attachments. An attachments of a post can currently be of type text or a url. A text can be used to represent the user generated text. A url is useful for different use-case such as a reference to an image or video. Furthermore you can specify a name for each attachments to add more context to the post.

# Comments & Reactions

Posts are the core entity of a news feed. To provide a richer and more engaging experiences, Tapglue enables you to comment or like posts.

## Create Comments

To create a comment, simply call:
- `tapglue.createComment(postId, comment)`

You have to create a comment object that specifies the content and can then create it. Following example will show you how to do it:

```java
Map<String, String> content = new HashMap<>();
content.put("en-US", "the comment");
Comment comment = new Comment(content);

tapglue.createComment(postId, comment);
```

The content keys have to be BCP 47 compliant strings.

## Retrieve Comments

To retrieve all comments that have been created on a post use following option:

- `tapglue.retrieveCommentsForPost(postId)`

```java
Tapglue tapglue = new Tapglue(configuration, context);
List<Comment> comments = tapglue.retrieveCommentsForPost(postId);
```

## Update Comments

To update or delete a comment you can use:

- `updateComment`
- `deleteComment`

## Reacting on Posts

The following reactions are supported on posts: `LIKE`, `LOVE`, `WOW`, `HAHA`, `ANGRY`, `SAD`. An example of how to create a reaction:

```java
tapglue.createReaction(postId, WOW).subscribe();
```

## Remove Reaction

To remove a reaction call the `deleteReaction` method:

```java
tapglue.deleteReaction(postId, SAD).subscribe();
```

## Like Posts

Besides regular events that you can always use, we've created an explicit like method for posts as this is one of the core interactions of a social network. Similar to comments there is following method:

- `tapglue.createLike(postId)`

To create a like here is an example below:

```java
Tapglue tapglue = new Tapglue(configuration, context);
tapglue.createLike(postId);
```

## Retrieve Likes

To retrieve all likes for a post:

- `tapglue.retrieveLikesForPost(postId)`

Simply run the following to retrieve them:

```java
Tapglue tapglue = new Tapglue(configuration, context);
List<Like> likes = tapglue.retrieveLikesForPost(postId);
```

## Unlike Posts

If a user unlikes a post again, use following method:

```java
Tapglue tapglue = new Tapglue(configuration, context);
tapglue.deleteLike(postId);
```

# Feeds

In general there are three different types of feeds that Tapglue provides:

- News Feed
- Posts Feed
- Events Feed

The News Feed contains both: Posts and Events that have been created in the users social graph.
The Posts- and Events Feeds only contain entries of their associated type.

Additionally Tapglue provides lists of Posts and Events for a single user.

- User posts
- User events

Eventually, there is also the opportunity to query the feeds to only get certain types of events.

## Newsfeed

When retrieving the news feed you will get to lists: `posts` and `events` to do so run:

```java
Tapglue tapglue = new Tapglue(configuration, context);
NewsFeed feed = tapglue.retrieveNewsFeed();
List<Post> posts = feed.getPosts();
List<Event> events = feed.getEvents();
```

## Posts Feed

To retrieve a Posts Feed there is following method:

```java
Tapglue tapglue = new Tapglue(configuration, context);
List<Post> posts = tapglue.retrievePostFeed();
```

## Events Feed

Similar to the examples above, you can retrieve an events feed as shown in the example below:

```java
Tapglue tapglue = new Tapglue(configuration, context);
List<Event> events = tapglue.retrieveEventFeed();
```

## User Posts

You can also retrieve the posts of a single user and display them under a profile screen for example.

- `retrievePostsByUser(userId)`

```java
Tapglue tapglue = new Tapglue(configuration, context);
List<Post> posts = tapglue.retrievePostsByUser(userId);
```

## User Events

For retrieving a single users events:

- `retrieveEventsForUser(userId)`

```java
Tapglue tapglue = new Tapglue(configuration, context);
List<Event> events = tapglue.retrieveEventsForUser(userId);
```

## Friends and Followers

You might want to show friends, follower and following lists to user in your app. Our SDK provides three methods to do so:

- tapglue.retrieveFollowers()
- tapglue.retrieveFollowings()
- tapglue.retrieveFriends()

These methods can also be applied to other users with:

- tapglue.retrieveFollowersForUser(userId)
- tapglue.retrieveFollowingsForUser(userId)
- tapglue.retrieveFriendsForUser(userId)

## Retrieve Followers

Here is an example to retrieve all follower of the currentUser:

```java
Tapglue tapglue = new Tapglue(configuration, context);
List<User> followers = tapglue.retrieveFollowers();
```

## Debugging and Logging

You can turn on Tapglue logging by initialising the SDK with a custom configuration and setting enabling the debug mode there.

```java
Tapglue.TGConfiguration config = new Tapglue.TGConfiguration()
    .setDebugMode(true);
```

Setting `.setDebugMode(true)` will cause the Tapglue library to log the users, queueing, and uploading of events, and other fine-grained info that's useful for understanding what the library is doing.

## Error handling

Error handling is an important area when building apps. To always provide the best user-experience to your users we defined custom errors that might happen when implementing Tapglue.

Most methods will provide you either a value or an error. We recommend to always check the `success` or value first and handle errors in case they occur. Each error will contain a `code` and a `message`. You can use the codes do define the behaviour on certain errors.
