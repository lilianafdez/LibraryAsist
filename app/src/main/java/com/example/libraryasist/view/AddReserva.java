package com.example.libraryasist.view;



import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import com.example.libraryasist.database.DBManager;
import com.example.libraryasist.model.LibroFacade;

import java.util.ArrayList;
import java.util.List;

public class AddReserva extends AppCompatActivity {

    LibroFacade libroFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reserva);

        DBManager dbManager = ((MyApplication) this.getApplication()).getDBManager();
        libroFacade = new LibroFacade(dbManager);

        Button botonAñadir = (Button) this.findViewById(R.id.buttonAddReserva);

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
                AlertDialog.Builder builder = new AlertDialog.Builder(AddReserva.this);
                //Libro libro = adapterView.getItemAtPosition(i);
                builder.setTitle(arrayLibros.get(i).getTitulo());

                String msg = "Autor: "+ arrayLibros.get(i).getAutor()
                        +"\n\n Quiere reservar este libro?";

                builder.setMessage(msg);


                builder.setNegativeButton("Cancelar", null);
                builder.setPositiveButton("Aceptar", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


    }

    private void añadirReserva(Libro libro){



    }



    @SuppressLint("Range")
    private ArrayList<Libro> listaLibros(){

        Cursor librosCursor = libroFacade.getLibros();

        ArrayList<Libro> arrayList = new ArrayList<>();

        while(librosCursor.moveToNext()){
            Libro temp = new Libro();

            temp.setIsbm(librosCursor.getString(librosCursor.getColumnIndex(DBManager.LIBROS_CODIGO)));
            temp.setTitulo(librosCursor.getString(librosCursor.getColumnIndex(DBManager.LIBROS_TITULO)));
            temp.setAutor(librosCursor.getString(librosCursor.getColumnIndex(DBManager.LIBROS_AUTOR)));

            arrayList.add(temp);
        }

        return arrayList;
    }
}