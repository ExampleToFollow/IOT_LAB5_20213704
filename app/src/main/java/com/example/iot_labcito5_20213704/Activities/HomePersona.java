package com.example.iot_labcito5_20213704.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iot_labcito5_20213704.Beans.Comida;
import com.example.iot_labcito5_20213704.Beans.Persona;
import com.example.iot_labcito5_20213704.R;

import org.w3c.dom.Text;

public class HomePersona extends AppCompatActivity {
    Persona personita ;
    Double calorias;
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
        if (personita.getComidas()!=null){
            for(Comida comida : personita.getComidas()){
                caloriasConsumidas = caloriasConsumidas + comida.getCalorias();
            }
        }
        ( (TextView) findViewById(R.id.ola)).setText("Para lograr el objetivo de " + personita.getObjetivo() + " necesitas " + calorias + " calorías");
        ( (TextView) findViewById(R.id.ola2)).setText("Total Consumido de Calorías: " + caloriasConsumidas );
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

}