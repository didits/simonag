package com.simonag.simonag.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simonag.simonag.R;

/**
 * Created by diditsepiyanto on 5/30/17.
 */

public class AlertDialogCustom {
    private Context context;
    AlertDialog alert;

    public AlertDialogCustom(Context context) {
        this.context = context;
        AlertDialog.Builder dialognya = new AlertDialog.Builder(context);
        alert = dialognya.create();
    }

    public void simple(String judul, String isi, int gambar, View.OnClickListener l) {
        LayoutInflater li = LayoutInflater.from(context);
        View inputnya = li.inflate(R.layout.dialog_simple, null);
        final Button btnYa = (Button) inputnya.findViewById(R.id.ya);
        final Button btnTidak = (Button) inputnya.findViewById(R.id.tidak);
        TextView judulTextView = (TextView) inputnya.findViewById(R.id.judul_dialog);
        TextView isiTextView = (TextView) inputnya.findViewById(R.id.isi);
        ImageView gambarImg = (ImageView) inputnya.findViewById(R.id.gambar);
        gambarImg.setBackgroundDrawable(inputnya.getResources().getDrawable(gambar));

        judulTextView.setText(judul);
        isiTextView.setText(isi);
        btnTidak.setVisibility(View.GONE);
        btnYa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.cancel();
            }
        });

        alert.setView(inputnya);
        alert.show();
    }

    public void konfirmasi(String judul, String isi, int gambar, View.OnClickListener l, String ya, String tidak) {
        LayoutInflater li = LayoutInflater.from(context);
        View inputnya = li.inflate(R.layout.dialog_simple, null);
        Button btnYa = (Button) inputnya.findViewById(R.id.ya);
        Button btnBatal = (Button) inputnya.findViewById(R.id.tidak);

        btnYa.setText(ya);
        btnBatal.setText(tidak);

        TextView judulTextView = (TextView) inputnya.findViewById(R.id.judul_dialog);
        TextView isiTextView = (TextView) inputnya.findViewById(R.id.isi);
        ImageView gambarImg = (ImageView) inputnya.findViewById(R.id.gambar);
        gambarImg.setBackgroundDrawable(inputnya.getResources().getDrawable(gambar));

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.cancel();
            }
        });
        judulTextView.setText(judul);
        isiTextView.setText(isi);
        btnYa.setOnClickListener(l);

        alert.setView(inputnya);
        alert.show();
    }

    public void tanggal_awal_akhir(String judul, String isi, int gambar, View.OnClickListener l, String ya, String tidak) {
        LayoutInflater li = LayoutInflater.from(context);
        View inputnya = li.inflate(R.layout.dialog_kalender, null);
        Button btnYa = (Button) inputnya.findViewById(R.id.ya);
        Button btnBatal = (Button) inputnya.findViewById(R.id.tidak);

        btnYa.setText(ya);
        btnBatal.setText(tidak);

        TextView judulTextView = (TextView) inputnya.findViewById(R.id.judul_dialog);
        TextView isiTextView = (TextView) inputnya.findViewById(R.id.isi);
        ImageView gambarImg = (ImageView) inputnya.findViewById(R.id.gambar);
        gambarImg.setBackgroundDrawable(inputnya.getResources().getDrawable(gambar));

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.cancel();
            }
        });
        judulTextView.setText(judul);
        isiTextView.setText(isi);
        btnYa.setOnClickListener(l);

        alert.setView(inputnya);
        alert.show();
    }



    public void dismiss(){
        if(alert != null)
        alert.cancel();
    }
}
