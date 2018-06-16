package com.example.make.myapplication.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.make.myapplication.ImpCorredor.CorredorImpl;
import com.example.make.myapplication.Modelo.Corredor;
import com.example.make.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;


public class ActivarLoc extends AppCompatActivity {

    private MapView map = null;

    private IMapController mapController;
    private Marker startMarker;
    private GeoPoint startPoint;
    private LocationManager locationManager;
    private String dorsalLoc;
    private boolean gpsActivo;
    static final int MI_RESULTADO = 0;
    private static RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //handle permissions first, before map is created. not depicted here


        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        //inflate and create the map
        setContentView(R.layout.activity_loc_dorsal);

        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.OpenTopo);

        mapController = map.getController();
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController.setZoom(14);

        pedirDorsal();

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

            // Pide la actualización de la ubicación al objeto location manager cada 10 segundos, con 20 metros de distancia mínima
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
                    //get gps
                }
            });
            dialog.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {

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
            updateEnMovimiento(dorsalLoc,"SI");
            updateLocalizacion(dorsalLoc,lon, lat);
        } else agregarMarker(37.143051, -4.073414);
    }

    // Método que recibe una latitud y una longitud y crea un geopoint
    // centra el mapa en el geopoint y determina las opciones del overlay del mapa
    public void agregarMarker(Double lat, Double lon) {
        startPoint = new GeoPoint(lat, lon);

        mapController.setCenter(startPoint);
        startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setIcon(getResources().getDrawable(R.drawable.marcadordeposicion));
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

    // Método que actualiza un corredor y lo pone a compartir ubicación seleccionado por su dorsal
    public void updateEnMovimiento(String dorsal, String enmov){
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

    // Método que pide un dorsal en un dialog y lo pasa como parámetro al método que lo busca en la base de datos
    public void pedirDorsal(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dorsal_ubicacion));

        // EdirText que recoje el texto
        final EditText input = new EditText(this);
        // Tipo de entrada de datos
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        // Configuramos los botones aceptar / cancelar
        builder.setPositiveButton(getString(R.string.aceptar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dorsalLoc = input.getText().toString();
                peticionVolley(dorsalLoc);

            }
        });
        builder.setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });

        builder.show();
    }

    // Método que realiza petición de corredor a la base de datos con un parámetro dorsal
    public void peticionVolley(final String dorsal){

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

                            updateEnMovimiento(corredor.getDorsal(),"SI");
                            ubicacion();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Log", "Error Respuesta en JSON: " + error.getMessage());

                        // Si la consulta devuelve un error al pedir un dorsal no existente
                        Toast.makeText(getApplicationContext(),getString(R.string.nohaydorsal),Toast.LENGTH_LONG).show();
                        pedirDorsal();
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
        // Al salir cambiamos el estado de compartir ubicación del dorsal de si a no
        updateEnMovimiento(dorsalLoc,"NO");
    }
}

