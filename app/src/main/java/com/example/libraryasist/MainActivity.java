package com.example.libraryasist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.libraryasist.core.Usuario;
import com.example.libraryasist.model.UsuarioFacade;
import com.example.libraryasist.view.AddReserva;
import com.example.libraryasist.view.RegistroView;
import com.example.libraryasist.view.UsuarioView;
import com.example.libraryasist.view.Vista_admin;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private UsuarioFacade usuarioDB;
    private boolean admin;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.usuarioDB=new UsuarioFacade(((MyApplication) this.getApplication()).getDBManager());
        Button botonAcceder = findViewById(R.id.buttonAcceder);

 //       this.createAdmin();

        //startActivity( new Intent( MainActivity.this, AddReserva.class ) );

        botonAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean acceder=MainActivity.this.checkLogIn();
                if(acceder){
                    MainActivity.this.admin=((MyApplication)MainActivity.this.getApplication()).esAdmin();
                }
/*
                if(acceder && !admin){
                    Intent menu_usuario=new Intent(MainActivity.this, Vista_usuario.class);
                    MainActivity.this.goTo(menu_usuario,"nombeusuario");
                    MainActivity.this.finish();
                    }
*/


                else if(acceder && admin){
                    Intent menu_admin=new Intent(MainActivity.this, Vista_admin.class);
                    MainActivity.this.goTo(menu_admin,"nombreusuario");
                    MainActivity.this.finish();

                }
                else{
                    Toast.makeText(MainActivity.this,"Usuario o contraseña incorrecta",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button botonRegistrarse = (Button) this.findViewById(R.id.buttonCambiarRegistrar);

        botonRegistrarse.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity( new Intent( MainActivity.this, RegistroView.class ) );

                            }
                        }

        );

    }

    private void goTo(Intent intent, String username){
        intent.putExtra("username",username);
        this.startActivity(intent);
    }

    private boolean checkLogIn() {
        boolean toret=false;

        EditText user=this.findViewById(R.id.editTextUsuario);
        EditText password=this.findViewById(R.id.editTextPassword);

        Cursor usuario=this.usuarioDB.logIn(user.getText().toString());
        String passwordIntroducida = password.getText().toString();
        user.getText().clear();
        password.getText().clear();

        if(usuario!=null){
            usuario.moveToFirst();
            Usuario actual=UsuarioFacade.readUsuario(usuario);
            long id=UsuarioFacade.getID(usuario);
            if(actual.getPassword() == passwordIntroducida){
                toret=true;
                MyApplication app=(MyApplication) this.getApplication();
                app.setLogeado(actual);
                app.setId_user_logged(id);
            }
            usuario.close();
        }
        return toret;
    }
    private void createAdmin(){
        Usuario admin=new Usuario("11111","admin","admin","admin");
        admin.setEs_Admin(1);
    //    if(!this.usuarioDB.existeAdmin()){
            this.usuarioDB.createUsuario(admin);
     //   }
    }
    /*private UsuarioFacade usuarioFacade;
    private UsuarioCursorAdapter usuairoCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioFacade = new UsuarioFacade(getDBManager());
        //usuairoCursorAdapter = new UsuarioCursorAdapter(MainActivity.this, null, usuarioFacade);

        Button botonAcceder = (Button) this.findViewById(R.id.buttonAcceder);
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
    }*/

}