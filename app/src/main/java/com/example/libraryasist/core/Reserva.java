package com.example.libraryasist.core;

public class Reserva {
    private Usuario usuario;
    private Libro libro;

    public Reserva (){

    }
    public Reserva (Usuario usuario, Libro libro){
        this.usuario = usuario;
        this.libro = libro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }
}
