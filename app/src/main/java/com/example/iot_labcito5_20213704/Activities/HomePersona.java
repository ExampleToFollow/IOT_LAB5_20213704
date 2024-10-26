package com.example.iot_labcito5_20213704.Activities;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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

import com.example.iot_labcito5_20213704.Beans.ActividadFisica;
import com.example.iot_labcito5_20213704.Beans.Comida;
import com.example.iot_labcito5_20213704.Beans.Persona;
import com.example.iot_labcito5_20213704.R;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
        Double caloriasConsumidas = new Double(0.0);
        LocalDateTime medianocheManana = LocalDate.now().plusDays(1).atStartOfDay(); // Mañana a las 00:00
        if (personita.getComidas()!=null){
            for(Comida comida : personita.getComidas()){
                if(comida.getFecha().isBefore(medianocheManana.atZone(ZoneId.systemDefault()).toInstant())){
                    caloriasConsumidas = caloriasConsumidas + comida.getCalorias();
                }
            }
        }
         if(personita.getActividadesFisicas() != null){
             for(ActividadFisica a : personita.getActividadesFisicas()){
                 if(a.getFecha().isBefore(medianocheManana.atZone(ZoneId.systemDefault()).toInstant())){
                     caloriasConsumidas = caloriasConsumidas - a.getCalorias();
                 }
             }
         }
        ( (TextView) findViewById(R.id.ola)).setText("Para lograr el objetivo de " + personita.getObjetivo() + " necesitas " + calorias + " calorías");
        ( (TextView) findViewById(R.id.ola2)).setText("Total Consumido de Calorías (considerando actividad física), hoy: " + caloriasConsumidas );
        crearCanalNotificacion();
        if(caloriasConsumidas>calorias){
            lanzarNotificacion("Limite rebasado" , "Ya consumiste más caloría de las que debías , deberías de realizar más actividad física o consumir comida con menos calorías");
        }
        TextView ola  = findViewById(R.id.metaDiaria);
        if(caloriasConsumidas>calorias){
            //EXCESO
            ola.setText("Te excediste en " + (caloriasConsumidas-calorias) + " calorías");
        }else{
            //NO EXCESO
            ola.setText("Te faltan " + (calorias -caloriasConsumidas) + " calorías para alcanzar el objetivo");
        }
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
}