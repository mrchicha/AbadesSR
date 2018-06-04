package com.example.make.myapplication.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.make.myapplication.ImpCorredor.CorredorImpl;
import com.example.make.myapplication.Modelo.Corredor;
import com.example.make.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Clasificacion extends Fragment {

    private TextView corredor;
    private CorredorImpl corredorImpl = new CorredorImpl();
    private Corredor cor;
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

        cor=corredorImpl.SeleccionarPorID(1001);

        corredor.setText(cor.getNombre() + " " + cor.getApellidos());
    }
}
