package com.example.red;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.entity.FileEntity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import cz.msebera.android.httpclient.Header;

public class Descarga extends AppCompatActivity implements View.OnClickListener {
    EditText texto;
    Button botonImagen,botonDesFich;
    ImageView imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descarga);
        texto = (EditText) findViewById(R.id.edtUrlImagenDes);
        botonImagen = (Button) findViewById(R.id.btnDesImagen);
        botonImagen.setOnClickListener(this);
        imagen = (ImageView) findViewById(R.id.imgDes);
        botonDesFich = (Button) findViewById(R.id.btnDesFichero);
        botonDesFich.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        String url = texto.getText().toString();
        if (v == botonImagen)
            descargaImagen(url);
        if(v==botonDesFich)
            descargaFichero(url);
    }
    private void descargaImagen(String url) {
        Picasso.with(this).load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder_error)
                .into(imagen);
        //utilizar OkHttp3
    }

    public void descargaFichero(final String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        imagen.setImageResource(R.drawable.placeholder);
        final ProgressDialog progresoso = new ProgressDialog(this);
        File miFichero = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(),url.substring(url.lastIndexOf('/')));

        client.get(url, new FileAsyncHttpResponseHandler(miFichero) {
            @Override
            public void onStart() {
                super.onStart();
                progresoso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progresoso.setMessage("Conectado ...");
                progresoso.setCancelable(false);
                progresoso.show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                progresoso.dismiss();
                Toast.makeText(Descarga.this,"Se fallo las descarga",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                progresoso.dismiss();
                Toast.makeText(Descarga.this,"Se descargar correctamente",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
