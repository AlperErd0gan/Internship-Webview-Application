package com.example.webview;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class PowerStateReceiver extends BroadcastReceiver {
    private boolean wasScreenOff = false;
    private ScheduleJob scheduleJob;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                // Screen is turned off
                wasScreenOff = true;
                Log.d("TAG", "onReceive: SCREEN OFFFFFFFF ");
            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                // Screen is turned
                // .makeText(context, "Screen is turned on", Toast.LENGTH_SHORT).show();
                wasScreenOff = false;
                Log.d("TAG", "onReceive: SCREEN ONNNNNNNN ");
                scheduleJob = new ScheduleJob();
                scheduleJob.scheduleJob(context);
                Toast.makeText(context, "App is starting", Toast.LENGTH_LONG).show();

                /*if (wasScreenOff) {
                    // Screen was previously off, now turned on
                    Toast.makeText(context, "Screen was turned off but now turned on", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "onReceive: SCREEN WAS OFFF NOW ONNNN ");
                     // Reset the flag
                }*/
            }
        }
    }
    public class ScheduleJob {

        private static final int JOB_ID = 1;

        public void scheduleJob(Context context) {

            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            JobInfo.Builder builder = new JobInfo.Builder(JOB_ID,
                    new ComponentName(context, AppJobService.class))
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true); // Persist the job across reboots

            jobScheduler.schedule(builder.build());
        }
    }

}
