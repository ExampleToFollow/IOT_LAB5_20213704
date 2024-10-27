package com.example.iot_labcito5_20213704.Activities;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.iot_labcito5_20213704.R;
import com.example.iot_labcito5_20213704.WorkerManagerNotification;

public class ConfigNotificationsActivity extends AppCompatActivity {
    private TextView tvHoraSeleccionada;
    private Button btnSeleccionarHora;
    private int selectedHour = -1;
    private int selectedMinute = -1;

    private int hora;
    private int minuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_config_notifications);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvHoraSeleccionada = findViewById(R.id.tvHoraSeleccionada);
        btnSeleccionarHora = findViewById(R.id.btnSeleccionarHora);
        btnSeleccionarHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarTimePicker();
            }
        });
    }
    private void mostrarTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Actualizar el TextView con la hora seleccionada
                        tvHoraSeleccionada.setText(String.format("Hora seleccionada: %02d:%02d", hourOfDay, minute));
                        hora  =  hourOfDay;
                        minuto = minute;
                        Button ola = findViewById(R.id.crearNotificacion);
                        ola.setVisibility(View.VISIBLE);
                        ola.setClickable(true);
                    }
                },
                hour,
                minute,
                true
        );
        timePickerDialog.show();
    }

    public void crearCanalNotificacion(View view){
        Calendar noti =  java.util.Calendar.getInstance();
        noti.set(Calendar.HOUR_OF_DAY, hora);
        noti.set(Calendar.MINUTE,minuto);
        noti.set(Calendar.SECOND, 0);
        long time = noti.getTimeInMillis();
        if(time<=System.currentTimeMillis()){
            time = time + TimeUnit.DAYS.toMillis(1);
        }
        Data mensajeComida = new Data.Builder().putString("food","nuevaNoti").build();
        PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(WorkerManagerNotification.class,1, TimeUnit.DAYS).setInitialDelay(time- System.currentTimeMillis(), TimeUnit.MILLISECONDS).setInputData(mensajeComida).build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("nuevaNoti", ExistingPeriodicWorkPolicy.REPLACE,workRequest);

    }
}