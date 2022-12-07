package com.example.libraryasist.view;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.libraryasist.MyApplication;
import com.example.libraryasist.R;
import com.example.libraryasist.core.Libro;
import com.example.libraryasist.core.Reserva;
import com.example.libraryasist.core.Usuario;
import com.example.libraryasist.database.DBManager;
import com.example.libraryasist.model.LibroFacade;
import com.example.libraryasist.model.ReservasFacade;

import java.util.ArrayList;
import java.util.List;


public class UsuarioView extends AppCompatActivity {

    LibroFacade libroFacade;
    ReservasFacade reservasFacade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_usuario);

        DBManager dbManager = ((MyApplication) this.getApplication()).getDBManager();
        reservasFacade = new ReservasFacade(dbManager);
        libroFacade = new LibroFacade(dbManager);

        ListView listViewLibros = (ListView) this.findViewById(R.id.listViewReserva);

        List<Reserva> arrayReservas = new ArrayList<>();



        listViewLibros.setAdapter(
                new ArrayAdapter<String>(
                        this, android.R.layout.simple_expandable_list_item_1,
                        UsuarioView.this.listaLibros() )
        );

    }


    @SuppressLint("Range")
    private String []listaLibros(){

        Cursor librosCursor = libroFacade.getLibros();

        String [] temp = new String[3];
        int i = 0;
        while(librosCursor.moveToNext()){
            temp[i++]= librosCursor.getString(librosCursor.getColumnIndex(DBManager.LIBROS_TITULO));
        }

        return temp;
    }

    @SuppressLint("Range")
    private ArrayList<Reserva> listaReserva(){

        Cursor librosCursor = libroFacade.getLibros();

        ArrayList<Reserva> arrayList = new ArrayList<>();

        while(librosCursor.moveToNext()){
            Libro temp = new Libro();

            temp.setIsbm(librosCursor.getString(librosCursor.getColumnIndex(DBManager.LIBROS_CODIGO)));
            temp.setTitulo(librosCursor.getString(librosCursor.getColumnIndex(DBManager.LIBROS_TITULO)));
            temp.setAutor(librosCursor.getString(librosCursor.getColumnIndex(DBManager.LIBROS_AUTOR)));

            Usuario user = new Usuario("49672434J","Anton","Canzobre", "anton");
            arrayList.add(new Reserva(user,temp));
        }

        return arrayList;
    }

}
