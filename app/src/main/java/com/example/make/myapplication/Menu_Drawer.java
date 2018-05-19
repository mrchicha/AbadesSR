package com.example.make.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.make.myapplication.fragments.AbadesStoneRace;
import com.example.make.myapplication.fragments.MediaStone;
import com.example.make.myapplication.fragments.MedioVertical;
import com.example.make.myapplication.fragments.MiniStoneRace;

public class Menu_Drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        android.support.v4.app.FragmentManager fragmentmanager=getSupportFragmentManager();

         if (id == R.id.nav_actividades) {
             fragmentmanager.beginTransaction().replace(R.id.contenedor,new AbadesStoneRace()).commit();

        } else if (id == R.id.nav_resultados) {

        } else if (id == R.id.nav_patrocinadores) {

        } else if (id == R.id.nav_informacion) {
            cargarInformacion();
        }   else if (id == R.id.nav_loc_dorsal) {
            cargarActLocalizacion();
        } else if (id == R.id.nav_loc) {

        }

        switch (id){
            case R.id.nav_abades:
                fragmentmanager.beginTransaction().replace(R.id.contenedor,new AbadesStoneRace()).commit();
                break;
            case R.id.nav_media:
                fragmentmanager.beginTransaction().replace(R.id.contenedor,new MediaStone()).commit();
                break;
            case R.id.nav_mini:
                fragmentmanager.beginTransaction().replace(R.id.contenedor,new MiniStoneRace()).commit();
                break;
            case R.id.nav_vertical:
                fragmentmanager.beginTransaction().replace(R.id.contenedor,new MedioVertical()).commit();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void cargarPruebas(){

            Intent i=new Intent(this,Pruebas.class);
            startActivity(i);

    }

    public void cargarActividades(){

        Intent i=new Intent(this,pruebaMenu.class);
        startActivity(i);

    }

    public void cargarInformacion(){

        Intent i=new Intent(this,Informacion.class);
        startActivity(i);

    }

    public void cargarActLocalizacion(){

        Intent i=new Intent(this,ActLocalizacion.class);
        startActivity(i);

    }

    public void cargarInscripcion(View v){
        Uri uri = Uri.parse("https://www.cruzandolameta.es/ver/abades-stone-race-2017---536/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }
}
