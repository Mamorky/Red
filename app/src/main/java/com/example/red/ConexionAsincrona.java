package com.example.red;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class ConexionAsincrona extends AppCompatActivity implements View.OnClickListener{

    EditText direccion;
    RadioButton radioJava,radioApache;
    Button conectar;
    WebView web;
    TextView tiempo;
    long inicio, fin;
    public static final String JAVA = "Java";
    public static final String APACHE = "Apache";
    TareaAsincrona tareaAsincrona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conexion_asincrona);
        iniciar();
    }

    private void iniciar() {
        direccion = (EditText) findViewById(R.id.direccion);
        radioJava = (RadioButton) findViewById(R.id.rdbJava);
        radioApache = (RadioButton) findViewById(R.id.rdbApache);
        conectar = (Button) findViewById(R.id.btnConectar);
        conectar.setOnClickListener(this);
        web = (WebView) findViewById(R.id.web);
        tiempo = (TextView) findViewById(R.id.txvTiempo);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
    }

    @Override
    public void onClick(View v) {
        tareaAsincrona = new TareaAsincrona(this);
        if(v == conectar){
            Resultado resultado;
            if(radioJava.isChecked())
                tareaAsincrona.execute(direccion.getText().toString(),JAVA);
            else
                tareaAsincrona.execute(direccion.getText().toString(),APACHE);
        }
    }

    public class TareaAsincrona extends AsyncTask<String, Integer, Resultado > {
        private ProgressDialog progreso;
        private Context context;
        public TareaAsincrona(Context context){
            this.context = context;
        }
        protected void onPreExecute() {
            progreso = new ProgressDialog(context);
            progreso.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progreso.setMessage("Conectando . . .");
            progreso.setCancelable(
                    true
            );
            progreso.setOnCancelListener(new DialogInterface.OnCancelListener(){
                public void onCancel(DialogInterface dialog){
                    TareaAsincrona.this.cancel(true);
                }
            });
            progreso.show();
        }
        protected Resultado doInBackground(String... cadena) {
            Resultado resultado = new Resultado();
            int i = 1;
            try {
                inicio = System.currentTimeMillis();
                if (cadena[1].equals(JAVA))
                    resultado = Conexion.conectarJava(cadena[0]);
                else
                    resultado = Conexion.conectarApache(cadena[0]);

                publishProgress(i++);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultado;
        }
        protected void onProgressUpdate(Integer... progress) {
            progreso.setMessage("Conectando " + Integer.toString(progress[0]));
        }
        protected void onPostExecute(Resultado resultado) {
            fin = System.currentTimeMillis();
            if (resultado.getCodigo())
                web.loadDataWithBaseURL(null, resultado.getContenido(),"text/html", "UTF-8", null);
            else
                web.loadDataWithBaseURL(null, resultado.getMensaje(),"text/html", "UTF-8", null);
            tiempo.setText("Duración: " + String.valueOf(fin - inicio) + " milisegundos");
            progreso.dismiss();
        }
        protected void onCancelled()
        {
            progreso.dismiss();
        }
    }
}
