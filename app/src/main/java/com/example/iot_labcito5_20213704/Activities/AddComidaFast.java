package com.example.iot_labcito5_20213704.Activities;

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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iot_labcito5_20213704.Beans.Persona;
import com.example.iot_labcito5_20213704.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddComidaFast extends AppCompatActivity {
    private Spinner spinnerAlimentos;
    private TextView tvCalorias;
    private Button btnGuardar;
    Persona personita ;
    Double calorias;
    String channelId = "oli";
    // HashMap para almacenar los alimentos y sus valores calóricos
    private HashMap<String, Integer> alimentosCalorias;

    // Lista para guardar los alimentos seleccionados y sus calorías
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

        spinnerAlimentos = findViewById(R.id.spinnerAlimentos);
        tvCalorias = findViewById(R.id.tvCalorias);
        btnGuardar = findViewById(R.id.btnGuardar);

        alimentosCalorias = new HashMap<>();
        alimentosCalorias.put("Manzana", 52);
        alimentosCalorias.put("Banana", 89);
        alimentosCalorias.put("Pan Blanco", 265);
        alimentosCalorias.put("Leche (1 taza)", 42);
        alimentosCalorias.put("Arroz Cocido (1 taza)", 130);

        ArrayList<String> listaAlimentos = new ArrayList<>(alimentosCalorias.keySet());

        // Configurar el adaptador para el Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaAlimentos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlimentos.setAdapter(adapter);

        // Inicializar la lista de registro de comidas
        registroComidas = new ArrayList<>();

        // Configurar el Listener para detectar la selección de un alimento
        spinnerAlimentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String alimentoSeleccionado = parent.getItemAtPosition(position).toString();
                int calorias = alimentosCalorias.get(alimentoSeleccionado);

                // Mostrar las calorías del alimento seleccionado
                tvCalorias.setText(String.valueOf(calorias));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tvCalorias.setText("0");
            }
        });

        // Configurar el evento onClick del botón para guardar la selección
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alimentoSeleccionado = spinnerAlimentos.getSelectedItem().toString();
                int calorias = alimentosCalorias.get(alimentoSeleccionado);

                // Guardar el alimento y las calorías en la lista de registro
                registroComidas.add(alimentoSeleccionado + " - " + calorias + " calorías");

                // Mostrar un mensaje de confirmación
                Toast.makeText(AddComidaFast.this, "Alimento guardado: " + alimentoSeleccionado, Toast.LENGTH_SHORT).show();
            }
        });
        personita = (Persona) getIntent().getSerializableExtra("persona");
        calorias = (Double) getIntent().getSerializableExtra("calorias");
    }





}