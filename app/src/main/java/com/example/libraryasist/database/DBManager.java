package com.example.libraryasist.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBManager extends SQLiteOpenHelper{

    public static final int DB_VERSION = 1;
    public static final String DB_NOMBRE = "BIBLIOTECA";
    public static final String _id="_id";

    //Tabla usuarios
    public static final String USUARIOS_TABLE_NAME="USUARIOS";
    public static final String USUARIOS_ID = _id;
    public static final String USUARIOS_DNI = "USUARIOS_DNI";
    public static final String USUARIOS_NOMBRE = "USUARIOS_NOMBRE";
    public static final String USUARIOS_APELLIDOS = "USUARIOS_APELLIDOS";
    public static final String USUARIOS_PASSWORD = "USUARIOS_PASSWORD";
    public static final String USUARIOS_ES_ADMIN = "USUARIOS_ES_ADMIN";

    //Tabla libros
    public static final String LIBROS_TABLE_NAME="LIBROS";
    public static final String LIBROS_ID = _id;
    public static final String LIBROS_CODIGO = "LIBROS_CODIGO";
    public static final String LIBROS_TITULO = "LIBROS_TITULO";
    public static final String LIBROS_AUTOR = "LIBROS_AUTOR";
    public static final String LIBROS_RESERVADO= "LIBROS_RESERVADO";

    //Tabla libros prestados
    public static String USUARIO_LIBROS_TABLE_NAME = "USUARIO_LIBROS";
    public static String USUARIO_LIBROS_USUARIO_ID = "_id_USUARIO";
    public static String USUARIO_LIBROS_LIBRO_ID = "_id_LIBRO";

    //public static final String LIBROS_CODIGO = USUARIO_LIBROS_USUARIO_ID;
    public DBManager(@Nullable Context context){
        super(context,DB_NOMBRE,null,DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(DB_NOMBRE,"Creando la base de datos");
        try {
            db.beginTransaction();
            //CREACION DE TABLA DE USUARIOS
            db.execSQL("CREATE TABLE IF NOT EXISTS " + USUARIOS_TABLE_NAME +"(" +
                    _id +" INTEGER PRIMARY KEY," +
                    USUARIOS_DNI + " TEXT NOT NULL UNIQUE," +
                    USUARIOS_NOMBRE + " TEXT NOT NULL," +
                    USUARIOS_APELLIDOS + " TEXT NOT NULL," +
                    USUARIOS_PASSWORD + " BLOB," +
                    USUARIOS_ES_ADMIN + " INTEGER" +
                    ")");


            //CREACION DE TABLA DE LIBROS
            db.execSQL("CREATE TABLE IF NOT EXISTS " + LIBROS_TABLE_NAME +"(" +
                    _id +" INTEGER PRIMARY KEY," +
                    LIBROS_CODIGO + " TEXT NOT NULL UNIQUE," +
                    LIBROS_TITULO + " TEXT NOT NULL," +
                    LIBROS_AUTOR + " TEXT NOT NULL," +
                    LIBROS_RESERVADO + " INTEGER NOT NULL" +
                    ")");

            //CREACION DE TABLA DE LIBROS RESERVADOS POR UN USUARIO
            db.execSQL("CREATE TABLE IF NOT EXISTS " + USUARIO_LIBROS_TABLE_NAME +"(" +
                    USUARIO_LIBROS_USUARIO_ID+" INTEGER," +
                    USUARIO_LIBROS_LIBRO_ID + " INTEGER,"+
                    "foreign key ("+USUARIO_LIBROS_USUARIO_ID+" ) references "+USUARIOS_TABLE_NAME+"("+USUARIOS_ID+"),"+
                    "foreign key ("+USUARIO_LIBROS_LIBRO_ID+" ) references "+LIBROS_TABLE_NAME+"("+LIBROS_ID+"),"+
                    "PRIMARY KEY("+USUARIO_LIBROS_USUARIO_ID+","+USUARIO_LIBROS_LIBRO_ID+")"+
                    ")");

            db.setTransactionSuccessful();
        } catch(SQLException exc) {
            Log.e(DBManager.class.getName(), "onCreate", exc);

        } finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i( "DBManager", "DB: " + DB_NOMBRE + ": v" + i + " -> v" + i1 );
        try {

            sqLiteDatabase.beginTransaction();
            //ELIMINACION DE TABLA DE USUARIOS
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USUARIOS_TABLE_NAME);


            //ELIMINACION DE TABLA DE LIBROS
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LIBROS_TABLE_NAME);


            //ELIMINACION DE TABLA DE LIBROS POR USUARIO
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USUARIO_LIBROS_TABLE_NAME);



            sqLiteDatabase.setTransactionSuccessful();

        }catch (SQLException exception){
            Log.e(DBManager.class.getName(), "onCreate", exception);
        }finally {
            sqLiteDatabase.endTransaction();
        }
    }

    public Cursor getLibros()
    {
        return this.getReadableDatabase().query( LIBROS_TABLE_NAME,
                null, null, null, null, null, null );
    }
}
