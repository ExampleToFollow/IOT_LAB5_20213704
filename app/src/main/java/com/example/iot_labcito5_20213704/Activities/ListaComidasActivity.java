package com.example.iot_labcito5_20213704.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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
    }
    public void volver(View view){
        Intent intent = new Intent(this,HomePersona.class);
        intent.putExtra("persona", personita);
        intent.putExtra("calorias" ,calorias );
        startActivity(intent);
    }
}