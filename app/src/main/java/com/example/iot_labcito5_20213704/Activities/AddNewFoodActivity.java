package com.example.iot_labcito5_20213704.Activities;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class AddNewFoodActivity extends AppCompatActivity {
    Persona personita ;
    Double calorias;
    String channelId = "oli";
    private TextInputLayout inputLayoutNombreComida, inputLayoutCalorias;
    private TextInputEditText etNombreComida, etCaloriasComida;
    private MaterialButton btnGuardar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_food);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //----------------
        personita = (Persona) getIntent().getSerializableExtra("persona");
        calorias = (Double) getIntent().getSerializableExtra("calorias");
        //----------------
        inputLayoutNombreComida = (TextInputLayout) findViewById(R.id.etNombreComidaLayout);
        inputLayoutCalorias = (TextInputLayout) findViewById(R.id.etCaloriasComidaLayout);
        etNombreComida = (TextInputEditText)findViewById(R.id.etNombreComida);
        etCaloriasComida = (TextInputEditText) findViewById(R.id.etCaloriasComida);
        btnGuardar = (MaterialButton)findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(v -> {
            if (validarCampos()) {
                Toast.makeText(this, "Datos guardados exitosamente", Toast.LENGTH_SHORT).show();
                String nombreComida = etNombreComida.getText().toString().trim();
                String caloriasStr = etCaloriasComida.getText().toString().trim();
                Instant instante = Instant.now();
                if(personita.getComidas() == null){
                    Comida comida = new Comida();
                    comida.setNombre(nombreComida);
                    comida.setCalorias(Double.parseDouble(caloriasStr));
                    comida.setFecha(instante);
                    ArrayList<Comida> comidas = new ArrayList<Comida>() ;
                    comidas.add(comida);
                    personita.setComidas(comidas);
                }else{
                    Comida comida = new Comida();
                    comida.setNombre(nombreComida);
                    comida.setCalorias(Double.parseDouble(caloriasStr));
                    comida.setFecha(instante);
                    ArrayList<Comida> listaComida=  personita.getComidas();
                    listaComida.add(comida);
                    personita.setComidas(listaComida);
                }
                Toast.makeText(this, "Guardado con exito", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HomePersona.class);
                intent.putExtra("persona", personita);
                intent.putExtra("calorias" ,calorias );
                startActivity(intent);
            }else{
                Toast.makeText(this, "Por favor, complete todos los campos correctamente", Toast.LENGTH_SHORT).show();
            }
        });
        verificarNotificacion();
    }
    private boolean validarCampos() {
        String nombreComida = etNombreComida.getText().toString().trim();
        if (TextUtils.isEmpty(nombreComida)) {
            inputLayoutNombreComida.setError("Ingrese el nombre de la comida");
            return false;
        } else {
            inputLayoutNombreComida.setError(null); // Quita el error si el campo está correcto
        }
        String caloriasStr = etCaloriasComida.getText().toString().trim();
        if (TextUtils.isEmpty(caloriasStr)) {
            inputLayoutCalorias.setError("Ingrese las calorías");
            return false;
        } else {
            inputLayoutCalorias.setError(null);
        }
        try {
            int calorias = Integer.parseInt(caloriasStr);
            if (calorias <= 0) {
                inputLayoutCalorias.setError("Las calorías deben ser un número positivo");
                return false;
            }
        } catch (NumberFormatException e) {
            inputLayoutCalorias.setError("Ingrese un número válido para las calorías");
            return false;
        }
        return true;
    }
    public void volver(View view) {
        Intent intent = new Intent(this, HomePersona.class);
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
        if(caloriasConsumidas>calorias){
            lanzarNotificacion("Limite rebasado" , "Ya consumiste más caloría de las que debías , deberías de realizar más actividad física o consumir comida con menos calorías");
        }

    }

}