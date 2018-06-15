package com.example.make.myapplication.Componentes;

public class Category {
    private String posicion;
    private String dorsal;
    private String nombre;

    public Category(String posicion, String dorsal, String nombre) {
        this.posicion = posicion;
        this.dorsal = dorsal;
        this.nombre = nombre;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public String getDorsal() {
        return dorsal;
    }

    public void setDorsal(String dorsal) {
        this.dorsal = dorsal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
