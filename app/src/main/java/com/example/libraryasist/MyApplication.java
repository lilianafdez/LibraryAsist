package com.example.libraryasist;

import android.app.Application;

import com.example.libraryasist.database.DBManager;
import com.example.libraryasist.core.Usuario;
import com.example.libraryasist.model.UsuarioFacade;

public class MyApplication extends Application {
    private Usuario logeado;
    private long id_user_logged;
    private DBManager db;

    @Override
    public void onCreate() {
        super.onCreate();
        this.db=new DBManager(this.getApplicationContext());
    }

    public void setLogeado(Usuario logeado) {
        this.logeado = logeado;
    }

    public boolean esAdmin(){
        boolean toret = false;
        if(logeado.getEs_Admin() == 1){
            toret = true;
        }
        return toret;
    }

    public void setId_user_logged(long id_user_logged) {
        this.id_user_logged = id_user_logged;
    }

    public DBManager getDBManager(){
        return this.db;
    }

}
