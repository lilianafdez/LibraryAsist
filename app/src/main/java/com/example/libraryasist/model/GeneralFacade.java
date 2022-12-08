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

    public Cursor getById(long id){
        Cursor toret=null;

        toret=this.dbManager.getReadableDatabase().rawQuery("SELECT * FROM "+this.nombreTabla +" WHERE "+DBManager._id+ " == ? ",new String[]{""+id});
        return toret;
    }


}
