Настройка push-уведомлений с помощью Firebase Cloud Messaging.
-----------------------------

### Создание проекта.

Войдите в [Firebase](https://console.firebase.google.com/), затем создайте свой проект. Нажмите на **Create a project** и выполните следующие инструкции.

<img src="https://user-images.githubusercontent.com/81690520/144238781-44fbe2d1-cb37-41a1-8ce6-7625e82e281c.png" width="630">


Введите название вашего проекта.

<img src="https://user-images.githubusercontent.com/81690520/144238826-371e6266-960b-450d-ac94-184bf537b3ae.png" width="630">


Добавьте **Google Analytics** в ваш проект(Рекомендация)

<img src="https://user-images.githubusercontent.com/81690520/144238845-b901d9be-e533-4dc8-86ec-8a1a3d3b361b.png" width="630">


Выберите соответствующие настройки и нажмите **Create project**.

<img src="https://user-images.githubusercontent.com/81690520/144238863-4688e9b1-67ef-4478-9502-2e38703fb1dc.png" width="630">


Дождитесь сборки проекта.

<img src="https://user-images.githubusercontent.com/81690520/144238882-ceabd77c-ac50-40c0-ba81-4b36857e2c2e.png" width="630">

### Получение google-services.json.

Нажмите на кнопку <img src="https://user-images.githubusercontent.com/81690520/144238902-8f6a0074-132a-4dd3-a13b-d00dfea81929.png" width="16">, затем выберете **Project settings**.

<img src="https://user-images.githubusercontent.com/81690520/144238932-8c16050c-0f4a-487d-a7f6-75c3c16c773c.png" width="630">


В **Your apps**, щелкните значок **Android** (<img src="https://user-images.githubusercontent.com/81690520/144238953-08a73ba5-34e0-4a46-bd3b-801f5b72bf24.png" width="16">) или **Add app**. Если у вас есть уже приложение, то выберете имя пакета, для которого нужен файл конфигурации.

<img src="https://user-images.githubusercontent.com/81690520/144238968-3ecaf9f5-183d-4112-ae7c-6299dc193164.png" width="630">


Введите [имя пакета](https://developer.android.com/studio/build/configure-app-module?authuser=0#set_the_application_id) вашего приложения в поле имени пакета **Android**.

<img src="https://user-images.githubusercontent.com/81690520/144238985-1aac954f-dd8a-400a-83e7-cf999ff21c96.png" width="630">


(Необязательно) Введите другую информацию о приложении: псевдоним приложения и сертификат подписи отладки **SHA-1**.

Скопируйте в приложение файл конфигурации **Firebase Android**. Нажмите **Download google-services.json**, чтобы получить файл конфигурации **Firebase Android (google-services.json)**. Переместите файл конфигурации в каталог модуля (уровня приложения) вашего приложения.

<img src="https://user-images.githubusercontent.com/81690520/144239001-b65f32fd-2035-4a9e-b61c-12759daecf5e.png" width="630">


### Получение приватного ключа.

Для генерации приватного ключа нажать **Generate new private key** и сохранить новый ключ.

<img src="https://user-images.githubusercontent.com/81690520/144239017-974225bc-6fa8-486e-97ad-0ff256ad227a.png" width="630">


### Привязка private key к каналу SDK.

Для добавления private key, вам нужно зайти в [приложение Jivo](http://app.jivosite.com/), затем перейти в **Управление > Каналы связи**, в разделе Управление каналами найти нужный вам канал и нажать кнопку Настроить.

<img src="https://user-images.githubusercontent.com/81690520/144239039-5a435d45-2e06-4aba-b022-c149ae0a7cdb.png" width="630">


В настройках канала выбрать **Настройки Push** и нажать кнопку **Загрузить JSON**, выбрать приватный ключ и дождаться выгрузки.

В рамках одного канала **Mobile SDK** можно подключить по одному приложению для каждой платформы: **iOS и Android**. Соответствующий интерфейс загрузки **P8 сертификата** для работы **Push уведомлений** через **Apple APNS** смотрите ниже.

<img src="https://user-images.githubusercontent.com/81690520/144239059-4de99283-4010-4584-9a8b-c903feaa2271.png" width="630">


<img src="https://user-images.githubusercontent.com/81690520/144239071-c8b0a486-996b-4376-be2d-3123e444c222.png" width="630">


### Добавление зависимостей.

В файле **Gradle** корневого уровня (уровня проекта) (`build.gradle`) добавьте правила для включения подключаемого модуля **Gradle** служб **Google**. Убедитесь, что у вас есть репозиторий **Maven** от **Google**.

<details>
<summary>Пример:</summary>
<p>

```gradle
buildscript {
    ...
    repositories {
         mavenCentral()
        ...
    }
    dependencies {
        ...
        classpath 'com.google.gms:google-services:4.3.8'
        ...
    }
}

allprojects {
    repositories {
        ...
     mavenCentral()
        ...
    }
}
...
}
```
</p>
</details>

В вашем модуле (уровня приложения) файле **Gradle** (обычно `app/build.gradle`) примените плагин **Google Services Gradle** и объявите зависимости для продуктов **Firebase**:

<details>
<summary>Пример:</summary>
<p>

```gradle
plugins {
    ...
    id 'com.google.gms.google-services'
}
...
dependencies {
    ...
    implementation platform('com.google.firebase:firebase-bom:26.2.0')
    implementation 'com.google.firebase:firebase-messaging'
    implementation 'com.google.firebase:firebase-analytics-ktx'
}
```
</p>
</details>


### Инициализация Jivo Firebase Messaging Service.

Создайте класс `PushService` и унаследуйте его от класса `JivoFirebaseMessagingService`

<details>
<summary>Пример:</summary>
<p>

>Kotlin
>```kotlin
>package com.jivosite.example
>
>import com.jivosite.sdk.push.JivoFirebaseMessagingService
>
>class PushService : JivoFirebaseMessagingService()
>```

>Java
>```java
>package com.jivosite.example
>
>import com.jivosite.sdk.push.JivoFirebaseMessagingService
>
>public class PushService extends FirebaseMessagingService {
>    ...
>}
>```

</p>
</details>

Затем в `AndroidManifest.xml` добавьте сервис и метаданные.

<details>
<summary>Пример:</summary>
<p>

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.jivosite.jivosdk_android_sample">
    ...
    <service
        android:name="com.jivosite.jivosdk_android_sample.PushService"
        android:enabled="true"
        android:exported="false">
        <intent-filter>
            <action android:name="com.google.firebase.MESSAGING_EVENT" />
        </intent-filter>
    </service>

    <meta-data
        android:name="com.google.firebase.messaging.default_notification_icon"
        android:resource="@drawable/ic_notification_small" />
    <meta-data
        android:name="com.google.firebase.messaging.default_notification_color"
        android:resource="@color/darkPastelGreen" />

</application>
```
</p>
</details>


### Обработка Push notifications.

Если в вашем приложении используются **Push уведомления** с помощью **Firebase Cloud Messaging**, то для корректной работы сервиса в классе, унаследованном от класса `FirebaseMessagingService` , потребуется выполнить следующие настройки:

-   В методе `onNewToken()` добавить вызов статического метода  `Jivo.updatePushToken()` . Данный метод отвечает за обновление токена.
-   В методе `onMessageReceived()` добавить вызов статического метода  `Jivo.handleRemoteMessage()`. Данный метод выполняет проверку и возвращает `true/false`, в зависимости от принадлежности сообщения к **JivoSDK**.

<details>
<summary>Пример:</summary>
<p>

>Kotlin
>```kotlin
>class PushService : FirebaseMessagingService() {
>    ...
>
>    override fun onNewToken(token: String) {
>        ...
>        Jivo.updatePushToken(token)
>    }
>
>    override fun onMessageReceived(message: RemoteMessage) {
>        ...
>        if (!Jivo.handleRemoteMessage(message)) {
>            //Выполнить обработку сообщения.
>        }
>    }
>}
>```

>Java
>```java
>public class PushService extends FirebaseMessagingService {
>
>    @Override
>    public void onNewToken(@NonNull String token) {
>        super.onNewToken(token);
>        Jivo.updatePushToken(token);
>    }
>
>    @Override
>    public void onMessageReceived(@NonNull RemoteMessage message) {
>        super.onMessageReceived(message);
>        if (!Jivo.handleRemoteMessage(message)) {
>            //Выполнить обработку сообщения.
>        }
>    }
>}
>```

</p>
</details>
