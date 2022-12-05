package com.example.libraryasist.core;

import com.example.libraryasist.model.UsuarioFacade;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registrar {

    public Registrar(UsuarioFacade usuarioFacade, String dni, String nombre, String apellidos, String contraseña){

        if(comprobarDni(dni) && nombre != "" && apellidos != "" && contraseña != ""){
            Usuario usuario = new Usuario(dni, nombre, apellidos, contraseña);
            usuarioFacade.createUsuario(usuario);
        }



    }

    public boolean comprobarDni(String dni){

        Pattern patron = Pattern.compile("[0-9]{8}[A-Z a-z]");
        Matcher mat = patron.matcher(dni);

        return mat.matches();
    }

}
