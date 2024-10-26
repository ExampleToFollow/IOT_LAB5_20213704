package com.example.iot_labcito5_20213704.Activities;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iot_labcito5_20213704.Adapters.ComidaAdapter;
import com.example.iot_labcito5_20213704.Beans.Comida;
import com.example.iot_labcito5_20213704.Beans.Persona;
import com.example.iot_labcito5_20213704.R;

import java.util.ArrayList;
import java.util.List;

public class ListaComidasActivity extends AppCompatActivity {
    Persona personita ;
    Double calorias;
    String channelId = "oli";
    private RecyclerView recyclerView;
    private ComidaAdapter adapter;
    private List<Comida> listaComidas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_comidas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        personita = (Persona) getIntent().getSerializableExtra("persona");
        calorias = (Double) getIntent().getSerializableExtra("calorias");
        if(personita.getComidas()!=null){
            recyclerView = findViewById(R.id.irvComidas);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            listaComidas = personita.getComidas();
            adapter = new ComidaAdapter(listaComidas);
            recyclerView.setAdapter(adapter);
        }
        Double caloriasConsumidas = new Double(0.0);
        if (personita.getComidas()!=null){
            for(Comida comida : personita.getComidas()){
                caloriasConsumidas = caloriasConsumidas + comida.getCalorias();
            }
        }
        crearCanalNotificacion();
        if(caloriasConsumidas>calorias){
            lanzarNotificacion("Limite rebasado" , "Ya consumiste más caloría de las que debías , deberías de realizar más actividad física o consumir comida con menos calorías");
        }
    }
    public void volver(View view){
        Intent intent = new Intent(this,HomePersona.class);
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