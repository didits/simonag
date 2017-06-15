package com.simonag.simonag;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;
import com.simonag.simonag.model.Aktifitas;
import com.simonag.simonag.utils.Config;
import com.simonag.simonag.utils.GetToken;
import com.simonag.simonag.utils.VolleyClass;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by diditsepiyanto on 6/14/17.
 */

public class TambahAktifitas extends AppCompatActivity {

    @BindView(R.id.et_nama)
    EditText etNama;
    @BindView(R.id.et_target)
    EditText etTarget;
    @BindView(R.id.et_revenue)
    EditText etRevenue;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.tv_duedate)
    TextView tvDuedate;
    @BindView(R.id.sp_kategori)
    Spinner spKategori;
    @BindView(R.id.sp_satuan)
    Spinner spSatuan;
    HashMap<Integer, String> kategoriMap, satuanMap;
    DatePickerDialog datepicker;
    SimpleDateFormat dateFormatter;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    Aktifitas aktifitas;
    int id_program;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Asap-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_tambah_aktifitas);
        ButterKnife.bind(this);
        avi.hide();
        setTitle("Tambah Aktivitas");
        showActionBar();
//        Kategori spinner
        String[] kategoriArray = new String[3];
        kategoriMap = new HashMap<Integer, String>();
        kategoriMap.put(0, "Kualitas");
        kategoriMap.put(1, "Kuantitas");
        kategoriMap.put(2, "Komersial");
        kategoriArray[0] = "Kualitas";
        kategoriArray[1] = "Kuantitas";
        kategoriArray[2] = "Komersial";
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategoriArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKategori.setAdapter(adapter);
//        Satuan Spinner
        String[] satuanArray = new String[5];
        satuanMap = new HashMap<Integer, String>();
        satuanMap.put(0, "Unit");
        satuanMap.put(1, "Gigabyte");
        satuanMap.put(2, "Penumpang");
        satuanMap.put(3, "Pelanggan");
        satuanMap.put(4, "Orang");
        satuanArray[0] = "Unit";
        satuanArray[1] = "Gigabyte";
        satuanArray[2] = "Penumpang";
        satuanArray[3] = "Pelanggan";
        satuanArray[4] = "Orang";
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, satuanArray);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSatuan.setAdapter(adapter2);
        id_program = getIntent().getExtras().getInt("id_program");
        if(getIntent().hasExtra("aktifitas")) {
            aktifitas = Parcels.unwrap(getIntent().getParcelableExtra("aktifitas"));
            tvDuedate.setText(aktifitas.getDuedate());
            etNama.setText(aktifitas.getNama());
            etTarget.setText(aktifitas.getTarget()+"");
            etRevenue.setText(aktifitas.getRealisasi()+"");
        }
    }

    private void showActionBar() {
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setElevation(0);
        actionbar.setDisplayUseLogoEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.tv_duedate, R.id.button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_duedate:
                getdate();
                break;
            case R.id.button:
                tambah_aktifitas();
                break;
        }
    }

    private void getdate() {
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        datepicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tvDuedate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datepicker.show();
    }

    private void tambah_aktifitas() {
        int id_kategori = spKategori.getSelectedItemPosition()+1;
        int id_satuan = 0;
        String nama_satuan = spKategori.getSelectedItem().toString();
        String deadline = tvDuedate.getText().toString();
        String keterangan = "cobacoba";
        String nama_aktivitas = etNama.getText().toString();
        int target_nilai = Integer.parseInt(etTarget.getText().toString());
        int revenue_target_nilai = Integer.parseInt(etRevenue.getText().toString());
        if(getIntent().hasExtra("aktifitas"))
            editAktifitas(
                    aktifitas.getId(),
                    id_program,
                    id_kategori,
                    id_satuan,
                    nama_satuan,
                    deadline,
                    keterangan,
                    nama_aktivitas,
                    target_nilai,
                    revenue_target_nilai);
        else
        uploadAktifitas(
                id_program,
                id_kategori,
                id_satuan,
                nama_satuan,
                deadline,
                keterangan,
                nama_aktivitas,
                target_nilai,
                revenue_target_nilai);
    }


    private void editAktifitas(
            int id_target,
            int id_program,
            int id_kategori,
            int id_satuan,
            String nama_satuan,
            String deadline,
            String keterangan,
            String nama_aktivitas,
            int target_nilai,
            int revenue_target_nilai) {
        avi.show();
        String token = Prefs.getString(Config.TOKEN_BUMN, "");
        VolleyClass cek = new VolleyClass(this, true);
        cek.get_data_from_server(new VolleyClass.VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                avi.hide();
                try {
                    JSONObject jObject = new JSONObject(response);
                    String status = jObject.getString("status");
                    if (status.equals("edit-success")) {
                        Toast toast = Toast.makeText(TambahAktifitas.this, "Sukses Mengedit Program", Toast.LENGTH_LONG);
                        toast.show();
                        onBackPressed();
                    } else if (status.equals("wrong-id")) {
                        Toast.makeText(TambahAktifitas.this, "Aktivitas tidak ada", Toast.LENGTH_LONG).show();
                    } else if (status.equals("edit-failed")) {
                        Toast.makeText(TambahAktifitas.this, "Edit data gagal", Toast.LENGTH_LONG).show();
                    } else {
                        GetToken k = new GetToken(TambahAktifitas.this);
                        k.setCallback(new GetToken.callback() {
                            @Override
                            public void action(boolean success) {
                                tambah_aktifitas();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError() {
                avi.hide();
            }
        }, Config.URL_EDIT_TARGET_PROGRAM + token, new String[]{
                "id_target" + "|" + id_target,
                "id_program" + "|" + id_program,
                "id_kategori" + "|" + id_kategori,
                "id_satuan" + "|" + id_satuan,
                "nama_satuan" + "|" + nama_satuan,
                "deadline" + "|" + deadline,
                "keterangan" + "|" + keterangan,
                "nama_aktivitas" + "|" + nama_aktivitas,
                "target_nilai" + "|" + target_nilai,
                "revenue_target_nilai" + "|" + revenue_target_nilai
        });
    }

    private void uploadAktifitas(
            int id_program,
            int id_kategori,
            int id_satuan,
            String nama_satuan,
            String deadline,
            String keterangan,
            String nama_aktivitas,
            int target_nilai,
            int revenue_target_nilai) {
        avi.show();
        String token = Prefs.getString(Config.TOKEN_BUMN, "");
        VolleyClass cek = new VolleyClass(this, true);
        cek.get_data_from_server(new VolleyClass.VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                avi.hide();
                Log.d("respon onSuccess", response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    String status = jObject.getString("status");
                    if (status.equals("post-success")) {
                        Toast toast = Toast.makeText(TambahAktifitas.this, "Sukses Menambahkan Program", Toast.LENGTH_LONG);
                        toast.show();
                        onBackPressed();
                    } else if (status.equals("wrong-id")) {
                        Toast.makeText(TambahAktifitas.this, "Aktivitas tidak ada", Toast.LENGTH_LONG).show();
                    } else if (status.equals("post-failed")) {
                        Toast.makeText(TambahAktifitas.this, "Post data gagal", Toast.LENGTH_LONG).show();
                    } else {
                        GetToken k = new GetToken(TambahAktifitas.this);
                        k.setCallback(new GetToken.callback() {
                            @Override
                            public void action(boolean success) {
                                tambah_aktifitas();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError() {
                avi.hide();
            }
        }, Config.URL_POST_TARGET_PROGRAM + token, new String[]{
                "id_program" + "|" + id_program,
                "id_kategori" + "|" + id_kategori,
                "id_satuan" + "|" + id_satuan,
                "nama_satuan" + "|" + nama_satuan,
                "deadline" + "|" + deadline,
                "keterangan" + "|" + keterangan,
                "nama_aktivitas" + "|" + nama_aktivitas,
                "target_nilai" + "|" + target_nilai,
                "revenue_target_nilai" + "|" + revenue_target_nilai
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}