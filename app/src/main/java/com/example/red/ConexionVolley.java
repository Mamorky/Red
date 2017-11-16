package com.example.red;

import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;

public class ConexionVolley extends AppCompatActivity implements View.OnClickListener{

    EditText direccion;
    Button conectar;
    WebView web;
    TextView tiempo;
    long fin;
    long inicio;
    RequestQueue mRequestQueue;

    public static final String TAG = "MyTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_volley);
        iniciar();
    }

    private void iniciar(){
        direccion = (EditText) findViewById(R.id.direccion);
        conectar = (Button) findViewById(R.id.btnConectar);
        conectar.setOnClickListener(this);
        web = (WebView) findViewById(R.id.web);
        tiempo = (TextView) findViewById(R.id.txvTiempo);
        //StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        mRequestQueue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
    }

    @Override
    public void onClick(View view) {
        String url;
        if (view == conectar) {
            inicio = System.currentTimeMillis();
            url = direccion.getText().toString();
            makeRequest(url);
        }
    }

    public void makeRequest(String url) {
        final String enlace = url;
        // Instantiate the RequestQueue.
        // mRequestQueue = Volley.newRequestQueue(this);
        final ProgressDialog progreso = new ProgressDialog(ConexionVolley.this);

        progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progreso.setMessage("Conectando . . .");
        //progreso.setCancelable(false);
        progreso.setOnCancelListener(new DialogInterface.OnCancelListener(){
            public void onCancel(DialogInterface dialog){
                mRequestQueue.cancelAll(TAG);
            }
        });
        progreso.show();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fin = System.currentTimeMillis();
                        progreso.dismiss();
                        web.loadDataWithBaseURL(enlace,response,"text/html","utf-8",null);
                        tiempo.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        fin = System.currentTimeMillis();
                        String mensaje = "Error";
                        if (error instanceof TimeoutError || error instanceof NoConnectionError)
                            mensaje = "Timeout Error: " + error.getMessage();
                        else {
                            NetworkResponse errorResponse = error.networkResponse;
                            if (errorResponse != null && errorResponse.data != null)
                                try {
                                    mensaje = "Error: " + errorResponse.statusCode + " " + "\n" + new
                                            String(errorResponse.data, "UTF-8");
                                    Log.e("Error", mensaje);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                    mensaje = "Error sin informacion";
                                }
                        }
                        web.loadDataWithBaseURL(null,mensaje,"text/html","UTF-8",null);
                        tiempo.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");
                        progreso.dismiss();
                    }
                });
        // Set the tag on the request.
        stringRequest.setTag(TAG);
        // Set retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(3000, 1, 1));
        // Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }
}
