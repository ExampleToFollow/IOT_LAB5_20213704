package com.example.iot_labcito5_20213704.Adapters;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iot_labcito5_20213704.Beans.Comida;
import com.example.iot_labcito5_20213704.R;

import java.util.List;

public class ComidaAdapter extends RecyclerView.Adapter<ComidaAdapter.ComidaViewHolder> {

    private List<Comida> listaComidas;

    public ComidaAdapter(List<Comida> listaComidas) {
        this.listaComidas = listaComidas;
    }

    @NonNull
    @Override
    public ComidaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.irv_item_comida, parent, false);
        return new ComidaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComidaViewHolder holder, int position) {
        Comida comida = listaComidas.get(position);
        holder.tvNombreComida.setText(comida.getNombre());
        holder.tvCalorias.setText(String.valueOf(comida.getCalorias()));
    }

    @Override
    public int getItemCount() {
        return listaComidas.size();
    }

    static class ComidaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreComida, tvCalorias;

        public ComidaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreComida = itemView.findViewById(R.id.tvNombreComida);
            tvCalorias = itemView.findViewById(R.id.tvCalorias);
        }
    }
}