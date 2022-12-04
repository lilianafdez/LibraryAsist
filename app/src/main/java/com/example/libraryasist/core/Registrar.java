package com.example.libraryasist.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registrar {

    public Registrar(String dni, String nombre, String apellidos, String contraseña){

        Usuario usuario = new Usuario(dni, nombre, apellidos,contraseña);



    }

    public boolean comprobarDni(String dni){

        Pattern patron = Pattern.compile("[0-9]{8}[A-Z a-z]");
        Matcher mat = patron.matcher(dni);

        return mat.matches();
    }

}
