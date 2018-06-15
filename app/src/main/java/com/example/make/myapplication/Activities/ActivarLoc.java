package com.example.make.myapplication.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.make.myapplication.R;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class ActivarLoc extends AppCompatActivity {
    MapView map = null;
    Double latitud;
    Double longitud;
    IMapController mapController;
    Marker startMarker;
    GeoPoint startPoint;
    LocationManager locationManager;
    boolean gpsActivo, locUsada;
    static final int MI_RESULTADO = 0;
    private static RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //handle permissions first, before map is created. not depicted here

        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's tile servers will get you banned based on this string

        //inflate and create the map
        setContentView(R.layout.activity_loc_dorsal);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.OpenTopo);

        mapController = map.getController();
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController.setZoom(14);

        //GeoPoint startPoint = new GeoPoint(37.143051, -4.073414);
        //mapController.setCenter(startPoint);
        ubicacion();
        //startMarker = new Marker(map);

        //startMarker.setPosition(startPoint);
    }

    // Método que añade ubicación al mapa y sube los datos a la base de datos
    public void ubicacion() {

        // permisos location manager
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){

            }
            else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MI_RESULTADO);
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

            // Pide la actualización de la ubicación al objeto location manager cada 20 segundos, con 2 metros de distancia mínima
            // y usando el listener del location manager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,20,locationListener);
        }
        else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("El GPS no está activo");
            dialog.setPositiveButton("Activar el GPS desde el menú", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( getBaseContext(),MenuLateral.class);
                    startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
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
            agregarMarker(location.getLatitude(), location.getLongitude());
            // Añadir método que añade ubicación a base de datos
            String lon = location.getLongitude()+"";
            String lat = location.getLatitude() +"";
            updateEnMovimiento("1001","SI");
            updateLocalizacion("1001",lon, lat);
        } else agregarMarker(37.143051, -4.073414);
    }

    // Método que recibe una latitud y una longitud y crea un geopoint
    // centra el mapa en el geopoint y determina las opciones del overlay del mapa
    public void agregarMarker(Double lat, Double lon) {
        startPoint = new GeoPoint(lat, lon);

        mapController.setCenter(startPoint);
        startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setIcon(getResources().getDrawable(R.drawable.marcadorp));
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlay().clear();
        map.getOverlays().add(startMarker);
        //map.invalidate();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MI_RESULTADO: {

                if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //permiso concedido
                }
                else {
                    //permiso denegado
                }

                return;
            }
        }
    }

    public void updateEnMovimiento(String dorsal, String enmov){
        // Crear nueva cola de peticiones
        requestQueue = Volley.newRequestQueue(this);

        // Nueva petición JSONObject
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

    public void updateLocalizacion(String dorsal, String lon, String lat){
        // Crear nueva cola de peticiones
        requestQueue= Volley.newRequestQueue(this);

        // Nueva petición JSONObject
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

    public void onResume(){
        super.onResume();
        map.onResume();
    }

    public void onPause(){
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateEnMovimiento("1001","NO");
    }
}

