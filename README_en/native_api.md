# JivoSDK basic settings

Examples and descriptions of basic JivoSDK settings:

- [Jivo.init](#jivo_init) - Initialisation of the main components of **JivoSDK**.
- [Jivo.setData](#jivo_set_data) - Set up the channel, JWT token and host.
- [Jivo.enableLogging](#jivo_enable_logging) - Включение логирования **JivoSDK**.
- [Jivo.setContactInfo](#jivo_set_contact_info) - Enabling **JivoSDK** logging.
- [Jivo.setCustomData](#jivo_set_custom_data) - Sending additional information about the customer.
- [Jivo.clear](#jivo_clear) - Clearing **JivoSDK** data.
- [Jivo.addNewMessageListener](#jivo_add_new_message_listener) - Receive information about unread messages.
- [Jivo.disableInAppNotification](#jivo_disable_in_app_notification) - Disabling in-app notifications.
- [Jivo.addNotificationPermissionListener](#jivo_add_notification_permission_listener) - Receive information about the
  authorisation of notifications.
- [Jivo.setConfig](#jivo_set_config) - Additional **JivoSDK** settings.

## <a name="jivo_init">Jivo.init.</a>

The main method for initialising the JivoSDK. After calling the method, the main components for the full operation of JivoSDK are
initialised.
> [!IMPORTANT]<br>Initialise the JivoSDK library only in the `Application.onCreate()` method. If your application has multiple
> processes, make sure that JivoSDK is only initialised in the main process.

Example:
> <details><summary>Kotlin</summary>
>
>```kotlin
>class App : Application() {
>
>    override fun onCreate() {
>        super.onCreate()
>        Jivo.init(appContext = this)
>    }
>}
>```
></details>
>
> <details> <summary>Java</summary>
>
>```java
>public class App extends Application {
>    ...
>
>    @Override
>    public void onCreate() {
>        super.onCreate();
>        Jivo.init(this);
>        ...
>    }
>    ...
>}
>```
></details>

## <a name="jivo_set_data">Jivo.setData.</a>

To set and change the channel, save the history and set the connection host (optional), it is necessary to use
the `Jivo.setData()` method.

The `Jivo.setData()` method accepts the following parameters:
| Name | Type | Description |
| ------------- |---------|--------------------------------------------------------|
| widgetId | String | Unique channel id.|
| userToken | String | Optional parameter, JWT token is used to save the history. |
| host | String | Optional parameter, you can pass an empty string. |

Example:
> <details><summary>Kotlin</summary>
>
>```kotlin
>    Jivo.setData(widgetId = "xXxXxXxXx", userToken = "your_jwt_token", host = "")
>```
></details>
>
> <details> <summary>Java</summary>
>
>```java
>    Jivo.setData("xXxXxXxXx", "your_jwt_token", "");
>```
></details>

> [!IMPORTANT]<br>The `Jivo.setData()` method, must be called before opening a chat.
> <br>After calling `Jivo.setData()`, it is not necessary to call the `Jivo.clear()`, `Jivo.unsubscribeFromPush()` methods.
> <br>If you use notifications in your application, after calling `Jivo.setData()`, call the
> method `Jivo.subscribeFromPush()`.

> [!NOTE]<br>Saves chat history after deleting an app or wiping data. To save the chat history, you need to generate and use a
> **JWT-токен**. To sign the **JWT token**, you will need to generate a **SECRET** and pass it thought the Jivo desktop/web app in the SDK channel settings. Store **SECRET** only on the
**backend'e**, otherwise security will be compromised.
> The generated **JWT token** needs to be passed to the `Jivo.setData()` method. For more information on the JWT token, please
> visit https://jwt.io/introduction
>
>Example of payload generation:
>
>```kotlin
>{
>    "id": "123", // Unique identifier of the client, mandatory field name must be ‘id’
>    ... // Any parameters
>}
>```
>
>Example of token generation:
>
>```kotlin
>token = jwt.encode(payload, secret, HS256)
>```

## <a name="jivo_enable_logging">Jivo.enableLogging.</a>

Enable logging **JivoSDK**, in a class inherited from the `Application` class, in the body of the overridden `onCreate()` method,
it is necessary to initialise `Timber` and add a call to the `Jivo.enableLogging()` method. To filter logging
use the tag - `JivoSDK`.

Example:
> <details><summary>Kotlin</summary>
>
>```kotlin
>class App : Application() {
>    ...
>    override fun onCreate() {
>        ...
>        Timber.plant(Timber.DebugTree())
>        Jivo.init(this)
>        Jivo.enableLogging()
>        ...
>    }
>    ...
>}
>```
></details>
>
> <details> <summary>Java</summary>
>
>```java
>public class App extends Application implements ReactApplication {
>    ...
>
>    @Override
>    public void onCreate() {
>        ...
>        Timber.plant(new Timber.DebugTree());
>        Jivo.init(this);
>        Jivo.enableLogging();
>        ...
>    }
>    ...
>}
>```

Demonstration of logging using the inbuilt **Logcat** tool:

<img src="https://user-images.githubusercontent.com/81690520/144239108-ae744cf5-1ba0-4aa0-b5f2-af964b6e18ad.png" width="968">
</p>
</details>

## <a name="jivo_set_contact_info">Jivo.setContactInfo.</a>

Sets the visitor's contact details. The data is displayed to the operator as if it was entered by the visitor in the contact form.

| Name        | Type   | Description                                                                                              |
|-------------|--------|----------------------------------------------------------------------------------------------------------|
| name        | String | Username, used as an address in messages                                                                 |
| email       | String | E-mail address, for sending messages                                                                     |
| phone       | String | Phone number for calls                                                                                   |
| description | String | Additional information on the client (displayed in the field ‘Description’ - section ‘About the client’) |

Example:
> <details><summary>Kotlin</summary>
>
>```kotlin
>Jivo.setContactInfo(
>    contactInfo {
>        name = userName.value
>        email = userEmail.value ?: ""
>        phone = userPhone.value ?: ""
>        description = userDescription.value
>    }
>)
>```
></details>
>
> <details> <summary>Java</summary>
>
>```java
>Jivo.setContactInfo(
>        new ContactInfo(
>        name,
>        email,
>        phone,
>        description
>        )
>);
>```
></details>

## <a name="jivo_set_custom_data">Jivo.setCustomData.</a>

This function can be used to send arbitrary additional information about the customer to the operator. The information is
displayed in the information panel on the right side of the operator's application. The method can be called any number of times -
if the dialogue with the operator is already dialogue with the operator, the data in the operator's application will be updated in
real time. Fields are displayed in the order
of their sequence in the array.
| Name | Type | Description |
| ------------- | ------------- | ------------- |
| content |String | Data field content. Tags are escaped |
| title | String | Header to be added on top of the data field |
| link | String | URL opened by clicking on a data field |
| key | String | Description of the data field, added in bold before the field content by a colon |

Example:
> <details><summary>Kotlin</summary>
>
>```kotlin
>Jivo.setCustomData(
>    listOf(
>        CustomData(content = "Some content", title = "Some title", link = "Some link", key = "Some key")
>    )
>)
>```
></details>
>
> <details> <summary>Java</summary>
>
>```java
>List<CustomData> customDataFields=new ArrayList<>();
>        CustomData customData=new CustomData("Some content","Some title","Some link","Some key");
>        customDataFields.add(customData);
>
>        Jivo.setCustomData(customDataFields);
>```
></details>

## <a name="jivo_clear">Jivo.clear.</a>

Clearing JivoSDK data. After calling the `Jivo.clear()` method, the data in storage, repositories, database and
disabling the distribution of fluff.
> [!IMPORTANT]<br>For JivoSDK to work correctly, data cleansing should be performed after a logout in your application.

Example:
> <details><summary>Kotlin</summary>
>
>```kotlin
>override fun logout() {
>    ...
>    Jivo.clear()
>    ...
>}
>```
></details>
>
> <details> <summary>Java</summary>
>
>```java
>@Override
>void logout(){
>        ...
>        Jivo.clear();
>        ...
>}
>```
></details>

## <a name="jivo_add_new_message_listener">Jivo.addNewMessageListener.</a>

You must use the `Jivo.addNewMessageListener()` method to get information about unread messages. This method takes as input an
object of `NewMessageListener` type, which needs to implement the callback method `onNewMessage(hasUnread: Boolean)`.
callback method `onNewMessage(hasUnread: Boolean)`. The unread message status will be passed to the parameter of this method. To
work correctly, you must implement the `NewMessageListener` interface in the main `Activity`, `Viewmodel` or in the `Repository`.

Example:
> <details><summary>Kotlin</summary>
>
>```kotlin
>class MainViewModel : ViewModel(), NewMessageListener {
>
>    init {
>        Jivo.addNewMessageListener(this)
>    }
>
>    override fun onNewMessage(hasUnread: Boolean) {
>        ... //Some code
>    }
>}
>```
></details>
>
> <details> <summary>Java</summary>
>
>```java
>public class MainActivity extends AppCompatActivity implements NewMessageListener {
>       
>    ...
>    @Override
>    protected void onCreate(Bundle savedInstanceState) {
>        super.onCreate(savedInstanceState);
>        ...
>        Jivo.addNewMessageListener(this);
>    }
>
>    @Override
>    public void onNewMessage(boolean hasUnread) {
>        ... //Some code
>    }
>
>}
>```
></details>

## <a name="jivo_disable_in_app_notification">Jivo.disableInAppNotification.</a>

To disable in-app notifications, you need to call the `Jivo.disableInAppNotification()` method and pass a parameter of
type `Boolean`.
> [!NOTE]<br>The `Jivo.disableInAppNotification() method disables notifications only when the application is open.

> ```kotlin
>Jivo.disableInAppNotification()
>```

## <a name="jivo_add_notification_permission_listener">Jivo.addNotificationPermissionListener.</a>

To get notification permission information, use the `Jivo.addNotificationPermissionListener()` static method. This method takes as
input an object of type `NotificationPermissionListener`, for which it is required to implement the callback
method `onNotificationPermissionGranted(isGranted: Boolean)`. A flag indicating that notifications are enabled will be passed to
the parameter of this method. To work correctly, you must implement the `NotificationPermissionListener` interface in the
main `Activity`.

> [!NOTE]<br>This functionality works on Android 13 (API level 33) and higher.

Example:
> <details><summary>Kotlin</summary>
>
>```kotlin
>class MainActivity: NotificationPermissionListener {
>
>    init {
>        Jivo.addNotificationPermissionListener(this)
>    }
>    
>    override fun onNotificationPermissionGranted(isGranted: Boolean) {
>        ... //Some code
>    }
>}
>```
></details>
>
> <details> <summary>Java</summary>
>
>```java
>public class MainActivity extends AppCompatActivity implements NotificationPermissionListener {
>
>    ...
>    @Override
>    protected void onCreate(Bundle savedInstanceState) {
>        super.onCreate(savedInstanceState);
>        ...
>        Jivo.addNotificationPermissionListener(this);
>    }
>
>    @Override
>    public void onNotificationPermissionGranted(boolean isGranted) {
>        ... //Some code
>    }
>}
>```
></details>

## <a name="jivo_set_config">Jivo.setConfig.</a>

Advanced settings for **JivoSDK**. To add advanced settings for **JivoSDK**, create an object of the `Config.Builder` class with
the required settings.
> [!IMPORTANT]<br>You should only call the `Jivo.setConfig()` method in the `Application.onCreate()` method after calling
> the `Jivo.init()` method.

Example:
> <details><summary>Kotlin</summary>
>
>```kotlin
>class App : Application() {
>    ...
>    override fun onCreate() {
>        super.onCreate()
>        ...
>        Jivo.init(appContext = this)
>        Jivo.setConfig(
>            Config.Builder()
>                .setWelcomeMessage(R.string.welcome_message_placeholder)
>                .setOfflineMessage(R.string.offline)
>                .build()
>        )
>        ...
>    }
>    ...
>}
>```
></details>
>
> <details> <summary>Java</summary>
>
>```java
>public class App extends Application {
>   ...
>
>    @Override
>    public void onCreate() {
>        super.onCreate();
>        ...
>        Jivo.init(this);
>        Jivo.setConfig(new Config.Builder()
>                .setWelcomeMessage(R.string.welcome_message_placeholder)
>                .setOfflineMessage(R.string.offline)
>                .build());
>    }
>   ...
>}
>```
></details>

Table of JivoSDK advanced settings:
| Name of method | Short description |
| --- | --- |
| `setWelcomeMessage` | [Set the text of the welcome message.](#setWelcomeMessage) |
| `setOpenNotification` | [Custom navigation on click notification.](#setOpenNotification) |
| `setOnBackPressed` | [Custom navigation on the “back” button tap](#setOnBackPressed) |
| `setUriNotificationSound` | [Set a custom notification sound.](#setUriNotificationSound) |
| `setNotificationSmallIcon` | [Set a small push notification icon.](#setNotificationColorIcon) |
| `setOfflineMessage` | [Notification of the absence of operators on the channel.](#setOfflineMessage) |
| `useRattingStringsRes` | [Using custom, string resources for the evaluation form](#useRattingStringsRes) |

### <a name="setWelcomeMessage">setWelcomeMessage</a>

Set the text of the welcome message. You need to pass a string resource to the `setWelcomeMessage()` method. Welcome message is
displayed if there are no messages in the chat room.

Example:
> <details><summary>Kotlin</summary>
>
>```kotlin
>Jivo.setConfig(
>   Config.Builder()                  
>       .setWelcomeMessage(R.string.welcome_message_placeholder)
>       .build()
>)
>```
></details>
>
> <details> <summary>Java</summary>
>
>```java
>Jivo.setConfig(new Config.Builder()
>        .setWelcomeMessage(R.string.welcome_message_placeholder)
>        .build());
>```
></details>

### <a name="setOpenNotification">setOpenNotification</a>

To correctly open the chat in the correct view, you must use this interface. In `Config` was added
function `setOpenNotification()`, to which you need to pass a lambda that will return an object of type `PendingIntent`, thereby
so you can pass the parameters you need to the `PendingIntent.getActivity()` function.

Example:
> <details><summary>Kotlin</summary>
>
>```kotlin
>Jivo.setConfig(Config.Builder() 
>    .setOpenNotification {
>        PendingIntent.getActivity(
>            this,
>            0,
>            Intent(this, MainActivity::class.java),
>            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
>                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
>            } else {
>                PendingIntent.FLAG_UPDATE_CURRENT
>            }
>        )
>    }
>    .build()
>)
>```
></details>
>
> <details> <summary>Java</summary>
>
>```java
>Jivo.setConfig(new Config.Builder()
>        .setOpenNotification(() -> {
>            Intent mainIntent = new Intent(this, MainActivity.class);
>            Intent sdkIntent = new Intent(this, SdkChatActivity.class);
>            TaskStackBuilder builder = TaskStackBuilder.create(this).addNextIntent(mainIntent).addNextIntent(sdkIntent);
>            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
>                return builder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
>            } else {
>                return builder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
>            }
>        })
>        .build());
>}
>```      
></details>

### <a name="setOnBackPressed">setOnBackPressed</a>

Custom navigation on the “back” button tap

To handle pressing the “back” button, the `setOnBackPressed()` function was added to the config, in which the lambda is passed.
The lambda is called in the context of the `JivoChatFragment` object.

Example:
> <details><summary>Kotlin</summary>
>
>```kotlin
>Jivo.setConfig(Config.Builder()
>   .setOnBackPressed {
>       activity?.onBackPressed()
>   }
>   .build()
>)
>```
></details>
>
> <details> <summary>Java</summary>
>
>```java
> Jivo.setConfig(new Config.Builder()
>        .setOnBackPressed(fragment -> {
>            if (fragment != null) {
>                FragmentActivity a = fragment.getActivity();
>                if (a != null) {
>                    a.finish();
>                }
>            }
>            return Unit.INSTANCE;
>        })
>        .build());
> }
>```
></details>

### <a name="setUriNotificationSound">setUriNotificationSound</a>

The `setUriNotificationSound()` function has been added to `Config`, it is required to pass an object of type `Uri` which is
containing a reference to the sound resource.

Example:
> <details><summary>Kotlin</summary>
>
>```kotlin
>Jivo.setConfig(
>   Config.Builder()                  
>       .setUriNotificationSound(getUriNotificationSound(applicationContext))
>       .build()
>)
>
>fun getUriNotificationSound(applicationContext: Context): Uri =  Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${applicationContext.packageName}/${R.raw.jivo_tip}")
>```
></details>
>
> <details> <summary>Java</summary>
>
>```java
>Jivo.setConfig(
>   Config.Builder()                  
>       .setUriNotificationSound(getUriNotificationSound(applicationContext))
>       .build()
>)
>
>private Uri getUriNotificationSound(Context appContext) {
>    return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName() + "/" + R.raw.jivo_tip);
>}
>```
></details>

### <a name="setNotificationSmallIcon">setNotificationSmallIcon</a>

Setting a small push notification icon. It is necessary to pass the image resource to the `setNotificationSmallIcon()` method.

Example:
> <details><summary>Kotlin</summary>
>
>```kotlin
>Jivo.setConfig(
>   Config.Builder()                  
>       .setNotificationSmallIcon(R.drawable.ic_your_notification_small)
>       .build()
>)
>```
></details>
>
> <details> <summary>Java</summary>
>
>```java
>Jivo.setConfig(new Config.Builder()
>        .setNotificationSmallIcon(R.drawable.ic_your_notification_small)
>        .build());
>```
></details>

### <a name="setNotificationColorIcon">setNotificationColorIcon</a>

Setting the colour of the push notification icon. The `setNotificationColorIcon()` method requires passing a colour resource.

Example:
> <details><summary>Kotlin</summary>
>
>```kotlin
>Jivo.setConfig(
>   Config.Builder()                  
>       .setNotificationColorIcon(R.color.your_notification_color_icon)
>       .build()
>)
>```
></details>
>
> <details> <summary>Java</summary>
>
>```java
>Jivo.setConfig(new Config.Builder()
>        .setNotificationColorIcon(R.color.your_notification_color_icon)
>        .build());
>```
></details>

### <a name="setOfflineMessage">setOfflineMessage</a>

Notification about the absence of agents on the channel, displayed in the absence of agents on the channel.
In the `setOfflineMessage()` function, you need to pass a string resource. Channel Absence Alert, displayed when there are no
operators on the channel.

Example:
> <details><summary>Kotlin</summary>
>
>```kotlin
>Jivo.setConfig(
>   Config.Builder()                  
>       .setOfflineMessage(R.string.offline)
>       .build()
>)
>```
></details>
>
> <details> <summary>Java</summary>
>
>```java
>Jivo.setConfig(new Config.Builder()
>        .setOfflineMessage(R.string.offline)
>        .build());
>```
></details>

### <a name="useRattingStringsRes">useRattingStringsRes</a>

Using custom, string resources for the evaluation form. To do this, you will need to add keys to your application's string
resources
keys and call enable the use of custom string resources for the evaluation form `useRattingStringsRes()`.

Example:
> <details><summary>Kotlin</summary>
>
>```kotlin
>Jivo.setConfig(
>   Config.Builder()                  
>       .useRattingStringsRes()
>       .build()
>)
>```
></details>
>
> <details> <summary>Java</summary>
>
>```java
>Jivo.setConfig(new Config.Builder()
>        .useRattingStringsRes()
>        .build());
>```

> Location of strings.xml in the project
> ```
>YourProject/
>    src/
>        Activity.java
>    res/
>        values/
>            strings.xml
>
> ```
>strings.xml
>```xml
><?xml version="1.0" encoding="UTF-8"?>
><resources>
>    ...   
>    <string name="rate_form.description">How satisfied are you with the chat answers?</string>
>    <string name="rate_form.finish_description_bad">We strive to do the best service for you, we will work on the bugs. Thank you for your feedback!</string>
>    <string name="rate_form.finish_description_good">Glad you liked it :)</string>
></resources>
>```
></details>
