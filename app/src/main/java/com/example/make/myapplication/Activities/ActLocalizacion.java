package com.example.make.myapplication.Activities;

import android.content.Context;
import android.net.Uri;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.make.myapplication.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ActLocalizacion extends AppCompatActivity {
    MapView map = null;
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
                cargaMap(37.15637,-4.2067,"http://www.2654420-1.web-hosting.es/abadesstone.kml");
                break;
            case 1:
                cargaMap(37.171537,-4.154954,"http://www.2654420-1.web-hosting.es/abadesministonerace.kml");
                break;
            case 2:
                cargaMap(37.171537,-4.154954,"http://www.2654420-1.web-hosting.es/abadesministonerace.kml");
                break;
            case 3:
                cargaMap(37.171537,-4.154954,"http://www.2654420-1.web-hosting.es/abadesministonerace.kml");
                break;
            default:
                cargaMap(37.143051, -4.073414,"");
                break;
        }
    }

    public void cargaMap(Double latitud, Double longitud, String rutaKML){


        IMapController mapController = map.getController();
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        mapController.setZoom(16);
        GeoPoint startPoint = new GeoPoint(latitud, longitud);
        mapController.setCenter(startPoint);


        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        //startMarker.setIcon(getResources().getDrawable(R.drawable.placeholder));
        //startMarker.setTitle("Inicio carrera");
        map.getOverlays().add(startMarker);


        //File file=new File("C:\\Users\\make\\Desktop\\abadesstone.kml");
        //Uri path = Uri.fromFile(new File(("///android_asset/abadesstone.kml"));
        //String newPath = path.toString(path);

        //File file = new File(("///android_asset/abadesstone.kml"));


        //File file = new File("file:///android_asset/abadesstone.kml");
        KmlDocument kmlDocument = new KmlDocument();
        if(!rutaKML.equals(""))
        {
            //kmlDocument.parseKMLFile(file);
            kmlDocument.parseKMLUrl(rutaKML);
        }


        FolderOverlay kmlOverlay = (FolderOverlay)kmlDocument.mKmlRoot.buildOverlay(map, null, null, kmlDocument);
        map.getOverlays().add(kmlOverlay);

        map.invalidate();
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

