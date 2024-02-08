# Основные настройки JivoSDK
Ниже перечислены примеры и описание основных настроек JivoSDK:

- [Jivo.init](#jivo_init) - Инициализация основных компонентов **JivoSDK**.
- [Jivo.setData](#jivo_set_data) - Установка канала, JWT-токена и хоста.
- [Jivo.enableLogging](#jivo_enable_logging) - Включение логирования **JivoSDK**.
- [Jivo.setContactInfo](#jivo_set_contact_info) - Отправка контактных данных посетителя.
- [Jivo.setCustomData](#jivo_set_custom_data) - Отправка дополнительной информации о клиенте.
- [Jivo.clear](#jivo_clear) - Очистка данных **JivoSDK**.
- [Jivo.addNewMessageListener](#jivo_add_new_message_listener) - Получения информации о непрочитанных сообщениях.
- [Jivo.disableInAppNotification](#jivo_disable_in_app_notification) - Отключения in-app уведомлений.
- [Jivo.addNotificationPermissionListener](#jivo_add_notification_permission_listener) - Получение информации о разрешении уведомлений.
- [Jivo.setConfig](#jivo_set_config) - Дополнительные настройки **JivoSDK**.


## <a name="jivo_init">Jivo.init.</a>

Основной метод для инициализации JivoSDK. После вызова метода инициализируются основные компоненты для полноценной работы JivoSDK.
> [!IMPORTANT]<br>Инициализируйте библиотеку JivoSDK только в методе `Application.onCreate()`. Если в приложении есть несколько
> процессов, убедитесь, что JivoSDK инициализируется только в главном процессе.

Пример:
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

Для установки и смены канала, сохранения истории и установки хоста подключения(дополнительная опция), необходимо использовать метод `Jivo.setData()`. 

Метод `Jivo.setData()` принимает следующие параметры:
| Название | Тип | Описание |
| ------------- |---------|--------------------------------------------------------|
| widgetId | String | Уникальный id канала.|
| userToken | String | Необязательный параметр, JWT-токен используется для сохранения истории. |
| host | String | Необязательный параметр, можно передать пустую строку. |

Пример:
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

> [!IMPORTANT]<br>Метод `Jivo.setData()`, необходимо вызвать до открытия чата.
> <br>После вызова `Jivo.setData()`, не требуется вызывать методы `Jivo.clear()`, `Jivo.unsubscribeFromPush()`.
> <br>Если вы используете уведомления в вашем приложении, то после вызова `Jivo.setData()`, вызовите метод `Jivo.subscribeFromPush()`.

>[!NOTE]<br>Сохранение истории чата после удаления приложения или очистки данных. Для сохранения истории необходимо сгенерировать
>**JWT-токен**. Для подписи **JWT-токена**, потребуется сгенерировать **SECRET** и передать его в Jivo(Этого пока нет
>в интерфейсе приложения.). Хранить **SECRET** только на **backend’e**, иначе безопасность будет нарушена.
>Сгенерированный **JWT-токен** нужно передать метод `Jivo.setData()`. Более подробную информацию JWT-токене вы можете получить на сайте https://jwt.io/introduction 
>
>Пример формирования payload:
>
>```kotlin
>{
>    "id": "123", // Уникальный идентификатор клиента, обязательным условием имя поля должно быть "id"
>    ... // Любые параметры на усмотрение клиента
>}
>```
>
>Пример формирования токена:
>
>```kotlin
>token = jwt.encode(payload, secret, HS256)
>```

## <a name="jivo_enable_logging">Jivo.enableLogging.</a>

Включение логирования **JivoSDK**, в классе унаследованном от класса `Application`, в теле переопределенного метода `onCreate()`,
необходимо проинициализировать `Timber` и добавить вызов метода `Jivo.enableLogging()`. Для фильтрации логов
используйте тег - `JivoSDK`.

Пример:
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

Демонстрация логирования с помощью встроенного инструмента **Logcat**:

<img src="https://user-images.githubusercontent.com/81690520/144239108-ae744cf5-1ba0-4aa0-b5f2-af964b6e18ad.png" width="968">
</p>
</details>

## <a name="jivo_set_contact_info">Jivo.setContactInfo.</a>

Устанавливает контактные данные посетителя. Данные отображаются оператору, как будто их ввел посетитель в форме контактов.

| Название    | Тип    | Описание                                                                                  |
|-------------|--------|-------------------------------------------------------------------------------------------|
| name        | String | Имя пользователя, используется как обращение в сообщениях                                 |
| email       | String | Адрес электронной почты, для отправки сообщений                                           |
| phone       | String | Номер телефона для звонков                                                                |
| description | String | Дополнительная информация по клиенту (отобразится в поле "Описание" - раздел "О клиенте") |

Пример:
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

С помощью этой функции можно передать произвольную дополнительную информацию о клиенте оператору. Информация отображается в
информационной панели справа в приложении оператора. Метод может быть вызван любое число раз - если диалог с оператором уже
установлен, то данные в приложении оператора будут обновлены в реальном времени. Поля выводятся в порядке их следования в массиве.
| Название | Тип | Описание |
| ------------- | ------------- | ------------- |
| content |String | Содержимое поля данных. Теги экранируются |
| title | String | Заголовок, добавляемый сверху поля данных |
| link | String | URL, открываемый при клике на поле данных |
| key | String | Описание поля данных, добавляемое жирным шрифтом перед содержимым поля через двоеточие |

Пример:
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

Очистка данных JivoSDK. После вызова метода `Jivo.clear()` происходит очистка данных в storage, репозиториях, базы данных и
отключение рассылки пушей.
> [!IMPORTANT]<br>Для корректной работы JivoSDK очистку данных нужно выполнять после логаута в вашем приложении.

Пример:
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

Для получения информации о непрочитанных сообщениях необходимо использовать метод `Jivo.addNewMessageListener()`. Данный метод
принимает на вход объект типа `NewMessageListener`, у которого требуется реализовать метод обратного
вызова `onNewMessage(hasUnread: Boolean)`. В параметр данного метода будет передаваться состояние о непрочитанных сообщениях. Для
корректной работы необходимо реализовать интерфейс `NewMessageListener` в главной `Activity`, `Viewmodel` или в `Repository`.

Пример:
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

Для отключения in-app уведомлений, потребуется вызвать метод `Jivo.disableInAppNotification()` и передать параметр типа `Boolean`. 
> [!NOTE]<br>Метод `Jivo.disableInAppNotification() отключает уведомления только когда приложение открыто.

>```kotlin
>Jivo.disableInAppNotification()
>```

##  <a name="jivo_add_notification_permission_listener">Jivo.addNotificationPermissionListener.</a>
Получение информации о разрешении уведомлений. Для получения информации о разрешении уведомлений нужно использовать метод `Jivo.addNotificationPermissionListener()`. Данный метод принимает на вход объект типа `NotificationPermissionListener`, у которого требуется реализовать метод обратного вызова `onNotificationPermissionGranted(isGranted: Boolean)`. В параметр данного метода будет передаваться флаг о наличии разрешения уведомлений. Для корректной работы необходимо реализовать интерфейс `NotificationPermissionListener` в главной `Activity`.

> [!NOTE]<br>Данный функционал работает на **Android 13** (**API level 33**) и выше.

Пример:
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
Дополнительные настройки JivoSDK. Чтобы добавить дополнительные настройки для JivoSDK, создайте объект класса Config.Builder с необходимыми настройками.
> [!IMPORTANT]<br>Вызов метода `Jivo.setConfig()` нужно выполнять только в методе `Application.onCreate()`.

Пример:
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

Таблица дополнительных настроек JivoSDK:
| Название метода | Краткое описание |
| --- | --- |
| `setWelcomeMessage` | [Установка текста привественного сообщения.](#setWelcomeMessage) |
| `setOpenNotification` | [Кастомная навигация по клику на уведомление.](#setOpenNotification) |
| `setOnBackPressed` | [Кастомная навигация по клику кнопки “назад”.](#setOnBackPressed) |
| `setUriNotificationSound` | [Установка кастомного звука push-уведомления.](#setUriNotificationSound) |
| `setNotificationSmallIcon` | [Установка малой иконки push-уведомления.](#setNotificationColorIcon) |
| `setOfflineMessage` | [Оповещение об отсутствии операторов на канале.](#setOfflineMessage) |
| `useRattingStringsRes` | [Использование кастомных, строковых ресурсов для формы оценки](#useRattingStringsRes) |

### <a name="setWelcomeMessage">setWelcomeMessage</a> 
Установка текста привественного сообщения. В метод `setWelcomeMessage()` требуется передать строковый ресурс. Приветственное сообщение отображается, если отсутствуют сообщения в чате.

Пример:
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
Для корректного открытия чата в нужном представлении, необходимо использовать данный интерфейс. В `Config` была добавлена функция `setOpenNotification()`, в которую требуется передать лямбду, которая будет возвращать объект типа `PendingIntent`, тем самым в функцию `PendingIntent.getActivity()` вы сможете передать нужные для вас параметры.

Пример:
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
Кастомная навигация по клику кнопки “назад”.

Для обработки нажатия кнопки “назад” в конфиг была добавлена функция `setOnBackPressed()` в которую передается лямбда. Лямбда вызывается в контексте объекта `JivoChatFragment`.

Пример:
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
Установка кастомного звука push-уведомления. В метод `setUriNotificationSound()` требуется передать объект типа `Uri`, содержащий адрес звукового ресурса.

Пример:
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
Установка малой иконки push-уведомления. В метод `setNotificationSmallIcon()` требуется передать ресурс изображения.

Пример:
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
Установка цвета иконки push-уведомления. В метод `setNotificationColorIcon()` требуется передать цветовой ресурс.

Пример:
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
Установка текста оповещения об отсутствии операторов на канале. В метод `setOfflineMessage()`, требуется передать строковый ресурс. Оповещение об отсутствии операторов на канале, отображается в случае отсутствия операторов на канале.

Пример:
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

Использование кастомных, строковых ресурсов для формы оценки. Для этого потребуется добавить в строковые ресурсы ващего приложения ключи и вызвать включить использование кастомных строковых ресурсов для формы оценки `useRattingStringsRes()`.

Пример:
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

> Расположение strings.xml в проекте
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
>    <string name="rate_form.description">Насколько вы удовлетворены ответами в чате?</string>
>    <string name="rate_form.finish_description_bad">Мы стремимся делать лучший сервис для вас, будем работать над ошибками. Спасибо за вашу оценку!</string>
>    <string name="rate_form.finish_description_good">Рады, что вам понравилось :)</string>
></resources>
>```
></details>
