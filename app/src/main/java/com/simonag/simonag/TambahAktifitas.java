package com.simonag.simonag;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pixplicity.easyprefs.library.Prefs;
import com.simonag.simonag.model.Aktifitas;
import com.simonag.simonag.model.Satuan;
import com.simonag.simonag.utils.AlertDialogCustom;
import com.simonag.simonag.utils.Config;
import com.simonag.simonag.utils.GetToken;
import com.simonag.simonag.utils.VolleyClass;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    @BindView(R.id.sp_kategori)
    Spinner spKategori;
    HashMap<String, Integer> satuanMap;
    SimpleDateFormat dateFormatter;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    Aktifitas aktifitas;
    int id_program;
    NumberPicker presentase;
    @BindView(R.id.tv_target_presentase)
    TextView tvTargetPresentase;

    @BindView(R.id.judul_revenue)
    TextView judulRevenue;
    @BindView(R.id.ac_satuan)
    AutoCompleteTextView acSatuan;
    @BindView(R.id.tv_duedate)
    TextView tvDuedate;

    DatePickerDialog datepicker;

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
        showActionBar();
        setTitle("Tambah Aktifitas");
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
//        Kategori spinner
        String[] kategoriArray = new String[3];
        kategoriArray[0] = "Kualitas";
        kategoriArray[1] = "Kuantitas";
        kategoriArray[2] = "Komersial";
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategoriArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKategori.setAdapter(adapter);
        spKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int item = spKategori.getSelectedItemPosition();
                if (item == 0) {
                    etRevenue.setVisibility(View.GONE);
                    judulRevenue.setVisibility(View.GONE);
                    acSatuan.setText("%");
                    acSatuan.setEnabled(false);
                    tvTargetPresentase.setVisibility(View.VISIBLE);
                    etTarget.setVisibility(View.GONE);
                } else if (item == 1) {
                    etRevenue.setVisibility(View.GONE);
                    judulRevenue.setVisibility(View.GONE);
                    acSatuan.setEnabled(true);
                    tvTargetPresentase.setVisibility(View.GONE);
                    etTarget.setVisibility(View.VISIBLE);
                } else {
                    etRevenue.setVisibility(View.VISIBLE);
                    judulRevenue.setVisibility(View.VISIBLE);
                    acSatuan.setEnabled(true);
                    tvTargetPresentase.setVisibility(View.GONE);
                    etTarget.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });
        getSatuan();
        id_program = getIntent().getExtras().getInt("id_program");
        if (getIntent().hasExtra("aktifitas")) {
            button.setText("Simpan");
            setTitle("Edit Aktivfitas");
            aktifitas = Parcels.unwrap(getIntent().getParcelableExtra("aktifitas"));
            etNama.setText(aktifitas.getNama());
            etTarget.setText(aktifitas.getTarget() + "");
            tvTargetPresentase.setText(aktifitas.getTarget() + "");
            etRevenue.setText(aktifitas.getTarget_revenue() + "");
            spKategori.setSelection(aktifitas.getIdKategori() - 1);
            acSatuan.setText(aktifitas.getSatuan());
            tvDuedate.setText(aktifitas.getDuedate());
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

    private void getpresentase() {
        final AlertDialog dialog = buildDialog("Target");
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTargetPresentase.setText(String.valueOf(presentase.getValue()));
                dialog.dismiss();
            }
        });
    }

    private AlertDialog buildDialog(String title) {
        AlertDialog.Builder result = new AlertDialog.Builder(this);
        View alertView = getLayoutInflater().inflate(R.layout.dialog_presentase, null);
        presentase = (NumberPicker) alertView.findViewById(R.id.presentase);
        presentase.setMinValue(0);
        presentase.setMaxValue(100);
        result.setTitle(title)
                .setView(alertView)
                .setPositiveButton("Simpan", null);
        AlertDialog dialog = result.create();
        dialog.show();
        return dialog;
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

    @OnClick({R.id.button, R.id.tv_target_presentase, R.id.tv_duedate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button:
                tambah_aktifitas();
                break;
            case R.id.tv_target_presentase:
                getpresentase();
                break;
            case R.id.tv_duedate:
                getdate();
                break;
        }
    }


    private void tambah_aktifitas() {
        int id_kategori = spKategori.getSelectedItemPosition() + 1;
        int id_satuan = satuanMap.get(acSatuan.getText().toString());
        String nama_satuan = acSatuan.getText().toString();
        String deadline = tvDuedate.getText().toString();
        String keterangan = "null";
        String nama_aktivitas = etNama.getText().toString();
        int target_nilai = 0;
        try {
            target_nilai = Integer.parseInt(etTarget.getText().toString());
        } catch (Exception e) {

        }

        int revenue_target_nilai = 0;
        try {
            if (etRevenue.getText().toString().isEmpty()) etRevenue.setText(0);
            revenue_target_nilai = Integer.parseInt(etRevenue.getText().toString());
        } catch (Exception e) {

        }
        AlertDialogCustom ad = new AlertDialogCustom(this);

        if (id_kategori == 1) {
            if (nama_aktivitas.equals("") || deadline.equals("")) {
                ad.simple("Peringatan", "Data harus terisi semua", R.drawable.info_danger, null);
                return;
            }
        } else if (id_kategori == 2) {
            if (nama_aktivitas.equals("") || deadline.equals("") || nama_satuan.equals("")) {
                ad.simple("Peringatan", "Data harus terisi semua", R.drawable.info_danger, null);
                return;
            }
        }else if(id_kategori == 3){
            if (nama_aktivitas.equals("") || deadline.equals("") || nama_satuan.equals("")) {
                ad.simple("Peringatan", "Data harus terisi semua", R.drawable.info_danger, null);
                return;
            }
        }

        if (getIntent().hasExtra("aktifitas"))
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

    private void getSatuan() {
        avi.show();
        String tokena = Prefs.getString(Config.TOKEN_BUMN, "");
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = Config.URL_GET_ALL_SATUAN + tokena;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        avi.hide();
                        try {
                            if (response.getString("status").equals("success")) {
                                ArrayList<Satuan> db = jsonDecodeSatuan(response.getString("satuan"));
                                String[] satuanArray = new String[db.size()];
                                satuanMap = new HashMap<>();
                                for (int i = 0; i < db.size(); i++) {
                                    satuanMap.put(db.get(i).getNama_satuan(), db.get(i).getId_satuan());
                                    satuanArray[i] = db.get(i).getNama_satuan();
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(TambahAktifitas.this, android.R.layout.simple_list_item_1, satuanArray);
                                acSatuan.setAdapter(adapter);
                                acSatuan.setThreshold(1);
                            } else if (response.getString("status").equals("invalid-token")) {
                                GetToken k = new GetToken(TambahAktifitas.this);
                                k.setCallback(new GetToken.callback() {
                                    @Override
                                    public void action(boolean success) {
                                        getSatuan();
                                    }
                                });
                            }

                        } catch (JSONException E) {
                            Log.e("json_error", E.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(getRequest);
        queue.add(getRequest);
    }


    public ArrayList<Satuan> jsonDecodeSatuan(String jsonStr) {
        ArrayList<Satuan> billing = new ArrayList<>();
        if (jsonStr != null) {
            try {
                JSONArray transaksi = new JSONArray(jsonStr);
                for (int i = 0; i < transaksi.length(); i++) {
                    JSONObject jObject = transaksi.getJSONObject(i);
                    Satuan d = new Satuan(
                            jObject.getInt("id_satuan"),
                            jObject.getString("nama_satuan")
                    );
                    billing.add(d);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return billing;
    }

}