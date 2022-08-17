Jivo Mobile SDK - Android
=========================

**Jivo Mobile SDK** позволяет встроить чат в нативные мобильные приложения **Android** и принимать обращения клиентов. Интеграция занимает несколько минут, так как интерфейс чата с лентой сообщений уже реализован – вам понадобится только добавить несколько строк кода в ваш проект.

### Возможности актуальной версии

-   Переписка клиента с оператором
-   Отправка файлов в обе стороны
-   История переписки клиента с оператором
-   Статусы отправки и прочитанности сообщений в чате
-   Базовые настройки UI
-   Индикатор новых сообщений внутри приложения интегратора
-   Push-уведомления

### Актуальная версия: 1.0.5

Список изменений:

-   исправлен баг с некорректным отображением индикатора новых сообщений;

### Известные проблемы:
- На устройствах марки **Xiaomi**, возникают проблемы с отображением цветов в чате **SDK**. Решение, добавить флаг в стили вашего приложения:
          `<item name="android:forceDarkAllowed">false</item>`.

### Демо-приложение для Android

<img src="https://user-images.githubusercontent.com/81690520/144190746-278592b3-b704-4f9e-9642-ea40ce60f29c.png" width="300"> <img src="https://user-images.githubusercontent.com/81690520/144189472-f5d4c1fd-ded0-493f-860e-5301980e89c1.png" width="300">


Посмотрите, как работает чат внутри приложения на примере нашего демо-приложения. Это простое приложение под Android позволяет написать в чат технической поддержке нашего сервиса - Jivo.

-   [Демо-приложение для Android](https://github.com/JivoChat/JivoSDK-Android/tree/develop/sample)

### Требования

-   Android API level 21+
-   Android Studio 4.2.1+

Добавление нового канала Mobile SDK
-----------------------------------

**Внимание!** Данный тип канала доступен в *Корпоративной* версии сервиса [Jivo](https://www.jivo.ru/pricing/). За дополнительной информацией обратитесь, пожалуйста, в чат техподдержки на сайте [Jivo.ru](https://www.jivo.ru/).

Для добавления нового канала SDK необходимо зайти в [приложение Jivo](https://app.jivosite.com/), затем перейти в **Управление > Каналы связи**, в разделе **Добавьте новый канал** найти пункт **Мобильное SDK** и нажать **Добавить**.

<img src="https://user-images.githubusercontent.com/81690520/144237727-fbc36415-946b-4822-9a86-b6a49e6b51d6.png" width="630">

Далее необходимо ввести название (имя) вашего будущего канала:

<img src="https://user-images.githubusercontent.com/81690520/144238618-8fdd66c2-1b56-4f48-9722-c283dc741e65.png" width="630">

<img src="https://user-images.githubusercontent.com/81690520/144238660-698a94b5-7ed3-4d16-95ee-3b3f20fe5467.png" width="630">

### После добавления нового канала автоматически откроются его настройки.  Сохраните widget_id для дальнейшей интеграции JivoSDK.

В рамках одного канала **Mobile SDK** можно подключить по одному приложению для каждой платформы: iOS и Android. Используйте один общий **widget_id**.

<img src="https://user-images.githubusercontent.com/81690520/144238692-6e76b28f-4429-4e02-afda-a453199a7809.png" width="630">

Интеграция JivoSDK
------------------

### Настройка Gradle Scripts

В файле **Gradle** корневого уровня (уровень проекта) (`build.gradle`) добавьте следующее:

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

В вашем модуле (уровне приложения) файле **Gradle** (обычно `app/build.gradle`) добавьте следующее:

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
   implementation 'com.jivosite.sdk:android-sdk:1.0.3'
   //firebase
   implementation platform('com.google.firebase:firebase-bom:26.2.0')
   implementation 'com.google.firebase:firebase-messaging'
   implementation 'com.google.firebase:firebase-analytics-ktx'
}
```

### Инициализация JivoSDK

Для инициализации **JivoSDK** в классе, унаследованном от класса `Application`, в теле переопределенного метода `onCreate()` добавьте вызов статического метода `Jivo.init()`. Статический метод `Jivo.init()` принимает следующие параметры:

-   **appContext** - контекст приложения.
-   **widgetId** - уникальный id.
-   **host** - необязательный параметр, можно передать пустую строку.

Если у вас нет этого класса, который унаследован от класса `Application`, то создайте его и пропишите название в `AndroidManifest.xml`

Пример кода класса, унаследованного от класса `Application`:

```kotlin
class App() : Application() {

    override fun onCreate() {
        super.onCreate()
        Jivo.init(
            appContext = this,
            widgetId = "xXxXxxXxXx"
        )
    }
}
```

Пример кода `AndroidManifest.xml`:

```xml
<manifest ... >

   <application
       android:name=".App"
       ...>
     ...
   </application>

</manifest>
```

Запуск JivoSDK
--------------

Открытие чата реализовано на примере демо -  приложения  [JivosSdkSample](https://github.com/JivoChat/JivoSDK-Android-Sample). Добавьте в `main_fragment.xml` кнопку `JivoChatButton`. Пример  кода приведен ниже:

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    ...

<com.jivosite.sdk.ui.views.JivoChatButton
android:id="@+id/jivoBtn"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
app:layout_constraintBottom_toBottomOf="parent"
app:layout_constraintEnd_toEndOf="parent" />

    </androidx.constraintlayout.widget.ConstraintLayout>
```

Создайте `JivoChatFragment` и добавьте в `MainFragment` следующий код:

```kotlin
class MainFragment: Fragment(R.layout.main_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.jivoBtn)?.run {
            setOnClickListener {
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, JivoChatFragment())
                    .commit()
            }
        }
    }
}
```

Настройка UI JivoSDK
--------------------

Для **настройки UI** в статический метод `Jivo.setConfig()` требуется передать объект типа `Config`. Данный объект можно сконфигурировать с помощью класса `Builder`, пример кода ниже:

```kotlin
Jivo.setConfig(
    Config.Builder()
        .setLogo(R.drawable.vic_jivosdk_logo)
        .setBackground(R.drawable.bg_jivosdk_appbar)
        .setTitle(R.string.jivosdk_title)
        .setTitleTextColor(R.color.white)
        .setSubtitle(R.string.jivosdk_subtitle)
        .setSubtitleTextColor(R.color.white)
        .setSubtitleTextColorAlpha(0.6f)
        .setWelcomeMessage(R.string.jivosdk_welcome)
        .setOutgoingMessageColor(Config.Color.GREY)
        .build()
)
```

Разберем по отдельности каждый параметр:

-   **setLogo()** - изменяет логотип appbar-a, принимает на вход ресурс изображения, например векторное изображение.
-   **setBackground()** - изменяет фон appbar-a, принимает на вход ресурс изображения, позволяет использовать градиент, картинки,  например векторное изображение, пример:

```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">

    <item>
        <shape>
            <gradient
                android:angle="180"
                android:endColor="#18C139"
                android:startColor="#222D38"
                android:type="linear" />
        </shape>
    </item>

    <item android:drawable="@drawable/bitmap_bg_appbar" />

</layer-list>
```
-   **setTitle()** - строковый ресурс, изменяет текст заголовка appbar-a
-   **setTitleTextColor()** - цветовой ресурс, изменяет цвет текста заголовка appbar-a
-   **setSubtitle()** - строковый ресурс, изменяет текст подзаголовка appbar-a
-   **setSubtitleTextColor()** - цветовой ресурс, изменяет цвет текста подзаголовка appbar-a
-   **setSubtitleTextColorAlpha()** - принимает на входит значение типа `Float`, изменяет прозрачность цвета текста подзаголовка appbar-a
-   **setWelcomeMessage()** - строковый ресурс, изменяет текст приветственного сообщения.
-   **setOutgoingMessageColor()** - принимает на вход enum, изменяет цвет фона исходящего сообщения. Доступны три основных цвета на выбор(`Config.Color.GREEN, Config.Color.GREY, Config.Color.BLUE`)
-   **hideLogo()** - скрывает логотип.
-   **setOfflineMessage()** - строковый ресурс, изменяет текст оповещения об отсутствии операторов на канале.
-   **setNotificationSmallIcon()** - изменяет иконку push-уведомления, принимает на вход ресурс изображения.
-   **setNotificationColorIcon()** - цветовой ресурс, изменяет иконку push-уведомления.

Настройка push notifications.
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

В вашем модуле (уровня приложения) файле **Gradle** (обычно `app/build.gradle`) примените плагин **Google Services Gradle** и объявите зависимости для продуктов **Firebase**:

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

### Инициализация Jivo Firebase Messaging Service.

Создайте класс `PushService` и унаследуйте его от класса `JivoFirebaseMessagingService`

```kotlin
package com.jivosite.example

import com.jivosite.sdk.push.JivoFirebaseMessagingService

class PushService : JivoFirebaseMessagingService()
```

Затем в `AndroidManifest.xml` добавьте сервис и метаданные. Пример кода приведен ниже:

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

### Обработка Push notifications.

Если в вашем приложении используются **Push уведомления** с помощью **Firebase Cloud Messaging**, то для корректной работы сервиса в классе, унаследованном от класса `FirebaseMessagingService` , потребуется выполнить следующие настройки:

-   В методе `onNewToken()` добавить вызов статического метода  `Jivo.updatePushToken()` . Данный метод отвечает за обновление токена.
-   В методе `onMessageReceived()` добавить вызов статического метода  `Jivo.handleRemoteMessage()`. Данный метод выполняет проверку и возвращает `true/false`, в зависимости от принадлежности сообщения к **JivoSDK**.

```kotlin
class PushService : FirebaseMessagingService() {
    ...

    override fun onNewToken(token: String) {
        ...
        Jivo.updatePushToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        ...
        if (!Jivo.handleRemoteMessage(message)) {
            //Выполнить обработку сообщения.
        }
    }
}
```

Дополнительные настройки JivoSDK.
---------------------------------

### Включение логирования.

Для отображения логов **JivoSDK**, в классе унаследованном от класса `Application`, в теле переопределенного метода `onCreate()`, необходимо проинициализировать `Timber` и добавить вызов статического метода `Jivo.enableLogging()`, пример кода ниже:

```kotlin
class ExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Jivo.init(this, "xXxXxXxXxXx")
        Jivo.enableLogging()
    }
}
```

Демонстрация логирования с помощью встроенного инструмента **Logcat**:

<img src="https://user-images.githubusercontent.com/81690520/144239108-ae744cf5-1ba0-4aa0-b5f2-af964b6e18ad.png" width="968">


### Передача информации о клиенте.

Для передачи информации о клиенте требуется вызвать статический метод  `Jivo.setClientInfo()` и передать объект типа `ClientInfo`. Данный объект можно сконфигурировать с помощью класса `Builder`, пример кода ниже:

```kotlin
Jivo.setClientInfo(
    ClientInfo.Builder()
        .setName("John Doe")
        .setEmail("example@email.tld")
        .setPhone("+7911234567")
        .setDescription("Awesome client")
        .build())
```       

Разберем по отдельности каждый параметр:

-   **name** - имя пользователя, используется как обращение в сообщениях;
-   **email** - адрес электронной почты, для отправки сообщений;
-   **phone** - номер телефона для звонков;
-   **description** - описание, как комментарий, можно почтовый адрес, должность и пр.

Переданные данные отобразятся в основном приложении в колонке справа от диалога.

<img src="https://user-images.githubusercontent.com/81690520/144239118-bb809274-a096-4e78-884c-74316804b37d.png" width="300">


### Уведомление о непрочитанных сообщениях.

Для получения информации о непрочитанных сообщениях необходимо использовать статический метод `Jivo.addNewMessageListener()`. Данный метод принимает на вход объект типа `NewMessageListener`, у которого требуется реализовать метод обратного вызова `onNewMessage(hasUnread: Boolean)`. В параметр данного метода будет передаваться состояние о непрочитанных сообщениях. Для корректной работы необходимо реализовать интерфейс `NewMessageListener` в главной `Activity`, `Viewmodel` или в `Repository`. Ниже представлен пример кода:

```kotlin
class MainViewModel: ViewModel(), NewMessageListener {

    init {
        Jivo.addNewMessageListener(this)
    }
    
    override fun onNewMessage(hasUnread: Boolean) {
        … //Some code
    }
}
```

### Очистка данных.

Для очистки данных необходимо вызвать статический метод  `Jivo.clear()`. Данный метод рекомендуется вызывать после успешного логаута.

### Передача user-token’a.

Для передачи **user-token’a** необходимо вызвать статический метод `Jivo.setUserToken()`. Данный метод рекомендуется вызывать после успешной авторизации пользователя. В параметре которого можно передавать как **JWT-токен** так и уникальную строку.

### Использование механизма JWT
Для сохранения переписки необходимо сгенерировать **JWT-токен**. Для подписи **JWT-токена**, потребуется сгенерировать **SECRET** и передать его в Jivo(Этого пока нет в интерфейсе приложения.). Хранить **SECRET** только на **backend’e**, иначе безопасность будет нарушена.
Сгенерированный **JWT-токен** передается в статический метод `Jivo.setUserToken()`.
Пример формирования payload:
```kotlin
{
  id: 123, // Уникальный идентификатор клиента, обязательным условием имя поля должно быть "id"
  … // Любые параметры на усмотрение клиента
}
```
Пример формирования токена:
```kotlin
token = jwt.encode(payload, secret, HS256)
```
### Кастомная навигация по клику на уведомление.

Для корректного открытия чата в нужном представлении, необходимо использовать данный интерфейс. В `Config` была добавлена функция `setOpenNotification()`, в которую требуется передать лямбду, которая будет возвращать объект типа `PendingIntent`, тем самым в функцию `PendingIntent.getActivity()` вы сможете передать нужные для вас параметры.
Пример реализации:
```kotlin
Jivo.setConfig(Config.Builder()
   .setOpenNotification {
       PendingIntent.getActivity(
           this,
           0,
           Intent(this, MainActivity::class.java).apply {
               putExtra(EXTRA_TARGET, TARGET_CHAT)
           },
           PendingIntent.FLAG_UPDATE_CURRENT
       )
   }
   .build()
)
```
### Кастомная навигация по клику кнопки “назад”.

Для обработки нажатия кнопки “назад” в конфиг была добавлена функция `setOnBackPressed()` в которую передается лямбда. Лямбда вызывается в контексте объекта `JivoChatFragment`.
Пример реализации:
```kotlin
Jivo.setConfig(Config.Builder()
   .setOnBackPressed {
       activity?.onBackPressed()
   }
   .build()
)
```
### Установка кастомного звука push-уведомления.

В `Config` была добавлена функция `setUriNotificationSound()`, в которую требуется передать объект типа `Uri`, содержащий ссылку на звуковой ресурс. Пример реализации:
```kotlin
Jivo.setConfig(
   Config.Builder()                  
       .setUriNotificationSound(getUriNotificationSound(applicationContext))
       .build()
)

fun getUriNotificationSound(applicationContext: Context): Uri =  Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${applicationContext.packageName}/${R.raw.jivo_tip}")
```

### Отключение in-app уведомлений.

Для отключения in-app уведомлений, потребуется вызвать статический метод `Jivo.disableInAppNotification()` и передать параметр типа `Boolean`. Пример реализации:
```kotlin
Jivo.disableInAppNotification(true)
```

Интеграция JivoSDK(React Native).
=================================

Открытие проекта.
-----------------

Для начала откройте проект **Android** в приложении **Android Studio**. Вы можете найти свой **Android-проект** в папке проекта приложений **React Native**:

<img src="https://user-images.githubusercontent.com/81690520/144239149-22151f18-d938-40c3-b71d-6ff92d5245dc.png" width="630">


**NB!** Мы рекомендуем использовать **Android Studio** для написания собственного кода. **Android studio - это IDE**, созданная для разработки под **Android**, и ее использование поможет вам быстро разрешать некоторые проблемы, такие как ошибки синтаксиса кода.

Настройка Gradle Scripts.
-------------------------

В ваш модуль (уровне приложения), в `Gradle` (обычно `app/build.gradle`) добавьте следующее:

<img src="https://user-images.githubusercontent.com/81690520/144239161-a240f4be-0676-4b8f-a80c-56a0a4a4bedd.png" width="968">


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
   implementation 'com.jivosite.sdk:android-sdk:1.0.3'
   //firebase
   implementation platform('com.google.firebase:firebase-bom:26.2.0')
   implementation 'com.google.firebase:firebase-messaging'
   implementation 'com.google.firebase:firebase-analytics-ktx'
}
```

Инициализация JivoSDK.
----------------------

Для инициализации **JivoSDK** в классе `MainApplication.java`, который находится в папке `android/app/src/main/java/com/your-app-name/`, в тело переопределенного метода `onCreate()` добавьте вызов статического метода `Jivo.init()`. Статический метод `Jivo.init()` принимает следующие параметры:

-   **appContext** - контекст приложения.
-   **widgetId** - уникальный `id`.
-   **host** - необязательный параметр, можно передать пустую строку.

<img src="https://user-images.githubusercontent.com/81690520/144239174-f0d89cbb-cf85-46f5-9b52-e61a8cee6619.png" width="968">


```java
public class MainApplication extends Application implements ReactApplication {
...
    @Override
    public void onCreate() {
   ...
        Jivo.init(this, "xXxXxXxXx", "");
    }
...
}
```

Добавление модуля.
------------------

Создайте `JivoSDKModule.java` в папке `android/app/src/main/java/com/your-app-name/` со следующим содержанием:

<img src="https://user-images.githubusercontent.com/81690520/144239190-39195667-a8ad-434e-9de8-24950c93f311.png" width="968">

```javascript
package com.jivosdkreactnativesample;

import android.content.Intent;

import androidx.annotation.NonNull;

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
       ReactApplicationContext context = getReactApplicationContext();
       Intent intent = new Intent(context, JivoChatActivity.class);
       if (intent.resolveActivity(context.getPackageManager()) != null) {
           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           context.startActivity(intent);
       }
   }
}
```

После написания собственного модуля его необходимо зарегистрировать в **React Native**. Для этого вам необходимо добавить собственный модуль в **ReactPackage** и зарегистрировать **ReactPackage** в **React Native**.

Чтобы добавить собственный модуль в **ReactPackage**, сначала создайте новый класс **Java** с именем `JivoSDKPackage.java`, который реализует **ReactPackage** внутри папки `android/app/src/main/java/com/your-app-name/`:

<img src="https://user-images.githubusercontent.com/81690520/144239213-2a8a5394-2d30-4b1f-8bf5-396473510bdb.png" width="968">


```javascript
package com.jivosdkreactnativesample;

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

Открытие чата.
--------------

Найдите в приложении место, где вы хотели бы добавить вызов метода `openJivoSdk()`.

<img src="https://user-images.githubusercontent.com/81690520/144239236-aace1c4d-294b-4783-a32b-3925c941a474.png" width="968">


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

Changelog
=========

1.0.5 (2022-06-14)
-----------------------

### Bug Fixes:

- исправлен баг с некорректным отображением индикатора новых сообщений;

1.0.4 (2022-06-06)
-----------------------

### Bug Fixes:

- исправлено некорректное отображение оповещения об отсутствии операторов;

### Features:

- добавлена форма ввода контактов;
- добавлено ограничение в 1000 символов для отправляемого сообщения;
- добавлена возможность замены стандартной иконки push - уведомлений и цвета иконки push - уведомлений;

1.0.3 (2022-04-07)
-----------------------
### Features:

- добавлено оповещение об отсутствии операторов на канале;

1.0.2 (2022-02-11)
-----------------------
### Features:

- добавлена возможность отключения in-app уведомлений;

1.0.1 (2022-02-03)
-----------------------

### Bug Fixes:

- исправлено расстояние между сообщениями;

### Features:

- добавлена возможность установки кастомного звука push-уведомления;
- добавлена обфускация логирования;

1.0.0 (2021-12-16)
-----------------------

### Bug Fixes:

- исправлен баг с отображением статуса доставки отправленного сообщения;
- исправлены баги с отображением цветов темной темы;

### Features:

- изменена визуальная составляющая выгрузки медиа файла;
- добавлен механизм, скрывающий кнопку загрузки файла при отсутствии лицензии;
- добавлена поддержка отображения уведомления во время закрытого чата;
- добавлен интерфейс для реализации кастомной навигации по клику на уведомление;
- добавлен интерфейс для реализации кастомной навигации по клику кнопки “назад”;

1.0.0-rc05 (2021-11-24)
-----------------------

### Bug Fixes

-   исправлен баг с отображением заголовка чата;
-   исправлен баг с отображением формата времени;
-   исправлен баг с некорректным отображением типа файла после неудачного отправления;
-   исправлен баг с отсутствием времени у отправляемого сообщения;

### Features

-   добавлена поддержка темной темы;
-   добавлена поддержка нового медиа сервиса;
-   добавлен интерфейс очистки данных:
-   добавлена очередь неотправленных сообщений;
-   добавлен интерфейс передачи user-token’a;

1.0.0-rc04 (2021-09-20)
-----------------------

### Bug Fixes

-   исправлен баг с отображением нескольких сообщений с одним и тем же временем;
-   исправлен баг со скачиванием файлов;
-   исправлен баг с некорректным отображением индикатора новых сообщений;

### Features

-   добавлен [интерфейс], оповещающий о непрочитанных сообщениях;
-   обновлены настройки [UI JivoSDK], добавлена возможно скрывать логотип с помощью настройки hideLogo();

1.0.0-alpha17 (2021-08-23)
--------------------------

### Bug Fixes

-   Исправлен баг с падением JivoSDK при вызове метода Jivo.updatePushToken() из  background потока;

### Features

-   Добавлен [интерфейс], оповещающий о непрочитанных сообщениях;
-   Обновлены настройки [UI JivoSDK], добавлена возможно скрывать логотип с помощью настройки hideLogo();

1.0.0-alpha16 (2021-06-11)
--------------------------

### Features

-   добавлена возможность отключения in-app уведомлений;

1.0.0-alpha15 (2021-06-10)
--------------------------

### Bug Fixes

-   Исправлен баг с отсутствием звукового уведомления в push notification;
-   Исправлен баг коллизии разметки;
-   Исправлен баг коллизии имени пакета FileProvider
-   Исправлен баг статуса доставки сообщений
-   Исправлен баг с артефактами, после клика по пушу

### Features

-   Добавлена возможность кастомизации JivoSDK чата;
-   Добавлен интерфейс обработки Push notifications;
-   Добавлен интерфейс обновления PushToken
-   Добавлен интерфейс для передачи информации о клиенте.

1.0.0-alpha14 (2021-05-18)
--------------------------

### Bug Fixes

-   Исправлен баг с загрузкой файлов;

### Features

-   Убран параметр “port” из функции Jivo.init()

1.0.0-alpha01 (2021-04-09)
--------------------------

### Bug Fixes

-   Исправлен баг с некорректным отображением истории;

### Features

-   Дабавлено отображении имени оператора, присоединившегося к чату;
-   Добавлено время и статус отправки сообщений;
-   Добавлено приветственное сообщение;
-   Добавлено отображение тайпинга;
-   Добавлено отображение ошибки при неудачной отправке сообщения;
-   Добавлен полноэкранный просмотр изображения;
-   Добавлено копирование сообщения с использованием “long click”;
