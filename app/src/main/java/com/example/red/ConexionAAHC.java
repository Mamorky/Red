package com.example.red;

import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.DialogInterface;
import android.os.StrictMode;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class ConexionAAHC extends AppCompatActivity implements View.OnClickListener{

    EditText direccion;
    Button conectar;
    WebView web;
    TextView tiempo;
    long fin;
    long inicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_aahc);

        iniciar();

    }

    private void iniciar(){
        direccion = (EditText) findViewById(R.id.direccion);
        conectar = (Button) findViewById(R.id.btnConectar);
        conectar.setOnClickListener(this);
        web = (WebView) findViewById(R.id.web);
        tiempo = (TextView) findViewById(R.id.txvTiempo);
        //StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }

    private void AAHC() {
        final String texto = direccion.getText().toString();
        final ProgressDialog progreso = new ProgressDialog(ConexionAAHC.this);
        inicio = System.currentTimeMillis();
        RestClient.get(texto, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
                progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progreso.setMessage("Conectando . . .");
                //progreso.setCancelable(false);
                progreso.setOnCancelListener(new DialogInterface.OnCancelListener(){
                    public void onCancel(DialogInterface dialog){
                        RestClient.cancelRequests(getApplicationContext(), true);
                    }
                });
                progreso.show();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String response) {
                // called when response HTTP status is "200 OK"
                fin = System.currentTimeMillis();
                progreso.dismiss();
                web.loadDataWithBaseURL(null, response,"text/html", "UTF-8", null);
                tiempo.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");
            }
            

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                fin = System.currentTimeMillis();
                progreso.dismiss();
                web.loadDataWithBaseURL(null, "Error "+ statusCode +" "+response+" "+t.getMessage().toString(),"text/html", "UTF-8", null);
                tiempo.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == conectar)
            AAHC();
    }
}
