package com.example.libraryasist.view;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.libraryasist.MyApplication;
import com.example.libraryasist.R;
import com.example.libraryasist.database.DBManager;
import com.example.libraryasist.model.LibroFacade;


public class UsuarioView extends AppCompatActivity {

    LibroFacade libroFacade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_usuario);

        DBManager dbManager = ((MyApplication) this.getApplication()).getDBManager();
        libroFacade = new LibroFacade(dbManager);

        ListView listViewLibros = (ListView) this.findViewById(R.id.listViewReserva);

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

}
