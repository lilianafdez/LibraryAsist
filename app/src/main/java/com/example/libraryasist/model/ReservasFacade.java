package com.example.libraryasist.model;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.libraryasist.core.Libro;
import com.example.libraryasist.core.Reserva;
import com.example.libraryasist.core.Usuario;
import com.example.libraryasist.database.DBManager;

public class ReservasFacade extends GeneralFacade{

    private static LibroFacade libroFacade;
    private static UsuarioFacade usuarioFacade;

    public ReservasFacade (DBManager dbManager){
        super(dbManager, DBManager.USUARIO_LIBROS_TABLE_NAME);
        this.libroFacade = new LibroFacade(dbManager);
        this.usuarioFacade = new UsuarioFacade(dbManager);
    }


    public void createReservas(Reserva reserva) {
        SQLiteDatabase writableDatabase = dbManager.getWritableDatabase();
        try{
            writableDatabase.beginTransaction();
            writableDatabase.execSQL(
                    "INSERT INTO " +DBManager.USUARIO_LIBROS_TABLE_NAME +
                            "(" +
                            DBManager.USUARIO_LIBROS_USUARIO_ID +
                            ","+
                            DBManager.USUARIO_LIBROS_LIBRO_ID +
                            ") VALUES (?,?)"
                    , new Object[]{reserva.getUsuario().getCodigo(), reserva.getLibro().getId()});
            writableDatabase.setTransactionSuccessful();
        }catch(SQLException exception){
            Log.e(ReservasFacade.class.getName(), "createReserva", exception);
        }finally {
            writableDatabase.endTransaction();
        }
    }

    public void removeLibro(Reserva reserva) {
        SQLiteDatabase writableDatabase = dbManager.getWritableDatabase();
        try{
            writableDatabase.beginTransaction();
            writableDatabase.execSQL(
                    "DELETE FROM "
                            + DBManager.USUARIO_LIBROS_TABLE_NAME
                            + " WHERE "
                            + DBManager.USUARIO_LIBROS_USUARIO_ID +"=? AND "
                            + DBManager.USUARIO_LIBROS_LIBRO_ID + "=?"
                    , new Object[]{reserva.getUsuario().getCodigo(), reserva.getLibro().getId()});
            writableDatabase.setTransactionSuccessful();
        }catch(SQLException exception){
            Log.e(ReservasFacade.class.getName(), "removeReserva", exception);
        }finally {
            writableDatabase.endTransaction();
        }
    }


    public Cursor getReservasByUser(String dni){
        Usuario temp = usuarioFacade.getUsuariosByDni(dni);

        Cursor toret = null;

        toret = dbManager.getReadableDatabase().rawQuery("SELECT * FROM "+DBManager.USUARIO_LIBROS_TABLE_NAME+
                        " WHERE " + DBManager.USUARIO_LIBROS_USUARIO_ID + " LIKE " + temp.getCodigo(),
                new String[]{});
        //new String[]{temp.getCodigo()});

        return toret;
    }




}
