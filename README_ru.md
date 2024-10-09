Jivo Mobile SDK - Android
=========================
<p align="left">
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://github.com/JivoChat/JivoSDK-Android/releases"><img src="https://img.shields.io/github/v/release/JivoChat/JivoSDK-Android" /></a>
</p>

![Jivo Logo](./README_ru/resources/jivo_logo.svg)

**Jivo Mobile SDK** позволяет встроить чат в нативные мобильные приложения **Android** и принимать обращения клиентов. Интеграция занимает несколько минут, так как интерфейс чата с лентой сообщений уже реализован – вам понадобится только добавить несколько строк кода в ваш проект.

### Возможности актуальной версии

-   Переписка клиента с оператором
-   Отправка файлов в обе стороны
-   История переписки клиента с оператором
-   Статусы отправки и прочитанности сообщений в чате
-   Базовые настройки UI
-   Индикатор новых сообщений внутри приложения интегратора
-   Push-уведомления

### Актуальная версия: 2.5.0

Список изменений:

-   добавлено отключение JivoWebSocketService после закрытия чата;

### Известные проблемы:
- На устройствах марки **Xiaomi**, возникают проблемы с отображением цветов в чате **SDK**. Решение, добавить флаг в стили вашего приложения:
  `<item name="android:forceDarkAllowed">false</item>`.

### Демо-приложение для Android

<img src="https://user-images.githubusercontent.com/81690520/144190746-278592b3-b704-4f9e-9642-ea40ce60f29c.png" width="300"> <img src="https://user-images.githubusercontent.com/81690520/144189472-f5d4c1fd-ded0-493f-860e-5301980e89c1.png" width="300">


Посмотрите, как работает чат внутри приложения на примере нашего демо-приложения. Это простое приложение под Android позволяет написать в чат технической поддержке нашего сервиса - Jivo.

-   [Демо-приложение для Android](https://github.com/JivoChat/JivoSDK-Android/tree/develop/sample)
-   [Демо-приложение для Flutter](https://github.com/JivoChat/JivoSDK-FlutterDemo)

### Требования

-   Android API level 21+
-   Android Studio 4.2.1+

### Этапы интеграции Jivo Mobile SDK:

- [Добавление нового канала Mobile SDK](./README_ru/add_channel.md)
- Интеграция JivoSDK([native](./README_ru/native_setup.md), [react](./README_ru/react_setup.md), [flutter](./README_ru/flutter_setup.md))
- [Настройка push-уведомлений](./README_ru/firebase_notifications_setup.md)
- [Основные настройки JivoSDK](./README_ru/native_api.md) (Опционально)
- [Настройка UI JivoSDK](./README_ru/ui_setup.md) (Опционально)
- [Советы и рекомендации](./README_ru/recommendations.md)
- [Changelog](./README_ru/changelog.md)
