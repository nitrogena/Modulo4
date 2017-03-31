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

    public void modificarUsuario(ContentValues contentValues, String strCorreo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(ConstantesBD.TABLE_NAME_POS, contentValues, ConstantesBD.TABLE_POS_EMAIL + " = ?",
                new String[]{String.valueOf(strCorreo)});

        db.close();
    }

    public void borrarUsuario(String strUsuario)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ConstantesBD.TABLE_NAME_POS, ConstantesBD.TABLE_POS_EMAIL + "=?",
                new String[]{String.valueOf(strUsuario)});
        db.close();
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
        Usuario usuario = new Usuario();
        if (cursor != null) {
            if (cursor.getCount() > 0) {


                cursor.moveToFirst();

            /*Usuario usuario = new Usuario(
                    cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    Integer.parseInt(cursor.getString(4)), cursor.getString(5), cursor.getString(6),
                    cursor.getString(7));
            usuario.setId(Integer.parseInt(cursor.getString(0)));*/
                usuario.setCorreo(cursor.getString(1));
                usuario.setPassword(cursor.getString(2));
                usuario.setGenero(cursor.getString(3));
                usuario.setFoto(Integer.parseInt(cursor.getString(4)));
                usuario.setNombre(cursor.getString(5));
                usuario.setTelefono(cursor.getString(6));
                usuario.setDescripcion(cursor.getString(7));
            }
        }
            db.close();
            return usuario;


    }

    public int obtenerFoto(String strCorreo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ConstantesBD.TABLE_NAME_POS, new String[] {
                        ConstantesBD.TABLE_POS_PHOTO},
                ConstantesBD.TABLE_POS_EMAIL + "=?",
                new String[] { String.valueOf(strCorreo) }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        int intFoto = Integer.parseInt(cursor.getString(0));
        db.close();
        return intFoto;
    }

    public boolean existeUsuario(String strCorreo) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ConstantesBD.TABLE_NAME_POS, new String[] { ConstantesBD.TABLE_POS_ID,
                        ConstantesBD.TABLE_POS_EMAIL, ConstantesBD.TABLE_POS_PASSWORD,
                        ConstantesBD.TABLE_POS_GENDER, ConstantesBD.TABLE_POS_PHOTO,
                        ConstantesBD.TABLE_POS_NAME, ConstantesBD.TABLE_POS_TEL,
                        ConstantesBD.TABLE_POS_DESCRPTION},
                ConstantesBD.TABLE_POS_EMAIL + "=?",
                new String[] { String.valueOf(strCorreo) }, null, null, null, null);
        boolean blExiste = false;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                blExiste = true;
            }
        }
        db.close();
        return blExiste;
    }


}
