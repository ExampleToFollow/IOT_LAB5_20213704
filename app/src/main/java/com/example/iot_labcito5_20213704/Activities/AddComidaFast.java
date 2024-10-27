package com.example.iot_labcito5_20213704.Activities;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddComidaFast extends AppCompatActivity {
    private Spinner spinnerAlimentos;
    private TextView tvCalorias;
    private Button btnGuardar;
    Persona personita ;
    Double calorias2;
    String channelId = "oli";
    private HashMap<String, Integer> alimentosCalorias;
    private List<String> registroComidas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_comida_fast);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Recogemos datos persona
        personita = (Persona) getIntent().getSerializableExtra("persona");
        calorias2 = (Double) getIntent().getSerializableExtra("calorias");
        //---------------------

        spinnerAlimentos = findViewById(R.id.spinnerAlimentos);
        tvCalorias = findViewById(R.id.tvCalorias);
        btnGuardar = findViewById(R.id.btnGuardar);
        alimentosCalorias = new HashMap<>();
        alimentosCalorias.put("Manzana", 52);
        alimentosCalorias.put("Banana", 89);
        alimentosCalorias.put("Pan Blanco", 265);
        alimentosCalorias.put("Leche (1 taza)", 42);
        alimentosCalorias.put("Arroz Cocido", 130);
        ArrayList<String> listaAlimentos = new ArrayList<>(alimentosCalorias.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaAlimentos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlimentos.setAdapter(adapter);
        registroComidas = new ArrayList<>();
        spinnerAlimentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String alimentoSeleccionado = parent.getItemAtPosition(position).toString();
                int calorias = alimentosCalorias.get(alimentoSeleccionado);
                tvCalorias.setText(String.valueOf(calorias));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tvCalorias.setText("0");
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alimentoSeleccionado = spinnerAlimentos.getSelectedItem().toString();
                int calorias = alimentosCalorias.get(alimentoSeleccionado);
                if(personita.getActividadesFisicas() == null ){
                    ArrayList<Comida> lista  =  new ArrayList<>();
                    Comida ola = new Comida();
                    ola.setCalorias(Double.parseDouble("" + calorias));
                    ola.setFecha(Instant.now());
                    ola.setNombre(alimentoSeleccionado);
                    lista.add(ola);
                    personita.setComidas(lista);
                }else{
                    Comida ola = new Comida();
                    ola.setCalorias(Double.parseDouble("" + calorias));
                    ola.setFecha(Instant.now());
                    ola.setNombre(alimentoSeleccionado);
                    ArrayList<Comida> lista2 = personita.getComidas();
                    lista2.add(ola);
                    personita.setComidas(lista2);
                }
                Intent intent = new Intent(AddComidaFast.this, HomePersona.class);
                intent.putExtra("persona", personita);
                intent.putExtra("calorias" ,calorias2 );
                startActivity(intent);
                Toast.makeText(AddComidaFast.this, "Alimento guardado: " + alimentoSeleccionado, Toast.LENGTH_SHORT).show();
            }
        });
        verificarNotificacion();
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
         crearCanalNotificacion();
        if(caloriasConsumidas>calorias2){
            lanzarNotificacion("Limite rebasado" , "Ya consumiste más caloría de las que debías , deberías de realizar más actividad física o consumir comida con menos calorías");
        }

    }
}