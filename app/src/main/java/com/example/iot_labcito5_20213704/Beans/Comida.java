package com.example.iot_labcito5_20213704.Beans;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

public class Comida implements Serializable {
    private String nombre;
    private Double calorias;
    private Instant fecha;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getCalorias() {
        return calorias;
    }

    public void setCalorias(Double precio) {
        this.calorias = precio;
    }

    public Instant getFecha() {
        return fecha;
    }

    public void setFecha(Instant fecha) {
        this.fecha = fecha;
    }
}
