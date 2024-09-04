Jivo Mobile SDK
=========================
[![Pub](https://img.shields.io/pub/v/stream_chat_flutter.svg)](https://pub.dartlang.org/packages/jivosdk_plugin)


The Jivo Mobile SDK allows you to embed a chat into your Flutter mobile applications to receive customer requests. The integration takes just a few minutes, as the chat interface with the message feed is already implemented - you only need to add a few lines of code to your project.

## Demo App

This repository includes a fully functional [example app](https://github.com/JivoChat/JivoSDK-FlutterDemo).

## Add dependency
Add this to your package's pubspec.yaml file.
```yaml
dependencies:
 stream_chat_flutter: 0.9.1
```
You should then run `flutter packages get`

## Quick start

```dart
import 'package:jivosdk_plugin/bridge.dart';

// Configure for authorized user
Jivo.session.setContactInfo(name: "Homer", email: "h.simpson@springfield.com", phone: "Simpson", brief: "Family guy");
Jivo.session.setup(channelId: "YOUR_CHANNEL_ID", userToken: "USER_JWT_TOKEN");

// ... or, configure for anonymous user
Jivo.session.setup(channelId: "YOUR_CHANNEL_ID", userToken: "");

// Present a chat
Jivo.display.present();
```

## Configuration

### Platform: iOS

Please add following lines into beginning of `ios/Podfile` before initial run:

```ruby
source 'https://github.com/cocoapods/specs'
source 'https://github.com/jivochat/cocoapods-repo'
```

Please make sure to call some corresponding JivoSDK methods
in your AppDelegate class for handling APNS, same way as in following example:

```swift
import UserNotifications
import Flutter
import JivoSDK


@UIApplicationMain
@objc class AppDelegate: FlutterAppDelegate {
    override func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        UNUserNotificationCenter.current().delegate = self
        GeneratedPluginRegistrant.register(with: self)
        Jivo.notifications.handleLaunch(options: launchOptions)
        return super.application(application, didFinishLaunchingWithOptions: launchOptions)
    }
    
    override func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Jivo.notifications.setPushToken(data: deviceToken)
    }
    
    override func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        Jivo.notifications.setPushToken(data: nil)
    }
    
    override func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        if let result = Jivo.notifications.didReceiveRemoteNotification(userInfo: userInfo) {
            completionHandler(result)
        }
        else {
            completionHandler(.noData)
        }
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        if let options = Jivo.notifications.willPresent(notification: notification, preferableOptions: .banner) {
            completionHandler(options)
        }
        else {
            completionHandler([])
        }
    }
    
    override func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        Jivo.notifications.didReceive(response: response)
        completionHandler()
    }
}
```

### Platform: Android

In your **root-level (project-level) Gradle file** (`<project>/build.gradle`), add the Google services plugin as a dependency:

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

In your **module (app-level) Gradle file** (usually `<project>/<app-module>/build.gradle`), add the dependencies and enable **dataBinding**:

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
Download and then add the Firebase Android configuration file (`google-services.json`) to your app:
1. Click Download google-services.json to obtain your Firebase Android config file.
2. Move your config file into the module (app-level) root directory of your app.

# What's next

This project is a starting point for a Flutter application.

A few resources to get you started if this is your first Flutter project:

- [Lab: Write your first Flutter app](https://docs.flutter.dev/get-started/codelab)
- [Cookbook: Useful Flutter samples](https://docs.flutter.dev/cookbook)

For help getting started with Flutter development, view the
[online documentation](https://docs.flutter.dev/), which offers tutorials,
samples, guidance on mobile development, and a full API reference.
