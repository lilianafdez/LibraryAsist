package com.example.libraryasist.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.libraryasist.MyApplication;
import com.example.libraryasist.R;
import com.example.libraryasist.core.Libro;
import com.example.libraryasist.database.DBManager;
import com.example.libraryasist.model.LibroFacade;

public class AddLibro extends AppCompatActivity {

    LibroFacade libroFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_libro);

        DBManager dbManager = ((MyApplication) this.getApplication()).getDBManager();

        libroFacade = new LibroFacade(dbManager);

        Button botonAddLibro = this.findViewById(R.id.buttonAddLibro);
        Button botonVolver = this.findViewById(R.id.buttonVolverAddLibro);

        EditText etCodigo = this.findViewById(R.id.editTextCodigo);
        EditText etTituloLibro = this.findViewById(R.id.editTextTitulo);
        EditText etAutorLibro = this.findViewById(R.id.editTextAutor);


        botonAddLibro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Libro libroAnhadir = new Libro( etCodigo.getText().toString(),etTituloLibro.getText().toString(),etAutorLibro.getText().toString());
                libroFacade.createLibro(libroAnhadir);

                Intent intent = new Intent(AddLibro.this, Vista_admin.class);

                AddLibro.this.startActivity(intent);
                AddLibro.this.finish();
            }
        });


        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent volver = new Intent(AddLibro.this, Vista_admin.class);
                AddLibro.this.startActivity(volver);
            }
        });
    }

    public static long convertToLong(String strNum) {
        long valor;
        try {
            valor = Long.parseLong(strNum);
        } catch (NumberFormatException | NullPointerException nfe) {
            return 0; //Valor default en caso de no poder convertir  a Long
        }
        return valor;
    }
}
