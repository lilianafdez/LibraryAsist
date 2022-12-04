package com.example.libraryasist.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.libraryasist.LibraryAsist;
import com.example.libraryasist.R;
import com.example.libraryasist.database.DBManager;
import com.example.libraryasist.database.UsuarioFacade;

public class MainActivity extends Activity {

    private UsuarioFacade usuarioFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioFacade = new UsuarioFacade(getDBManager());

        Button botonRegistrarse = (Button) this.findViewById(R.id.buttonCambiarRegistrar);

        botonRegistrarse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity( new Intent( MainActivity.this, RegistroView.class ) );

                    }
                }

        );

    }



    private DBManager getDBManager(){
        return ((LibraryAsist) getApplication()).getDbManager();
    }

}