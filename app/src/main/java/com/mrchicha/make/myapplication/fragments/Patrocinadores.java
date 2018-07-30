package com.mrchicha.make.myapplication.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mrchicha.make.myapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Patrocinadores extends Fragment implements View.OnClickListener {

    private ImageView imgAbades, imgMalaCara, imgAyunta, imgApolo, imgCola, imgPower;

    public Patrocinadores() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patrocinadores, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       imgAbades = getView().findViewById(R.id.imgAbades);
       imgAbades.setOnClickListener(this);
       imgMalaCara = getView().findViewById(R.id.imgMalaCara);
       imgMalaCara.setOnClickListener(this);
       imgAyunta = getView().findViewById(R.id.imgAyunta);
       imgAyunta.setOnClickListener(this);
       imgApolo = getView().findViewById(R.id.imgApolo);
       imgApolo.setOnClickListener(this);
       imgCola = getView().findViewById(R.id.imgCola);
       imgCola.setOnClickListener(this);
       imgPower = getView().findViewById(R.id.imgPower);
       imgPower.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgAbades:
                cargarAbadesPatrocinador("http://www.abades.com/");
                break;
            case R.id.imgMalaCara:
                cargarAbadesPatrocinador("https://malacaraclubrunning.blogspot.com/");
                break;
            case R.id.imgAyunta:
                cargarAbadesPatrocinador("http://www.aytoloja.org/ayuntamiento/telefonos.htm");
                break;
            case R.id.imgApolo:
                cargarAbadesPatrocinador("https://www.mariscosapolo.com/contacto/");
                break;
            case R.id.imgCola:
                cargarAbadesPatrocinador("https://www.cocacola.es/es/home/");
                break;
            case R.id.imgPower:
                cargarAbadesPatrocinador("https://www.powerade.com/");
                break;
        }
    }

    public void cargarAbadesPatrocinador(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
