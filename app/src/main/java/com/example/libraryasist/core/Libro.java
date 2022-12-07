package com.example.libraryasist.core;

import java.io.Serializable;

public class Libro implements Serializable {
    private String isbm;
    private String titulo;
    private String autor;
    private Integer reservado;

    public Libro(String isbm, String titulo, String autor) {
        this.isbm = isbm;
        this.titulo = titulo;
        this.autor = autor;
        reservado = 0;
    }
    public Libro() {

    }

    public Integer getReservado() {
        return reservado;
    }

    public void setReservado(Integer reservado) {
        this.reservado = reservado;
    }

    public String getIsbm() {
        return isbm;
    }

    public void setIsbm(String isbm) {
        this.isbm = isbm;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }
}
