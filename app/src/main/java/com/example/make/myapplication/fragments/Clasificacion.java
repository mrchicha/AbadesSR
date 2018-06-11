package com.example.make.myapplication.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Clasificacion extends Fragment {

    private TextView corredor;
    private DatabaseReference mAbades;
    private FirebaseDatabase fAbades;
    private Query query;
    private DatabaseReference mAbadessr;


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
        corredor = (TextView) getView().findViewById(R.id.txtcorredor);

        //Fireabase

        fAbades = FirebaseDatabase.getInstance();
        mAbades = fAbades.getReference("dorsal");



        query = mAbades.orderByChild("dorsal").equalTo(1).limitToFirst(1);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               for( DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                   corredor.setText(dataSnapshot1.getValue().toString());
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
