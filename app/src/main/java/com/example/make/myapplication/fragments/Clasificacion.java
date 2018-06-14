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

import com.example.make.myapplication.ImpCorredor.CorredorImpl;
import com.example.make.myapplication.Modelo.Abadessr;
import com.example.make.myapplication.Modelo.Corredor;
import com.example.make.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Clasificacion extends Fragment {

    private TextView corredor;
    private ListView listViewCorredores;
    private DatabaseReference mAbades;
    private DatabaseReference mCorredor;
    private FirebaseDatabase fAbades;
    private Query query;
    private DatabaseReference mAbadessr;
    String datos="";
    long i;
    int in=0;
    ArrayAdapter<Abadessr> adapter;
    ArrayList<Abadessr> listaCorredores=new ArrayList<Abadessr>();
    ArrayList<String> lista=new ArrayList<String>();


    //private CorredorImpl corredorImpl = new CorredorImpl();
    //private Corredor cor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clasificacion, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //corredor = (TextView) getView().findViewById(R.id.txtcorredor);
        listViewCorredores= (ListView) getView().findViewById(R.id.run);

        CorredorImpl corredor = new CorredorImpl();
        corredor.SeleccionarPorID(1107);

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

                        /*YOUR CHOICE OF COLOR*/
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

    }
}
