package com.simonag.simonag;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.pixplicity.easyprefs.library.Prefs;
import com.shawnlin.numberpicker.NumberPicker;
import com.simonag.simonag.model.Aktifitas;
import com.simonag.simonag.model.Satuan;
import com.simonag.simonag.utils.AlertDialogCustom;
import com.simonag.simonag.utils.Config;
import com.simonag.simonag.utils.GetToken;
import com.simonag.simonag.utils.VolleyClass;
import com.simonag.simonag.utils.VolleyClass2;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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

    @BindView(R.id.tv_lokasi)
    EditText etLokasi;

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

    @BindView(R.id.ac_satuan)
    AutoCompleteTextView acSatuan;
    @BindView(R.id.tv_duedate)
    TextView tvDuedate;

    @BindView(R.id.text_target)
    TextInputLayout text_target;
    @BindView(R.id.text_target_presentase)
    TextInputLayout text_target_presentase;
    @BindView(R.id.text_target_revenue)
    TextInputLayout text_target_revenue;
    @BindView(R.id.txt_lokasi)
    TextInputLayout text_lokasi;
    @BindView(R.id.text_tanggal)
    TextInputLayout text_tanggal;
    @BindView(R.id.text_satuan)
    TextInputLayout text_satuan;
    @BindView(R.id.lin_file)
    LinearLayout lin_file;

    byte[] bytesArray = null;
    File uploadedImage;

    DatePickerDialog datepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Prefs.Builder()
                .setContext(this)
                .setMode(Context.MODE_PRIVATE)
                .setPrefsName(Config.SHARED_USER)
                .setUseDefaultSharedPreference(true)
                .build();
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
        String[] kategoriArray = new String[4];
        kategoriArray[0] = "Kualitas";
        kategoriArray[1] = "Kapasitas";
        kategoriArray[2] = "Komersial";
        kategoriArray[3] = "Komersial-Kontra Prestasi";
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategoriArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKategori.setAdapter(adapter);
        spKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int item = spKategori.getSelectedItemPosition();
                if (item == 0) {
                    text_target_revenue.setVisibility(View.GONE);
                    acSatuan.setText("%");
                    acSatuan.setEnabled(false);
                    text_target_presentase.setVisibility(View.VISIBLE);
                    text_target.setVisibility(View.GONE);
                    text_lokasi.setVisibility(View.GONE);
                    text_satuan.setVisibility(View.VISIBLE);
                    lin_file.setVisibility(View.GONE);
                    text_tanggal.setVisibility(View.VISIBLE);
                } else if (item == 1) {
                    text_target_revenue.setVisibility(View.GONE);
                    acSatuan.setEnabled(true);
                    acSatuan.setText("");
                    text_target_presentase.setVisibility(View.GONE);
                    text_target.setVisibility(View.VISIBLE);
                    text_lokasi.setVisibility(View.GONE);
                    text_satuan.setVisibility(View.VISIBLE);
                    lin_file.setVisibility(View.GONE);
                    text_tanggal.setVisibility(View.VISIBLE);
                } else if (item == 3) {
                    text_target_revenue.setVisibility(View.VISIBLE);
                    text_lokasi.setVisibility(View.VISIBLE);
                    text_target_presentase.setVisibility(View.GONE);
                    text_target.setVisibility(View.GONE);
                    text_satuan.setVisibility(View.GONE);
                    lin_file.setVisibility(View.VISIBLE);
                    text_tanggal.setVisibility(View.GONE);
                } else {
                    acSatuan.setText("");
                    text_target_revenue.setVisibility(View.VISIBLE);
                    acSatuan.setEnabled(true);
                    text_target_presentase.setVisibility(View.GONE);
                    text_target.setVisibility(View.VISIBLE);
                    text_lokasi.setVisibility(View.GONE);
                    text_satuan.setVisibility(View.VISIBLE);
                    lin_file.setVisibility(View.GONE);
                    text_tanggal.setVisibility(View.VISIBLE);
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
            etLokasi.setText(aktifitas.getLokasi());
        }
        Button tambah_file = (Button) findViewById(R.id.file);
        tambah_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFile();
            }
        });
    }

    private void addFile() {
        openFilePicker();
    }

    private void openFilePicker() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "pilih file"), 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 200) {
                if (data.getData() != null) {
                    TextView link_file = (TextView) findViewById(R.id.link_file);
                    String uri = UriConverter.getPath(TambahAktifitas.this, data.getData());
                    if (uri != null) {
                        uploadedImage = new File(uri);
                        long fileSizeInBytes = uploadedImage.length();
                        long fileSizeInKB = fileSizeInBytes / 1024;
                        long fileSizeInMB = fileSizeInKB / 1024;
                        if ((fileSizeInMB) >= 4) {
                            AlertDialogCustom ad = new AlertDialogCustom(TambahAktifitas.this);
                            ad.simple("Error", "File terlalu besar, maksimal 4mb", R.drawable.info_danger, null);
                            uploadedImage = null;
                            link_file.setText("");
                        } else link_file.setText(uploadedImage.getPath());
                    } else {
                        uploadedImage = null;
                        link_file.setText("");
                    }
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /*if (dialog != null) {   //Show dialog if the read permission has been granted.
                        dialog.show();
                    }*/
                    openFilePicker();
                } else {
                    //Permission has not been granted. Notify the user.
                    Toast.makeText(TambahAktifitas.this, "Permission is Required for getting list of files", Toast.LENGTH_SHORT).show();
                }
            }
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
        final AlertDialog dialog = buildDialog("Target (1-100%)");
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
        presentase.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%d %%", i);
            }
        });
        presentase.setMinValue(1);
        presentase.setMaxValue(100);
        presentase.setValue(100);
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
        String keterangan = "null";
        BigInteger target_nilai = new BigInteger("0");
        int id_satuan = 0;
        BigInteger revenue_target_nilai = new BigInteger("0");
        String nama_satuan = "", deadline = "", nama_aktivitas = "";
        String lokasi = "-";
        int id_kategori = spKategori.getSelectedItemPosition() + 1;
        try {
            id_satuan = satuanMap.get(acSatuan.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        nama_satuan = acSatuan.getText().toString();
        deadline = tvDuedate.getText().toString();
        nama_aktivitas = etNama.getText().toString();
        if (etTarget.getText().toString().isEmpty())
            etTarget.setText("0");
        target_nilai = new BigInteger(etTarget.getText().toString());
        if (etRevenue.getText().toString().isEmpty())
            etRevenue.setText("0");
        revenue_target_nilai = new BigInteger(etRevenue.getText().toString());
        lokasi = etLokasi.getText().toString();


        AlertDialogCustom ad = new AlertDialogCustom(this);

        if (id_kategori == 1) {
            if (nama_aktivitas.equals("") || deadline.equals("") || tvTargetPresentase.getText().toString().equals("")) {
                ad.simple("Peringatan", "Data harus terisi semua. Coba cek kembali data yang kosong", R.drawable.info_danger, null);
                return;
            }
            target_nilai = new BigInteger(tvTargetPresentase.getText().toString());
        } else if (id_kategori == 2) {
            if (nama_aktivitas.equals("") || deadline.equals("") || nama_satuan.equals("")) {
                ad.simple("Peringatan", "Data harus terisi semua. Coba cek kembali data yang kosong", R.drawable.info_danger, null);
                return;
            }
        } else if (id_kategori == 3) {
            if (nama_aktivitas.equals("") || deadline.equals("") || nama_satuan.equals("") || etRevenue.getText().toString().equals("")) {
                ad.simple("Peringatan", "Data harus terisi semua. Coba cek kembali data yang kosong", R.drawable.info_danger, null);
                return;
            }
        } else if (id_kategori == 4) {
            if (nama_aktivitas.equals("") || etRevenue.getText().toString().equals("")) {
                ad.simple("Peringatan", "Data harus terisi semua. Coba cek kembali data yang kosong", R.drawable.info_danger, null);
                return;
            }
        }

        if (deadline.equals("")){
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            deadline = df.format(c);
            Log.d("mantan", deadline);
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
                    revenue_target_nilai,
                    lokasi);
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
                    revenue_target_nilai,
                    lokasi);
    }


    private void editAktifitas(final int id_target,
                               final int id_program,
                               final int id_kategori,
                               final int id_satuan,
                               final String nama_satuan,
                               final String deadline,
                               final String keterangan,
                               final String nama_aktivitas,
                               final BigInteger target_nilai,
                               final BigInteger revenue_target_nilai,
                               final String lokasi) {
        avi.show();
        button.setEnabled(false);
        String token = Prefs.getString(Config.TOKEN_BUMN, "");

        if (uploadedImage != null) {

            bytesArray = new byte[(int) uploadedImage.length()];
            try {
                FileInputStream fis = new FileInputStream(uploadedImage);
                fis.read(bytesArray); //read file into bytes[]
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Config.URL_EDIT_TARGET_PROGRAM + token, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                try {
                    JSONObject jObject = new JSONObject(resultResponse);
                    String status = jObject.getString("status");
                    if (status.equals("edit-success")) {
                        Toast toast = Toast.makeText(TambahAktifitas.this, "Sukses Mengedit Aktivitas", Toast.LENGTH_LONG);
                        toast.show();
                        onBackPressed();
                    } else if (status.equals("wrong-id")) {
                        Toast.makeText(TambahAktifitas.this, "Aktivitas tidak ada", Toast.LENGTH_LONG).show();
                    } else if (status.equals("post-failed")) {
                        Toast.makeText(TambahAktifitas.this, "Post data gagal", Toast.LENGTH_LONG).show();
                    }
                    avi.hide();
                    button.setEnabled(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                button.setEnabled(true);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_target", String.valueOf(id_target));
                params.put("id_program", String.valueOf(id_program));
                params.put("id_kategori", String.valueOf(id_kategori));
                params.put("id_satuan", String.valueOf(id_satuan));
                params.put("nama_satuan", String.valueOf(nama_satuan));
                params.put("deadline", String.valueOf(deadline));
                params.put("keterangan", String.valueOf(keterangan));
                params.put("nama_aktivitas", String.valueOf(nama_aktivitas));
                params.put("target_nilai", String.valueOf(target_nilai));
                params.put("revenue_target_nilai", String.valueOf(revenue_target_nilai));
                params.put("lokasi", String.valueOf(lokasi));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                if (uploadedImage != null)
                    params.put("attachment", new DataPart(uploadedImage.toString(), bytesArray));
                return params;
            }
        };

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
    }


    private void uploadAktifitas(final int id_program,
                                 final int id_kategori,
                                 final int id_satuan,
                                 final String nama_satuan,
                                 final String deadline,
                                 final String keterangan,
                                 final String nama_aktivitas,
                                 final BigInteger target_nilai,
                                 final BigInteger revenue_target_nilai,
                                 final String lokasi) {
        avi.show();
        button.setEnabled(false);
        String token = Prefs.getString(Config.TOKEN_BUMN, "");

        if (uploadedImage != null) {

            bytesArray = new byte[(int) uploadedImage.length()];
            try {
                FileInputStream fis = new FileInputStream(uploadedImage);
                fis.read(bytesArray); //read file into bytes[]
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Config.URL_POST_TARGET_PROGRAM + token, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                try {
                    JSONObject jObject = new JSONObject(resultResponse);
                    String status = jObject.getString("status");
                    if (status.equals("post-success")) {
                        Toast toast = Toast.makeText(TambahAktifitas.this, "Sukses Menambahkan Aktivitas", Toast.LENGTH_LONG);
                        toast.show();
                        finish();
                    } else if (status.equals("wrong-id")) {
                        Toast.makeText(TambahAktifitas.this, "Aktivitas tidak ada", Toast.LENGTH_LONG).show();
                    } else if (status.equals("post-failed")) {
                        Toast.makeText(TambahAktifitas.this, "Post data gagal", Toast.LENGTH_LONG).show();
                    }
                    avi.hide();
                    button.setEnabled(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                button.setEnabled(true);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_program", String.valueOf(id_program));
                params.put("id_kategori", String.valueOf(id_kategori));
                params.put("id_satuan", String.valueOf(id_satuan));
                params.put("nama_satuan", String.valueOf(nama_satuan));
                params.put("deadline", String.valueOf(deadline));
                params.put("keterangan", String.valueOf(keterangan));
                params.put("nama_aktivitas", String.valueOf(nama_aktivitas));
                params.put("target_nilai", String.valueOf(target_nilai));
                params.put("revenue_target_nilai", String.valueOf(revenue_target_nilai));
                params.put("lokasi", String.valueOf(lokasi));
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                if (uploadedImage != null)
                    params.put("attachment", new DataPart(uploadedImage.toString(), bytesArray));
                return params;
            }
        };

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
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