package mx.unam.dadm.modulo4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

public class InformativoActivity extends AppCompatActivity {

    private TextView tvTexto;
    private String strGenero;
    private String strUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informativo);

        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle parametros = getIntent().getExtras();
        String strTexto = parametros.getString("texto");
        strGenero = parametros.getString("genero");
        strUsuario = parametros.getString("correo");
        Log.i("texto", strTexto);

        tvTexto = (TextView) findViewById(R.id.tvTexto);

        if (strTexto.contentEquals("acerca")){

            tvTexto.setText(getResources().getString(R.string.ai_acercaDe));
        }
        if (strTexto.contentEquals("creditos")){
            tvTexto.setText(getResources().getString(R.string.ai_Creditos));

        }
        if (strTexto.contentEquals("ayuda")){

            tvTexto.setText(getResources().getString(R.string.ai_ayuda));
        }



    }

    public void regresar(){
        Intent intent = new Intent(InformativoActivity.this, DetalleActivity.class);
        intent.putExtra("correo", strUsuario);
        intent.putExtra("genero", strGenero);
        intent.putExtra("token", "informativo");
        startActivity(intent);
    }
}
