package com.example.libraryasist.model;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.libraryasist.core.Libro;
import com.example.libraryasist.database.DBManager;

public class LibroFacade extends GeneralFacade {

    public LibroFacade (DBManager dbManager){
        super(dbManager, DBManager.LIBROS_TABLE_NAME);
    }

    @SuppressLint("Range")
    public static Libro readLibro(Cursor cursor){

        Libro toret = null;
        if (cursor!=null){
            try {
                toret = new Libro();
                toret.setIsbm(cursor.getString(cursor.getColumnIndex(DBManager.LIBROS_CODIGO)));
                toret.setTitulo(cursor.getString(cursor.getColumnIndex(DBManager.LIBROS_TITULO)));
                toret.setAutor(cursor.getString(cursor.getColumnIndex(DBManager.LIBROS_AUTOR)));

            }catch (Exception e){
                Log.e(LibroFacade.class.getName(),"readLibro" ,e);
            }
        }
        return toret;
    }

    public Libro getLibroById(long id){
        Cursor c =  super.getById(id);
        c.moveToFirst();
        return LibroFacade.readLibro(c);
    }

    public void createLibro(Libro libro) {
        SQLiteDatabase writableDatabase = dbManager.getWritableDatabase();
        try{
            writableDatabase.beginTransaction();
            writableDatabase.execSQL(
                    "INSERT INTO " +DBManager.LIBROS_TABLE_NAME +
                            "(" +
                            DBManager.LIBROS_TITULO +
                            ","+
                            DBManager.LIBROS_AUTOR +
                            ") VALUES (?,?)"
                    , new Object[]{libro.getIsbm(), libro.getTitulo(), libro.getAutor()});
            writableDatabase.setTransactionSuccessful();
        }catch(SQLException exception){
            Log.e(LibroFacade.class.getName(), "createLibro", exception);
        }finally {
            writableDatabase.endTransaction();
        }
    }

    public void removeLibro(Libro libro) {
        SQLiteDatabase writableDatabase = dbManager.getWritableDatabase();
        try{
            writableDatabase.beginTransaction();
            writableDatabase.execSQL(
                    "DELETE FROM "
                            + DBManager.LIBROS_TABLE_NAME
                            + " WHERE "
                            + DBManager.LIBROS_CODIGO +"=?"
                    , new Object[]{libro.getIsbm()});
            writableDatabase.setTransactionSuccessful();
        }catch(SQLException exception){
            Log.e(LibroFacade.class.getName(), "removeLibro", exception);
        }finally {
            writableDatabase.endTransaction();
        }
    }

    public void updateLibro(Libro libro) {
        SQLiteDatabase writableDatabase = dbManager.getWritableDatabase();
        try{
            writableDatabase.beginTransaction();
            writableDatabase.execSQL(
                    "UPDATE "
                            + DBManager.LIBROS_TABLE_NAME
                            + " SET "
                            + DBManager.LIBROS_TITULO + "=? ,"
                            + DBManager.LIBROS_AUTOR + "=?"

                            + "WHERE "+DBManager.LIBROS_CODIGO +"=?",
                    new Object[]{libro.getTitulo(), libro.getTitulo(), libro.getIsbm()});

            writableDatabase.setTransactionSuccessful();
        }catch(SQLException exception){
            Log.e(LibroFacade.class.getName(), "updateLibro", exception);
        }finally {
            writableDatabase.endTransaction();
        }
    }

    public Cursor getLibros(){
        Cursor toret = null;

        toret = dbManager.getReadableDatabase().rawQuery("SELECT * FROM "+DBManager.LIBROS_TABLE_NAME,
                null);

        return toret;
    }

    @SuppressLint("Range")
    public static long getID(Cursor libro) {
        long toret;

        toret=libro.getLong(libro.getColumnIndex(DBManager.LIBROS_CODIGO));
        return toret;
    }

    public boolean checkLibro(String isbn){
        Cursor libro=this.dbManager.getReadableDatabase().rawQuery("SELECT * FROM "
                + DBManager.LIBROS_TABLE_NAME + " WHERE " + DBManager.LIBROS_CODIGO + " LIKE ?", new String[]{isbn});
        if(libro.getCount()==1) {
            return true;

        }else{
            return false;
        }
    }


}
