package com.example.webview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.util.Log;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

    private static final int JOB_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // Log for debugging purposes
            Log.d("BootReceiver", "Device booted, scheduling job...");

            // Schedule the job
            scheduleJob(context);
            Toast.makeText(context, "App is starting", Toast.LENGTH_LONG).show();
        }
    }

    private void scheduleJob(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID,
                new ComponentName(context, AppJobService.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true); // Persist the job across reboots

        jobScheduler.schedule(builder.build());
    }
}
