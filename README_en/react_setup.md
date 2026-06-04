Jivo SDK integration(React Native).
=================================

Opening of the project.
-----------------

First, open the **Android** project in the **Android Studio** application. You can find your **Android project** in the **React Native** app project folder:

> [!NOTE]<br>We recommend using **Android Studio** to write your own code. **Android studio is an IDE** designed for **Android** development, and using it will help you quickly resolve some issues such as code syntax errors. You can get more information at [link](https://reactnative.dev/docs/native-modules-android)

Project structure
-------------------------

```
android/
        app/ 
           src/
              main/
                  java/
                      your.package.name/
                                       JivoSDKModule
                                       JivoSDKPackage
                                       MainActivity
                                       MainApplication
                                       PushService
                                       SdkChatActivity
                  res/
                     layout/
                           activity_sdk_chat.xml
                     values/
                           strings.xml
                           styles.xml
                  AndroidManifest.xml        
           build.gradle  
       build.gradle
 ```

Settings Gradle Scripts.
-------------------------

Settings **Gradle** (project level) (`build.gradle`):

> <details><summary>Kotlin</summary>
>
>```gradle
>buildscript {
>    ...
>    ext {
>        buildToolsVersion = "35.0.0"
>        minSdkVersion = 24
>        compileSdkVersion = 35
>        targetSdkVersion = 35
>        ndkVersion = "26.1.10909125"
>        kotlinVersion = "1.9.24"
>    }
>    repositories {
>        google()
>        mavenCentral()
>    }
>    dependencies {
>        ...
>        classpath("com.android.tools.build:gradle")
>        classpath("com.facebook.react:react-native-gradle-plugin")
>        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin")   
>        classpath 'com.google.gms:google-services:4.4.2'
>    }
>}
>
>apply plugin: "com.facebook.react.rootproject"
>```
></details>
>
> <details> <summary>Java</summary>
>
>```gradle
>buildscript {
>    ...
>    repositories {
>        google()
>        mavenCentral()
>    }
>    dependencies {
>        ...
>        classpath 'com.google.gms:google-services:4.4.2'
>    }
>}
>
>allprojects {
>    repositories {
>        ...
>        google()
>        mavenCentral()
>        maven { url 'https://www.jitpack.io' }
>    }
>}
>```
></details>


Settings **Gradle** (application level) (`app/build.gradle`):

> <details><summary>Kotlin</summary>
>
>```gradle
>android {
>   ...
>   namespace "com.jivosite.jivosdk_reactnative_sample"
>   ...
>   defaultConfig {
>        applicationId "com.jivosite.jivosdk_reactnative_sample"
>        ...
>    }
>   ...
>   buildFeatures {
>       dataBinding = true
>   }
>   ...
>}
>
>dependencies {
>   ...
>   //JivoSDK
>   implementation 'com.jivosite.sdk:android-sdk:2.8.3'
>   //firebase
>   implementation platform('com.google.firebase:firebase-bom:33.12.0')
>   implementation 'com.google.firebase:firebase-messaging'
>}
>```
></details>
>
> <details><summary>Java</summary>
>
>```gradle
>android {
>   ...
>   buildFeatures {
>       dataBinding = true
>   }
>   ...
>}
>
>dependencies {
>   ...
>   //JivoSDK
>   implementation 'com.jivosite.sdk:android-sdk:2.8.3'
>   //firebase
>   implementation platform('com.google.firebase:firebase-bom:33.12.0')
>   implementation 'com.google.firebase:firebase-messaging'
>}
>```
></details>

Settings AndroidManifest.xml.
-------------------------

> <details><summary>AndroidManifest.xml</summary>
>
>```xml
><manifest xmlns:android="http://schemas.android.com/apk/res/android"
>    xmlns:tools="http://schemas.android.com/tools"
>    package="your.package.name">
>
>    <uses-permission android:name="android.permission.INTERNET" />
>
>    <application
>        android:name=".MainApplication"
>        android:allowBackup="false"
>        android:icon="@mipmap/ic_launcher"
>        android:label="@string/app_name"
>        android:roundIcon="@mipmap/ic_launcher_round"
>        android:theme="@style/AppTheme">
>        <activity
>            android:name=".MainActivity"
>            android:configChanges="keyboard|keyboardHidden|orientation|screenSize|uiMode"
>            android:exported="true"
>            android:label="@string/app_name"
>            android:launchMode="singleTask"
>            android:windowSoftInputMode="adjustResize">
>            <intent-filter>
>                <action android:name="android.intent.action.MAIN" />
>                <category android:name="android.intent.category.LAUNCHER" />
>            </intent-filter>
>        </activity>
>        <activity
>            android:name=".SdkChatActivity"
>            android:configChanges="keyboard|keyboardHidden|orientation|screenSize|uiMode"
>            android:exported="true"
>            android:launchMode="singleTop"
>            android:windowSoftInputMode="adjustResize"
>            android:theme="@style/AppTheme"
>            tools:replace="android:theme"/>
>
>        <service
>            android:name=".PushService"
>            android:enabled="true"
>            android:exported="false">
>            <intent-filter>
>                <action android:name="com.google.firebase.MESSAGING_EVENT" />
>            </intent-filter>
>        </service>
>
>        <meta-data
>            android:name="com.google.firebase.messaging.default_notification_icon"
>            android:resource="@drawable/ic_notification"
>            tools:replace="android:resource" />
>        <meta-data
>            android:name="com.google.firebase.messaging.default_notification_color"
>            android:resource="@color/notificationColor"
>            tools:replace="android:resource" />
>
>    </application>
></manifest>
>```
></details>


### Initialisation JivoSDK
----------------------

To initialise **JivoSDK** in a class inherited from the `Application` class, add a call to the `Jivo.init()` static method in the body of the overridden `onCreate()` method.
> [!IMPORTANT]<br>Initialise the JivoSDK library only in the `Application.onCreate()` method. If the application has multiple processes, make sure that JivoSDK is only initialised in the main process.

The `Jivo.init()` static method accepts the following parameters:
| Name | Type | Description |
| ------------- |---------|--------------------------------------------------------|
| appContext | Context | Application context. |

If you don't have class, which inherits from the `Application` class, then create it and write the name in `AndroidManifest.xml`

Example initialisation:

> <details><summary>Kotlin</summary>
>
>```kotlin
>
>...
>import com.jivosite.sdk.Jivo
>import com.jivosite.sdk.support.builders.Config
>...
>class MainApplication : Application(), ReactApplication {
>
>    override val reactNativeHost: ReactNativeHost =
>        object : DefaultReactNativeHost(this) {
>            override fun getPackages(): List<ReactPackage> =
>                PackageList(this).packages.apply {
>                    add(JivoSDKPackage())
>                }
>
>            override fun getJSMainModuleName(): String = "index"
>
>            override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG
>
>            override val isNewArchEnabled: Boolean = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
>            override val isHermesEnabled: Boolean = BuildConfig.IS_HERMES_ENABLED
>        }
>
>    override val reactHost: ReactHost
>        get() = getDefaultReactHost(applicationContext, reactNativeHost)
>
>    override fun onCreate() {
>        super.onCreate()
>        SoLoader.init(this, OpenSourceMergedSoMapping)
>        if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
>            load()
>        }
>
>        Jivo.init(this)
>
>        Jivo.setConfig(
>            Config.Builder()
>                .setWelcomeMessage(R.string.welcome_message_placeholder)
>                .build()
>        )
>    }
>}
>```
></details>
>
> <details><summary>Java</summary>
>
>```java
>public class MainApplication extends Application implements ReactApplication {
>...
>    @Override
>    public void onCreate() {
>    ...
>        Jivo.init(this);
>    
>        //Optional
>        Jivo.setConfig(new Config.Builder()
>                .setOnBackPressed(fragment -> {
>                    if (fragment != null) {
>                        FragmentActivity a = fragment.getActivity();
>                        if (a != null) {
>                            a.finish();
>                        }
>                    }
>                    return Unit.INSTANCE;
>                })
>                .setOpenNotification(() -> {
>                    Intent mainIntent = new Intent(this, MainActivity.class);
>                    Intent sdkIntent = new Intent(this, SdkChatActivity.class);
>                    TaskStackBuilder builder = TaskStackBuilder.create(this).addNextIntent(mainIntent).addNextIntent(sdkIntent);
>                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
>                        return builder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
>                    } else {
>                        return builder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
>                    }
>                })
>                .build());
>
>    }
>}
>...
>```
></details>


Adding a module.
------------------

Need to create  `JivoSDKModule` inside the `android/app/src/main/java/your.package.name/` directory

> <details><summary>Kotlin</summary>
>
>```kotlin
>import com.jivosite.sdk.Jivo
>import com.jivosite.sdk.support.builders.ContactInfo
>import com.jivosite.sdk.ui.chat.JivoChatActivity
>
>
>class JivoSDKModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
>
>    override fun initialize() {
>        super.initialize()
>        Jivo.init(reactApplicationContext)
>    }
>
>    override fun getName() = "JivoSDKModule"
>
>
>    @ReactMethod
>    fun createNewMessageEvent(name: String, location: String, callback: Callback) {
>        callback.invoke()
>    }
>
>    @ReactMethod
>    fun setup(channelId: String, userToken: String) {
>        channelId.split('/').apply {
>            Jivo.setData(widgetId = last(), userToken = userToken, host = firstOrNull { size > 1 } ?: "")
>        }
>    }
>
>    @ReactMethod
>    fun present() {
>        val activity = reactApplicationContext.currentActivity
>        Intent(activity, JivoChatActivity::class.java).apply {
>            if (activity?.packageManager?.let { resolveActivity(it) } != null) {
>                flags = Intent.FLAG_ACTIVITY_NEW_TASK
>                reactApplicationContext.startActivity(this)
>            }
>        }
>    }
>
>    @ReactMethod
>    fun setContactInfo(contactInfo: ReadableMap) {
>        Jivo.setContactInfo(ContactInfo.contactInfo {
>            this.name = contactInfo.getString("name") ?: ""
>            this.email = contactInfo.getString("email") ?: ""
>            this.phone = contactInfo.getString("phone") ?: ""
>            this.description = contactInfo.getString("description") ?: ""
>        })
>    }
>}
>```
></details>
> <details><summary>Java</summary>
>
>```java
>import android.content.Intent;
>
>
>import com.facebook.react.bridge.ReactApplicationContext;
>import com.facebook.react.bridge.ReactContextBaseJavaModule;
>import com.facebook.react.bridge.ReactMethod;
>import com.jivosite.sdk.ui.chat.JivoChatActivity;
>
>public class JivoSDKModule extends ReactContextBaseJavaModule {
>
>   JivoSDKModule(ReactApplicationContext context) {
>       super(context);
>   }
>
>   @NonNull
>   @Override
>   public String getName() {
>       return "JivoSDKModule";
>   }
>
>    @ReactMethod
>    public void openJivoSdk() {
>        Activity context = getCurrentActivity();
>        SdkChatActivity.show(context);
>    }
>}
>```
></details>


Next, it is necessary to create `JivoSDKPackage` inside the `android/app/src/main/java/your.package.name/` directory

> <details><summary>Kotlin</summary>
>
>```kotlin
>class JivoSDKPackage : ReactPackage {
>
>    override fun createViewManagers(
>        reactContext: ReactApplicationContext,
>    ): MutableList<ViewManager<View, ReactShadowNode<*>>> = mutableListOf()
>
>    override fun createNativeModules(
>        reactContext: ReactApplicationContext,
>    ): MutableList<NativeModule> = listOf(JivoSDKModule(reactContext)).toMutableList()
>}
>```
></details>
>
> <details><summary>Java</summary>
>
>```java
>
>import androidx.annotation.NonNull;
>
>import com.facebook.react.ReactPackage;
>import com.facebook.react.bridge.NativeModule;
>import com.facebook.react.bridge.ReactApplicationContext;
>import com.facebook.react.uimanager.ViewManager;
>
>import java.util.ArrayList;
>import java.util.Collections;
>import java.util.List;
>
>class JivoSDKPackage implements ReactPackage {
>
>   @NonNull
>   @Override
>   public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext reactContext) {
>       List<NativeModule> modules = new ArrayList<>();
>       modules.add(new JivoSDKModule(reactContext));
>       return modules;
>   }
>
>   @NonNull
>   @Override
>   public List<ViewManager> createViewManagers(@NonNull ReactApplicationContext reactContext) {
>       return Collections.emptyList();
>   }
>}
>```
></details>

To register the **JivoSDKModule** package, you must add **JivoSDKPackage** to the list of packages returned by the **getPackages() method of ReactNativeHost**. Open the **MainApplication** file, which is located in the **android/app/src/main/java/com/your-app-name/** directory.

> <details><summary>Kotlin</summary>
>
>```kotlin
>class MainApplication : Application(), ReactApplication {
>...
>    override val reactNativeHost: ReactNativeHost =
>        object : DefaultReactNativeHost(this) {
>            override fun getPackages(): List<ReactPackage> =
>                PackageList(this).packages.apply {
>                    add(JivoSDKPackage())
>                }
>
>            override fun getJSMainModuleName(): String = "index"
>
>            override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG
>
>            override val isNewArchEnabled: Boolean = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED
>            override val isHermesEnabled: Boolean = BuildConfig.IS_HERMES_ENABLED
>        }
>
>    override val reactHost: ReactHost
>        get() = getDefaultReactHost(applicationContext, reactNativeHost)
>...                
>}
>```
></details>
>
> <details><summary>Java</summary>
>
>```java
>public class MainApplication extends Application implements ReactApplication {
>...
>    @Override
>    public void onCreate() {
>   ...
>        Jivo.init(this);
>    }
>
>    private final ReactNativeHost mReactNativeHost =
>            new ReactNativeHost(this) {
>                @Override
>                public boolean getUseDeveloperSupport() {
>                    return BuildConfig.DEBUG;
>                }
>
>                @Override
>                protected List<ReactPackage> getPackages() {
>                    List<ReactPackage> packages = new PackageList(this).getPackages();
>                    packages.add(new JivoSDKPackage());
>                    return packages;
>                }
>
>                @Override
>                protected String getJSMainModuleName() {
>                    return "index";
>                }
>            };
>
>    @Override
>    public ReactNativeHost getReactNativeHost() {
>        return mReactNativeHost;
>    }
>...
>}
>```
></details>

Launch Jivo SDK.
--------------

Example of opening a **JivoSDK** chat

```javascript
import React from 'react';
import {NativeModules, StyleSheet, Text, View, Button} from 'react-native';

export default function App() {
 return (
   ...
     <Button
       title="OpenJivoSDK"
       onPress={() => {
         NativeModules.JivoSDKModule.openJivoSdk();
       }}
     />
  ...
 );
}
... 
```

Need to create `SdkChatActivity` in the `android/app/src/main/java/your.package.name/` directory

> <details><summary>Kotlin</summary>
>
>```kotlin
>
>class MainActivity : ReactActivity() {
>
>
>  override fun getMainComponentName(): String = "JivoSDKReactNativeSample"
>
>  override fun createReactActivityDelegate(): ReactActivityDelegate =
>  DefaultReactActivityDelegate(this, mainComponentName, fabricEnabled)
>
>}
>```
></details>
>
> <details><summary>Java</summary>
>
>```java
>
>package your.package.name;
>
>import android.content.Context;
>import android.content.Intent;
>import android.os.Bundle;
>
>import com.facebook.react.ReactActivity;
>import com.jivosite.sdk.ui.chat.JivoChatFragment;
>
>public class SdkChatActivity extends ReactActivity {
>
>    public static void show(Context context) {
>        Intent intent = new Intent(context, SdkChatActivity.class);
>        context.startActivity(intent);
>    }
>
>    @Override
>    protected void onCreate(Bundle savedInstanceState) {
>        super.onCreate(savedInstanceState);
>        setContentView(R.layout.activity_sdk_chat);
>        getSupportFragmentManager()
>                .beginTransaction()
>                .add(R.id.container, new JivoChatFragment())
>                .commit();
>    }
>}
>```
></details>

Need to create `activity_sdk_chat.xml` in the `res/layout/` directory

```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/container"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".SdkChatActivity">

</FrameLayout>
```