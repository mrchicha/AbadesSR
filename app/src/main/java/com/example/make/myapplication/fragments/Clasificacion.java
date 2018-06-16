package com.example.make.myapplication.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.make.myapplication.Componentes.Category;
import com.example.make.myapplication.Componentes.ListViewAdapter;
import com.example.make.myapplication.ImpCorredor.CorredorImpl;
import com.example.make.myapplication.Modelo.Corredor;
import com.example.make.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Clasificacion extends Fragment {

    private ListView listViewCorredores;
    private ListViewAdapter listViewAdapter;
    private ArrayList<Category> Arraycategory = new ArrayList<>();

    private RequestQueue requestQueue;
    private ArrayList<Corredor> listaClasificados = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clasificacion, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listViewCorredores = getView().findViewById(R.id.run);

        // Lanzamos la petición json para obtener los 5 primeros clasificados
        peticionJson();
    }

    // Método que realiza una petición a la base de datos con servicio Volley
    public void peticionJson(){
        // Crear nueva cola de peticiones
        requestQueue= Volley.newRequestQueue(getContext());

        // Nueva petición JSONObject
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://2654420-1.web-hosting.es/select_clasificacion.php?min=1&max=5",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            // Obtiene un jsonArray de la respuesta de la petición a la base de datos
                            JSONArray jsonArray = response.getJSONArray("corredores");

                            // Obtiene un arrayList de correodores de un arrayjson
                            listaClasificados = CorredorImpl.listaCorredores(jsonArray);

                            // Relleno de datos en el listView
                            rellenoArrays(listaClasificados);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Log", "Error Respuesta en JSON: " + error.getMessage());
                    }
                }
        );
        requestQueue.add(jsArrayRequest);
    }

    // Método que rellena un listView con un array de corredores usando un adapter personalizado
    public void rellenoArrays(ArrayList<Corredor> corredores){

        for(Corredor c : corredores){
            Category category = new Category(
                    getString(R.string.clasificado)+ " " + c.getClasificacion(),
                    getString(R.string.dorsal) + " " + c.getDorsal(),
                    getString(R.string.nombre) + " " + c.getNombre() + " " + c.getApellidos());
            Arraycategory.add(category);
        }
        listViewAdapter = new ListViewAdapter(getActivity(),Arraycategory );
        listViewCorredores.setAdapter(listViewAdapter);
    }
}
