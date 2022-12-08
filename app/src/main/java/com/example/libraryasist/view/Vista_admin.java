package com.example.libraryasist.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.libraryasist.MainActivity;
import com.example.libraryasist.MyApplication;
import com.example.libraryasist.R;
import com.example.libraryasist.adapter.LibrosAdapterCursor;
import com.example.libraryasist.core.Libro;
import com.example.libraryasist.core.Reserva;
import com.example.libraryasist.core.Usuario;
import com.example.libraryasist.database.DBManager;
import com.example.libraryasist.model.LibroFacade;

import java.util.ArrayList;
import java.util.Locale;


public class Vista_admin extends AppCompatActivity {
    private LibrosAdapterCursor librosAdapter;
    private LibroFacade libros;
    private ListView listViewLibros;
    private ArrayAdapter<String> itemsAdapter;
    private ArrayList<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_admin);

        DBManager db=((MyApplication) this.getApplication()).getDBManager();
        this.libros=new LibroFacade(db);

        Button btAdd= this.findViewById(R.id.buttonAnhadirLibro);
        this.createLibros();

        Cursor cursorlibros = this.libros.getLibros();

        this.librosAdapter=new LibrosAdapterCursor(this,cursorlibros,this.libros);
        this.listViewLibros= (ListView) this.findViewById(R.id.lvAdmin);

        this.registerForContextMenu(listViewLibros);

        this.listViewLibros.setAdapter(librosAdapter);
        //Al pulsar en "Añadir", se llama a la vista AddLibro para poder añadir un nuevo libro a la BBDD.
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent anhadirLibro=new Intent(Vista_admin.this, AddLibro.class);
                Vista_admin.this.startActivity(anhadirLibro);
            }
        });
    }
    //Cuando se restaura la actividad, se actualiza el listview.
    @Override
    public void onResume() {
        super.onResume();

        DBManager db=((MyApplication) this.getApplication()).getDBManager();
        this.libros=new LibroFacade(db);

        Cursor cursorlibros = this.libros.getLibros();
        this.librosAdapter=new LibrosAdapterCursor(this,cursorlibros,this.libros);
        this.listViewLibros= (ListView) this.findViewById(R.id.lvAdmin);

        this.listViewLibros.setAdapter(librosAdapter);
    }
    //Cerrar el cursor cuando se pare la actividad.
    @Override
    public void onStop(){
        super.onStop();
        if(this.librosAdapter.getCursor()!=null){
            this.librosAdapter.getCursor().close();
        }
    }
    //MENÚ GENERAL PARA QUE EL USUARIO PUEDA DESCONECTARSE DE LA SESIÓN.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_general,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

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
    //MENÚ CONTEXTUAL QUE PERMITE BORRAR UN LIBRO MANTENIENDO PULSADO SOBRE ÉL.
    public void onCreateContextMenu(ContextMenu contxt, View v, ContextMenu.ContextMenuInfo cmi){
        if(v.getId() == R.id.lvAdmin){
            this.getMenuInflater().inflate(R.menu.context_menu, contxt);
        }
    }

    public boolean onContextItemSelected(MenuItem item){
        boolean toret = false;
        long id=((AdapterView.AdapterContextMenuInfo) item.getMenuInfo() ).id;
        switch(item.getItemId()) {
            case R.id.context_opDel:
                mostrarAlertDialog(libros.getLibroById(id));
                break;
            case R.id.context_opCancel:
                Toast.makeText(Vista_admin.this,"Operacion Cancelada",Toast.LENGTH_SHORT).show();
                break;
        }
        return toret;

    }

    private void borrarLibro(Libro libro){
        libros.removeLibro(libro);
    }

    //Se crean 4 libros para que ya existan en la BBDD al iniciar la aplicación.
    private void createLibros(){
        Libro libro1=new Libro("ISBN111","El Quijote","Cervantes");
        Libro libro2=new Libro("ISBN222","La Fundacion","Antonio Buero Vallejo");
        Libro libro3=new Libro("ISBN333","Cumbres Borrascosas","Emily Bronte");
        Libro libro4=new Libro("ISBN444","El Cuarto Mono","J. D. Barker");

        if(!libros.checkLibro(libro1.getCodigo())){
            libros.createLibro(libro1);
        }
        if(!libros.checkLibro(libro2.getCodigo())){
            libros.createLibro(libro2);
        }
        if(!libros.checkLibro(libro3.getCodigo())){
            libros.createLibro(libro3);
        }
        if(!libros.checkLibro(libro4.getCodigo())){
            libros.createLibro(libro4);
        }
    }

    //Función para mostrar un diálogo que pregunte al administrador si está seguro de querer borrar el libro seleccionado.
    private void mostrarAlertDialog (Libro libro){
        AlertDialog.Builder builder = new AlertDialog.Builder(Vista_admin.this);
        builder.setTitle(libro.getTitulo());

        String msg = "Autor: "+ libro.getAutor()
                +"\n\n Eliminar este libro de la biblioteca?";

        builder.setMessage(msg);


        builder.setNegativeButton("Cancelar", null);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            //Al pulsar "Aceptar", se elimina el libro y se informa al admin de que se ha realizado con éxito.
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                borrarLibro(libro);
                Toast.makeText(Vista_admin.this,"Libro borrado con éxito",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Vista_admin.this, Vista_admin.class);
                Vista_admin.this.startActivity(intent);

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
