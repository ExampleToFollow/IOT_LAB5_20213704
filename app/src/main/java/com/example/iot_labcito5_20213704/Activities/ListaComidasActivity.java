package com.example.iot_labcito5_20213704.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iot_labcito5_20213704.Beans.Persona;
import com.example.iot_labcito5_20213704.R;

public class ListaComidasActivity extends AppCompatActivity {
    Persona personita ;
    Double calorias;
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
    }
    public void volver(View view){
        Intent intent = new Intent(this, HomePersona.class);
        intent.putExtra("persona", personita);
        intent.putExtra("calorias" ,calorias );
        startActivity(intent);
    }
}