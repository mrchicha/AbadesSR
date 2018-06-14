package com.example.make.myapplication.Activities;

import android.Manifest;
import android.content.Context;
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

import com.example.make.myapplication.R;
import com.google.android.gms.common.api.GoogleApiClient;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class LocDorsal extends AppCompatActivity {
    MapView map = null;
    Double latitud;
    Double longitud;
    IMapController mapController;
    Marker startMarker;
    GeoPoint startPoint;
    static final int MI_RESULTADO = 0;

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

    // Método que recibe una localización y pasa la latitud y la longitud a otro método
    // que pinta el punto de ubicación
    public void actualizarUbicacion(Location location) {
        if (location != null) {
            agregarMarker(location.getLatitude(), location.getLongitude());
        } else agregarMarker(37.143051, -4.073414);
    }

    // Método que recibe una latitud y una longitud y crea un geopoint
    // centra el mapa en el geopoint y determina las opciones del overlay del mapa
    public void agregarMarker(Double lat, Double lon) {
        startPoint = new GeoPoint(lat, lon);

       //startPoint.setCoords(lat, lon);
        mapController.setCenter(startPoint);
        startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlay().clear();
        map.getOverlays().add(startMarker);
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
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    // Método que comprueba los permisos de ubicación y si los tiene le pide al usuario
    // activarlos
    public void ubicacion() {

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
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //Log.i("ubicacion",location.getLatitude() + " " + location.getLongitude());

        // Llama al método de actualizar la ubicación con el objeto location
        actualizarUbicacion(location);

        // Pide la actualización de la ubicación al objeto location manager cada 20 segundos, con 2 metros de distancia mínima
        // y usando el listener del location manager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,2,locationListener);
    }

    public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause(){
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }


}

