package com.example.make.myapplication.ImpCorredor;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.make.myapplication.Modelo.Corredor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CorredorImpl {

    private static RequestQueue requestQueue;
    static Corredor corredor;


    public static ArrayList<Corredor> corredoresLista(){
        ArrayList<Corredor> corredores = new ArrayList<>();




        return corredores;
    }

    public static Corredor obtenerCorredor(String dorsal, Activity activity){


        // Crear nueva cola de peticiones
        requestQueue= Volley.newRequestQueue(activity);

        // Nueva petición JSONObject
        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://2654420-1.web-hosting.es/select_id_01.php?dorsal=" + dorsal,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            corredor = corredorDorsal(response);

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


        return corredor;
    }

    public static Corredor corredorDorsal(JSONObject jsonObject) throws JSONException {
        Corredor corredor = new Corredor();

        corredor.setDorsal(jsonObject.getString("dorsal"));
        corredor.setModalidad(jsonObject.getString("modalidad"));
        corredor.setNombre(jsonObject.getString("nombre"));
        corredor.setApellidos(jsonObject.getString("apellidos"));
        corredor.setEmail(jsonObject.getString("email"));
        corredor.setFechanacimiento(jsonObject.getString("fechanacimiento"));
        corredor.setTipodocumento(jsonObject.getString("tipodocumento"));
        corredor.setDni(jsonObject.getString("dni"));
        corredor.setCodigopostal(jsonObject.getString("codigopostal"));
        corredor.setLocalidad(jsonObject.getString("localidad"));
        corredor.setProvincia(jsonObject.getString("provincia"));
        corredor.setGenero(jsonObject.getString("genero"));
        corredor.setTelefono(jsonObject.getString("telefono"));
        corredor.setFederado(jsonObject.getString("federado"));
        corredor.setClub(jsonObject.getString("club"));
        corredor.setKmvertical(jsonObject.getString("kmvertical"));
        corredor.setEnmovimiento(jsonObject.getString("enmovimiento"));
        corredor.setLatitud(jsonObject.getString("latitud"));
        corredor.setLongitud(jsonObject.getString("longitud"));
        corredor.setClasificacion(jsonObject.getString("clasificacion"));


        return corredor;
    }
    public static void actualizarPosicionamineto(String dorsal, String latitud, String longitud){

    }
}
