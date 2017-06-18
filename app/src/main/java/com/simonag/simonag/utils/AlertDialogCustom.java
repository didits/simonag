package com.simonag.simonag.utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.simonag.simonag.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by diditsepiyanto on 5/30/17.
 */

public class AlertDialogCustom {
    private Context context;
    AlertDialog alert;
    String tanggal;

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

    public void tanggal_awal_akhir(View.OnClickListener l) {
        LayoutInflater li = LayoutInflater.from(context);
        final View inputnya = li.inflate(R.layout.dialog_kalender, null);
        final Button btnYa = (Button) inputnya.findViewById(R.id.ya);
        Button btnBatal = (Button) inputnya.findViewById(R.id.tidak);
        final TextView tanggal_awal = (TextView) inputnya.findViewById(R.id.tanggal_awal);
        final TextView tanggal_akhir = (TextView) inputnya.findViewById(R.id.tanggal_akhir);

        btnYa.setEnabled(false);
        btnYa.setBackground(context.getResources().getDrawable(R.drawable.button_disabled));

        tanggal_awal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdate(inputnya);
            }
        });

        tanggal_akhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdateAkhir(inputnya);
            }
        });

        tanggal_awal.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!tanggal_awal.getText().toString().equals("") && !tanggal_akhir.getText().toString().equals("")) {
                    btnYa.setEnabled(true);
                    btnYa.setBackground(context.getResources().getDrawable(R.drawable.button));
                } else {
                    btnYa.setEnabled(false);
                    btnYa.setBackground(context.getResources().getDrawable(R.drawable.button_disabled));
                }
            }
        });

        tanggal_akhir.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!tanggal_awal.getText().toString().equals("") && !tanggal_akhir.getText().toString().equals("")) {
                    btnYa.setEnabled(true);
                    btnYa.setBackground(context.getResources().getDrawable(R.drawable.button));
                } else {
                    btnYa.setEnabled(false);
                    btnYa.setBackground(context.getResources().getDrawable(R.drawable.button_disabled));
                }
            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.cancel();
            }
        });
        btnYa.setOnClickListener(l);

        alert.setView(inputnya);
        alert.show();
    }

    private void getdate(final View v) {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datepicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                TextView k = (TextView) v.findViewById(R.id.tanggal_awal);
                k.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datepicker.show();
    }

    private void getdateAkhir(final View v) {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datepicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                TextView k = (TextView) v.findViewById(R.id.tanggal_akhir);
                k.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datepicker.show();
    }


    public void dismiss() {
        if (alert != null)
            alert.cancel();
    }
}
