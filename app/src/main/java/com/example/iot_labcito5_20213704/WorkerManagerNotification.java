package com.example.iot_labcito5_20213704;
import static android.Manifest.permission.POST_NOTIFICATIONS;

import static androidx.core.content.ContextCompat.getSystemService;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.iot_labcito5_20213704.Activities.MainActivity;

import java.util.concurrent.TimeUnit;

public class WorkerManagerNotification extends Worker {
     String channelId =  "Ch";

    public WorkerManagerNotification(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }
    @NonNull
    @Override
    public Result doWork(){
        Log.d("Ola", "auxilio");
        notificacionComidaDiaria(getInputData().getString("food"));
        return Result.success();
    }

    public void notificacionComidaDiaria(String food){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"oli")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("No olvides registrar tu comida")
                .setContentText("Registra tu "+ food+"!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        if(ActivityCompat.checkSelfPermission(getApplicationContext(),POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            notificationManagerCompat.notify(2,builder.build());
        }
    }



}
