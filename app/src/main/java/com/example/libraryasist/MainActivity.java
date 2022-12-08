package com.example.libraryasist;

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
import com.example.libraryasist.view.RegistroView;
import com.example.libraryasist.view.UsuarioView;
import com.example.libraryasist.view.Vista_admin;

public class MainActivity extends AppCompatActivity {

    private UsuarioFacade usuarioDB;
    private boolean admin;
    private String dniUsuario;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.usuarioDB=new UsuarioFacade(((MyApplication) this.getApplication()).getDBManager());
        Button botonAcceder = findViewById(R.id.buttonAcceder);

        this.createAdmin();

        botonAcceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean acceder=MainActivity.this.checkLogIn();
                if(acceder){
                    MainActivity.this.admin=((MyApplication)MainActivity.this.getApplication()).esAdmin();
                }

                if(acceder && !admin){
                    Intent menu_usuario=new Intent(MainActivity.this, UsuarioView.class);


                    menu_usuario.putExtra("usuarioLogueado",dniUsuario);

                    Toast.makeText(MainActivity.this,"NO-ADMIN",Toast.LENGTH_SHORT).show();
                    MainActivity.this.goTo(menu_usuario,"nombreusuario");
                    MainActivity.this.finish();
                }

                else if(acceder && admin){
                    Intent menu_admin=new Intent(MainActivity.this, Vista_admin.class);
                    Toast.makeText(MainActivity.this,"SI-ADMIN",Toast.LENGTH_SHORT).show();
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

        this.dniUsuario = user.getText().toString().toUpperCase();

        Cursor usuario=this.usuarioDB.logIn(user.getText().toString().toUpperCase());
        String passwordIntroducida = password.getText().toString();
        user.getText().clear();
        password.getText().clear();

        if(usuario!=null){
            usuario.moveToFirst();
            Usuario actual=UsuarioFacade.readUsuario(usuario);
            long id=UsuarioFacade.getID(usuario);

            if(actual.getPassword().equals(passwordIntroducida)){
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
        Usuario admin=new Usuario("admin","admin","admin","admin");
        admin.setEs_Admin(1);
        if(!this.usuarioDB.existeAdmin()){
            this.usuarioDB.createUsuario(admin);
        }
    }
}