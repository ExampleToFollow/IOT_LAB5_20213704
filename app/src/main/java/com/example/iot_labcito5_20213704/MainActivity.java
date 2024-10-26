package com.example.iot_labcito5_20213704;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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


        // Inicializar los componentes
        etPeso = findViewById(R.id.etPeso);
        etAltura = findViewById(R.id.etAltura);
        etEdad = findViewById(R.id.etEdad);
        rgGenero = findViewById(R.id.rgGenero);
        rgObjetivo = findViewById(R.id.rgObjetivo);
        spinnerActividad = findViewById(R.id.spinnerActividadFisica);
        btnCalcular = findViewById(R.id.btnCalcular);

        // Configurar el botón de cálculo
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

        // Validar que los campos no estén vacíos
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

        // Validar que un género esté seleccionado
        if (rgGenero.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Selecciona tu género", Toast.LENGTH_SHORT).show();
            esValido = false;
        }

        // Validar que un objetivo esté seleccionado
        if (rgObjetivo.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Selecciona tu objetivo", Toast.LENGTH_SHORT).show();
            esValido = false;
        }

        return esValido;
    }

    private void calcularCalorias() {
        // Obtén los valores del formulario
        double peso = Double.parseDouble(etPeso.getText().toString());
        double altura = Double.parseDouble(etAltura.getText().toString());
        int edad = Integer.parseInt(etEdad.getText().toString());

        // Determinar género
        String genero = (rgGenero.getCheckedRadioButtonId() == R.id.rbMasculino) ? "masculino" : "femenino";

        // Obtener el nivel de actividad
        int actividad = spinnerActividad.getSelectedItemPosition();

        // Determinar el objetivo
        String objetivo = "";
//        switch (rgObjetivo.getCheckedRadioButtonId()) {
//            case (R.id.rbSubirPeso):
//                objetivo = "subir";
//                break;
//            case R.id.rbBajarPeso:
//                objetivo = "bajar";
//                break;
//            case R.id.rbMantenerPeso:
//                objetivo = "mantener";
//                break;
//        }

        // Cálculo de las calorías utilizando la fórmula de Harris-Benedict
        double tmb;
        if (genero.equals("masculino")) {
            tmb = 88.36 + (13.4 * peso) + (4.8 * altura) - (5.7 * edad);
        } else {
            tmb = 447.6 + (9.2 * peso) + (3.1 * altura) - (4.3 * edad);
        }

        // Ajustar el TMB según el nivel de actividad
        tmb *= (actividad == 0) ? 1.2 :
                (actividad == 1) ? 1.375 :
                        (actividad == 2) ? 1.55 :
                                (actividad == 3) ? 1.725 : 1.9;

        // Ajustar según el objetivo
        if (objetivo.equals("subir")) {
            tmb += 500;
        } else if (objetivo.equals("bajar")) {
            tmb -= 300;
        }

        // Mostrar las calorías recomendadas
        Toast.makeText(this, "Calorías recomendadas: " + tmb, Toast.LENGTH_LONG).show();
    }






}