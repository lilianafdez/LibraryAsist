package com.example.libraryasist.model;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.libraryasist.core.Usuario;
import com.example.libraryasist.database.DBManager;

public class UsuarioFacade extends GeneralFacade {

    public UsuarioFacade(DBManager dbManager){

        super(dbManager, DBManager.USUARIOS_TABLE_NAME);
    }

    @SuppressLint("Range")
    public static Usuario readUsuario(Cursor cursor){
        Usuario toret = null;
        if (cursor!=null){
            try {
                toret = new Usuario();
                toret.setCodigo(cursor.getLong(cursor.getColumnIndex(DBManager.USUARIOS_ID)));
                toret.setDni(cursor.getString(cursor.getColumnIndex(DBManager.USUARIOS_DNI)));
                toret.setNombre(cursor.getString(cursor.getColumnIndex(DBManager.USUARIOS_NOMBRE)));
                toret.setApellidos(cursor.getString(cursor.getColumnIndex(DBManager.USUARIOS_APELLIDOS)));
                toret.setPassword(cursor.getString(cursor.getColumnIndex(DBManager.USUARIOS_PASSWORD)));
                toret.setEs_Admin(Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBManager.USUARIOS_ES_ADMIN))));

            }catch (Exception e){
                Log.e(UsuarioFacade.class.getName(),"readUsuario" ,e);
            }
        }
        return toret;
    }

    public void createUsuario(Usuario usuario) {
        SQLiteDatabase writableDatabase = dbManager.getWritableDatabase();
        try{
            writableDatabase.beginTransaction();
            writableDatabase.execSQL(
                    "INSERT INTO " +DBManager.USUARIOS_TABLE_NAME +
                            "(" +
                            DBManager.USUARIOS_DNI +
                            ","+
                            DBManager.USUARIOS_NOMBRE +
                            ","+
                            DBManager.USUARIOS_APELLIDOS +
                            ","+
                            DBManager.USUARIOS_PASSWORD+
                            ","+
                            DBManager.USUARIOS_ES_ADMIN+
                            ") VALUES (?,?,?,?,?)"
                    , new Object[]{usuario.getDni(), usuario.getNombre(), usuario.getApellidos(), usuario.getPassword(), usuario.getEs_Admin()});
            writableDatabase.setTransactionSuccessful();
        }catch(SQLException exception){
            Log.e(UsuarioFacade.class.getName(), "createUsuario", exception);
        }finally {
            writableDatabase.endTransaction();
        }
    }

    public Cursor getUsuario(){
        Cursor toret = null;

        toret = dbManager.getReadableDatabase().rawQuery("SELECT * FROM "+DBManager.USUARIOS_TABLE_NAME,
                null);

        return toret;
    }

    //Funcion que busca a un usuario por el dni
    public Usuario getUsuariosByDni(String dni) {
        Usuario toret = null;
        if (dni!=null){
            Cursor cursor =
                    dbManager.getReadableDatabase().rawQuery("SELECT * FROM " + DBManager.USUARIOS_TABLE_NAME
                                    + " WHERE "
                                    + DBManager.USUARIOS_DNI + " = ?",
                            new String[]{dni+""});

            if (cursor.moveToFirst()){
                toret = readUsuario(cursor);
            }
            cursor.close();
        }
        return toret;

    }
    @SuppressLint("Range")
    public static long getID(Cursor usuario) {
        long toret;

        toret=usuario.getLong(usuario.getColumnIndex(DBManager.USUARIOS_ID));
        return toret;
    }

    //fucion para comprobar el login de un usuario
    public Cursor logIn(String user){
        Cursor usuario=this.dbManager.getReadableDatabase().rawQuery("SELECT * FROM " + DBManager.USUARIOS_TABLE_NAME + " WHERE " + DBManager.USUARIOS_DNI + " LIKE ?", new String[]{user});
        if(usuario.getCount()==1) {
            return usuario;

        }else{
            return null;
        }
    }

    //funcion que comprueba si el usuaario es admin
    public boolean existeAdmin(){
        boolean toret=true;
        String nombre="admin";
        Cursor c=this.dbManager.getReadableDatabase().rawQuery("SELECT * FROM "+DBManager.USUARIOS_TABLE_NAME +" WHERE "+DBManager.USUARIOS_DNI+" LIKE ?", new String[]{nombre});

        if(c.getCount()!=1) {
            toret=false;
        }
        c.close();

        return toret;
    }

}
