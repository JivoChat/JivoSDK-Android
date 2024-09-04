Changelog
=========

2.4.0 (04/09/2024)
-----------------------

### Features:

-   added disabling the socket service after closing the chat;

2.3.2 (24/05/2024)
-----------------------

### Bug Fixes:

-   fixed errors that caused the SDK to crash and not work correctly;

2.3.1 (09/05/2024)
-----------------------

### Bug Fixes:

-   fixed errors that caused the SDK to crash and not work correctly;

2.3.0 (22/04/2024)
-----------------------

### Bug Fixes:

-   fixed errors that caused the SDK to crash and not work correctly;

### Features:

-   added sending photos from camera;

2.2.3 (03/13/2024)
-----------------------

### Bug Fixes:

-   fixed incorrect display of `markdown` text;

2.2.2 (02/08/2024)
-----------------------

### Bug Fixes:

-   fixed errors that caused the SDK to crash and not work correctly;

### Features:

-   Jivo.changeChannelId() and Jivo.setUserToken() methods have been replaced with Jivo.setData() method;

2.2 (11/21/2023)
-----------------------

### Bug Fixes:

-   fixed incorrect display of `markdown` list;

### Features:

-   added support for customizing the chat button;
-   added support for customizing a message with a picture;

2.1.1 (09/20/2023)
-----------------------

### Bug Fixes:

-   fixed crash when calling `setContactInfo()` function from background thread;
-   fixed incorrect display of contact form after restarting application;

### Features:

-   disabled backing up user data using the auto backup function;

2.1.0 (07/20/2023)
-----------------------

### Bug Fixes:

-   service restart is disabled;
-   fixed some UI bugs;

### Features:

-   added the possibility of using own translations to assess the quality of service;

2.0.0 (05/30/2023)
-----------------------

### Bug Fixes:

-   fixed incorrect message status display;
-   fixed some bugs that caused the SDK to crash and work incorrectly;

### Features:

-   added quality of service assessment;
-   redesigned API responsible for SDK connections;
-   renamed Jivo.setClientInfo() method to Jivo.setContactInfo() method for transferring client contact data;
-   added support for connection status customization;

1.2.1 (02/28/2023)
-----------------------

### Bug Fixes:

-   fixed some bugs that caused the SDK to crash and work incorrectly;

1.2.0 (12/06/2022)
-----------------------

### Features:

-   added the ability to change the channel id (widget_id) on the fly;
-   added transfer of additional information about the client - custom data;

1.1.0 (11/25/2022)
-----------------------

### Features:

- added support for chatbot messages;

1.0.7 (10/13/2022)
-----------------------

### Features:

- added support for notifications for Android 13 (API level 33) and higher devices;

1.0.6 (08/31/2022)
-----------------------

### Bug Fixes:

- fixed bug with repeated display of in-app notification;
- fixed bug with repeated display of file upload error;

### Features:

- added a new chat state if the client is blacklisted;

1.1.0-alpha03 (07/22/2022)
-----------------------

### Bug Fixes:

- fixed some UI bugs;

### Features:

- added setting toolbar-a using attributes;
- removed UI settings from config;

1.1.0-alpha02 (07/20/2022)
-----------------------

### Features:

- added more flexible UI customization using styles;

1.0.5 (06/14/2022)
-----------------------

### Bug Fixes:

- fixed a bug with incorrect display of the indicator of new messages;

1.0.4 (06/06/2022)
-----------------------

### Bug Fixes:

- fixed incorrect display of the notification about the absence of operators;

### Features:

- added form for entering contacts;
- added a limit of 1000 characters for the sent message;
- added the ability to replace the standard PUSH notification icon and the color of the PUSH notification icon;

1.0.3 (04/07/2022)
-----------------------
### Features:

- added notification about the absence of operators on the channel;

1.0.2 (02/11/2022)
-----------------------
### Features:

- added the ability to disable in-app notifications;

1.0.1 (02/03/2022)
-----------------------

### Bug Fixes:

- fixed spacing between messages;

### Features:

- added the ability to set a custom sound for PUSH notifications;
- added logging obfuscation.

1.0.0 (12/16/2021)
-----------------------

### Bug Fixes:

- fixed a bug with displaying the delivery status of a sent message;
- fixed bugs with displaying dark theme colors;

### Features:

- the visual component of uploading a media file has been changed;
- added a mechanism that hides the file download button in the absence of a license;
- added support for displaying a notification while the chat windows is closed;
- added an interface for implementing custom navigation by tapping on a PUSH notification;
- added an interface for implementing custom navigation on the click of the “back” button.

1.0.0-rc05 (11/24/2021)
-----------------------

### Bug Fixes

-   fixed a bug with displaying the chat title;
-   fixed bug with time format display;
-   fixed bug with incorrect display of file type after unsuccessful upload;
-   fixed a bug with the lack of time for the sent message;

### Features

-   added dark theme support;
-   added support for a new media service;
-   added data cleaning interface:
-   added a queue of unsent messages;
-   added user-token transfer interface.

1.0.0-rc04 (09/20/2021)
-----------------------

### Bug Fixes

-   fixed bug with displaying several messages with the same time;
-   fixed bug with downloading files;
-   fixed a bug with incorrect display of the indicator of new messages;

### Features

-   added [interface] notifying about unread messages;
-   updated [UI Jivo SDK] settings, added the ability to hide the logo using the hideLogo() setting.

1.0.0-alpha17 (08/23/2021)
--------------------------

### Bug Fixes

-   fixed bug with Jivo SDK crash when calling Jivo.updatePushToken() method from background thread;

### Features

-   added [interface] notifying about unread messages;
-   updated [UI Jivo SDK] settings, added the ability to hide the logo using the hideLogo() setting.

1.0.0-alpha16 (06/11/2021)
--------------------------

### Features

-   added the ability to disable in-app notifications.

1.0.0-alpha15 (06/10/2021)
--------------------------

### Bug Fixes

-   fixed bug with no sound notification in PUSH notification;
-   fixed markup collision bug;
-   fixed FileProvider package name collision bug;
-   fixed message delivery status bug;
-   fixed bug with artifacts after tapping on the PUSH.

### Features

-   added ability to customize Jivo SDK chat;
-   added interface for processing PUSH notifications;
-   added PushToken update interface;
-   added interface for passing information about the client.

1.0.0-alpha14 (05/18/2021)
--------------------------

### Bug Fixes

-   fixed bug with uploading files;

### Features

-   removed "port" parameter from Jivo.init() function.

1.0.0-alpha01 (04/09/2021)
--------------------------

### Bug Fixes

-   fixed bug with incorrect display of history;

### Features

-   added display of the agent's name who joined the chat;
-   added time and status of sending messages;
-   welcome message added;
-   added typing insights;
-   added error message when message is not sent to chat;
-   added full-screen image preview;
-   added message copying using “long tap;