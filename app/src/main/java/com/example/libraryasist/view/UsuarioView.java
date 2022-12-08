package com.example.libraryasist.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
        //Inicializamos las clases fachadas
        reservasFacade = new ReservasFacade(dbManager);
        libroFacade = new LibroFacade(dbManager);
        usuarioFacade = new UsuarioFacade(dbManager);
        this.createLibros(); //Introduce unos libros de prueba

        //Guardamos informacion del usuario logeado para poder mostrar las reservas
        String dni = getIntent().getStringExtra("dniUsuario");
        usuarioActual = usuarioFacade.getUsuariosByDni(dni);


        //asociamos los botones y el listView
        listViewLibros = (ListView) this.findViewById(R.id.listViewReserva);
        Button botonAñadir = (Button) this. findViewById(R.id.buttonVistaAñadirReserva);
        Button botonEliminar = (Button) this. findViewById(R.id.buttonEliminarReservas);


        UsuarioView.this.actualizarListView();//funcion encargada de cargar los datos en el listView

        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UsuarioView.this.borrarReservas(filas);//funcion encargada de borrar las reservas selecionadas

            }
        });

        //al clickar te envia a la vista de añadir
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
        arrayReservas = listaReserva();//obtemos la lista de reservas
        filas = new ArrayList<>();//el array que se asocia al listView

        for (int i = 0; i < arrayReservas.size(); i++) {
            filas.add(i, new ListViewReservas(arrayReservas.get(i).getLibro().getTitulo(), arrayReservas.get(i).getLibro().getId()));
            //añadimos los titulos y codigos de los libros
        }
        this.adaptadorArray = new ReservasArrayAdapter(this, filas); //asociamos la lista de los titulos al array

        listViewLibros.setAdapter(this.adaptadorArray); //establecemos el adaptador al listView

        //creamos un listener que cuando se pulse la reserva se seleccione o deseleccione el chek para eliminar
        listViewLibros.setOnItemClickListener(new AdapterView.OnItemClickListener(
                                              ) {
                                                  @Override
                                                  public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                                      UsuarioView.this.adaptadorArray.getItem(i).invertirSeleccion();
                                                      UsuarioView.this.adaptadorArray.notifyDataSetChanged(); //actualizamos los cambios


                                                  }
                                              }

        );
    }


    @SuppressLint("Range")
    private List<String> listaLibros(){
        //Listamos los libros
        Cursor librosCursor = libroFacade.getLibros();

        ArrayList<String> arrayList = new ArrayList<>();

        while(librosCursor.moveToNext()){

            arrayList.add(librosCursor.getString(librosCursor.getColumnIndex(DBManager.LIBROS_TITULO)));
        }

        return arrayList;
    }

    @SuppressLint("Range")
    private ArrayList<Reserva> listaReserva(){
        //Listamos las reservas del usuario logueado a traves de un cursor
        Cursor librosCursor = reservasFacade.getReservasByUser(usuarioActual.getDni());

        ArrayList<Reserva> arrayList = new ArrayList<>();

        //Extraemos los libros del cursor
        while(librosCursor.moveToNext()){


            Libro temp = libroFacade.getLibroById(librosCursor.getLong(librosCursor.getColumnIndex(DBManager.USUARIO_LIBROS_LIBRO_ID)));

            arrayList.add(new Reserva(usuarioActual,temp));
        }

        return arrayList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Creamos el menu de desconexion
        this.getMenuInflater().inflate(R.menu.menu_general,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Creamos las opciones del menu
        switch (item.getItemId()){

            case R.id.miLogOut:
                MyApplication myapp=(MyApplication) this.getApplication();
                myapp.setLogeado(null);
                myapp.setId_user_logged(0);

                this.startActivity(new Intent(this.getBaseContext(),MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));

                this.finish();

                break;
        }
        return true;
    }

    private void borrarReservas(List<ListViewReservas> arrayReservas){

        List<Reserva> arrayLibrosEliminar = new ArrayList<>();//Inicializamos el array de los libros seleccionados para eliminar

        for (int i = 0; i < arrayReservas.size(); i++) {
            if(arrayReservas.get(i).isCheked()){ //Se comprueba que la reserva este selecionado para poder elimianr
                Libro temp = libroFacade.getLibroById(arrayReservas.get(i).getId());
                Reserva reservaAñadir = new Reserva(usuarioActual, temp);
                arrayLibrosEliminar.add(reservaAñadir); //añadimos la reserva a eliminar
            }
        }

        //Si hay reservas introducidas se crea un alertDialog de confirmacion
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
            //Si aceptas se elimina las reservas selecionadas
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    for (int j = 0; j < arrayLibrosEliminar.size(); j++) {
                        reservasFacade.removeLibro(arrayLibrosEliminar.get(j));//usamos la clase fachada para eliminar las reservas
                        Libro libroSinReserva = arrayLibrosEliminar.get(j).getLibro(); //Se quita la reserva del libro
                        libroSinReserva.setReservado(0);
                        libroFacade.updateLibro(libroSinReserva);//actualizamos la informacion del libro


                        UsuarioView.this.actualizarListView();// actualizamos la listView para quitar las reservas eliminadas
                        UsuarioView.this.adaptadorArray.notifyDataSetChanged();

                    }
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();// mostramos el dialog


        }else{
            Toast.makeText(UsuarioView.this, "Ningun libro seleccionado", Toast.LENGTH_SHORT).show();
        }
    }

    private void createLibros(){
        Libro libro1=new Libro("ISBN111","El Quijote","Cervantes");
        Libro libro2=new Libro("ISBN222","La Fundacion","Antonio Buero Vallejo");
        Libro libro3=new Libro("ISBN333","Cumbres Borrascosas","Emily Bronte");
        Libro libro4=new Libro("ISBN444","El Cuarto Mono","J. D. Barker");

        if(!libroFacade.checkLibro(libro1.getCodigo())){
            libroFacade.createLibro(libro1);
        }
        if(!libroFacade.checkLibro(libro2.getCodigo())){
            libroFacade.createLibro(libro2);
        }
        if(!libroFacade.checkLibro(libro3.getCodigo())){
            libroFacade.createLibro(libro3);
        }
        if(!libroFacade.checkLibro(libro4.getCodigo())){
            libroFacade.createLibro(libro4);
        }
    }

}
