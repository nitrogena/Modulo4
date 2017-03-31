package mx.unam.dadm.modulo4;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;

import mx.unam.dadm.modulo4.basedatos.Sql;
import mx.unam.dadm.modulo4.datos.Usuario;

import static android.Manifest.permission.READ_CONTACTS;

public class AutenticaActivity extends AppCompatActivity implements View.OnClickListener {

    private AutoCompleteTextView actvCorreo;
    private EditText etContrasenia;
    private View pbProgress;
    private View svScroll;
    private Button btnIngresar;
    private IniciarSesion isAutentica = null;

    private TextView tvCuenta;

    private String strCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autentica);

        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);

        findViewById(R.id.btnIngresar).setOnClickListener(this);
        findViewById(R.id.btnLimpiar).setOnClickListener(this);
        findViewById(R.id.tvEnlaceRegistrar).setOnClickListener(this);

        svScroll = findViewById(R.id.svScroll);
        pbProgress = findViewById(R.id.pbProgress);

        mostrarPreferencia();

        /*if( getIntent().getBooleanExtra("salir", false)){
            finish();
        }*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnIngresar:
                autenticar(view);
                break;
            case R.id.btnLimpiar:
                limpiarCampos(view);
                break;
            case R.id.tvEnlaceRegistrar:
                crearCuenta(view);
                break;
        }
    }

    private void crearCuenta(View view) {
        Intent intent = new Intent(AutenticaActivity.this, CreaActivity.class);
        startActivity(intent);
    }

    public void limpiarCampos(View view){
        actvCorreo = (AutoCompleteTextView) findViewById(R.id.actvCorreo);
        //populateAutoComplete();
        etContrasenia = (EditText) findViewById(R.id.etContrasenia);

        actvCorreo.setText("");
        etContrasenia.setText("");
    }

    /*public boolean mostrarInicio(String strUsuarioA, String strContraseniaA){


        SharedPreferences spAutentica = getSharedPreferences("Autenticacion", Context.MODE_PRIVATE);
        String strUsuario = spAutentica.getString("usuario", "No existe usuario");
        String strContrasenia = spAutentica.getString("contrasenia", "No existe contrasenia");
        String strToken = spAutentica.getString("token", "No existe token");

        //TextView tvDesc = (TextView) findViewById(R.id.ar_tvDesc);
        if (strUsuarioA.equals(strUsuario)){
            if (strContraseniaA.equals(strContrasenia)){

                if (strToken.equals("No existe token")){
                    return true;
                }
                else{
                    //deve ir inicio

                    Intent intent = new Intent(this, DetalleActivity.class);
                    intent.putExtra("token", strToken);
                    startActivity(intent);
                }
            }
        }

        //Sql bdBase = new Sql(this);
        //return bdBase.existeUsuario(strUsuarioA);
        return false;
    }*/

    private void autenticar(View view) {
        actvCorreo = (AutoCompleteTextView) findViewById(R.id.actvCorreo);
        etContrasenia = (EditText) findViewById(R.id.etContrasenia);


        if (isAutentica != null) {
            return;
        }

        // Reset errors.
        actvCorreo.setError(null);
        etContrasenia.setError(null);

        // Store values at the time of the login attempt.
        strCorreo = actvCorreo.getText().toString();
        String strContrasenia = etContrasenia.getText().toString();

        boolean blBandera = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(strContrasenia) && !validarContrasenia(strContrasenia)) {
            etContrasenia.setError(getString(R.string.aa_msgErrorContrasenia));
            focusView = etContrasenia;
            blBandera = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(strCorreo)) {
            actvCorreo.setError(getString(R.string.aa_msgErrorRequerido));
            focusView = actvCorreo;
            blBandera = true;
        } else if (!validarCorreo(strCorreo)) {
            actvCorreo.setError(getString(R.string.aa_msgErrorCorreo));
            focusView = actvCorreo;
            blBandera = true;
        }

        if (blBandera) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            isAutentica = new IniciarSesion(strCorreo, strContrasenia);
            isAutentica.execute((Void) null);
        }
    }

    private boolean validarCorreo(String strCorreo) {
        //TODO: Replace this with your own logic
        Boolean blValida = false;
        if (strCorreo.contains("@")){
            if (strCorreo.contains(".")){
                if (strCorreo.length() > 5){
                    blValida = true;
                }
            }

        }
        return  blValida;

    }

    private boolean validarContrasenia(String strContrasenia) {
        //TODO: Replace this with your own logic
        return strContrasenia.length() > 6;
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_longAnimTime);

            svScroll.setVisibility(show ? View.GONE : View.VISIBLE);
            svScroll.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    svScroll.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            pbProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            pbProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    pbProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            pbProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            svScroll.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    public class IniciarSesion extends AsyncTask<Void, Void, Boolean> {

        private final String strUsuario;
        private final String strContrasenia;

        IniciarSesion(String strUsuarioA, String strContraseniaA) {
            strUsuario = strUsuarioA;
            strContrasenia = strContraseniaA;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            Boolean blRes = false;

            try {
                // Simulate network access.
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                return false;
            }
            //blRes = mostrarInicio(strUsuario, strContrasenia);
            Sql bdBase = new Sql(AutenticaActivity.this);
            blRes = bdBase.existeUsuario(strUsuario);

            //  register the new account here.
            return blRes;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            isAutentica = null;
            showProgress(false);

            if (success) {

                Sql bdBase = new Sql(AutenticaActivity.this);
                Usuario usuario = bdBase.obtenerUsuario(strUsuario);

                if (usuario.getPassword().equals(strContrasenia)){
                    Intent intent = new Intent(AutenticaActivity.this, DetalleActivity.class);
                    intent.putExtra("correo", strUsuario);
                    intent.putExtra("token", "autenticar");
                    intent.putExtra("genero", usuario.getGenero());
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), R.string.aa_msgErrorContra, Toast.LENGTH_LONG).show();
                }


            }
            else {

                Snackbar.make(etContrasenia, R.string.aa_preguntaRegistrar, Snackbar.LENGTH_INDEFINITE)
                        .setAction(android.R.string.ok, new View.OnClickListener() {
                            public void onClick(View v) {
                                //registrarUsuario(v);
                                crearCuenta(v);
                            }
                        })
                        .show();
                //etContrasenia.setError(getString(R.string.aa_msgError));
                //etContrasenia.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            isAutentica = null;
            showProgress(false);
        }
    }

    public void mostrarPreferencia(){
        SharedPreferences spAutentica = getSharedPreferences("Autenticacion", Context.MODE_PRIVATE);
        String strUsuario = spAutentica.getString("usuario", "No existe usuario");
        String strContrasenia = spAutentica.getString("contrasenia", "No existe contrasenia");
        String strToken = spAutentica.getString("token", "No existe token");

        actvCorreo = (AutoCompleteTextView) findViewById(R.id.actvCorreo);
        etContrasenia = (EditText) findViewById(R.id.etContrasenia);

        if (strUsuario != "No existe usuario") {
            actvCorreo.setText(strUsuario);
            etContrasenia.setText(strContrasenia);
        }

        //String strValores = "\nUsuario: " +strUsuario+ "\nContrasenia: " + strContrasenia;
    }

}

