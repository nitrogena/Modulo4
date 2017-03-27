package mx.unam.dadm.modulo4;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.util.Random;


public class DetalleActivity extends AppCompatActivity {

    TextView tvNombre;
    TextView tvTelefono;
    TextView tvCorreo;
    ImageView ivFoto;

    ImageButton ibContactar;
    ImageButton ibOcultar;
    ImageButton ibPreguntar;

    private static final int MY_PERMISSIONS_REQUEST_CALL = 3;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;

    private static final Random random = new Random();

    private static final Integer[] imagenesID = {
            R.drawable.deportista_en_silla_de_ruedas_48,
            R.drawable.enamorado_48,
            R.drawable.golf_48,
            R.drawable.hombre_de_jengibre_48,
            R.drawable.hombre_de_negocios_48,
            R.drawable.mujer_de_negocios_48,
            R.drawable.mujer_tur_stica_48,
            R.drawable.pelo_mujer_48,
            R.drawable.persona_de_edad_avanzada_48,
            R.drawable.persona_de_sexo_masculino_48,
            R.drawable.persona_femenina_48
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);

       /*   VER SI SE PONE
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle parametros = getIntent().getExtras();
        String nombre = parametros.getString("nombre");
        String telefono = parametros.getString("telefono");
        String correo = parametros.getString("correo");


        int foto = parametros.getInt("foto");
        */

        tvNombre = (TextView) findViewById(R.id.tvNombre);
        tvTelefono = (TextView) findViewById(R.id.tvTel);
        tvCorreo = (TextView) findViewById(R.id.tvCorreo);

        ivFoto = (ImageView) findViewById(R.id.ivFoto);

        /*
        Log.i("telefono", telefono);

        tvNombre.setText(nombre);
        tvCorreo.setText(correo);
        tvTelefono.setText(telefono);

        ivFoto.setImageResource(foto);
    */

        //contactar();


        int resource = imagenesID[random.nextInt(imagenesID.length)];
        ivFoto.setImageResource(resource);
    }

    public void llamarTel(View view) {
        String telefono = tvTelefono.getText().toString();
        //Intent implicito


        Log.i("telefonoLlamar", telefono);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL);

            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL);
            }
            return;
        }
        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telefono)));

        /*Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + telefono ));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        }
        */

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //External storage activado
                    Toast.makeText(this, R.string.mensaje2_camara, Toast.LENGTH_SHORT).show();

                } else {
                    //¿sin permiso external storage?
                    Toast.makeText(this, R.string.mensaje3_camara, Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //External storage activado
                    Toast.makeText(this, R.string.mensaje2_camara, Toast.LENGTH_SHORT).show();

                } else {
                    //¿sin permiso external storage?
                    Toast.makeText(this, R.string.mensaje3_camara, Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //camara activado
                    Toast.makeText(this, R.string.mensaje4_camara, Toast.LENGTH_SHORT).show();
                } else {
                    //¿sin permiso camara?
                    Toast.makeText(this, R.string.mensaje5_camara, Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }


    public void enviarCorreo(View view) {
        String correo = tvCorreo.getText().toString();
        Intent itCorreo = new Intent((Intent.ACTION_SEND));
        itCorreo.setData(Uri.parse("mailto:"));
        itCorreo.putExtra(Intent.EXTRA_EMAIL, correo);
        itCorreo.setType("message/rfc822");
        startActivity(Intent.createChooser(itCorreo, "Correo-e"));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent itKd = new Intent(this, AutenticaActivity.class);
            startActivity(itKd);
        }
        return super.onKeyDown(keyCode, event);
    }



    public void verMenuPopup(View view) {
        ivFoto = (ImageView) view.findViewById(R.id.ivFoto);
        PopupMenu pmPopupMenu = new PopupMenu(this, ivFoto);
        pmPopupMenu.getMenuInflater().inflate(R.menu.menu_popup, pmPopupMenu.getMenu());

        /*camara empieza*/
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        //revisar el estatus del permiso
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {

            //Soliicitar permiso
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)){

                //El permiso ya fue otorgado previamente
                Toast.makeText(this, R.string.mensaje1_camara, Toast.LENGTH_SHORT).show();

            }else{

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        }
        if (permissionCheck2 != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                //El permiso ya fue otorgado previamente
                Toast.makeText(this, R.string.mensaje1_camara, Toast.LENGTH_SHORT).show();

            }else{

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

            }
        }



        /*camara finaliza*/

        pmPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mpuEditarFotoCam:
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.mpu_Editar), Toast.LENGTH_LONG).show();

                        /*camara empieza*/

                        //Creamos el Intent para llamar a la Camara
                        Intent cameraIntent = new Intent(
                                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        //Creamos una carpeta en la memeria del terminal
                        File imagesFolder = new File(
                                Environment.getExternalStorageDirectory(), "PrototEmplea");
                        imagesFolder.mkdirs();
                        //añadimos el nombre de la imagen
                        File image = new File(imagesFolder, "fotoOrg.jpg");
                        Uri uriSavedImage = Uri.fromFile(image);
                        //Le decimos al Intent que queremos grabar la imagen
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                        //Lanzamos la aplicacion de la camara con retorno (forResult)
                        startActivityForResult(cameraIntent, 1);

                        /*camara finaliza*/


                        break;
                    case R.id.mpuEditarFotoGal:
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.mpu_Editar), Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });


        pmPopupMenu.show();
    }

    /*camara empieza*/
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Comprovamos que la foto se a realizado
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Creamos un bitmap con la imagen recientemente
            //almacenada en la memoria
            Bitmap bMap = BitmapFactory.decodeFile(
                    Environment.getExternalStorageDirectory()+
                            "/PrototEmplea/"+"fotoOrg.jpg");
            //Añadimos el bitmap al imageView para
            //mostrarlo por pantalla
            ivFoto.setImageBitmap(bMap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_opcion_detalle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.modEditar:
                Intent intent3 = new Intent(DetalleActivity.this, CreaActivity.class);
                intent3.putExtra("editar", true);
                startActivity(intent3);
                break;
            case R.id.modEliminar:
                //refrescar();
                break;
            case R.id.moAcerca:
                mostrarInformativo("acerca");
                break;
            case R.id.moCreditos:
                mostrarInformativo("creditos");
                break;
            case R.id.modSalir:
                Intent intent = new Intent(DetalleActivity.this, AutenticaActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("salir", true);
                startActivity(intent);
                finish();
                break;
            case R.id.movEditar:
                //refrescar();
                break;
            case R.id.movEliminar:
                //refrescar();
                break;
            case R.id.movSalir:
                Intent intent2 = new Intent(DetalleActivity.this, AutenticaActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent2.putExtra("salir", true);
                startActivity(intent2);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void mostrarInformativo(String strOpcion){

        Intent intent = new Intent(this, InformativoActivity.class);
        intent.putExtra("texto", strOpcion);
        startActivity(intent);
    }

    /*Se usaron imagenes de
    <a href="https://es.icons8.com/web-app/34104/Persona-femenina">Persona femenina créditos de icono</a> */

}

