Советы и рекомендации.
=================================

## Рекомендации при использовании ProGuard
Если в вашем проекте используется обфускацию кода, для **ProGuard** обязательно добавьте следующие настройки:

```
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response

-keep,allowobfuscation,allowshrinking class com.jivosite.sdk.model.** { *; }
-keep,allowobfuscation,allowshrinking class com.jivosite.sdk.network.** { *; }
```

## Резервное копирование пользовательских данных с помощью Auto Backup
Если в вашем проекте используется резервное копирование пользовательских данных с помощью Auto Backup(`android:allowBackup="true"`), **необходимо** отключить резервное копирование для Jivo SDK.

Настройки для `AndroidManifest.xml`
>AndroidManifest.xml
>```xml
><manifest ... >
>    ...
>    <application
>        ...
>        android:allowBackup="true"
>        android:dataExtractionRules="@xml/data_extraction_rules"
>        android:fullBackupContent="@xml/backup_rules"
>        //Опционально, если у вас уже есть правила резервного копирования
>        tools:replace="android:fullBackupContent"
>        ...
>    </application>
>
></manifest>
>```

Отключение резервного копирования для **Jivo SDK** на **Android 11** и в более ранних версиях:
>backup_rules.xml
>```xml
><?xml version="1.0" encoding="utf-8"?>
><full-backup-content>
>    <exclude
>        domain="sharedpref"
>        path="com.jivosite.sdk.session.xml" />
></full-backup-content>
>```

Отключение резервного копирования для **Jivo SDK** на **Android 12** или в более поздних версиях:
>data_extraction_rules.xml
>```xml
><?xml version="1.0" encoding="utf-8"?>
><data-extraction-rules>
>    <cloud-backup>
>        <exclude
>            domain="sharedpref"
>            path="com.jivosite.sdk.session.xml" />
>    </cloud-backup>
>    <device-transfer>
>        <exclude
>            domain="sharedpref"
>            path="com.jivosite.sdk.session.xml" />
>    </device-transfer>
></data-extraction-rules>
>```

## Отключение темной темы
Если в вашем проекте используется только светлая тема, то следующая настройка отключит активацию темной темы.

Пример:
> <details><summary>Kotlin</summary>
>
>```kotlin
>class MainActivity : AppCompatActivity() {
>
>    override fun onCreate(savedInstanceState: Bundle?) {
>        super.onCreate(savedInstanceState)
>        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
>    }
>}
>```
></details>
>
> <details> <summary>Java</summary>
>
>```java
>public class MainActivity extends AppCompatActivity {
>    ...
>
>    @Override
>    protected void onCreate(Bundle savedInstanceState) {
>        super.onCreate(savedInstanceState);
>        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
>        ...
>    }
>    ...
>}
>```
></details>