package mx.unam.dadm.modulo4;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import mx.unam.dadm.modulo4.basedatos.ConstantesBD;
import mx.unam.dadm.modulo4.basedatos.Sql;
import mx.unam.dadm.modulo4.datos.Usuario;

public class ModificaActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private AutoCompleteTextView actvCorreo;
    private EditText etContrasenia;
    private View pbProgress;
    private View svScroll;
    private Button btnIngresar;
    private IniciarSesionC isAutentica = null;

    private String strGenero = "no";
    private String strUsuario;
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica);

        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);

        Bundle parametros = getIntent().getExtras();
        strUsuario = parametros.getString("correo");

        //mostrarPreferencia();

        findViewById(R.id.btnIngresar).setOnClickListener(this);
        findViewById(R.id.btnLimpiar).setOnClickListener(this);

        svScroll = findViewById(R.id.svScroll);
        pbProgress = findViewById(R.id.pbProgress);

        spinner = (Spinner) findViewById(R.id.spinGenero);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genero_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        mostrarPreferencia(adapter);
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
        }
    }

    public void limpiarCampos(View view){
        actvCorreo = (AutoCompleteTextView) findViewById(R.id.actvCorreo);
        etContrasenia = (EditText) findViewById(R.id.etContrasenia);
        //actvCorreo.setText("");
        //etContrasenia.setText("");
        finish();
    }


    public void  modificarUsuario(){
        try{

            //SE DEBEN MODIFICAR LOS DATOS DEL USUARIO
            actvCorreo = (AutoCompleteTextView) findViewById(R.id.actvCorreo);
            etContrasenia = (EditText) findViewById(R.id.etContrasenia);


            String strUsuario = actvCorreo.getText().toString();
            String strContrasenia = etContrasenia.getText().toString();

            SharedPreferences spAutentica = getSharedPreferences("Autenticacion", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = spAutentica.edit();

            editor.putString("usuario", strUsuario);
            editor.putString("contrasenia", strContrasenia);
            editor.commit();

           /*BASE DE DATOS*/
            Sql bdBase = new Sql(this);

            ContentValues cvValues = new ContentValues();
            cvValues.put(ConstantesBD.TABLE_POS_PASSWORD,strContrasenia);
            cvValues.put(ConstantesBD.TABLE_POS_GENDER, strGenero);
            bdBase.modificarUsuario(cvValues, strUsuario);
            /**/

            Toast.makeText(this, R.string.ra_mensajeModificacion, Toast.LENGTH_LONG).show();


            Intent intent = new Intent(ModificaActivity.this, DetalleActivity.class);
            intent.putExtra("correo", strUsuario);
            intent.putExtra("genero", strGenero);
            intent.putExtra("token", "modificacion");
            startActivity(intent);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, R.string.ra_mensajeE, Toast.LENGTH_SHORT).show();
        }
    }



    public void mostrarPreferencia(ArrayAdapter<CharSequence> adapter){

        /*SharedPreferences spAutentica = getSharedPreferences("Autenticacion", Context.MODE_PRIVATE);
        String strUsuario = spAutentica.getString("usuario", "No existe usuario");
        String strContrasenia = spAutentica.getString("contrasenia", "No existe contrasenia");
        String strToken = spAutentica.getString("token", "No existe token");*/

        /*BASE DE DATOS*/
        Sql bdBase = new Sql(this);
        Usuario usuario =  bdBase.obtenerUsuario(strUsuario);
        String strContrasenia = usuario.getPassword();
        strGenero = usuario.getGenero();


        actvCorreo = (AutoCompleteTextView) findViewById(R.id.actvCorreo);
        etContrasenia = (EditText) findViewById(R.id.etContrasenia);
        spinner = (Spinner) findViewById(R.id.spinGenero);

        actvCorreo.setText(strUsuario);
        etContrasenia.setText(strContrasenia);
        //spinner.setSelection(getIndex(spinner, strGenero));

        for(int i=0; i < adapter.getCount(); i++) {
            if(strGenero.trim().equals(adapter.getItem(i).toString())){
                spinner.setSelection(i);
                break;
            }
        }

        actvCorreo.setEnabled(false);
    }

    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        //Toast.makeText(this, index , Toast.LENGTH_SHORT).show();
        return index;
    }

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
        String strCorreo = actvCorreo.getText().toString();
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
            isAutentica = new IniciarSesionC(strCorreo, strContrasenia);
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
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

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

    public class IniciarSesionC extends AsyncTask<Void, Void, Boolean> {

        private final String strUsuario;
        private final String strContrasenia;

        IniciarSesionC(String strUsuarioA, String strContraseniaA) {
            strUsuario = strUsuarioA;
            strContrasenia = strContraseniaA;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            isAutentica = null;
            showProgress(false);

            if (success) {

                modificarUsuario();

            }
        }

        @Override
        protected void onCancelled() {
            isAutentica = null;
            showProgress(false);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        strGenero = parent.getItemAtPosition(position).toString();
        int postition1 = parent.getFirstVisiblePosition();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + strGenero, Toast.LENGTH_LONG).show();
        Toast.makeText(parent.getContext(), "Selected: " + postition1, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
        strGenero = "no";
    }

    /**

     @Override
     public void onNothingSelected(AdapterView<?> parent) {

     }*/
}
