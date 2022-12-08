package com.example.libraryasist.view;



import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.libraryasist.model.UsuarioFacade;

import java.util.ArrayList;
import java.util.List;

public class AddReserva extends AppCompatActivity {

    ReservasFacade reservasFacade;
    LibroFacade libroFacade;
    UsuarioFacade usuarioFacade;
    Usuario usuarioActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reserva);

        DBManager dbManager = ((MyApplication) this.getApplication()).getDBManager();
        libroFacade = new LibroFacade(dbManager);
        reservasFacade = new ReservasFacade(dbManager);

        usuarioFacade = new UsuarioFacade(dbManager);
        String dni = getIntent().getStringExtra("usuarioActual");
        usuarioActual = usuarioFacade.getUsuariosByDni(dni);

        Button botonVolver = (Button) this.findViewById(R.id.buttonVolverUsuario);

        ListView listViewLibros = (ListView) this.findViewById(R.id.ListViewLibrosDisponibles);

        ArrayList <Libro> arrayLibros = AddReserva.this.listaLibros();
        List<String> arrayTitulos = new ArrayList<>();

        for (int i = 0; i < arrayLibros.size(); i++) {
            arrayTitulos.add(arrayLibros.get(i).getTitulo());
        }

        listViewLibros.setAdapter(
                new ArrayAdapter<String>(
                        this, android.R.layout.simple_expandable_list_item_1, arrayTitulos
                         )
        );

        listViewLibros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mostrarAlertDialog(view, arrayLibros.get(i));

            }
        });

        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddReserva.this, UsuarioView.class);
                intent.putExtra("usuarioLogueado",usuarioActual.getDni());

                AddReserva.this.startActivity(intent);
                AddReserva.this.finish();
            }
        });

    }

    private void mostrarAlertDialog (View v, Libro libro){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddReserva.this);
        //Libro libro = adapterView.getItemAtPosition(i);
        builder.setTitle(libro.getTitulo());

        String msg = "Autor: "+ libro.getAutor()
                +"\n\n Quiere reservar este libro?";

        builder.setMessage(msg);


        builder.setNegativeButton("Cancelar", null);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                libro.setReservado(1);
                libroFacade.updateLibro(libro);
                Reserva reserva = new Reserva(usuarioActual, libro);
                reservasFacade.createReservas(reserva);


                Intent intent = new Intent(AddReserva.this, UsuarioView.class);

                intent.putExtra("usuarioLogueado",usuarioActual.getDni());

                AddReserva.this.startActivity(intent);
                AddReserva.this.finish();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    @SuppressLint("Range")
    private ArrayList<Libro> listaLibros(){

        Cursor librosCursor = libroFacade.getLibros();

        ArrayList<Libro> arrayList = new ArrayList<>();

        while(librosCursor.moveToNext()){
            Libro temp = new Libro();

            temp.setId(librosCursor.getLong(librosCursor.getColumnIndex(DBManager.LIBROS_ID)));
            temp.setCodigo(librosCursor.getString(librosCursor.getColumnIndex(DBManager.LIBROS_CODIGO)));
            temp.setTitulo(librosCursor.getString(librosCursor.getColumnIndex(DBManager.LIBROS_TITULO)));
            temp.setAutor(librosCursor.getString(librosCursor.getColumnIndex(DBManager.LIBROS_AUTOR)));
            temp.setReservado(librosCursor.getInt(librosCursor.getColumnIndex(DBManager.LIBROS_RESERVADO)));

            if (!temp.estaReservado()){
                arrayList.add(temp);
            }

        }

        return arrayList;
    }
}