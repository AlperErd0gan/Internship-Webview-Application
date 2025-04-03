# WebView Application

This project **integrates a WebView application** with various **Android components**, including:

- **Broadcast Receivers**
- **Job Scheduling**
- **Permissions Management**

It handles:

- **Screen Events**
- **Volume Changes**
- **Device Boot Events**

and includes functionalities to manage:

- **Alarms**
- **Timers**

through a WebView interface.

The main objective is to **manipulate the Android TV environment** using **Android Studio** and **Java**.

## Components

### Activities and Services

- **`MainActivity`**
  - Initializes the WebView.
  - Handles screen and volume management.

- **`WebAppInterface`**
  - Provides JavaScript interface methods.
  - Includes methods like `setTimer` and `setAlarm`.
  - Includes `sendTimeLeftToFrontend`  methods that is         communicating with the forntend.
  - Includes `triggerAlarmNow` that is calling the AlarmReceiver in order to play the alarm sound.

- **`VolumeChangeReceiver`**
  - Listens to volume changes and interacts with WebView.
  - Primarly changes the background color of `index.html` via JavaScript methods in the `main.js`

- **`PowerStateReceiver`**
  - Responds to screen on/off events.
  - Schedules jobs when the screen turns on to call the MainActivity

- **`BootReceiver`**
  - Reacts to device boot completion.
  - Schedules jobs after boot to call the MainActivity.

- **`AppJobService`**
  - Executes scheduled jobs.
  - Starts `MainActivity` or performs other initialization tasks.

### Job Scheduling

- **`JobSchedulerHelper`**
  - Facilitates job scheduling.
  - Ensures jobs persist across reboots.

- **`AppJobService`**
  - Manages background tasks.
  - Executes scheduled jobs and starts activities.

### Broadcast Receivers

- **`VolumeChangeReceiver`**
  - Monitors volume changes.

- **`PowerStateReceiver`**
  - Handles screen state changes.

- **`BootReceiver`**
  - Handles device boot events.

### WebView Integration

- **`WebAppInterface`**
  - **Methods exposed to JavaScript, including:**
    - `setTimer`
    - `setAlarm`
    - `sendTimeLeftToFrontend`
    - `triggerAlarmNow`

### Permissions and Manifest

Ensure that `AndroidManifest.xml` includes:

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    package="com.example.webview">
    
    <!-- Permissions -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS"/>
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <uses-permission android:name="android.permission.WAKE_LOCK"/>
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>
    <uses-permission android:name="android.permission.SCHEDULE_EXACT_ALARM"/>
    <uses-permission android:name="android.permission.USE_EXACT_ALARM"/>
    <uses-permission android:name="com.android.alarm.permission.SET_ALARM"/>
    <uses-permission android:name="android.permission.VIBRATE"/>
    <uses-permission android:name="android.permission.DISABLE_KEYGUARD"/>

    <!-- Features -->
    <uses-feature android:name="android.hardware.touchscreen" android:required="false" />
    <uses-feature android:name="android.software.leanback" android:required="true" />
    <uses-feature android:name="android.hardware.camera" android:required="false" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:installLocation="internalOnly"
        android:supportsRtl="true"
        android:theme="@style/Theme.Webview">

        <!-- Main Activity -->
        <activity
            android:name=".MainActivity"
            android:banner="@drawable/app_icon_your_company"
            android:exported="true"
            android:showOnLockScreen="true"
            android:icon="@drawable/app_icon_your_company"
            android:theme="@style/Theme.AppCompat"
            android:logo="@drawable/app_icon_your_company"
            android:screenOrientation="landscape"
            tools:ignore="DiscouragedApi">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.HOME" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.LAUNCHER"/>
                <category android:name="android.intent.category.LEANBACK_LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- Broadcast Receivers -->
        <receiver
            android:name=".VolumeChangeReceiver"
            android:exported="true"
            tools:ignore="Instantiatable">
            <intent-filter>
                <action android:name="android.media.VOLUME_CHANGED_ACTION" />
            </intent-filter>
        </receiver>

        <receiver
            android:name=".BootReceiver"
            android:enabled="true"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED"/>
            </intent-filter>
        </receiver>

        <receiver
            android:name=".PowerStateReceiver"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.SCREEN_OFF"/>
                <action android:name="android.intent.action.SCREEN_ON"/>
            </intent-filter>
        </receiver>

        <receiver
            android:name=".AlarmReceiver"
            android:exported="true">
        </receiver>

        <receiver
            android:name=".MyDeviceAdminReceiver"
            android:exported="true"
            android:label="@string/app_name"
            android:permission="android.permission.BIND_DEVICE_ADMIN">
            <meta-data
                android:name="android.app.device_admin"
                android:resource="@xml/device_admin_sample" />
            <intent-filter>
                <action android:name="android.app.action.DEVICE_ADMIN_ENABLED" />
            </intent-filter>
        </receiver>

        <!-- Services -->
        <service
            android:name=".AppJobService"
            android:permission="android.permission.BIND_JOB_SERVICE"
            android:exported="false"/>
    </application>
</manifest>
```
### Alarm Sound Integration

- Place the alarm sound you want in the **`app/src/main/res/raw`** directory, make sure you are playing the correct sound in the AlarmReceiver.
- AlarmReceiver mainly plays the alarm sound and is called when the alarm is triggered.
- Setting up the alarm is mainly done in the **`WebAppInterface`**. In there we also communicate with the fronted and trigger the alarm. 

### JavaScript Integration

- Place `index.html` and other HTML files in the **`assets`** folder.
- You can also place directly the build file in the **`assets`** folder.

### Error Handling and Logging

- Implement appropriate error handling and logging for debugging and user notifications. Error handling and logging is done for all the receivers and services. Do not forget to add logs if you add a new class into the application for better readability and error handling.

### Testing and Validation

- **WebView**: Verify correct loading and interaction with JavaScript. You should add **`network_security_config.xml`** into this directory **app\src\main\res\xml**. It will give permission for Cleartext. Should be like below. Don't forget to add **`"android:networkSecurityConfig="@xml/network_security_config"`** under the application state in **`AndroidManifest.xml`**: 
```
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">13.91.163.33</domain>
    </domain-config>
</network-security-config>
```
Change the IP if you changed the built of NevoTV you used. Change the IP with the IP adress you use in your config files for NevoTV.
- **Receivers**: Test response to volume changes, screen events, and boot events.
- **Job Scheduling**: Ensure jobs are scheduled and executed as expected.

## Setup

1. Import the project into Android Studio.
2. Ensure all dependencies are resolved and.
3. Run the application on an emulator or device to test its functionalities.

For further information or issues, please refer to the project documentation or contact the maintainers.

## Erros
- `NET::ERR_CLEARTEXT_NOT_PERMITTED` 

If you are getting an error called `NET::ERR_CLEARTEXT_NOT_PERMITTED`  go to this stackoverflow link or go to the **Testing and Validation** part of this README file: https://stackoverflow.com/questions/45940861/android-8-cleartext-http-traffic-not-permitted



---

*This README provides an overview of the application's components and configuration. Ensure to adapt it according to the project's specific needs and updates.*

