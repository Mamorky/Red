package com.example.red;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnHTTP,btnAsincrona,boton3,boton4,btnExplorar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnHTTP = (Button)findViewById(R.id.btnHttp);
        btnHTTP.setOnClickListener(this);
        btnAsincrona = (Button)findViewById(R.id.btnAsincrona);
        btnAsincrona.setOnClickListener(this);
        boton3 = (Button)findViewById(R.id.button3);
        boton3.setOnClickListener(this);
        boton4 = (Button)findViewById(R.id.button4);
        boton4.setOnClickListener(this);
        btnExplorar = (Button)findViewById(R.id.btnExplorar);
        btnExplorar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;

        if(view==btnHTTP){
            i=new Intent(this,ConexionHTTP.class);
            startActivity(i);
        }

        if(view==btnAsincrona){
            i=new Intent(this,ConexionAsincrona.class);
            startActivity(i);
        }
        /*
        if(view==boton3){
            i=new Intent(this,CodificacionActivity.class);
            startActivity(i);
        }

        if(view==btnExplorar){
            i=new Intent(this,Exploracion.class);
            startActivity(i);
        }
        /**if(view==boton4){
         i=new Intent(this,EscribirInterna.class);
         startActivity(i);
         }*/
    }
}
