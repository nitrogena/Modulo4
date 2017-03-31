package mx.unam.dadm.modulo4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

public class InformativoActivity extends AppCompatActivity {

    // TextView tvAcercaDe;
    // TextView tvCreditos;
    TextView tvTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informativo);

        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle parametros = getIntent().getExtras();
        String strTexto = parametros.getString("texto");
        Log.i("texto", strTexto);

        /*tvAcercaDe = (TextView) findViewById(R.id.tvAcercaDe);
        tvCreditos = (TextView) findViewById(R.id.tvCreditos);*/

        tvTexto = (TextView) findViewById(R.id.tvTexto);

        /*if (strTexto == "acerca"){
            tvCreditos.setText("");
            tvAcercaDe.setText(R.string.ai_acercaDe);
        }
        if (strTexto == "creditos"){
            tvCreditos.setText(R.string.ai_Creditos);
            tvAcercaDe.setText("");
        }*/

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
}
