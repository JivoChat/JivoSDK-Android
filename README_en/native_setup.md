Jivo SDK integration
------------------
Project structure
-------------------------

```
your-project-name/
                 app/ 
                    src/
                       main/
                           java/
                               your.package.name/
                                       App.kt
                                       MainActivity.kt
                                       PushService.kt
                           res/
                              layout/
                                    activity_main.xml
                              values/
                                    strings.xml
                           AndroidManifest.xml        
                    build.gradle  
                 build.gradle
 ```

### Settings Gradle Scripts

Settings **Gradle** (project level) (`build.gradle`):

```gradle
buildscript {
   ext.kotlin_version = "1.5.10"
   repositories {
       google()
       mavenCentral()
   }
   dependencies {
       classpath 'com.android.tools.build:gradle:4.2.1'
       classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
       classpath 'com.google.gms:google-services:4.3.8'
   }
}

allprojects {
   repositories {
       maven { url "https://www.jitpack.io" }
       google()
       mavenCentral()
   }
}
...
```

Settings **Gradle** (application level) (`app/build.gradle`):

```gradle
plugins {
   ...
   id 'kotlin-kapt'
}

android {
   ...
   buildFeatures {
       dataBinding = true
   }
   ...
}

dependencies {
   ...
   //JivoSDK
   implementation 'com.jivosite.sdk:android-sdk:2.5.0'
   //firebase
   implementation platform('com.google.firebase:firebase-bom:26.2.0')
   implementation 'com.google.firebase:firebase-messaging'
}
```

Settings AndroidManifest.xml.
-------------------------

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    package="your.package.name">

    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:name=".App"
        android:allowBackup="false"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:theme="@style/AppTheme">
        <activity
            android:name=".MainActivity"
            android:configChanges="keyboard|keyboardHidden|orientation|screenSize|uiMode"
            android:exported="true"
            android:label="@string/app_name"
            android:launchMode="singleTask"
            android:windowSoftInputMode="adjustResize">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        
        <service
            android:name="your.package.name.PushService"
            android:enabled="true"
            android:exported="false">
            <intent-filter>
                <action android:name="com.google.firebase.MESSAGING_EVENT" />
            </intent-filter>
        </service>

        <meta-data
            android:name="com.google.firebase.messaging.default_notification_icon"
            android:resource="@drawable/ic_notification"
            tools:replace="android:resource" />
        <meta-data
            android:name="com.google.firebase.messaging.default_notification_color"
            android:resource="@color/notificationColor"
            tools:replace="android:resource" />

    </application>
</manifest>
```

### Initialisation JivoSDK

To initialise **JivoSDK** in a class inherited from the `Application` class, add a call to the `Jivo.init()` static method in the body of the overridden `onCreate()` method.
> [!IMPORTANT]<br>Initialise the JivoSDK library only in the `Application.onCreate()` method. If the application has multiple processes, make sure that JivoSDK is only initialised in the main process.

The `Jivo.init()` static method accepts the following parameters:
| Name | Type | Description |
| ------------- |---------|--------------------------------------------------------|
| appContext | Context | Application context. |

If you don't have class, which inherits from the `Application` class, then create it and write the name in `AndroidManifest.xml`

Example initialisation:
> Kotlin
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
> Java
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

Launch Jivo SDK.
--------------

Example of `activity_main.xml` implementation in the `res/layout/` directory

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/container"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".ui.MainActivity">
    
    ...
    
    <com.jivosite.sdk.ui.views.JivoChatButton
        android:id="@+id/jivoBtn"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

Add the following code to `MainActivity`:

```kotlin
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.jivoBtn)?.run {
            setOnClickListener {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, JivoChatFragment())
                    .commit()
            }
        }
    }
}
```
