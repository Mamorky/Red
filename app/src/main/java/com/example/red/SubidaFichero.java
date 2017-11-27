package com.example.red;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mamorky on 23/11/17.
 */

public class SubidaFichero extends AppCompatActivity implements View.OnClickListener{

    Button btnSubir;
    EditText edtFichero;
    public final static String WEB = "http://192.168.2.16/html/upload.php";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subida);
        edtFichero = (EditText) findViewById(R.id.edtFicheroSubir);
        btnSubir = (Button) findViewById(R.id.btnSubir);
        btnSubir.setOnClickListener(this);
    }

    private void subida() {
        String fichero = edtFichero.getText().toString();
        final ProgressDialog progreso = new ProgressDialog(SubidaFichero.this);
        File myFile;
        Boolean existe = true;
        myFile = new File(Environment.getExternalStorageDirectory(), fichero);
        //File myFile = new File("/path/to/file.png");
        RequestParams params = new RequestParams();
        try {
            params.put("fileToUpload", myFile);
        } catch (FileNotFoundException e) {
            existe = false;
            Toast.makeText(SubidaFichero.this,"Error en el fichero: " + e.getMessage(),Toast.LENGTH_SHORT).show();
            //Toast.makeText(this, "Error en el fichero: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        if (existe)
            RestClient.post(WEB, params, new TextHttpResponseHandler() {
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
                    progreso.dismiss();
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String response, Throwable t) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    progreso.dismiss();
                }
            });
    }

    @Override
    public void onClick(View v) {
        if(v==btnSubir)
            subida();
    }
}
