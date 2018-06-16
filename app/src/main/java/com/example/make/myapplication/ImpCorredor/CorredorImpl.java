package com.example.make.myapplication.ImpCorredor;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.make.myapplication.Modelo.Corredor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CorredorImpl {

    // Método que obtiene un arraylist de corredores de un jsonArray
    public static ArrayList<Corredor> listaCorredores(JSONArray jsonArray) throws JSONException {
        ArrayList<Corredor> corredores = new ArrayList<>();

        // Recorremos el jsonArray
        for(int i=0; i<jsonArray.length();i++){

            // Pasa cada objeto json a corredor y lo añade al arrayList de corredores
            corredores.add(corredorDorsal(jsonArray.getJSONObject(i)));
        }
        return corredores;
    }

    // Método que recibe un objeto json y devuelve un corredor
    public static Corredor corredorDorsal(JSONObject jsonObject) throws JSONException {
        Corredor corredor = new Corredor();

        // Obtenemos los elementos del json con su name correspondiente
        corredor.setDorsal(jsonObject.getString("dorsal"));
        corredor.setModalidad(jsonObject.getString("modalidad"));
        corredor.setNombre(jsonObject.getString("nombre"));
        corredor.setApellidos(jsonObject.getString("apellidos"));
        corredor.setEmail(jsonObject.getString("email"));
        corredor.setFechanacimiento(jsonObject.getString("fechanacimiento"));
        corredor.setTipodocumento(jsonObject.getString("tipodocumento"));
        corredor.setDni(jsonObject.getString("dni"));
        corredor.setCodigopostal(jsonObject.getString("codigopostal"));
        corredor.setLocalidad(jsonObject.getString("localidad"));
        corredor.setProvincia(jsonObject.getString("provincia"));
        corredor.setGenero(jsonObject.getString("genero"));
        corredor.setTelefono(jsonObject.getString("telefono"));
        corredor.setFederado(jsonObject.getString("federado"));
        corredor.setClub(jsonObject.getString("club"));
        corredor.setKmvertical(jsonObject.getString("kmvertical"));
        corredor.setEnmovimiento(jsonObject.getString("enmovimiento"));
        corredor.setLatitud(jsonObject.getString("latitud"));
        corredor.setLongitud(jsonObject.getString("longitud"));
        corredor.setClasificacion(jsonObject.getString("clasificacion"));


        return corredor;
    }

}
