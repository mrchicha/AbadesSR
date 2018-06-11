package com.example.make.myapplication.Modelo;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Abadessr {

    public String dorsal;
    public String dni;

    public Abadessr() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Abadessr(String dorsal, String dni) {
        this.dorsal = dorsal;
        this.dni = dni;
    }
}
