package com.example.libraryasist.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.libraryasist.MainActivity;
import com.example.libraryasist.MyApplication;
import com.example.libraryasist.R;
import com.example.libraryasist.core.Libro;
import com.example.libraryasist.core.Reserva;
import com.example.libraryasist.core.Usuario;
import com.example.libraryasist.database.DBManager;
import com.example.libraryasist.model.LibroFacade;
import com.example.libraryasist.model.ReservasFacade;
import com.example.libraryasist.model.UsuarioFacade;

import java.util.ArrayList;
import java.util.List;


public class UsuarioView extends AppCompatActivity {

    private LibroFacade libroFacade;
    private ReservasFacade reservasFacade;
    private UsuarioFacade usuarioFacade;
    private Usuario usuarioActual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_usuario);

        DBManager dbManager = ((MyApplication) this.getApplication()).getDBManager();
        reservasFacade = new ReservasFacade(dbManager);
        libroFacade = new LibroFacade(dbManager);
        usuarioFacade = new UsuarioFacade(dbManager);

        String dni = getIntent().getStringExtra("usuarioLogueado");

        usuarioActual = usuarioFacade.getUsuariosByDni(dni);

        ListView listViewLibros = (ListView) this.findViewById(R.id.listViewReserva);
        Button botonAñadir = (Button) this. findViewById(R.id.buttonVistaAñadirReserva);

        List<Reserva> arrayReservas = new ArrayList<>();
        arrayReservas = UsuarioView.this.listaReserva();
        List<String> arrayTitulos = new ArrayList<>();

        for (int i = 0; i < arrayReservas.size(); i++) {
            arrayTitulos.add(i,arrayReservas.get(i).getLibro().getTitulo());
        }

        listViewLibros.setAdapter(
                new ArrayAdapter<String>(
                        this, android.R.layout.simple_expandable_list_item_1,
                        arrayTitulos )
        );

        botonAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsuarioView.this, AddReserva.class);

                intent.putExtra("usuarioActual", usuarioActual.getDni());

                UsuarioView.this.startActivity(intent);
            }
        });

    }


    @SuppressLint("Range")
    private List<String> listaLibros(){

        Cursor librosCursor = libroFacade.getLibros();

        ArrayList<String> arrayList = new ArrayList<>();

        while(librosCursor.moveToNext()){

            arrayList.add(librosCursor.getString(librosCursor.getColumnIndex(DBManager.LIBROS_TITULO)));
        }

        return arrayList;
    }

    @SuppressLint("Range")
    private ArrayList<Reserva> listaReserva(){

        Cursor librosCursor = reservasFacade.getReservasByUser(usuarioActual.getDni());

        ArrayList<Reserva> arrayList = new ArrayList<>();

        while(librosCursor.moveToNext()){


            Libro temp = libroFacade.getLibroById(librosCursor.getLong(librosCursor.getColumnIndex(DBManager.USUARIO_LIBROS_LIBRO_ID)));

            arrayList.add(new Reserva(usuarioActual,temp));
        }

        return arrayList;
    }

}
