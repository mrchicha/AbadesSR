package com.mrchicha.make.myapplication.Componentes;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mrchicha.make.myapplication.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Category> items;

    public ListViewAdapter (Activity activity, ArrayList<Category> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<Category> category) {
        for (int i = 0; i < category.size(); i++) {
            items.add(category.get(i));
        }
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.lista_columnas2, null);
        }

        Category dir = items.get(position);

        TextView posicion = (TextView) v.findViewById(R.id.posicionAdapter2);
        posicion.setText(dir.getPosicion());

        TextView nombre = (TextView) v.findViewById(R.id.nombreAdapter2);
        nombre.setText(dir.getNombre());

        TextView dorsal = (TextView) v.findViewById(R.id.dorsalAdapter2);
        dorsal.setText(dir.getDorsal());


        return v;
    }
}
