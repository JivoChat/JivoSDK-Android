Интеграция JivoSDK(React Native).
=================================

Открытие проекта.
-----------------

Для начала откройте проект **Android** в приложении **Android Studio**. Вы можете найти свой **Android-проект** в папке проекта приложений **React Native**:

<img src="https://user-images.githubusercontent.com/81690520/144239149-22151f18-d938-40c3-b71d-6ff92d5245dc.png" width="630">

> [!NOTE]<br>Мы рекомендуем использовать **Android Studio** для написания собственного кода. **Android studio - это IDE**, созданная для разработки под **Android**, и ее использование поможет вам быстро разрешать некоторые проблемы, такие как ошибки синтаксиса кода. Более подробную информацию вы можете получить по [ссылке](https://reactnative.dev/docs/native-modules-android)
 
Структура проекта
-------------------------

```
android/
        app/ 
           src/
              main/
                  java/
                      your.package.name/
                                       JivoSDKModule.java
                                       JivoSDKPackage.java
                                       MainActivity.java
                                       MainApplication.java
                                       PushService.java
                                       SdkChatActivity.java
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

Настройка Gradle Scripts.
-------------------------

Настройки **Gradle** (уровень проекта) (`build.gradle`):

```gradle
buildscript {
    ...
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        ...
        classpath 'com.google.gms:google-services:4.3.14'
    }
}

allprojects {
    repositories {
        ...
        google()
        mavenCentral()
        maven { url 'https://www.jitpack.io' }
    }
}
```

Настройки **Gradle** (уровень приложения) (`app/build.gradle`):

```gradle
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
   implementation 'com.jivosite.sdk:android-sdk:2.3.0'
   //firebase
   implementation platform('com.google.firebase:firebase-bom:26.2.0')
   implementation 'com.google.firebase:firebase-messaging'
}
```

Настройка AndroidManifest.xml.
-------------------------

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    package="your.package.name">

    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:name=".MainApplication"
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
        <activity
            android:name=".SdkChatActivity"
            android:configChanges="keyboard|keyboardHidden|orientation|screenSize|uiMode"
            android:exported="true"
            android:launchMode="singleTop"
            android:windowSoftInputMode="adjustResize"
            android:theme="@style/AppTheme"
            tools:replace="android:theme"/>

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

Инициализация JivoSDK.
----------------------

Для инициализации **JivoSDK** в классе `MainApplication.java`, который находится в папке `android/app/src/main/java/com/your-app-name/`, в тело переопределенного метода `onCreate()` добавьте вызов статического метода `Jivo.init()`.
> [!IMPORTANT]<br>Инициализируйте библиотеку JivoSDK только в методе `Application.onCreate()`. Если в приложении есть несколько
> процессов, убедитесь, что JivoSDK инициализируется только в главном процессе.

Метод `Jivo.init()` принимает следующие параметры:
| Название | Тип | Описание |
| ------------- |---------|--------------------------------------------------------|
| appContext | Context | Контекст приложения. |

Если у вас нет этого класса, который унаследован от класса `Application`, то создайте его и пропишите название в `AndroidManifest.xml`

Пример инициализации:

```java
public class MainApplication extends Application implements ReactApplication {
...
    @Override
    public void onCreate() {
    ...
        Jivo.init(this);
    
        //Опционально
        Jivo.setConfig(new Config.Builder()
                .setOnBackPressed(fragment -> {
                    if (fragment != null) {
                        FragmentActivity a = fragment.getActivity();
                        if (a != null) {
                            a.finish();
                        }
                    }
                    return Unit.INSTANCE;
                })
                .setOpenNotification(() -> {
                    Intent mainIntent = new Intent(this, MainActivity.class);
                    Intent sdkIntent = new Intent(this, SdkChatActivity.class);
                    TaskStackBuilder builder = TaskStackBuilder.create(this).addNextIntent(mainIntent).addNextIntent(sdkIntent);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        return builder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
                    } else {
                        return builder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    }
                })
                .build());

    }
}
...
}
```

Добавление модуля.
------------------

Необходимо создать `JivoSDKModule.java` внутри папки `android/app/src/main/java/your.package.name/`

```java
import android.content.Intent;


import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.jivosite.sdk.ui.chat.JivoChatActivity;

public class JivoSDKModule extends ReactContextBaseJavaModule {

   JivoSDKModule(ReactApplicationContext context) {
       super(context);
   }

   @NonNull
   @Override
   public String getName() {
       return "JivoSDKModule";
   }

    @ReactMethod
    public void openJivoSdk() {
        Activity context = getCurrentActivity();
        SdkChatActivity.show(context);
    }
}
```

Далее необходимо создать `JivoSDKPackage.java` внутри папки `android/app/src/main/java/your.package.name/`

```java
import androidx.annotation.NonNull;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class JivoSDKPackage implements ReactPackage {

   @NonNull
   @Override
   public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext reactContext) {
       List<NativeModule> modules = new ArrayList<>();
       modules.add(new JivoSDKModule(reactContext));
       return modules;
   }

   @NonNull
   @Override
   public List<ViewManager> createViewManagers(@NonNull ReactApplicationContext reactContext) {
       return Collections.emptyList();
   }
}
```
Чтобы зарегистрировать пакет JivoSDKModule, вы должны добавить **JivoSDKPackage** в список пакетов, возвращаемых методом **getPackages() ReactNativeHost**. Откройте файл **MainApplication** , который находится в **android/app/src/main/java/com/your-app-name/**.

```java
public class MainApplication extends Application implements ReactApplication {
...
    @Override
    public void onCreate() {
   ...
        Jivo.init(this);
    }

    private final ReactNativeHost mReactNativeHost =
            new ReactNativeHost(this) {
                @Override
                public boolean getUseDeveloperSupport() {
                    return BuildConfig.DEBUG;
                }

                @Override
                protected List<ReactPackage> getPackages() {
                    List<ReactPackage> packages = new PackageList(this).getPackages();
                    packages.add(new JivoSDKPackage());
                    return packages;
                }

                @Override
                protected String getJSMainModuleName() {
                    return "index";
                }
            };

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }
...
}
```

Открытие чата.
--------------

Найдите в приложении место, где вы хотели бы добавить вызов метода `openJivoSdk()`.

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

Необходимо добавить `SdkChatActivity.java` в папку `android/app/src/main/java/your.package.name/`

```java
package your.package.name;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.react.ReactActivity;
import com.jivosite.sdk.ui.chat.JivoChatFragment;

public class SdkChatActivity extends ReactActivity {

    public static void show(Context context) {
        Intent intent = new Intent(context, SdkChatActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdk_chat);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new JivoChatFragment())
                .commit();
    }
}
```
Необходимо добавить `activity_sdk_chat.xml` в папку `res/layout/`

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