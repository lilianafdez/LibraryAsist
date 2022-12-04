package com.example.libraryasist;

import android.app.Application;

import com.example.libraryasist.database.DBManager;

public class LibraryAsist extends Application {
    private DBManager dbManager;

    @Override
    public void onCreate() {
        super.onCreate();
        this.dbManager = new DBManager(this);
    }

    public DBManager getDbManager() {
        return dbManager;
    }
}
