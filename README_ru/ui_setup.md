Настройка UI JivoSDK
=================================

Начиная с версии SDK 1.1.0 все **настройки UI** реализованы с помощью тем и стилей.
На выбор доступны три основные темы:

- **Main.Theme.JivoSDK**
- **Main.Theme.JivoSDK.Blue**
- **Main.Theme.JivoSDK.Graphite**

Для переопределения основной темы SDK добавьте данную конструкцию в стили вашего приложения:

```xml

<style name="JivoSDKThemeSwitcher">
    <item name="theme">@style/Main.Theme.JivoSDK.Blue</item>
</style>
```

Если вам требуется переопредить основные цвета SDK, то следует добавить тему и в `parent` указать `Base.Theme.JivoSDK`, а затем переопределить `JivoSDKThemeSwitcher`

- colorPrimary - основной цвет для интерфейса.
- colorPrimaryVariant — оттенок основного цвета.
- colorAccent - цвет по умолчанию для компонентов, которые находятся в фокусе или активны.

```xml

<style name="JivoSDKCustomTheme" parent="Base.Theme.JivoSDK">
    <item name="colorPrimary">@color/colorPrimary</item>
    <item name="colorPrimaryVariant">@color/colorPrimaryVariant</item>
    <item name="colorAccent">@color/colorAccent</item>
</style>

<style name="JivoSDKThemeSwitcher">
    <item name="theme">@style/JivoSDKCustomTheme</item>
</style>
```

Для более тонкой настройки UI необходимо переопределить стиль нужного элемента. Пример настройки темы:

```xml

<resources>
    <style name="AppTheme" parent="Theme.AppCompat.DayNight.NoActionBar">
        ...
        <item name="colorPrimaryDark">@color/midnight</item>
    </style>

    <style name="JivoSDKThemeSwitcher">
        <item name="theme">@style/Main.Theme.JivoSDK.Blue</item>
    </style>

    <style name="Widget.JivoSDK.AppBarLayout" parent="Widget.Material3.AppBarLayout">
        <item name="android:background">@drawable/bg_appbar</item>
    </style>

    <style name="Widget.JivoSDK.Toolbar" parent="">
        <item name="navigationIcon">@drawable/jivo_sdk_vic_arrow_24dp</item>
        <item name="navigationIconTint">?attr/colorOnPrimary</item>
        <item name="title">@string/title</item>
        <item name="subtitle">@string/subtitle</item>
        <item name="titleTextAppearance">?attr/textAppearanceSubtitle2</item>
        <item name="titleTextColor">?attr/colorOnPrimary</item>
        <item name="subtitleTextColor">@color/jivo_sdk_color_on_surface_84</item>
        <item name="subtitleTextAppearance">?attr/textAppearanceCaption</item>
        <item name="logo">@drawable/vic_logo</item>
        <item name="titleMarginStart">8dp</item>
    </style>

</resources>
```

> [!NOTE]<br> Для Flutter или React Native используйте атрибут `<item name="colorPrimaryDark">@color/midnight</item>` который
> изменяет цвет Status bar.

Список основных элементов SDK
-----------------

**Изменение цвета фона чата**

```xml

<style name="JivoSDK.ChatBackground" parent="Main.Theme.JivoSDK">
    <item name="android:background">@color/jivo_sdk_surface</item>
</style>
```

**Изменение формы и цвета исходящего сообщения**

```xml
<style name="JivoSDK.OutgoingBubbleBackground" parent="Main.Theme.JivoSDK">
    <item name="android:background">@drawable/jivo_sdk_bg_outgoing_bubble</item>
</style>
```

**Изменение формы и цвета входящего сообщения**

```xml

<style name="JivoSDK.IncomingBubbleBackground" parent="Main.Theme.JivoSDK">
    <item name="android:background">@drawable/jivo_sdk_bg_incoming_bubble</item>
    <item name="android:paddingStart">16dp</item>
    <item name="android:paddingTop">8dp</item>
    <item name="android:paddingEnd">8dp</item>
    <item name="android:paddingBottom">4dp</item>
</style>
```

**Настройка ввода текста**

```xml

<style name="Widget.JivoSDK.TextInputEditText.TextInput" parent="Widget.Material3.TextInputEditText.FilledBox">
    <item name="android:textAppearance">@style/Base.TextAppearance.JivoSDK.Body1</item>
    <item name="android:paddingLeft">12dp</item>
    <item name="android:paddingRight">8dp</item>
    <item name="android:maxHeight">120dp</item>
    <item name="android:maxLength">1000</item>

    <item name="android:textColor">@color/jivo_sdk_color_on_surface_87</item>
    <item name="android:textColorHint">@color/jivo_sdk_color_on_surface_38</item>

    <item name="android:textCursorDrawable">@drawable/jivo_sdk_bg_cursor</item>
    <item name="android:textColorHighlight">@color/jivo_sdk_color_primary_20</item>

    <item name="android:focusableInTouchMode">true</item>
    <item name="android:checked">true</item>
    <item name="boxBackgroundMode">none</item>
</style>
```

**Изменение формы и цвета элемента формы контактов**

```xml

<style name="Main.Theme.JivoSDK.ContactFormBackground">
    <item name="android:background">@drawable/jivo_sdk_bg_form</item>
</style>
```

**Изменние кнопки отправки сообщения**

```xml

<style name="Widget.JivoSDK.Button.SendMessage" parent="Widget.Material3.Button.IconButton">
    <item name="icon">@drawable/jivo_sdk_vic_btn_send</item>
</style>
```

Список всех элементов находится в [**sdk/src/main/res/values/styles.xml**]()
> [!NOTE]<br> Для Flutter или React Native используйте атрибут `<item name="colorPrimaryDark">@color/midnight</item>` который
> изменяет цвет Status bar.
