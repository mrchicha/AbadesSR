package com.mrchicha.make.myapplication.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mrchicha.make.myapplication.R;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private Button btnConDorsal, btnSinDorsal;
    private TextView txtDorsal, txtDni, txtCorreo;
    private CheckBox chkRecordar;
    private SharedPreferences misPreferencias;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnConDorsal = findViewById(R.id.btnConDorsal);
        btnSinDorsal = findViewById(R.id.btnSinDorsal);
        btnSinDorsal.setOnClickListener(this);
        btnConDorsal.setOnClickListener(this);

        txtDni = findViewById(R.id.txtDni);
        txtDorsal = findViewById(R.id.txtdDorsal);
        txtCorreo = findViewById(R.id.txtCorreo);

        chkRecordar = findViewById(R.id.chkRecordar);

        misPreferencias = getSharedPreferences("misPreferencias",0);
        editor = misPreferencias.edit();

        if(misPreferencias.getBoolean("recordar",false)){
            txtDni.setText(misPreferencias.getString("dni",""));
            txtDorsal.setText(misPreferencias.getString("dorsal",""));
            txtCorreo.setText(misPreferencias.getString("correo",""));
            chkRecordar.setChecked(true);
        }
    }

    // Comprobamos que el usuario está acreditado en la base de datos
    public void comprobarUsuario(final View view){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://2654420-1.web-hosting.es/usuario.php" + "?dni=" + txtDni.getText() +
                "&dorsal=" + txtDorsal.getText() + "&correo=" + txtCorreo.getText();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        char a = response.charAt(0);
                        if(a=='1'){
                            cargarMenuCD(false,txtDorsal.getText().toString(), txtDni.getText().toString(),
                                    txtCorreo.getText().toString());
                        }
                        else {
                            Snackbar.make(view,"El usuario no esta registrado en la carrera",Snackbar.LENGTH_SHORT).show();
                            //Toast.makeText(getBaseContext(),""+ a,Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void cargarMenuCD(Boolean desactivar, String dorsal, String dni, String correo){
        Intent i = new Intent(this, MenuLateral.class);
        editor.putBoolean("desactivar",desactivar);
        editor.putBoolean("recordar",chkRecordar.isChecked());
        editor.putString("dorsal",dorsal);
        editor.putString("dni", dni);
        editor.putString("correo", correo);
        editor.commit();
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnConDorsal:
                comprobarUsuario(v);
                break;
            case R.id.btnSinDorsal:
                cargarMenuCD(true,"", "","");
                break;
        }
    }
}
