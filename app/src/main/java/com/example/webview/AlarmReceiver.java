package com.example.webview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    private MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Play an alarm sound
        mediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound);
        if (mediaPlayer != null) {
            mediaPlayer.start();

            // Show a toast message
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, "Alarm triggered!", Toast.LENGTH_SHORT).show());

            // Stop the media player after 15 seconds
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, "Alarm stopped!", Toast.LENGTH_SHORT).show());
                }
            }, 15000); // 15 seconds delay
        } else {
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(context, "Failed to play alarm sound", Toast.LENGTH_SHORT).show());
        }
    }
}
