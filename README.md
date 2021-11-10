Jivo Mobile SDK - Android
=========================

Jivo Mobile SDK позволяет встроить чат в нативные мобильные приложения Android и принимать обращения клиентов. Интеграция занимает несколько минут, так как интерфейс чата с лентой сообщений уже реализован – вам понадобится только добавить несколько строк кода в ваш проект.

### Возможности актуальной версии

-   Переписка клиента с оператором
-   Отправка файлов в обе стороны
-   История переписки клиента с оператором
-   Статусы отправки и прочитанности сообщений в чате
-   Базовые настройки UI
-   Индикатор новых сообщений внутри приложения интегратора
-   Push-уведомления

### Актуальная версия: 1.0.0-rc04

Список изменений:

-   исправлен баг с отображением нескольких сообщений с одним и тем же временем;
-   исправлен баг со скачиванием файлов;
-   исправлен баг с некорректным отображением индикатора новых сообщений;

-   добавлен [интерфейс](#h.gep4mcvqw57a), оповещающий о непрочитанных сообщениях;
-   обновлены настройки [UI](#h.k2vmvb7pfpk2)[JivoSDK](#h.k2vmvb7pfpk2), добавлена возможно скрывать логотип с помощью настройки hideLogo();

### Демо-приложение для Android

![](images/image26.png)![](images/image2.png)

Посмотрите, как работает чат внутри приложения на примере нашего демо-приложения. Это простое приложение под Android позволяет написать в чат технической поддержке нашего сервиса - Jivo.

-   [Демо-приложение для Android](https://www.google.com/url?q=https://github.com/JivoChat/JivoSDK-Android-Sample&sa=D&source=editors&ust=1635432992393000&usg=AOvVaw1IYa3MxigNN37sLIowzOpW)

### Требования

-   Android API level 21+
-   Android Studio 4.2.1+

Добавление нового канала mobile SDK
-----------------------------------

Для добавления нового канала SDK необходимо зайти в [приложение Jivo](https://www.google.com/url?q=http://app.jivosite.com/&sa=D&source=editors&ust=1635432992394000&usg=AOvVaw3EupELM0pAy1B6sa2MCpWc), затем перейти в Управление \> Каналы связи, в разделе Добавьте новый канал найти пункт Мобильное SDK и нажать Добавить.

![](images/image18.png)

Далее необходимо ввести название (имя) вашего будущего канала:

![](images/image14.png)

![](images/image6.png)

### После добавления нового канала автоматически откроются его настройки.  Сохраните widget\_id для дальнейшей интеграции JivoSDK.

В рамках одного канала Mobile SDK можно подключить по одному приложению для каждой платформы: iOS и Android. Используйте один общий widget\_id.

![](images/image27.png)

Интеграция JivoSDK
------------------

### Настройка Gradle Scripts

В файле Gradle корневого уровня (уровень проекта) (build.gradle) добавьте следующее:

<table>
<col width="100%" />
<tbody>
<tr class="odd">
<td align="left"><p>buildscript {</p>
<p>   ext.kotlin_version = &quot;1.5.10&quot;</p>
<p>   repositories {</p>
<p>       google()</p>
<p>       mavenCentral()</p>
<p>   }</p>
<p>   dependencies {</p>
<p>       classpath 'com.android.tools.build:gradle:4.2.1'</p>
<p>       classpath &quot;org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version&quot;</p>
<p>       classpath 'com.google.gms:google-services:4.3.8'</p>
<p>   }</p>
<p>}</p>
<p></p>
<p>allprojects {</p>
<p>   repositories {</p>
<p>       maven { url &quot;https://www.jitpack.io&quot; }</p>
<p>       google()</p>
<p>       mavenCentral()</p>
<p>   }</p>
<p>}</p>
<p>...</p></td>
</tr>
</tbody>
</table>

В вашем модуле (уровне приложения) файле Gradle (обычно app/build.gradle) добавьте следующее:

<table>
<col width="100%" />
<tbody>
<tr class="odd">
<td align="left"><p>plugins {<br />   ...<br />   id 'kotlin-kapt'<br />}<br /><br />android {<br />   ...<br />   buildFeatures {<br />       dataBinding = true<br />   }<br />   ...<br />}<br /><br />dependencies {<br />   ...<br />   //JivoSDK<br />   implementation 'com.jivosite.sdk:android-sdk:1.0.0-alpha17'<br />   //firebase<br />   implementation platform('com.google.firebase:firebase-bom:26.2.0')<br />   implementation 'com.google.firebase:firebase-messaging'<br />   implementation 'com.google.firebase:firebase-analytics-ktx'<br />}</p></td>
</tr>
</tbody>
</table>

### Инициализация JivoSDK

Для инициализации JivoSDK в классе, унаследованном от класса Application, в теле переопределенного метода onCreate() добавьте вызов статического метода Jivo.init() . Статический метод Jivo.init() принимает следующие параметры:

-   appContext - контекст приложения.
-   widgetId - уникальный id. [Получение](#h.eiyzwp39djsg)[widgetId](#h.eiyzwp39djsg).
-   host - необязательный параметр, можно передать пустую строку.

Если у вас нет этого класса, который унаследован от класса Application, то создайте его и пропишите название в AndroidManifest.xml

Пример кода класса, унаследованного от класса Application:

<table>
<col width="100%" />
<tbody>
<tr class="odd">
<td align="left"><p>class App() : Application() {<br /><br />    override fun onCreate() {<br />        super.onCreate()<br />        Jivo.init(<br />            appContext = this,<br />            widgetId = &quot;xXxXxxXxXx&quot;<br />        )<br />    }<br />}</p></td>
</tr>
</tbody>
</table>

Пример кода AndroidManifest.xml:

<table>
<col width="100%" />
<tbody>
<tr class="odd">
<td align="left"><p>&lt;manifest ... &gt;<br /><br />   &lt;application<br />       android:name=&quot;.App&quot;<br />       ...&gt;<br />     ...<br />   &lt;/application&gt;<br /><br />&lt;/manifest&gt;</p></td>
</tr>
</tbody>
</table>

Запуск JivoSDK
--------------

Открытие чата реализовано на примере демо -  приложения  [JivosSdkSample](https://www.google.com/url?q=https://github.com/JivoChat/JivoSDK-Android-Sample&sa=D&source=editors&ust=1635432992414000&usg=AOvVaw1dOIX7NBH3kfqDUozew1S_). Добавьте в main\_fragment.xml кнопку JivoChatButton. Пример  кода приведен ниже:

<table>
<col width="100%" />
<tbody>
<tr class="odd">
<td align="left"><p>&lt;?xml version=&quot;1.0&quot; encoding=&quot;utf-8&quot;?&gt;<br />&lt;androidx.constraintlayout.widget.ConstraintLayout<br />   ...<br /><br />   &lt;com.jivosite.sdk.ui.views.JivoChatButton<br />       android:id=&quot;@+id/jivoBtn&quot;<br />       android:layout_width=&quot;wrap_content&quot;<br />       android:layout_height=&quot;wrap_content&quot;<br />       app:layout_constraintBottom_toBottomOf=&quot;parent&quot;<br />       app:layout_constraintEnd_toEndOf=&quot;parent&quot; /&gt;<br /><br />&lt;/androidx.constraintlayout.widget.ConstraintLayout&gt;</p></td>
</tr>
</tbody>
</table>

Создайте JivoChatFragment и добавьте в MainFragment следующий код:

<table>
<col width="100%" />
<tbody>
<tr class="odd">
<td align="left"><p>class MainFragment: Fragment(R.layout.main_fragment) {<br /><br />   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {<br />       super.onViewCreated(view, savedInstanceState)<br />       view.findViewById&lt;View&gt;(R.id.jivoBtn)?.run {<br />           setOnClickListener {<br />               parentFragmentManager<br />                   .beginTransaction()<br />                   .replace(R.id.container, JivoChatFragment())<br />                   .commit()<br />           }<br />       }<br />   }<br />}</p></td>
</tr>
</tbody>
</table>

Настройка UI JivoSDK
--------------------

Для настройки UI в статический метод Jivo.setConfig() требуется передать объект типа Config. Данный объект можно сконфигурировать с помощью класса Builder, пример кода ниже:

<table>
<col width="100%" />
<tbody>
<tr class="odd">
<td align="left"><p>Jivo.setConfig(<br />   Config.Builder()<br />       .setLogo(R.drawable.vic_jivosdk_logo)<br />       .setBackground(R.drawable.bg_jivosdk_appbar)<br />       .setTitle(R.string.jivosdk_title)<br />       .setTitleTextColor(R.color.white)<br />       .setSubtitle(R.string.jivosdk_subtitle)<br />       .setSubtitleTextColor(R.color.white)<br />       .setSubtitleTextColorAlpha(0.6f)<br />       .setWelcomeMessage(R.string.jivosdk_welcome)<br />       .setOutgoingMessageColor(Config.Color.GREY)<br />       .build()<br />)</p></td>
</tr>
</tbody>
</table>

Разберем по отдельности каждый параметр:

-   setLogo() - изменяет логотип appbar-a, принимает на вход ресурс изображения, например векторное изображение.
-   setBackground() - изменяет фон appbar-a, принимает на вход ресурс изображения, позволяет использовать градиент, картинки,  например векторное изображение, пример:

<table>
<col width="100%" />
<tbody>
<tr class="odd">
<td align="left"><p>&lt;?xml version=&quot;1.0&quot; encoding=&quot;utf-8&quot;?&gt;<br />&lt;layer-list xmlns:android=&quot;http://schemas.android.com/apk/res/android&quot;&gt;<br /><br />&lt;item&gt;<br />   &lt;shape&gt;<br />       &lt;gradient<br />           android:angle=&quot;180&quot;<br />           android:endColor=&quot;#18C139&quot;<br />           android:startColor=&quot;#222D38&quot;<br />           android:type=&quot;linear&quot; /&gt;<br />   &lt;/shape&gt;<br />&lt;/item&gt;<br /><br />&lt;item android:drawable=&quot;@drawable/bitmap_bg_appbar&quot; /&gt;<br /><br />&lt;/layer-list&gt;</p></td>
</tr>
</tbody>
</table>

-   setTitle() - строковый ресурс, изменяет текст заголовка appbar-a
-   setTitleTextColor() - цветовой ресурс, изменяет цвет текста заголовка appbar-a
-   setSubtitle() - строковый ресурс, изменяет текст подзаголовка appbar-a
-   setSubtitleTextColor() - цветовой ресурс, изменяет цвет текста подзаголовка appbar-a
-   setSubtitleTextColorAlpha() - принимает на входит значение типа Float, изменяет прозрачность цвета текста подзаголовка appbar-a
-   setWelcomeMessage() - строковый ресурс, изменяет текст приветственного сообщения.
-   setOutgoingMessageColor() - принимает на вход enum, изменяет цвет фона исходящего сообщения. Доступны три основных цвета на выбор(Config.Color.GREEN, Config.Color.GREY, Config.Color.BLUE)
-   hideLogo() - скрывает логотип.

Настройка push notifications.
-----------------------------

### Создание проекта.

Войдите в [Firebase](https://www.google.com/url?q=https://console.firebase.google.com/&sa=D&source=editors&ust=1635432992427000&usg=AOvVaw2x3fDvjG2Gh6XQJHH9cQUP), затем создайте свой проект. Нажмите на Create a project и выполните следующие инструкции.

![](images/image5.png)

Введите название вашего проекта.

![](images/image17.png)

Добавьте Google Analytics в ваш проект(Рекомендация)

![](images/image19.png)

Выберите соответствующие настройки и нажмите Create project.

![](images/image3.png)

Дождитесь сборки проекта.

![](images/image13.png)

### Получение google-services.json.

Нажмите на кнопку ![](images/image25.png) , затем выберете Project settings.

![](images/image11.png)

В Your apps, щелкните значок Android (![](images/image22.png)) или Add app. Если у вас есть уже приложение, то выберете имя пакета, для которого нужен файл конфигурации.

![](images/image12.png)

Введите [имя пакета](https://www.google.com/url?q=https://developer.android.com/studio/build/application-id?authuser%3D0&sa=D&source=editors&ust=1635432992432000&usg=AOvVaw19GnrQ6usOJuSF9dLS_f55) вашего приложения в поле имени пакета Android.

![](images/image28.png)

(Необязательно) Введите другую информацию о приложении: псевдоним приложения и сертификат подписи отладки SHA-1 .

Скопируйте в приложение файл конфигурации Firebase Android. Нажмите Download google-services.json, чтобы получить файл конфигурации Firebase Android (google-services.json). Переместите файл конфигурации в каталог модуля (уровня приложения) вашего приложения.

![](images/image15.png)

### Получение приватного ключа.

Для генерации приватного ключа нажать Generate new private key и сохранить новый ключ.

![](images/image8.png)

### Привязка private key к каналу SDK.

Для добавления private key, вам нужно зайти в [приложение Jivo](https://www.google.com/url?q=http://app.jivosite.com/&sa=D&source=editors&ust=1635432992437000&usg=AOvVaw0QksMyM1hmC8Oysa0BTZFI), затем перейти в Управление \> Каналы связи, в разделе Управление каналами найти нужный вам канал и нажать кнопку Настроить.

![](images/image21.png)

В настройках канала выбрать Настройки Push и нажать кнопку Загрузить JSON, выбрать приватный ключ и дождаться выгрузки.

В рамках одного канала Mobile SDK можно подключить по одному приложению для каждой платформы: iOS и Android. Соответствующий интерфейс загрузки P8 сертификата для работы Push уведомлений через Apple APNS смотрите ниже.

![](images/image24.png)

![](images/image20.png)

### Добавление зависимостей.

В файле Gradle корневого уровня (уровня проекта) (build.gradle) добавьте правила для включения подключаемого модуля Gradle служб Google. Убедитесь, что у вас есть репозиторий Maven от Google.

<table>
<col width="100%" />
<tbody>
<tr class="odd">
<td align="left"><p>buildscript {<br />    ...<br />    repositories {<br />         mavenCentral()<br />        ...<br />    }<br />    dependencies {<br />        ...<br />        classpath 'com.google.gms:google-services:4.3.8'<br />        ...<br />    }<br />}<br /><br />allprojects {<br />    repositories {<br />        ...<br />     mavenCentral()</p>
<p>        ...<br />    }<br />}<br />...<br />}</p></td>
</tr>
</tbody>
</table>

В вашем модуле (уровня приложения) файле Gradle (обычно app/build.gradle) примените плагин Google Services Gradle и объявите зависимости для продуктов Firebase:

<table>
<col width="100%" />
<tbody>
<tr class="odd">
<td align="left"><p>plugins {<br />    ...<br />    id 'com.google.gms.google-services'<br />}<br />...<br />dependencies {<br />    ...<br />    implementation platform('com.google.firebase:firebase-bom:26.2.0')<br />    implementation 'com.google.firebase:firebase-messaging'<br />    implementation 'com.google.firebase:firebase-analytics-ktx'<br />}</p></td>
</tr>
</tbody>
</table>

### Инициализация Jivo Firebase Messaging Service.

Создайте класс PushService и унаследуйте его от класса  JivoFirebaseMessagingService

<table>
<col width="100%" />
<tbody>
<tr class="odd">
<td align="left"><p>package com.jivosite.example<br /><br />import com.jivosite.sdk.push.JivoFirebaseMessagingService<br /><br />class PushService : JivoFirebaseMessagingService()</p></td>
</tr>
</tbody>
</table>

Затем в AndroidManifest.xml добавьте сервис и метаданные. Пример кода приведен ниже:

<table>
<col width="100%" />
<tbody>
<tr class="odd">
<td align="left"><p>&lt;?xml version=&quot;1.0&quot; encoding=&quot;utf-8&quot;?&gt;<br />&lt;manifest xmlns:android=&quot;http://schemas.android.com/apk/res/android&quot;<br />    package=&quot;com.jivosite.jivosdk_android_sample&quot;&gt;<br />        ...<br />        &lt;service<br />            android:name=&quot;com.jivosite.jivosdk_android_sample.PushService&quot;<br />            android:enabled=&quot;true&quot;<br />            android:exported=&quot;false&quot;&gt;<br />            &lt;intent-filter&gt;<br />                &lt;action android:name=&quot;com.google.firebase.MESSAGING_EVENT&quot; /&gt;<br />            &lt;/intent-filter&gt;<br />        &lt;/service&gt;<br /><br />        &lt;meta-data<br />            android:name=&quot;com.google.firebase.messaging.default_notification_icon&quot;<br />            android:resource=&quot;@drawable/ic_notification_small&quot; /&gt;<br />        &lt;meta-data<br />            android:name=&quot;com.google.firebase.messaging.default_notification_color&quot;<br />            android:resource=&quot;@color/darkPastelGreen&quot; /&gt;<br /><br />    &lt;/application&gt;</p></td>
</tr>
</tbody>
</table>

### Обработка Push notifications.

Если в вашем приложении используются Push уведомления с помощью Firebase Cloud Messaging, то для корректной работы сервиса в классе, унаследованном от класса FirebaseMessagingService , потребуется выполнить следующие настройки:

-   В методе onNewToken() добавить вызов статического метода  Jivo.updatePushToken() . Данный метод отвечает за обновление токена.
-   В методе onMessageReceived() добавить вызов статического метода  Jivo.handleRemoteMessage(). Данный метод выполняет проверку и возвращает true/false, в зависимости от принадлежности сообщения к JivoSDK.

<table>
<col width="100%" />
<tbody>
<tr class="odd">
<td align="left"><p>class PushService : FirebaseMessagingService() {<br />   ...</p>
<p><br />   override fun onNewToken(token: String) {<br />       ...<br />       Jivo.updatePushToken(token)<br />   }<br /><br />   override fun onMessageReceived(message: RemoteMessage) {<br />       ...<br />       if (!Jivo.handleRemoteMessage(message)) {<br />           //Выполнить обработку сообщения.<br />       }<br />   }<br />}</p></td>
</tr>
</tbody>
</table>

Дополнительные настройки JivoSDK.
---------------------------------

### Включение логирования.

Для отображения логов JivoSDK, в классе унаследованном от класса Application, в теле переопределенного метода onCreate(), необходимо проинициализировать Timber и добавить вызов статического метода Jivo.enableLogging(), пример кода ниже:

<table>
<col width="100%" />
<tbody>
<tr class="odd">
<td align="left"><p>class ExampleApplication : Application() {<br /><br />   override fun onCreate() {<br />       super.onCreate()<br />       Timber.plant(Timber.DebugTree())<br />       Jivo.init(this, &quot;xXxXxXxXxXx&quot;)</p>
<p>       Jivo.enableLogging()<br />   }<br />}</p></td>
</tr>
</tbody>
</table>

Демонстрация логирования с помощью встроенного инструмента Logcat:

![](images/image16.png)

### Передача информации о клиенте.

Для передачи информации о клиенте требуется вызвать статический метод  Jivo.setClientInfo() и передать объект типа ClientInfo. Данный объект можно сконфигурировать с помощью класса Builder, пример кода ниже:

<table>
<col width="100%" />
<tbody>
<tr class="odd">
<td align="left"><p>Jivo.setClientInfo(<br />   ClientInfo.Builder()<br />       .setName(&quot;John Doe&quot;)<br />       .setEmail(&quot;example@email.tld&quot;)<br />       .setPhone(&quot;+7911234567&quot;)</p>
<p>       .setDescription(&quot;Awesome client&quot;)<br />       .build())</p></td>
</tr>
</tbody>
</table>

Разберем по отдельности каждый параметр:

-   name - имя пользователя, используется как обращение в сообщениях;
-   email - адрес электронной почты, для отправки сообщений;
-   phone - номер телефона для звонков;
-   description - описание, как комментарий, можно почтовый адрес, должность и пр.

Переданные данные отобразятся в основном приложении в колонке справа от диалога.

![](images/image10.png)

### Уведомление о непрочитанных сообщениях.

Для получения информации о непрочитанных сообщениях необходимо использовать статический метод Jivo.addNewMessageListener(). Данный метод принимает на вход объект типа NewMessageListener , у которого требуется реализовать метод обратного вызова onNewMessage(hasUnread: Boolean) . В параметр данного метода будет передаваться состояние о непрочитанных сообщениях. Ниже представлен пример кода:

<table>
<col width="100%" />
<tbody>
<tr class="odd">
<td align="left"><p>Jivo.addNewMessageListener(object : NewMessageListener {<br />   override fun onNewMessage(hasUnread: Boolean) {<br />      … //Some code<br />   }<br />})</p></td>
</tr>
</tbody>
</table>

Интеграция JivoSDK(React Native).
=================================

Открытие проекта.
-----------------

Для начала откройте проект Android в приложении Android Studio. Вы можете найти свой Android-проект в папке проекта приложений React Native:

![](images/image4.png)

NB! Мы рекомендуем использовать Android Studio для написания собственного кода. Android studio - это IDE, созданная для разработки под Android, и ее использование поможет вам быстро разрешать некоторые проблемы, такие как ошибки синтаксиса кода.

Настройка Gradle Scripts.
-------------------------

В ваш модуль (уровне приложения), в Gradle (обычно app/build.gradle ) добавьте следующее:

![](images/image23.png)

<table>
<col width="100%" />
<tbody>
<tr class="odd">
<td align="left"><p>android {<br />   ...<br />   buildFeatures {<br />       dataBinding = true<br />   }<br />   ...<br />}<br /><br />dependencies {<br />   ...<br />   //JivoSDK<br />   implementation 'com.jivosite.sdk:android-sdk:1.0.0-rc04'<br />   //firebase<br />   implementation platform('com.google.firebase:firebase-bom:26.2.0')<br />   implementation 'com.google.firebase:firebase-messaging'<br />   implementation 'com.google.firebase:firebase-analytics-ktx'<br />}</p></td>
</tr>
</tbody>
</table>

Инициализация JivoSDK.
----------------------

Для инициализации JivoSDK в классе MainApplication.java, который находится в папке  android/app/src/main/java/com/your-app-name/ , в тело переопределенного метода onCreate() добавьте вызов статического метода Jivo.init() . Статический метод Jivo.init() принимает следующие параметры:

-   appContext - контекст приложения.
-   widgetId - уникальный id. Получение [widgetId](#h.eiyzwp39djsg).
-   host - необязательный параметр, можно передать пустую строку.

![](images/image7.png)

<table>
<col width="100%" />
<tbody>
<tr class="odd">
<td align="left"><p>public class MainApplication extends Application implements ReactApplication {</p>
<p>...</p>
<p> @Override</p>
<p> public void onCreate() {</p>
<p>   ...</p>
<p>   Jivo.init(this, &quot;xXxXxXxXx&quot;, &quot;&quot;);</p>
<p> }</p>
<p>...</p>
<p>}</p></td>
</tr>
</tbody>
</table>

Добавление модуля.
------------------

Создайте JivoSDKModule.java в папке android/app/src/main/java/com/ your-app-name/ со следующим содержанием:

![](images/image1.png)

<table>
<col width="100%" />
<tbody>
<tr class="odd">
<td align="left"><p>package com.jivosdkreactnativesample;</p>
<p></p>
<p>import android.content.Intent;</p>
<p></p>
<p>import androidx.annotation.NonNull;</p>
<p></p>
<p>import com.facebook.react.bridge.ReactApplicationContext;</p>
<p>import com.facebook.react.bridge.ReactContextBaseJavaModule;</p>
<p>import com.facebook.react.bridge.ReactMethod;</p>
<p>import com.jivosite.sdk.ui.chat.JivoChatActivity;</p>
<p></p>
<p>public class JivoSDKModule extends ReactContextBaseJavaModule {<br /><br />   JivoSDKModule(ReactApplicationContext context) {<br />       super(context);<br />   }<br /><br />   @NonNull<br />   @Override<br />   public String getName() {<br />       return &quot;JivoSDKModule&quot;;<br />   }<br /><br />   @ReactMethod<br />   public void openJivoSdk() {<br />       ReactApplicationContext context = getReactApplicationContext();<br />       Intent intent = new Intent(context, JivoChatActivity.class);<br />       if (intent.resolveActivity(context.getPackageManager()) != null) {<br />           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);<br />           context.startActivity(intent);<br />       }<br />   }<br />}</p></td>
</tr>
</tbody>
</table>

После написания собственного модуля его необходимо зарегистрировать в React Native. Для этого вам необходимо добавить собственный модуль в ReactPackage и зарегистрировать ReactPackage в React Native.

Чтобы добавить собственный модуль в ReactPackage, сначала создайте новый класс Java с именем JivoSDKPackage.java, который реализует ReactPackage внутри папки android/app/src/main/java/com/ your-app-name/:

![](images/image29.png)

<table>
<col width="100%" />
<tbody>
<tr class="odd">
<td align="left"><p>package com.jivosdkreactnativesample;</p>
<p></p>
<p>import androidx.annotation.NonNull;</p>
<p></p>
<p>import com.facebook.react.ReactPackage;</p>
<p>import com.facebook.react.bridge.NativeModule;</p>
<p>import com.facebook.react.bridge.ReactApplicationContext;</p>
<p>import com.facebook.react.uimanager.ViewManager;</p>
<p></p>
<p>import java.util.ArrayList;</p>
<p>import java.util.Collections;</p>
<p>import java.util.List;</p>
<p></p>
<p>class JivoSDKPackage implements ReactPackage {<br /><br />   @NonNull<br />   @Override<br />   public List&lt;NativeModule&gt; createNativeModules(@NonNull ReactApplicationContext reactContext) {<br />       List&lt;NativeModule&gt; modules = new ArrayList&lt;&gt;();<br />       modules.add(new JivoSDKModule(reactContext));<br />       return modules;<br />   }<br /><br />   @NonNull<br />   @Override<br />   public List&lt;ViewManager&gt; createViewManagers(@NonNull ReactApplicationContext reactContext) {<br />       return Collections.emptyList();<br />   }<br />}</p></td>
</tr>
</tbody>
</table>

Открытие чата.
--------------

Найдите в приложении место, где вы хотели бы добавить вызов метода openJivoSdk().

![](images/image9.png)

<table>
<col width="100%" />
<tbody>
<tr class="odd">
<td align="left"><p>import React from 'react';<br />import {NativeModules, StyleSheet, Text, View, Button} from 'react-native';<br /><br />export default function App() {<br /> return (<br />   ...<br />     &lt;Button<br />       title=&quot;OpenJivoSDK&quot;<br />       onPress={() =&gt; {<br />         NativeModules.JivoSDKModule.openJivoSdk();<br />       }}<br />     /&gt;<br />  ...<br /> );<br />}<br />...</p></td>
</tr>
</tbody>
</table>

Changelog
=========

1.0.0-rc04 (2021-09-20)
-----------------------

### Bug Fixes

-   исправлен баг с отображением нескольких сообщений с одним и тем же временем;
-   исправлен баг со скачиванием файлов;
-   исправлен баг с некорректным отображением индикатора новых сообщений;

### Features

-   добавлен [интерфейс](#h.gep4mcvqw57a), оповещающий о непрочитанных сообщениях;
-   обновлены настройки [UI](#h.k2vmvb7pfpk2)[JivoSDK](#h.k2vmvb7pfpk2), добавлена возможно скрывать логотип с помощью настройки hideLogo();

1.0.0-alpha17 (2021-08-23)
--------------------------

### Bug Fixes

-   Исправлен баг с падением JivoSDK при вызове метода Jivo.updatePushToken() из  background потока;

### Features

-   Добавлен [интерфейс](#h.gep4mcvqw57a), оповещающий о непрочитанных сообщениях;
-   Обновлены настройки [UI](#h.k2vmvb7pfpk2)[JivoSDK](#h.k2vmvb7pfpk2), добавлена возможно скрывать логотип с помощью настройки hideLogo();

1.0.0-alpha16 (2021-06-11)
--------------------------

### Features

-   Добавлено включение логирования;

1.0.0-alpha15 (2021-06-10)
--------------------------

### Bug Fixes

-   Исправлен баг с отсутствием звукового уведомления в push notification;
-   Исправлен баг коллизии разметки;
-   Исправлен баг коллизии имени пакета FileProvider
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

1.0.0-alpha01 (2021-04-09)
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

