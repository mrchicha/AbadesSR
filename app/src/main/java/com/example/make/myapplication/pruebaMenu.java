package com.example.make.myapplication;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class pruebaMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba_menu);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_prueba) {

        } else if (id == R.id.nav_actividades) {

        } else if (id == R.id.nav_resultados) {

        } else if (id == R.id.nav_patrocinadores) {

        } else if (id == R.id.nav_informacion) {

        }   else if (id == R.id.nav_loc_dorsal) {

        } else if (id == R.id.nav_loc) {

        }
        return false;
    }
}
