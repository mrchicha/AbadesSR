package com.mrchicha.make.myapplication.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mrchicha.make.myapplication.ImpCorredor.CorredorImpl;
import com.mrchicha.make.myapplication.Modelo.Corredor;
import com.mrchicha.make.myapplication.R;
import com.mrchicha.make.myapplication.fragments.AbadesStoneRace;
import com.mrchicha.make.myapplication.fragments.Clasificacion;
import com.mrchicha.make.myapplication.fragments.Info;
import com.mrchicha.make.myapplication.fragments.MediaStone;
import com.mrchicha.make.myapplication.fragments.MedioVertical;
import com.mrchicha.make.myapplication.fragments.MiniStoneRace;
import com.mrchicha.make.myapplication.fragments.Patrocinadores;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;

public class MenuLateral extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private FragmentManager fragmentmanager = getSupportFragmentManager();
    private ImageView imgAbades;
    private SharedPreferences misPreferencias;
    private Boolean desactivar;
    private String dorsal;
    private MenuItem locDorsal, actLoc;
    private Switch switcher;
    private RequestQueue requestQueue;
    private LocationManager locationManager;
    private Boolean gpsActivo;
    static final int MI_RESULTADO = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_lateral);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Toggle item
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.app_bar_switch);
        MenuItem locDorsal = menu.findItem(R.id.nav_loc_dorsal);
        MenuItem locDorsalToggle = menu.findItem(R.id.app_bar_switch);
        View actionView = MenuItemCompat.getActionView(menuItem);

        switcher = actionView.findViewById(R.id.switcher);
        switcher.setChecked(false);
        updateEnMovimiento("NO");
        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, (switcher.isChecked()) ? "Está compartiendo su ubicación" : "Ha dejado de compartir su ubicación", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                if(switcher.isChecked())compartirUbicacion();
                else updateEnMovimiento("NO");
            }
        });

        misPreferencias = getSharedPreferences("misPreferencias",0);
        desactivar = misPreferencias.getBoolean("desactivar", false);
        dorsal = misPreferencias.getString("dorsal","");

        if(desactivar){
            //locDorsal.setEnabled(false);
            switcher.setEnabled(false);
            locDorsalToggle.setEnabled(false);

        }
        //actLoc = findViewById(R.id.nav_loc);
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
               // if(desactivar) Toast.makeText(this,"Debe iniciar sesión para compartir su ubicación", Toast.LENGTH_LONG).show();//locDorsal.setEnabled(false);
                cargarActLocalizacion();
                break;
            case R.id.nav_loc_dorsal:
                cargarlocDorsal();
                break;
            case R.id.app_bar_switch:
                switcher.setChecked(!switcher.isChecked());
                if(switcher.isChecked())compartirUbicacion();
                else updateEnMovimiento("NO");

                break;
            case R.id.login:
                cargarLogin();
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

    public void cargarLogin(){
        Intent i = new Intent(this, Login.class);
        startActivity(i);
        finish();
    }

    public void cargarInscripcion(View v){
        Uri uri = Uri.parse("https://www.cruzandolameta.es/ver/abades-stone-race-2017---536/");
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

    // Método que realiza petición de corredor a la base de datos con un parámetro dorsal
    public void compartirUbicacion(){

        // Crear nueva cola de peticiones
        requestQueue= Volley.newRequestQueue(this);

        // Nueva petición JSONObject
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://2654420-1.web-hosting.es/select_id_01.php?dorsal=" + dorsal,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Corredor corredor = CorredorImpl.corredorDorsal(response);
                            updateEnMovimiento("SI");
                            permisosUbicacion();
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

    // Método que añade ubicación al mapa y sube los datos a la base de datos
    public void permisosUbicacion() {

        // Permisos location manager
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                // Si se quiere que el permiso sea solicitado cada vez
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MI_RESULTADO);
            }
            else{
                // Pide el permiso para usar el GPS
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MI_RESULTADO);
            }
            return;
        }
        // Definición e instancia del objeto location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try{
            gpsActivo = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }catch (Exception e){}

        if(gpsActivo) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            // Llama al método de actualizar la ubicación con el objeto location
            actualizarUbicacion(location);

            // Pide la actualización de la ubicación location manager cada 10 segundos, con 20 metros de distancia mínima
            // y usando el listener del location manager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,20,locationListener);
        }
        else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(getString(R.string.gps_inactvo));
            dialog.setPositiveButton(getString(R.string.activar_gps), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( getBaseContext(),MenuLateral.class);
                    startActivity(myIntent);
                }
            });
            dialog.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    finish();
                }
            });
            dialog.show();
        }
    }

    // Método que recibe una localización y pasa la latitud y la longitud a otro método
    // que pinta el punto de ubicación
    public void actualizarUbicacion(Location location) {
        if (location != null) {
            String lon = location.getLongitude()+"";
            String lat = location.getLatitude() +"";
            updateEnMovimiento("SI");
            updateLocalizacion(dorsal,lon, lat);
        }
    }

    // Método que actualiza un corredor y lo pone a compartir ubicación seleccionado por su dorsal
    public void updateEnMovimiento( String enmov){
        // Crear nueva cola de peticiones
        requestQueue = Volley.newRequestQueue(this);

        // Nueva petición de actualización de un corredor
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://2654420-1.web-hosting.es/update_enmov.php?dorsal=" + dorsal + "&enmov=" + enmov,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

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

    // Método actualiza la ubicación de un corredor por su dorsal
    public void updateLocalizacion(String dorsal, String lon, String lat){
        // Crear nueva cola de peticiones
        requestQueue= Volley.newRequestQueue(this);

        // Nueva petición de actualización de latitud y longitud de un dorsal
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://2654420-1.web-hosting.es/update_long_lat.php?dorsal=" + dorsal + "&long=" + lon +"&lat=" +lat,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

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

    // Listener que detecta cuando cambia la ubicación y si es así llama al método que
    // actualiza la ubicación
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            actualizarUbicacion(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public boolean comprobarCompartir(){
        if(switcher.isChecked()) return true;
        else return false;
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
                        if(comprobarCompartir()) Toast.makeText(getApplicationContext(),
                                "Debe dejar de compartir su ubicación antes de salir", Toast.LENGTH_LONG).show();
                        else{
                            finishAffinity();
                            System.exit(0);
                        }
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

        // Inflamos el dialog
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MI_RESULTADO: {

                if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //permiso concedido
                    compartirUbicacion();
                }
                else {
                    //permiso denegado
                    Toast.makeText(getApplicationContext(),getString(R.string.permisodenegado),Toast.LENGTH_LONG).show();
                    finish();
                }
                return;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Al salir cambiamos el estado de compartir ubicación del dorsal de si a no
    }
}
