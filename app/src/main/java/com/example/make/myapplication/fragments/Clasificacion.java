package com.example.make.myapplication.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.make.myapplication.Modelo.Abadessr;
import com.example.make.myapplication.Modelo.Corredor;
import com.example.make.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Clasificacion extends Fragment {

    private ListView listViewCorredores;

    private Corredor corredor;
    private RequestQueue requestQueue;
    private ArrayAdapter<Corredor> adapter;
    private ArrayList<Corredor> listaClasificados = new ArrayList<>();
    private ArrayList<String> lista = new ArrayList<String>();




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clasificacion, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listViewCorredores= (ListView) getView().findViewById(R.id.run);

        listViewCorredores.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

                Toast.makeText(getContext(), "Clasificado Nº:  " + (position +1), Toast.LENGTH_SHORT).show();
            }

        });

        peticionJson();
        /*
        //Fireabase

       mAbades = FirebaseDatabase.getInstance().getReference("corredores");

       //mCorredor.child("nombre").setValue("Juan jose");
       query = mAbades.getRef().child("dorsal").equalTo("1");

        mAbades.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()) {

                    Abadessr abadessr = data.getValue(Abadessr.class);
                    datos = "dni: " + abadessr.dni + ", dorsal " + abadessr.dorsal ;
                    listaCorredores.add(abadessr);

                    //i= dataSnapshot.getChildrenCount();
                    //Log.i("elementos" ,dataSnapshot.getChildrenCount() +"");

                }
                    Log.i("array", listaCorredores.size()+"");
                adapter=new ArrayAdapter<Abadessr>(getActivity().getApplicationContext(),
                        R.layout.support_simple_spinner_dropdown_item,listaCorredores)
                {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view =super.getView(position, convertView, parent);

                        TextView textView=(TextView) view.findViewById(android.R.id.text1);


                        textView.setTextColor(Color.BLUE);

                        return view;
                    }
                };

                listViewCorredores.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listViewCorredores.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

                Toast.makeText(getContext(), "Clasificado Nº:  " + (position +1), Toast.LENGTH_SHORT).show();
            }

        });
        */

    }

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
                            //  tiene que generarse dentro del evento
                            JSONArray jsonArray = response.getJSONArray("corredores");

                            //obtiene un objeto json de un arrayjson

                            listaClasificados = listaCorredores(jsonArray);

                            //relleno con un objeto json

                            //listaClasificados.add(corredorDorsal(jsonArray.getJSONObject(2)));

                            //listaClasificados.add(corredorDorsal(response));
                            relleno(listaClasificados);

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

    public void relleno(ArrayList<Corredor> corredores){

        adapter = new ArrayAdapter<Corredor>(getActivity().getApplicationContext(),
                R.layout.support_simple_spinner_dropdown_item,corredores)
        {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);

                textView.setTextColor(Color.BLUE);

                return view;
            }
        };

        listViewCorredores.setAdapter(adapter);
    }

    public ArrayList<Corredor> listaCorredores(JSONArray jsonArray) throws JSONException {
        ArrayList<Corredor> corredores = new ArrayList<>();

        for(int i=0; i<jsonArray.length();i++){
            corredores.add(corredorDorsal(jsonArray.getJSONObject(i)));
        }
        return corredores;
    }

    public Corredor corredorDorsal(JSONObject jsonObject) throws JSONException {
        Corredor corredor = new Corredor();

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
