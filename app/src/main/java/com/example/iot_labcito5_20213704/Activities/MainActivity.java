package com.example.iot_labcito5_20213704.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.view.View;
import android.widget.*;

import com.example.iot_labcito5_20213704.Beans.Persona;
import com.example.iot_labcito5_20213704.R;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText etPeso, etAltura, etEdad;
    private RadioGroup rgGenero, rgObjetivo;
    private Spinner spinnerActividad;
    private Button btnCalcular;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etPeso = findViewById(R.id.etPeso);
        etAltura = findViewById(R.id.etAltura);
        etEdad = findViewById(R.id.etEdad);
        rgGenero = findViewById(R.id.rgGenero);
        rgObjetivo = findViewById(R.id.rgObjetivo);
        spinnerActividad = findViewById(R.id.spinnerActividadFisica);
        btnCalcular = findViewById(R.id.btnCalcular);

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarFormulario()) {
                    calcularCalorias();
                }
            }
        });

    }
    private boolean validarFormulario() {
        boolean esValido = true;
        if (etPeso.getText().toString().trim().isEmpty()) {
            etPeso.setError("Ingresa tu peso");
            esValido = false;
        }
        if (etAltura.getText().toString().trim().isEmpty()) {
            etAltura.setError("Ingresa tu altura");
            esValido = false;
        }
        if (etEdad.getText().toString().trim().isEmpty()) {
            etEdad.setError("Ingresa tu edad");
            esValido = false;
        }
        if (rgGenero.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Selecciona tu género", Toast.LENGTH_SHORT).show();
            esValido = false;
        }
        if (rgObjetivo.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Selecciona tu objetivo", Toast.LENGTH_SHORT).show();
            esValido = false;
        }
        return esValido;
    }

    private void calcularCalorias() {
        double peso = Double.parseDouble(etPeso.getText().toString());
        double altura = Double.parseDouble(etAltura.getText().toString());
        int edad = Integer.parseInt(etEdad.getText().toString());
        String genero = (rgGenero.getCheckedRadioButtonId() == R.id.rbMasculino) ? "masculino" : "femenino";
        int actividad = spinnerActividad.getSelectedItemPosition();
        String objetivo = "";
        String rbSubirPesoStr = R.id.rbSubirPeso + "";
        String rgObjetivoStr  = rgObjetivo.getCheckedRadioButtonId() + "";
        String rbBajarPesoStr  = R.id.rbBajarPeso+ "";
        String rbMantenerPesoStr  = R.id.rbMantenerPeso + "";

        if(rgObjetivoStr.equals(rbSubirPesoStr)){objetivo = "subir";};
        if(rgObjetivoStr.equals(rbBajarPesoStr)){objetivo = "bajar";};
        if(rgObjetivoStr.equals(rbMantenerPesoStr)){objetivo = "mantener";}
        String actividadStr = spinnerActividad.getSelectedItem().toString();
        Persona persona =  new Persona();
        persona.setActividad(actividadStr);
        persona.setAltura(altura);
        persona.setEdad(edad);
        persona.setGenero(genero);
        persona.setPeso(peso);
        persona.setObjetivo(objetivo);
        Double tmb;
        if (genero.equals("masculino")) {
            tmb = 10*peso + 6.25*altura - 5*edad + 5;
        } else {
            tmb = 10*peso + 6.25*altura - 5*edad - 161;
        }

        tmb *= (actividad == 0) ? 1.2 :
                (actividad == 1) ? 1.375 :
                        (actividad == 2) ? 1.55 :
                                (actividad == 3) ? 1.725 : 1.9;

        if (objetivo.equals("subir")) {
            tmb = tmb + 500;
        } else if (objetivo.equals("bajar")) {
            tmb = tmb - 300;
        }
        Toast.makeText(this, "Calorías recomendadas: " + tmb, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(MainActivity.this, HomePersona.class);
        intent.putExtra("persona", persona);
        intent.putExtra("calorias" ,tmb );
        startActivity(intent);
    }






}