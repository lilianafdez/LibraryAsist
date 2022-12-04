package com.example.libraryasist.core;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.example.libraryasist.R;
import com.example.libraryasist.database.UsuarioFacade;

public class UsuarioCursorAdapter extends CursorAdapter {
    private UsuarioFacade usuarioFacade;

    public UsuarioCursorAdapter(Context context, Cursor c, UsuarioFacade usuarioFacade) {
        super(context, c, false);
        this.usuarioFacade = usuarioFacade;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
