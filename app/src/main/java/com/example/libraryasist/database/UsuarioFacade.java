package com.example.libraryasist.database;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.libraryasist.core.Usuario;

public class UsuarioFacade {

    private DBManager dbManager;

    public UsuarioFacade(DBManager dbManager){

        this.dbManager=dbManager;
    }

    @SuppressLint("Range")
    public static Usuario readUsuario(Cursor cursor){
        Usuario toret = null;
        if (cursor!=null){
            try {
                toret = new Usuario();
                toret.setDni(cursor.getString(cursor.getColumnIndex(DBManager.USUARIOS_DNI)));
                toret.setNombre(cursor.getString(cursor.getColumnIndex(DBManager.USUARIOS_NOMBRE)));
                toret.setApellidos(cursor.getString(cursor.getColumnIndex(DBManager.USUARIOS_APELLIDOS)));
                toret.setPassword(cursor.getString(cursor.getColumnIndex(DBManager.USUARIOS_PASSWORD)));

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
                            ") VALUES (?,?,?,?)"
                    , new Object[]{usuario.getDni(), usuario.getNombre(), usuario.getApellidos(), usuario.getPassword()});
            writableDatabase.setTransactionSuccessful();
        }catch(SQLException exception){
            Log.e(UsuarioFacade.class.getName(), "createUsuario", exception);
        }finally {
            writableDatabase.endTransaction();
        }
    }

    public void removeUsuario(Usuario usuario) {
        SQLiteDatabase writableDatabase = dbManager.getWritableDatabase();
        try{
            writableDatabase.beginTransaction();
            writableDatabase.execSQL(
                    "DELETE FROM "
                            + DBManager.USUARIOS_TABLE_NAME
                            + " WHERE "
                            + DBManager.USUARIOS_DNI +"=?"
                    , new Object[]{usuario.getDni()});
            writableDatabase.setTransactionSuccessful();
        }catch(SQLException exception){
            Log.e(UsuarioFacade.class.getName(), "removeUsuario", exception);
        }finally {
            writableDatabase.endTransaction();
        }
    }

    public void updateUsuario(Usuario usuario) {
        SQLiteDatabase writableDatabase = dbManager.getWritableDatabase();
        try{
            writableDatabase.beginTransaction();
            writableDatabase.execSQL(
                    "UPDATE "
                            + DBManager.USUARIOS_TABLE_NAME
                            + " SET "
                            + DBManager.USUARIOS_NOMBRE + "=? ,"
                            + DBManager.USUARIOS_APELLIDOS + "=? ,"
                            + DBManager.USUARIOS_PASSWORD + "=? "

                            + "WHERE "+DBManager.USUARIOS_DNI +"=?",
                    new Object[]{usuario.getNombre(), usuario.getApellidos(), usuario.getPassword(), usuario.getDni()});

            writableDatabase.setTransactionSuccessful();
        }catch(SQLException exception){
            Log.e(UsuarioFacade.class.getName(), "updateUsuario", exception);
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

}
