package com.example.libraryasist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.libraryasist.R;
import com.example.libraryasist.view.ListViewReservas;

import java.util.List;

public class ReservasArrayAdapter extends ArrayAdapter<ListViewReservas> {


    public ReservasArrayAdapter(@NonNull Context context, List<ListViewReservas> entries) {
        super(context, 0, entries);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        final LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
        final ListViewReservas entry = this.getItem(position);

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.listview_reservas, null);
        }

        final CheckBox chkSelected = convertView.findViewById(R.id.chkEntryReserva);
        final TextView lblReserva = convertView.findViewById(R.id.lblEntryTituloReserva);

        chkSelected.setChecked(entry.isCheked());
        lblReserva.setText(entry.getTitulo());

        return convertView;
    }


}
