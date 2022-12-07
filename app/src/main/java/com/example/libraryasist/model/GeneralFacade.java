package com.example.libraryasist.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.libraryasist.database.DBManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public abstract class GeneralFacade {

    protected DBManager dbManager;

    private final String nombreTabla;

    public GeneralFacade(DBManager dbManager, String nombreTabla){
        this.dbManager=dbManager;
        this.nombreTabla=nombreTabla;
    }


    public Cursor getElements(){
        Cursor toret=null;

        toret=this.dbManager.getReadableDatabase().rawQuery("SELECT * FROM "+nombreTabla,null);

        return toret;
    }

    public boolean createObjectInDB(String query, Object[] objects){
        SQLiteDatabase writableDatabase = dbManager.getWritableDatabase();
        boolean toret=false;
        try{
            writableDatabase.beginTransaction();
            writableDatabase.execSQL(query, objects);
            writableDatabase.setTransactionSuccessful();
            toret=true;
        }catch(SQLException exception){
            Log.e("INSERT ACTION ",exception.getMessage());
        }finally {
            writableDatabase.endTransaction();
            return toret;
        }
    }

    public boolean deleteElement(String atributo,Object valor){
        SQLiteDatabase db=this.dbManager.getWritableDatabase();

        boolean toret=false;

        try{
            db.beginTransaction();
            db.execSQL("DELETE FROM "+this.nombreTabla+" WHERE "+atributo+" == ?",new Object[]{valor
            });
            db.setTransactionSuccessful();
            toret=true;
        }catch (SQLException exc){
            Log.e("DELETE ACTION",exc.getMessage());
        }finally{
            db.endTransaction();
            return toret;
        }
    }
    public boolean updateElement(String whereClause, ContentValues valores,String[] whereParams){
        SQLiteDatabase db=this.dbManager.getWritableDatabase();

        boolean toret=false;

        try{
            db.beginTransaction();
            db.update(this.nombreTabla,valores,whereClause,whereParams);
            db.setTransactionSuccessful();
            toret=true;
        }catch (SQLException exc){
            Log.e("UPDATE ACTION",exc.getMessage());
        }finally {
            db.endTransaction();
            return toret;
        }
    }
    public Cursor getTablaFiltrada(String atributo,String filtro){
        Cursor toret=null;
        filtro='%'+filtro+'%';
        toret=this.dbManager.getReadableDatabase().rawQuery("SELECT * FROM "+this.nombreTabla +" WHERE "+atributo+ " LIKE ? ",new String[]{filtro});

        return toret;
    }

    public Cursor getById(long id){
        Cursor toret=null;

        toret=this.dbManager.getReadableDatabase().rawQuery("SELECT * FROM "+this.nombreTabla +" WHERE "+DBManager._id+ " == ? ",new String[]{""+id});
        return toret;
    }

    private static Date convertDate(String cadena){
        SimpleDateFormat isoDateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss", Locale.ROOT );
        isoDateFormat.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
        String strFecha = cadena;
        Date fecha=null;
        try {
            fecha = isoDateFormat.parse(strFecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return fecha;
    }

}
