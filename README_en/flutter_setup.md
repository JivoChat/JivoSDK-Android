Jivo SDK integration(Flutter).
============================

Settings Gradle Scripts.
-------------------------

Settings **Gradle** (project level) (`build.gradle`):

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

Settings **Gradle** (application level) (`app/build.gradle`):

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
> [!NOTE]<br>For JivoSDK to work properly, you must set up push notifications using Firebase Cloud Messaging. Detailed [instructions](./firebase_notifications_setup.md).