package com.example.make.myapplication.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.make.myapplication.R;
import com.example.make.myapplication.fragments.AbadesStoneRace;
import com.example.make.myapplication.fragments.Clasificacion;
import com.example.make.myapplication.fragments.Info;
import com.example.make.myapplication.fragments.MediaStone;
import com.example.make.myapplication.fragments.MedioVertical;
import com.example.make.myapplication.fragments.MiniStoneRace;
import com.example.make.myapplication.fragments.Patrocinadores;

public class MenuLateral extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FragmentManager fragmentmanager = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_lateral);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            dialog();
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lateral, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Añadimos la opción de ir al menú de opciones del GPS
        if (id == R.id.action_settings) {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.nav_abades:
                cambioFragment(new AbadesStoneRace(),getString(R.string.Abades));
                break;
            case R.id.nav_media:
                cambioFragment(new MediaStone(),getString(R.string.Media));
                break;
            case R.id.nav_mini:
                cambioFragment(new MiniStoneRace(),getString(R.string.Mini));
                break;
            case R.id.nav_vertical:
                cambioFragment(new MedioVertical(),getString(R.string.Vertical));
                break;
            case R.id.nav_actividades:
                cargarInscripcion(null);
                break;
            case R.id.nav_resultados:
                cambioFragment(new Clasificacion(),getString(R.string.Resultados));
                break;
            case R.id.nav_patrocinadores:
                cambioFragment(new Patrocinadores(),getString(R.string.Patrocinadores_organizadores));
                break;
            case R.id.nav_informacion:
                cambioFragment(new Info(),getString(R.string.informacion));
                break;
            case R.id.nav_loc:
                cargarActLocalizacion();
                break;
            case R.id.nav_loc_dorsal:
                cargarlocDorsal();
                break;
            case R.id.salir:
                dialog();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Método para cambio de fragmen en el menú lateral y cambiar el título
    // a la actionBar
    public void cambioFragment(android.support.v4.app.Fragment fragment, String titulo){

        fragmentmanager.beginTransaction().replace(R.id.contenedor,fragment).commit();
        getSupportActionBar().setTitle(titulo);
    }

    public void cargarInscripcion(View v){
        Uri uri = Uri.parse("https://www.cruzandolameta.es/ver/abades-stone-race-2017---536/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void cargarAbadesPatrocinador(View v){
        Uri uri = Uri.parse("http://www.abades.com/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void cargarMalaCaraPatrocinador(View v){
        Uri uri = Uri.parse("https://malacaraclubrunning.blogspot.com/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void cargarAyuntamientoPatrocinador(View v){
        Uri uri = Uri.parse("http://www.aytoloja.org/ayuntamiento/telefonos.htm");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void cargarApoloPatrocinador(View v){
        Uri uri = Uri.parse("https://www.mariscosapolo.com/contacto/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void cargarCocacolaPatrocinador(View v){
        Uri uri = Uri.parse("https://www.cocacola.es/es/home/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void cargarPoweradePatrocinador(View v){
        Uri uri = Uri.parse("https://www.powerade.com/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public  void cargaMapAbades(View view){
        Intent i=new Intent(this,BuscarLoc.class);
        i.putExtra("fragment",0);
        startActivity(i);
    }
    public  void cargaMapMedia(View view){
        Intent i=new Intent(this,BuscarLoc.class);
        i.putExtra("fragment",1);
        startActivity(i);
    }

    public  void cargaMapMini(View view){
        Intent i=new Intent(this,BuscarLoc.class);
        i.putExtra("fragment",2);
        startActivity(i);
    }

    public  void cargaMapMedio(View view){
        Intent i=new Intent(this,BuscarLoc.class);
        i.putExtra("fragment",3);
        startActivity(i);
    }

    public void cargarActLocalizacion(){

        Intent i=new Intent(this,BuscarLoc.class);
        startActivity(i);
    }

    public void cargarlocDorsal(){

        Intent i=new Intent(this,ActivarLoc.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void dialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                this);

        // Título y mensaje del dialog
        alertDialog.setTitle(getString(R.string.mensajesalir));
        alertDialog.setMessage(getString(R.string.confirmarsalir));

        // Opción si
        alertDialog.setPositiveButton(getString(R.string.si),

                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                });

        // Opción no
        alertDialog.setNegativeButton(getString(R.string.no),

                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Cancelamos el diálogo en caso de pulsar en no
                        dialog.cancel();
                    }
                });

        // Showing Alert Message

        alertDialog.show();

    }

}
