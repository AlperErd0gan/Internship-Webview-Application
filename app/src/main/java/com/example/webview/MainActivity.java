package com.example.webview;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.util.Calendar;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Date;


import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private AudioManager audioManager;
    private VolumeChangeReceiver volumeChangeReceiver;
    private PowerStateReceiver powerStateReceiver;




    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Full-screen mode and keep screen on
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        // Add JavaScript interface
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");

        // Load your HTML page
        Log.d("url", "onCreate webview url 1");
        if (webView != null) {
            webView.loadUrl("file:///android_asset/index.html");

        } else {
            Toast.makeText(this, "WebView initialization failed", Toast.LENGTH_LONG).show();
        }
        Log.d("url", "onCreate webview url 2 ");
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // Initialize and register VolumeChangeReceiver
        volumeChangeReceiver = new VolumeChangeReceiver(webView, audioManager);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(volumeChangeReceiver, filter);

        // Initialize and register PowerStateReceiver

        powerStateReceiver = new PowerStateReceiver();
        IntentFilter powerFilter = new IntentFilter();
        powerFilter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(powerStateReceiver, powerFilter);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Restart the activity if it is destroyed
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
        Log.d("Destroy", "onDestroy: Attempted to restart the activity");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register the volume change receiver
        if (volumeChangeReceiver != null) {
            IntentFilter volumeFilter = new IntentFilter();
            volumeFilter.addAction("android.media.VOLUME_CHANGED_ACTION");
            registerReceiver(volumeChangeReceiver, volumeFilter);
        }


    }


    // JavaScript Interface
    public class WebAppInterface {
        private Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void volumeUp() {
            // Handle volume up action if needed in WebView
        }

        @JavascriptInterface
        public void volumeDown() {
            // Handle volume down action if needed in WebView
        }

        @JavascriptInterface
        public void callYoutube() {
            // Handle the Youtube button click
            Log.d("WebAppInterface", "openYoutube: Youtube button clicked");
            runOnUiThread(() -> webView.loadUrl("https://www.youtube.com"));
        }

        @JavascriptInterface
        public void callHTML() {
            // Handle the Call HTML button click
            Log.d("WebAppInterface", "loadAnotherHTML: Call HTML button clicked");
            runOnUiThread(() -> webView.loadUrl("file:///android_asset/hey.html"));
        }

        @JavascriptInterface
        public void setTimer(int seconds) {
            long triggerAtMillis = System.currentTimeMillis() + (seconds * 1000);
            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(mContext, AlarmReceiver.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);

            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(mContext, "Timer set for " + seconds + " seconds", Toast.LENGTH_SHORT).show());
        }

        @JavascriptInterface
        public void setAlarm(String dateTime) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault());
            try {
                Date alarmDate = sdf.parse(dateTime);
                if (alarmDate != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(alarmDate);
                    long triggerAtMillis = calendar.getTimeInMillis();
                    AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(mContext, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

                    if (triggerAtMillis < System.currentTimeMillis()) {
                        triggerAlarmNow();
                    } else {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);

                        // Calculate and send time left to frontend
                        long timeLeftMillis = triggerAtMillis - System.currentTimeMillis();
                        sendTimeLeftToFrontend(timeLeftMillis);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(mContext, "Failed to parse date and time", Toast.LENGTH_SHORT).show();
            }
        }

        private void sendTimeLeftToFrontend(long timeLeftMillis) {
            long hours = (timeLeftMillis / 1000) / 3600;
            long minutes = ((timeLeftMillis / 1000) % 3600) / 60;
            long seconds = (timeLeftMillis / 1000) % 60;

            @SuppressLint("DefaultLocale") String timeLeftMessage = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            new Handler(Looper.getMainLooper()).post(() -> webView.evaluateJavascript("updateTimeLeft('" + timeLeftMessage + "')", null));
        }

        private void triggerAlarmNow() {
            Intent intent = new Intent(mContext, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);

            Toast.makeText(mContext, "Alarm triggered immediately!", Toast.LENGTH_SHORT).show();
        }
    }

    }
