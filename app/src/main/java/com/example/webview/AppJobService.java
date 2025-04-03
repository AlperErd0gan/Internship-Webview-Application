package com.example.webview;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

public class AppJobService extends JobService {

    private static final String TAG = "AppJobService";

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started");

        // Start your MainActivity or perform other initialization
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // You can also start other background tasks or services here

        // Return true if your job needs to continue running
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job stopped");
        return false; // Return true if you want the job to reschedule
    }


}
