package com.example.libraryasist.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.libraryasist.R;
import com.example.libraryasist.core.Libro;
import com.example.libraryasist.model.LibroFacade;

public class LibrosAdapterCursor extends CursorAdapter {

    LibroFacade libros;
    public LibrosAdapterCursor(Context context, Cursor c, LibroFacade lf){
        super(context,c);
        this.libros=lf;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        return LayoutInflater.from(context).inflate(R.layout.listview_libros,
                viewGroup,false );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView titulo= view.findViewById(R.id.tvTituloLibro);
        TextView autor=view.findViewById(R.id.tvAutor);
        TextView reservado=view.findViewById(R.id.tvReservado);

        final Libro libro = LibroFacade.readLibro(cursor);
        titulo.setText(libro.getTitulo());
        autor.setText(libro.getAutor());
        reservado.setText(libro.getReservado().toString());

    }
}
