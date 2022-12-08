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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.libraryasist.MainActivity;
import com.example.libraryasist.MyApplication;
import com.example.libraryasist.R;
import com.example.libraryasist.adapter.ReservasArrayAdapter;
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

    private ReservasArrayAdapter adaptadorArray;

    List<Reserva> arrayReservas;
    List<ListViewReservas> filas;
    ListView listViewLibros;

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

        listViewLibros = (ListView) this.findViewById(R.id.listViewReserva);
        Button botonAñadir = (Button) this. findViewById(R.id.buttonVistaAñadirReserva);
        Button botonEliminar = (Button) this. findViewById(R.id.buttonEliminarReservas);




        UsuarioView.this.actualizarListView();

        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsuarioView.this.borrarReservas(filas);

            }
        });

        botonAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UsuarioView.this, AddReserva.class);

                intent.putExtra("usuarioActual", usuarioActual.getDni());

                UsuarioView.this.startActivity(intent);
            }
        });

    }

    private void actualizarListView (){
        arrayReservas = new ArrayList<>();
        arrayReservas = UsuarioView.this.listaReserva();
        filas = new ArrayList<>();

        for (int i = 0; i < arrayReservas.size(); i++) {
            filas.add(i, new ListViewReservas(arrayReservas.get(i).getLibro().getTitulo(), arrayReservas.get(i).getLibro().getId()));
        }
        this.adaptadorArray = new ReservasArrayAdapter(this, filas);

        listViewLibros.setAdapter(this.adaptadorArray);

        listViewLibros.setOnItemClickListener(new AdapterView.OnItemClickListener(

                                              ) {
                                                  @Override
                                                  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                                      UsuarioView.this.adaptadorArray.getItem(i).invertirSeleccion();
                                                      UsuarioView.this.adaptadorArray.notifyDataSetChanged();


                                                  }
                                              }

        );
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

    private void borrarReservas(List<ListViewReservas> arrayReservas){

        List<Reserva> arrayLibrosEliminar = new ArrayList<>();

        for (int i = 0; i < arrayReservas.size(); i++) {
            if(arrayReservas.get(i).isCheked()){
                Libro temp = libroFacade.getLibroById(arrayReservas.get(i).getId());
                Reserva reservaAñadir = new Reserva(usuarioActual, temp);
                arrayLibrosEliminar.add(reservaAñadir);
            }
        }

        if(arrayLibrosEliminar.size()>0){
            AlertDialog.Builder builder = new AlertDialog.Builder(UsuarioView.this);
            builder.setTitle("Eliminar reservas");
            StringBuilder msg = new StringBuilder();

            msg.append("¿Estas seguro de querer eliminar estas reservas?\n");
            for (int i = 0; i < arrayLibrosEliminar.size(); i++) {
                msg.append(arrayLibrosEliminar.get(i).getLibro().getTitulo()).append("\n");
            }
            builder.setMessage(msg);
            builder.setNegativeButton("Cancelar", null);
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    for (int j = 0; j < arrayLibrosEliminar.size(); j++) {
                        reservasFacade.removeLibro(arrayLibrosEliminar.get(j));
                        Libro libroSinReserva = arrayLibrosEliminar.get(j).getLibro();
                        libroSinReserva.setReservado(0);
                        libroFacade.updateLibro(libroSinReserva);


                        UsuarioView.this.actualizarListView();
                        UsuarioView.this.adaptadorArray.notifyDataSetChanged();

                    }
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();


        }else{
            Toast.makeText(UsuarioView.this, "Ningun libro seleccionado", Toast.LENGTH_SHORT).show();
        }


    }

}
