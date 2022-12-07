package com.example.libraryasist.view;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.libraryasist.MainActivity;
import com.example.libraryasist.MyApplication;
import com.example.libraryasist.R;
import com.example.libraryasist.adapter.LibrosAdapterCursor;
import com.example.libraryasist.core.Libro;
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

        Button btAdd=(Button) this.findViewById(R.id.buttonAnhadirLibro);
        this.createLibros();

        Cursor cursorlibros = this.libros.getLibros();

        this.librosAdapter=new LibrosAdapterCursor(this,cursorlibros,this.libros);
        this.listViewLibros= (ListView) this.findViewById(R.id.lvAdmin);

        this.registerForContextMenu(listViewLibros);

        this.listViewLibros.setAdapter(librosAdapter);

        this.listViewLibros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


            }
        });

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent anhadirLibro=new Intent(Vista_admin.this, AddLibro.class);
                Vista_admin.this.startActivity(anhadirLibro);
            }
        });
    }

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

    @Override
    public void onStop(){
        super.onStop();
        if(this.librosAdapter.getCursor()!=null){
            this.librosAdapter.getCursor().close();
        }
    }

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
                borrarLibro(libros.getLibroById(id));
                Toast.makeText(Vista_admin.this,"Libro Borrado con Éxito",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Vista_admin.this, Vista_admin.class);
                Vista_admin.this.startActivity(intent);
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

    private void createLibros(){
        Libro libro1=new Libro("ISBN111","El Quijote","Cervantes");
        Libro libro2=new Libro("ISBN222","La Fundacion","Antonio Buero Vallejo");
        Libro libro3=new Libro("ISBN333","Cumbres Borrascosas","Emily Bronte");
        Libro libro4=new Libro("ISBN444","Cronica de una Muerte Anunciada","Gabriel Garcia Marquez");

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
}
