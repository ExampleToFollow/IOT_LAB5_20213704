package com.example.iot_labcito5_20213704.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {
    Persona personita ;
    Double calorias;
    String channelId = "oli";
    private TextInputLayout inputLayoutNombreComida, inputLayoutCalorias;
    private TextInputEditText etNombreComida, etCaloriasComida;
    private MaterialButton btnGuardar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        personita = (Persona) getIntent().getSerializableExtra("persona");
        calorias = (Double) getIntent().getSerializableExtra("calorias");
        inputLayoutNombreComida = (TextInputLayout) findViewById(R.id.etNombreComidaLayout);
        inputLayoutCalorias = (TextInputLayout) findViewById(R.id.etCaloriasComidaLayout);
        etNombreComida = (TextInputEditText) findViewById(R.id.etNombreComida);
        etCaloriasComida = (TextInputEditText) findViewById(R.id.etCaloriasComida);
        btnGuardar = (MaterialButton) findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(v -> {
            if (validarCampos()) {
                Toast.makeText(this, "Datos guardados exitosamente", Toast.LENGTH_SHORT).show();
                String nombreComida = etNombreComida.getText().toString().trim();
                String caloriasStr = etCaloriasComida.getText().toString().trim();
                Instant instante = Instant.now();
                if (personita.getActividadesFisicas() == null) {
                    ActividadFisica comida = new ActividadFisica();
                    comida.setNombre(nombreComida);
                    comida.setCalorias(Double.parseDouble(caloriasStr));
                    comida.setFecha(instante);
                    ArrayList<ActividadFisica> comidas = new ArrayList<ActividadFisica>();
                    comidas.add(comida);
                    personita.setActividadesFisicas(comidas);
                } else {
                    ActividadFisica comida = new ActividadFisica();
                    comida.setNombre(nombreComida);
                    comida.setCalorias(Double.parseDouble(caloriasStr));
                    comida.setFecha(instante);
                    ArrayList<ActividadFisica> listaComida = personita.getActividadesFisicas();
                    listaComida.add(comida);
                    personita.setActividadesFisicas(listaComida);
                }
                Toast.makeText(this, "Guardado con exito", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ListaComidasActivity.class);
                calorias = calorias - Double.parseDouble(caloriasStr);
                intent.putExtra("persona", personita);
                intent.putExtra("calorias", calorias);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos correctamente", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private boolean validarCampos() {
        String nombreComida = etNombreComida.getText().toString().trim();
        if (TextUtils.isEmpty(nombreComida)) {
            inputLayoutNombreComida.setError("Ingrese el nombre de la actividad");
            return false;
        } else {
            inputLayoutNombreComida.setError(null);
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
    public void volver(View view){
        Intent intent = new Intent(this,HomePersona.class);
        intent.putExtra("persona", personita);
        intent.putExtra("calorias" ,calorias );
        startActivity(intent);    }

}