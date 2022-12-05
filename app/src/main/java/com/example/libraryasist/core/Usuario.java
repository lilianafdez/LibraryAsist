package com.example.libraryasist.core;

import java.io.Serializable;

public class Usuario implements Serializable {


    private String dni;
    private String nombre;
    private String apellidos;
    private String password;
    private Integer es_Admin;

    public Usuario(){    }
    public Usuario (String dni, String nombre, String apellidos, String password){
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.password = password;
        this.es_Admin = 0;

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

    public Integer getEs_Admin() {
        return es_Admin;
    }

    public void setEs_Admin(Integer es_Admin) {
        this.es_Admin = es_Admin;
    }
}
