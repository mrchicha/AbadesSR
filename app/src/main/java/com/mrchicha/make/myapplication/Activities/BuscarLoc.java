package com.mrchicha.make.myapplication.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.mrchicha.make.myapplication.ImpCorredor.CorredorImpl;
import com.mrchicha.make.myapplication.Modelo.Corredor;
import com.mrchicha.make.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;


public class BuscarLoc extends AppCompatActivity {
    private MapView map = null;
    private RequestQueue requestQueue;
    private String dorsalLoc = "";

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        // Inflamos el mapa
        setContentView(R.layout.activity_act_localizacion);

        // Seleccionamos el mapa y el tipo de capa a mostrar
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.OpenTopo);

        // Dato que recibimos para determinar desde donde se llama al Activity
        int i = getIntent().getIntExtra("fragment",-1);

        // En función de donde venga la petición ejecuta un método con los parámetros correspondientes
        switch (i){
            case 0:
                // Abades stone
                cargaMap(37.15637,-4.2067,"http://www.2654420-1.web-hosting.es/abades433.kml",
                        getString(R.string.inicio) + " " + getString(R.string.Abades));
                break;
            case 1:
                // Media stone
                cargaMap( 37.160249, -4.189244,"http://www.2654420-1.web-hosting.es/mediastone.kml",
                        getString(R.string.inicio) + " " + getString(R.string.Media));
                break;
            case 2:
                // Mini stone
                cargaMap(37.171537,-4.154954,"http://www.2654420-1.web-hosting.es/abadesministonerace.kml",
                        getString(R.string.inicio) + " " + getString(R.string.Mini));
                break;
            case 3:
                // Km vertical
                cargaMap( 37.157919,-4.141789,"http://www.2654420-1.web-hosting.es/mediovertical.kml",getString(R.string.inicio) + " " + getString(R.string.Vertical));
                break;
            default:
                // Entrada a buscar dorsal
                pedirDorsal();
                break;
        }
    }

    // Método que realiza petición de corredor a la base de datos con un parámetro dorsal
    public void peticionVolley(final String dorsal){

        // Crear nueva cola de peticiones
        requestQueue= Volley.newRequestQueue(this);

        // Nueva petición de un corredor por su dorsal
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://2654420-1.web-hosting.es/select_id_01.php?dorsal=" + dorsal,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            // Sacamos el corredor del responde de la petición
                            Corredor corredor = CorredorImpl.corredorDorsal(response);

                            // Comprobamos si el dorsal está compartiendo su ubicación
                            // si es así cargamos el mapa con su latitud y longitud
                            if(corredor.getEnmovimiento().equalsIgnoreCase("si")){
                                Double lon = Double.parseDouble(corredor.getLongitud());
                                Double lat = Double.parseDouble(corredor.getLatitud());
                                cargaMap(lat,lon,"", getString(R.string.posicion_actual)+ " " + dorsal);
                            }
                            // Si el dorsal no está compartiendo ubicación avisamoa al usuario y
                            // volvemos a pedir nuevamente el dorsal
                            else {
                                Toast.makeText(getApplicationContext(),getString(R.string.nodorsal),Toast.LENGTH_LONG).show();
                                pedirDorsal();
                            }

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

    // Método que carga un mapa con los datos: latitud, longitud, ruta kml, nombre del punto
    public void cargaMap(Double latitud, Double longitud, String rutaKML, String datoPunto){

        // Definimos el controlador para el mapa y le añadimos las funcionalidad
        IMapController mapController = map.getController();
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController.setZoom(14);

        // Creamos un punto con la latitud y longitud passadas por parámetro y ceentramos
        // el mapa en el punto creado
        GeoPoint startPoint = new GeoPoint(latitud, longitud);
        mapController.setCenter(startPoint);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setIcon(getResources().getDrawable(R.drawable.marcadordeposicion));
        startMarker.setTitle(datoPunto);
        map.getOverlays().add(startMarker);

        // Cargamos el documento kml, si lo hay y lo añadimos al mapa
        KmlDocument kmlDocument = new KmlDocument();
        if(!rutaKML.equals("")){kmlDocument.parseKMLUrl(rutaKML);}

        FolderOverlay kmlOverlay = (FolderOverlay)kmlDocument.mKmlRoot.buildOverlay(map, null, null, kmlDocument);
        map.getOverlays().add(kmlOverlay);

        // Repintamos el mapa
        map.invalidate();
    }

    // Método que pide un dorsal en un dialog y lo pasa como parámetro al método que lo busca en la base de datos
    public void pedirDorsal(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dorsal_localizar));

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

    public void onResume(){
        super.onResume();
        map.onResume();
    }

    public void onPause(){
        super.onPause();
        map.onPause();
    }

    protected void onDestroy(){
        super.onDestroy();
    }
}

