package com.example.webview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.webkit.WebView;

public class VolumeChangeReceiver extends BroadcastReceiver {
    private WebView webView;
    private AudioManager audioManager;
    private int previousVolume ; // Initial value to ensure first update

    // Constructor with WebView and AudioManager initialization
    public VolumeChangeReceiver(WebView webView, AudioManager audioManager) {
        this.webView = webView;
        this.audioManager = audioManager;
        this.previousVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC); // Initial volume

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

            if (currentVolume > previousVolume) {
                webView.evaluateJavascript("volumeUp();", null);
            } else if (currentVolume < previousVolume) {
                webView.evaluateJavascript("volumeDown();", null);
            }

            previousVolume = currentVolume;

        }
    }

}
