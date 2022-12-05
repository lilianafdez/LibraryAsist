package com.example.libraryasist.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.libraryasist.MainActivity;
import com.example.libraryasist.MyApplication;
import com.example.libraryasist.R;
import com.example.libraryasist.core.Usuario;
import com.example.libraryasist.database.DBManager;
import com.example.libraryasist.model.UsuarioFacade;

public class RegistroView extends Activity {

    UsuarioFacade usuarioFacade;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar_usuario);

       DBManager dbManager = ((MyApplication) this.getApplication()).getDBManager();

        usuarioFacade = new UsuarioFacade(dbManager);

        Button botonRegistrar = (Button) findViewById(R.id.buttonRegistrarUsuario);
        Button botonVolver = (Button) findViewById(R.id.buttonVolverRegistrar);


        EditText dniIntroducido = (EditText) findViewById(R.id.editTextDniRegistrar);
        EditText nombreIntroducido = (EditText) findViewById(R.id.editTextNombreRegistrar);
        EditText apellidosIntroducido = (EditText) findViewById(R.id.editTextApellidosRegistrar);
        EditText passwordIntroducido = (EditText) findViewById(R.id.editTextPasswordRegistrar);

        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!RegistroView.this.nombreUsuarioOcupado(dniIntroducido.getText().toString())){
                    Usuario usuarioRegistrar = new Usuario(dniIntroducido.getText().toString(), nombreIntroducido.getText().toString(),
                            apellidosIntroducido.getText().toString(), passwordIntroducido.getText().toString());

                    usuarioFacade.createUsuario(usuarioRegistrar);

                    Intent intent = new Intent(RegistroView.this, MainActivity.class);

                    RegistroView.this.startActivity(intent);
                    RegistroView.this.finish();

                }else{
                    RegistroView.dialogoErrorDatos("El dni introducido ya esta registrado", RegistroView.this);
                }
            }
        });

        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistroView.this, MainActivity.class);

                RegistroView.this.startActivity(intent);
                RegistroView.this.finish();

            }
        });

    }

    public static void dialogoErrorDatos(String msg, Context context) {
        AlertDialog.Builder dlg=new AlertDialog.Builder(context);
        dlg.setTitle("Error");
        dlg.setMessage(msg);
        dlg.setNeutralButton("volver",null);
        dlg.create().show();
    }

    private boolean nombreUsuarioOcupado(String dni){

        return usuarioFacade.getUsuariosByDni(dni)!=null;
    }

}
