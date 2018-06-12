package com.example.make.myapplication.Modelo;


public class Abadessr {

    public String dorsal;
    public String dni;

    public Abadessr() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Abadessr(String dni, String dorsal) {
        this.dorsal = dorsal;
        this.dni = dni;
    }

    @Override
    public String toString() {
        return "Dorsal '" + dorsal + '\'' +
                ", dni '" + dni + '\'';
    }
}
