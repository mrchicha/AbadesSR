package com.example.make.myapplication.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.make.myapplication.R;

public class MainActivity extends AppCompatActivity {

    private ImageView imgvLogo;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgvLogo = findViewById(R.id.imgvLogo);
        textView=findViewById(R.id.textView13);
        textView.setVisibility(View.INVISIBLE);

        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                cargarMenu();
            }

        }.start();

        new CountDownTimer( 2000,1000){
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                textView.setVisibility(View.VISIBLE);
            }
        }.start();
    }


    public void cargarMenu(){
        Intent i=new Intent(this,MenuLateral.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
