package mx.unam.dadm.modulo4.basedatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import mx.unam.dadm.modulo4.datos.Usuario;

/**
 * Created by USUARIO on 29/03/2017.
 */

public class Sql extends SQLiteOpenHelper {

    private Context contexto;

    public Sql(Context contexto) {
        super(contexto, ConstantesBD.BASE_DATOS, null, ConstantesBD.VERSION_BD);
        this.contexto = contexto;
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String queryCrearTablaPos = "CREATE TABLE " + ConstantesBD.TABLE_NAME_POS + "(" +
                ConstantesBD.TABLE_POS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ConstantesBD.TABLE_POS_NAME + " TEXT, " +
                ConstantesBD.TABLE_POS_TEL + " TEXT, " +
                ConstantesBD.TABLE_POS_EMAIL + " TEXT, " +
                ConstantesBD.TABLE_POS_DESCRPTION + " TEXT, " +
                ConstantesBD.TABLE_POS_PASSWORD + " TEXT, " +
                ConstantesBD.TABLE_POS_GENDER + " TEXT, " +
                ConstantesBD.TABLE_POS_PHOTO + " INTEGER " +
                ")";
        sqLiteDatabase.execSQL(queryCrearTablaPos);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + ConstantesBD.TABLE_NAME_POS);

        onCreate(sqLiteDatabase);


    }

    public void insertarUsuario(ContentValues contentValues){
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(ConstantesBD.TABLE_NAME_POS, null, contentValues);
        db.close();
    }

    public int modificarUsuario(Usuario usuario) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cvValues = new ContentValues();
        cvValues.put(ConstantesBD.TABLE_POS_EMAIL, usuario.getCorreo());
        cvValues.put(ConstantesBD.TABLE_POS_PASSWORD, usuario.getPassword());
        cvValues.put(ConstantesBD.TABLE_POS_GENDER, usuario.getGenero());
        cvValues.put(ConstantesBD.TABLE_POS_PHOTO, usuario.getFoto());
        cvValues.put(ConstantesBD.TABLE_POS_NAME, usuario.getNombre());
        cvValues.put(ConstantesBD.TABLE_POS_DESCRPTION, usuario.getDescripcion());
        cvValues.put(ConstantesBD.TABLE_POS_TEL, usuario.getTelefono());

        return db.update(ConstantesBD.TABLE_NAME_POS, cvValues, ConstantesBD.TABLE_POS_EMAIL + " = ?",
                new String[]{String.valueOf(usuario.getCorreo())});
    }

    public void borrarUsuario(Usuario usuario)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ConstantesBD.TABLE_NAME_POS, ConstantesBD.TABLE_POS_ID + "=?",
                new String[]{String.valueOf(usuario.getId())});
    }

    public Usuario obtenerUsuario(String strCorreo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ConstantesBD.TABLE_NAME_POS, new String[] { ConstantesBD.TABLE_POS_ID,
                        ConstantesBD.TABLE_POS_EMAIL, ConstantesBD.TABLE_POS_PASSWORD,
                        ConstantesBD.TABLE_POS_GENDER, ConstantesBD.TABLE_POS_PHOTO,
                        ConstantesBD.TABLE_POS_NAME, ConstantesBD.TABLE_POS_TEL,
                        ConstantesBD.TABLE_POS_DESCRPTION},
                ConstantesBD.TABLE_POS_EMAIL + "=?",
                new String[] { String.valueOf(strCorreo) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Usuario usuario = new Usuario(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                Integer.parseInt(cursor.getString(4)), cursor.getString(5), cursor.getString(6),
                cursor.getString(7));

        return usuario;
    }



}
