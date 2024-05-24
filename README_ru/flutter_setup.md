Интеграция JivoSDK(Flutter).
============================

Настройка Gradle Scripts.
-------------------------

Настройки **Gradle** (уровень проекта) (`build.gradle`):

```gradle
buildscript {
    ...
    repositories {
        google()
        mavenCentral()
        maven { url 'https://www.jitpack.io' }
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
plugins {
    ...
    id "dev.flutter.flutter-gradle-plugin"
    id 'com.google.gms.google-services'
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
    api platform('com.google.firebase:firebase-bom:32.7.2')
    api 'com.google.firebase:firebase-messaging'
}
```
> [!NOTE]<br>Для полноценной работы JivoSDK, необходимо настроить push-уведомления с помощью Firebase Cloud Messaging. Подробная [инструкция](./firebase_notifications_setup.md). 