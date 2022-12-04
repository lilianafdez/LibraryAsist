package com.example.libraryasist.core;

public class Usuario {


    private String dni;
    private String nombre;
    private String apellidos;
    private String password;

    public Usuario (){

    }

    public Usuario (String dni, String nombre, String apellidos, String password){
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.password = password;

    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
