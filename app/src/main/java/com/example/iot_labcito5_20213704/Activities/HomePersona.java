package com.example.iot_labcito5_20213704.Activities;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import static java.util.Calendar.*;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import java.util.ArrayList;
import java.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.iot_labcito5_20213704.Beans.ActividadFisica;
import com.example.iot_labcito5_20213704.Beans.Comida;
import com.example.iot_labcito5_20213704.Beans.Persona;
import com.example.iot_labcito5_20213704.R;
import com.example.iot_labcito5_20213704.WorkerManagerNotification;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

public class HomePersona extends AppCompatActivity {
    Persona personita ;
    Double calorias;
    String channelId = "oli";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_persona);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        personita = (Persona) getIntent().getSerializableExtra("persona");
        calorias = (Double) getIntent().getSerializableExtra("calorias");
        verificarNotificacion();
        notificarIngresoComida();
    }

    public void mostrarComidas(View view) {
        Intent intent =  new Intent(this , ListaComidasActivity.class);
        intent.putExtra("persona", personita);
        intent.putExtra("calorias" ,calorias );
        startActivity(intent);
    }
    public void agregarComida(View view) {
        Intent intent =  new Intent(this , AddNewFoodActivity.class);
        intent.putExtra("persona", personita);
        intent.putExtra("calorias" ,calorias );
        startActivity(intent);
    }
    public void actividad(View view){
        Intent intent =  new Intent(this , AddActivity.class);
        intent.putExtra("persona", personita);
        intent.putExtra("calorias" ,calorias );
        startActivity(intent);
    }
    public void addComidaFast (View view){
        Intent intent =  new Intent(this , AddComidaFast.class);
        intent.putExtra("persona", personita);
        intent.putExtra("calorias" ,calorias );
        startActivity(intent);
    }
    //PARA LAS NOTIFICACIONES
    public void crearCanalNotificacion(){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Canal notificaciones default",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Canal para notificaciones con prioridad default");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            askPermission();
        }
    }
    public void askPermission(){
        //android.os.Build.VERSION_CODES.TIRAMISU == 33
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) ==
                        PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{POST_NOTIFICATIONS},
                    101);
        }
    }
    public void lanzarNotificacion(String title,String texto) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(texto)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        }
    }
    public void verificarNotificacion(){
        Double caloriasConsumidas = new Double(0.0);
        LocalDateTime medianocheManana = LocalDate.now().plusDays(1).atStartOfDay(); // Mañana a las 00:00
        LocalDateTime medianocheMananaAyer = LocalDate.now().atStartOfDay(); // Mañana a las 00:00

        if (personita.getComidas()!=null){
            for(Comida comida : personita.getComidas()){
                if(comida.getFecha().isBefore(medianocheManana.atZone(ZoneId.systemDefault()).toInstant())
                        && comida.getFecha().isAfter(medianocheMananaAyer.atZone(ZoneId.systemDefault()).toInstant())
                ){
                    caloriasConsumidas = caloriasConsumidas + comida.getCalorias();
                }
            }
        }
        if(personita.getActividadesFisicas() != null){
            for(ActividadFisica a : personita.getActividadesFisicas()){
                if(a.getFecha().isBefore(medianocheManana.atZone(ZoneId.systemDefault()).toInstant())
                 && a.getFecha().isAfter(medianocheMananaAyer.atZone(ZoneId.systemDefault()).toInstant())
                ){
                    caloriasConsumidas = caloriasConsumidas - a.getCalorias();
                }
            }
        }
        ( (TextView) findViewById(R.id.ola)).setText("Para lograr el objetivo de " + personita.getObjetivo() + " necesitas " + calorias + " calorías");
        ( (TextView) findViewById(R.id.ola2)).setText("Calorias del día (considerando actividad física), hoy: " + caloriasConsumidas );
        crearCanalNotificacion();
        if(caloriasConsumidas>calorias){
            lanzarNotificacion("Limite rebasado" , "Ya consumiste más caloría de las que debías , deberías de realizar más actividad física o consumir comida con menos calorías");}
        TextView ola  = findViewById(R.id.metaDiaria);
        if(caloriasConsumidas>calorias){
            ola.setText("Te excediste en " + (caloriasConsumidas-calorias) + " calorías");
        }else{
            ola.setText("Te faltan " + (calorias -caloriasConsumidas) + " calorías para alcanzar el objetivo");
        }
    }
    public void notificarIngresoComida(){
        Data dataBreakFast = new Data.Builder().putString("food","breakfast").build();
        Data dataLunch = new Data.Builder().putString("food","lunch").build();
        Data dataDinner = new Data.Builder().putString("food","dinner").build();
        Calendar  breakFast =  Calendar.getInstance();
        breakFast.set(Calendar.HOUR_OF_DAY, 7);
        breakFast.set(Calendar.MINUTE,30);
        breakFast.set(Calendar.SECOND, 0);
        Calendar lunch =  Calendar.getInstance();
        lunch.set(Calendar.HOUR_OF_DAY, 13);
        lunch.set(Calendar.MINUTE, 0);
        lunch.set(Calendar.SECOND, 0);
        Calendar dinner =  Calendar.getInstance();
        dinner.set(Calendar.HOUR_OF_DAY, 20);
        dinner.set(Calendar.MINUTE, 30);
        dinner.set(Calendar.SECOND, 0);
        ArrayList<Calendar> fechas = new ArrayList<Calendar>();
        fechas.add(breakFast);
        fechas.add(lunch);
        fechas.add(dinner);
        ArrayList<Data> datosComidas = new ArrayList<Data>();
        datosComidas.add(dataBreakFast);
        datosComidas.add(dataLunch);
        datosComidas.add(dataDinner);
        for (int i = 0; i < fechas.size(); i++) {
            long time = fechas.get(i).getTimeInMillis();
            if(time<=System.currentTimeMillis()){
                time = time + TimeUnit.DAYS.toMillis(1);
            }
            PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(WorkerManagerNotification.class,1, TimeUnit.DAYS).setInitialDelay(time- System.currentTimeMillis(), TimeUnit.MILLISECONDS).setInputData(datosComidas.get(i)).build();
            WorkManager.getInstance(this).enqueueUniquePeriodicWork(datosComidas.get(i).getString("food"), ExistingPeriodicWorkPolicy.REPLACE,workRequest);
        }
    }
}