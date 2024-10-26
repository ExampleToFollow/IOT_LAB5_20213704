package com.example.iot_labcito5_20213704;
import static android.Manifest.permission.POST_NOTIFICATIONS;

import static androidx.core.content.ContextCompat.getSystemService;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

public class WorkerManagerNotification extends Worker {
     String channelId =  "Ch";

    public WorkerManagerNotification(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    @NonNull
    @Override
    public Result doWork(){
        String titulo   = getInputData().getString("titulo");
        String detail = getInputData().getString("texto");
        return Result.success();
    }

    public static void guardarNotificacion(Long duration, Data data , String tag){
        OneTimeWorkRequest notificacion = new OneTimeWorkRequest.Builder(WorkerManagerNotification.class)
                .setInitialDelay(duration, TimeUnit.MILLISECONDS)
                .addTag(tag)
                .setInputData(data)
                .build();
        WorkManager.getInstance().enqueue(notificacion);
    }



}
