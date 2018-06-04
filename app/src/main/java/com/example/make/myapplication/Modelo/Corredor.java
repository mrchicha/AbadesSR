package com.example.make.myapplication.Modelo;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.example.make.myapplication.ICorredor.ICorredor;

public class Corredor{

    public static String TABLA="inscritos";
    public static String CLAVE="dorsal";

    private int dorsal;
    private String nombre;
    private String apellidos;
    private  Boolean movimiento;
    private Double lat;
    private Double lon;
    private int clasificacion;

    public Corredor(){};

    public Corredor( int dorsal, String nombre, String apellidos){
        this.dorsal=dorsal;
        this.nombre=nombre;
        this.apellidos=apellidos;
    }

    public int getDorsal() {
        return dorsal;
    }

    public void setDorsal(int dorsal) {
        this.dorsal = dorsal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Boolean getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Boolean movimiento) {
        this.movimiento = movimiento;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public int getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(int clasificacion) {
        this.clasificacion = clasificacion;
    }

    @Override
    public String toString() {
        return "Corredor{" +
                "dorsal=" + dorsal +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", movimiento=" + movimiento +
                ", lat=" + lat +
                ", lon=" + lon +
                ", clasificacion=" + clasificacion +
                '}';
    }
}
