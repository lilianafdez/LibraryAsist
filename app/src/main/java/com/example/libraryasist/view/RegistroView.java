package com.example.libraryasist.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.libraryasist.MainActivity;
import com.example.libraryasist.MyApplication;
import com.example.libraryasist.R;
import com.example.libraryasist.core.Usuario;
import com.example.libraryasist.database.DBManager;
import com.example.libraryasist.model.UsuarioFacade;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistroView extends AppCompatActivity {

    UsuarioFacade usuarioFacade;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar_usuario);

        DBManager dbManager = ((MyApplication) this.getApplication()).getDBManager();

        usuarioFacade = new UsuarioFacade(dbManager); //se inicializa la clase fachada de usuario

        Button botonRegistrar = (Button) findViewById(R.id.buttonRegistrarUsuario); //boton para registar
        Button botonVolver = (Button) findViewById(R.id.buttonVolverRegistrar); //boton para volve al Login por si no quieres registrarse

        //EditText de los datos para registrarse
        EditText dniIntroducido = (EditText) findViewById(R.id.editTextDniRegistrar);
        EditText nombreIntroducido = (EditText) findViewById(R.id.editTextNombreRegistrar);
        EditText apellidosIntroducido = (EditText) findViewById(R.id.editTextApellidosRegistrar);
        EditText passwordIntroducido = (EditText) findViewById(R.id.editTextPasswordRegistrar);

        //Se comprueba si se pulsa el boton
        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!RegistroView.this.comprobarVacio()){ // funcion que comprueba si los EditText estan vacios
                    if(RegistroView.this.comprobarDni(dniIntroducido.getText().toString().toUpperCase())){ //Funcion que comprueba el formato del dni
                        if(!RegistroView.this.nombreUsuarioOcupado(dniIntroducido.getText().toString().toUpperCase())){ //Funcion que comprueba si el dni esta introducido
                            Usuario usuarioRegistrar = new Usuario(dniIntroducido.getText().toString().toUpperCase(), nombreIntroducido.getText().toString(),
                                    apellidosIntroducido.getText().toString(), passwordIntroducido.getText().toString()); //Se crea el usuario a registrar

                            usuarioFacade.createUsuario(usuarioRegistrar); //se introduce el usuario a la base de datos a traves de la clase fachada

                            Intent intent = new Intent(RegistroView.this, MainActivity.class); //Cuando se registra el usuario se manda al Login

                            RegistroView.this.startActivity(intent);
                            RegistroView.this.finish();

                        }else{
                            RegistroView.dialogoErrorDatos("El dni introducido ya esta registrado", RegistroView.this);
                        }
                    }else{
                        RegistroView.dialogoErrorDatos("El formato del dni no es valido", RegistroView.this);

                    }

                }else{
                    RegistroView.dialogoErrorDatos("Los datos no pueden ser vacios", RegistroView.this);

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

    //funcion que muestra los errores en el registro
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

    private boolean comprobarVacio(){
        EditText dni = (EditText) findViewById(R.id.editTextDniRegistrar);
        EditText nombre = (EditText) findViewById(R.id.editTextNombreRegistrar);
        EditText apellidos = (EditText) findViewById(R.id.editTextApellidosRegistrar);
        EditText password = (EditText) findViewById(R.id.editTextPasswordRegistrar);


        return dni.getText().toString().isEmpty() || nombre.getText().toString().isEmpty() || apellidos.getText().toString().isEmpty() || password.getText().toString().isEmpty() ;
    }

    private boolean comprobarDni(String dni){

        Pattern patron = Pattern.compile("[0-9]{8}[A-Z a-z]");
        Matcher mat = patron.matcher(dni);

        return mat.matches();
    }

}
