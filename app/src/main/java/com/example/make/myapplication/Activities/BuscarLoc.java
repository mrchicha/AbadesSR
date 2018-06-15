package com.example.make.myapplication.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

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
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;

import static com.example.make.myapplication.ImpCorredor.CorredorImpl.corredorDorsal;


public class BuscarLoc extends AppCompatActivity {
    MapView map = null;
    private RequestQueue requestQueue;
    private String dorsalLoc = "";

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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
        setContentView(R.layout.activity_act_localizacion);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.OpenTopo);
        //cargaMap(37.143051, -4.073414,"http://www.2654420-1.web-hosting.es/abadesstone.kml");
        int i = getIntent().getIntExtra("fragment",-1);

        switch (i){
            case 0:
                cargaMap(37.15637,-4.2067,"http://www.2654420-1.web-hosting.es/abades433.kml", "Inicio Abades stone");
                break;
            case 1:
                cargaMap( 37.160249, -4.189244,"http://www.2654420-1.web-hosting.es/mediastone.kml", "Inicio Media stone");
                break;
            case 2:
                cargaMap(37.171537,-4.154954,"http://www.2654420-1.web-hosting.es/abadesministonerace.kml", "Inicio Mini stone");
                break;
            case 3:
                cargaMap( 37.157919,-4.141789,"http://www.2654420-1.web-hosting.es/mediovertical.kml","Inicio KM vertical");
                break;
            default:

                pedirDorsal();






                break;
        }
    }

    public void peticionVolley(String dorsal){
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
                            Corredor corredor = corredorDorsal(response);
                            Double lon = Double.parseDouble(corredor.getLongitud());
                            Double lat = Double.parseDouble(corredor.getLatitud());

                            cargaMap(lat,lon,"", "Posición actual dorsal: 1001");
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

    public void cargaMap(Double latitud, Double longitud, String rutaKML, String datoPunto){


        IMapController mapController = map.getController();
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController.setZoom(14);
        GeoPoint startPoint = new GeoPoint(latitud, longitud);
        mapController.setCenter(startPoint);


        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setIcon(getResources().getDrawable(R.drawable.marcadorp));
        startMarker.setTitle(datoPunto);
        map.getOverlays().add(startMarker);

        KmlDocument kmlDocument = new KmlDocument();
        if(!rutaKML.equals(""))
        {
            kmlDocument.parseKMLUrl(rutaKML);
        }

        FolderOverlay kmlOverlay = (FolderOverlay)kmlDocument.mKmlRoot.buildOverlay(map, null, null, kmlDocument);
        map.getOverlays().add(kmlOverlay);

        map.invalidate();
    }

    public Corredor obtenerCorredor(String dorsal){
        Corredor corredor = CorredorImpl.obtenerCorredor(dorsal,this);

        return corredor;
    }

    public void cargarUbucacionDorsal(Corredor corredor){

    }

    public void pedirDorsal(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Indique el dorsal que quiere localizar");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dorsalLoc = input.getText().toString();

                peticionVolley(dorsalLoc);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }
}

