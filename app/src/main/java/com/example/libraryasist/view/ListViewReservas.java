package com.example.libraryasist.view;

public class ListViewReservas {

    private boolean cheked;
    private String titulo;
    private long id;



    public ListViewReservas(String titulo){
        this.titulo = titulo;
        this.cheked =false;
    }

    public ListViewReservas(String titulo, long id){
        this.titulo = titulo;
        this.cheked =false;
        this.id = id;
    }

    public boolean isCheked() {
        return cheked;
    }

    public void setCheked(boolean cheked) {
        this.cheked = cheked;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void invertirSeleccion(){
        this.cheked = !this.cheked;
    }
}
