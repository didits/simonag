package com.simonag.simonag;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.pixplicity.easyprefs.library.Prefs;
import com.simonag.simonag.utils.AlertDialogCustom;
import com.simonag.simonag.utils.Config;
import com.simonag.simonag.utils.GetToken;
import com.simonag.simonag.utils.VolleyClass;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

public class TambahRealisasi extends AppCompatActivity {

    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    SimpleDateFormat dateFormatter;
    int id_aktifitas;
    int id_kategori;
    @BindView(R.id.et_nilai)
    EditText etNilai;
    @BindView(R.id.et_revenue)
    EditText etRevenue;

    @BindView(R.id.button)
    Button button;
    DatePickerDialog datepicker;
    @BindView(R.id.tv_tanggal)
    TextView tvTanggal;
    @BindView(R.id.nama_program)
    TextView tvNamaProgram;
    @BindView(R.id.nama_aktivitas)
    TextView tvNamaAktivitas;
    @BindView(R.id.due_Date)
    TextView tvdueDate;
    @BindView(R.id.tipe_aktivitas)
    TextView tvtipeAltivitas;
    @BindView(R.id.realisasi)
    TextView tvRealisasi;
    @BindView(R.id.target)
    TextView tvTarget;
    @BindView(R.id.revenue)
    TextView tvRevenue;
    @BindView(R.id.pemisah)
    FrameLayout frameLayout;

    FilePickerDialog dialog;
    //String[] link_files;
    byte[] bytesArray = null;
    File uploadedImage;

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
        setContentView(R.layout.activity_tambah_realisasi);
        ButterKnife.bind(this);
        avi.hide();
        setTitle("Tambah Realisasi");
        showActionBar();

        tvRevenue.setText(getIntent().getExtras().getString("revenue"));
        tvTarget.setText(getIntent().getExtras().getString("target"));
        tvRealisasi.setText(getIntent().getExtras().getString("realisasi_persen"));
        tvNamaProgram.setText("Program: " + getIntent().getExtras().getString("nama_program"));
        tvNamaAktivitas.setText(getIntent().getExtras().getString("nama_aktivitas"));
        tvdueDate.setText(getIntent().getExtras().getString("due_date"));
        tvtipeAltivitas.setText(getIntent().getExtras().getString("kategori"));
        id_aktifitas = getIntent().getExtras().getInt("id_aktivitas");
        id_kategori = getIntent().getExtras().getInt("id_kategori");

        if (id_kategori < 3) {
            etRevenue.setVisibility(View.GONE);
            frameLayout.setVisibility(View.GONE);
            tvRevenue.setVisibility(View.GONE);
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
        /*DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;
        dialog = new FilePickerDialog(TambahRealisasi.this, properties);
        dialog.setTitle("Select a File");
        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                TextView link_file = (TextView) findViewById(R.id.link_file);
                link_files = files;
                File file = new File(link_files[0]);
                long fileSizeInBytes = file.length();
                long fileSizeInKB = fileSizeInBytes / 1024;
                long fileSizeInMB = fileSizeInKB / 1024;
                if ((fileSizeInMB) >= 4) {
                    AlertDialogCustom ad = new AlertDialogCustom(TambahRealisasi.this);
                    ad.simple("Error", "File terlalu besar, maksimal 4mb", R.drawable.info_danger, null);
                    link_files = null;
                    link_file.setText("");
                } else link_file.setText(link_files[0]);
            }
        });
        dialog.show();*/
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
                    String uri = UriConverter.getPath(TambahRealisasi.this, data.getData());
                    if (uri != null) {
                        uploadedImage = new File(uri);
                        long fileSizeInBytes = uploadedImage.length();
                        long fileSizeInKB = fileSizeInBytes / 1024;
                        long fileSizeInMB = fileSizeInKB / 1024;
                        if ((fileSizeInMB) >= 4) {
                            AlertDialogCustom ad = new AlertDialogCustom(TambahRealisasi.this);
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
                    Toast.makeText(TambahRealisasi.this, "Permission is Required for getting list of files", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void setEditListener() {
        button.setEnabled(false);
        button.setBackground(getResources().getDrawable(R.drawable.button_disabled));
        if (id_kategori < 3) {
            etNilai.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!etNilai.getText().toString().equals("")) {
                        button.setEnabled(true);
                        button.setBackground(getResources().getDrawable(R.drawable.button));
                    } else {
                        button.setEnabled(false);
                        button.setBackground(getResources().getDrawable(R.drawable.button_disabled));
                    }
                }
            });
        } else {
            etNilai.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!etRevenue.getText().toString().equals("") && !etNilai.getText().toString().equals("")) {
                        button.setEnabled(true);
                        button.setBackground(getResources().getDrawable(R.drawable.button));
                    } else {
                        button.setEnabled(false);
                        button.setBackground(getResources().getDrawable(R.drawable.button_disabled));
                    }
                }
            });


            etRevenue.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!etRevenue.getText().toString().equals("") && !etNilai.getText().toString().equals("")) {
                        button.setEnabled(true);
                        button.setBackground(getResources().getDrawable(R.drawable.button));
                    } else {
                        button.setEnabled(false);
                        button.setBackground(getResources().getDrawable(R.drawable.button_disabled));
                    }
                }
            });
        }

    }

    private void getdate() {
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        datepicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tvTanggal.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datepicker.show();
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

    @OnClick({R.id.button, R.id.tv_tanggal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button:
                tambah_realisasi();
                break;
            case R.id.tv_tanggal:
                getdate();
                break;
        }
    }


    private void tambah_realisasi() {
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        String tanggal_realisasi = "", keterangan = "null";
        String realisasi_nilai = "0", revenue_realisasi_nilai = "0";
        try {
            tanggal_realisasi = tvTanggal.getText().toString();
            realisasi_nilai = etNilai.getText().toString();
            if (id_kategori == 3)
                revenue_realisasi_nilai = etRevenue.getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (id_kategori == 1 || id_kategori == 2) {
            if (tanggal_realisasi.equals("") || etNilai.getText().toString().equals("")) {
                AlertDialogCustom ad = new AlertDialogCustom(TambahRealisasi.this);
                ad.simple("Peringatan", "Data harus terisi semua", R.drawable.info_danger, null);
                return;
            }
            if (Double.parseDouble(etNilai.getText().toString()) > Double.parseDouble(getIntent().getExtras().getString("target_real"))) {
                AlertDialogCustom ad = new AlertDialogCustom(TambahRealisasi.this);
                ad.simple("Peringatan", "Nilai realisasi harus kurang atau sama dengan " + getIntent().getExtras().getString("target_real"), R.drawable.info_danger, null);
                return;
            }

        }
        if (id_kategori == 3) {
            if (tanggal_realisasi.equals("") || etRevenue.getText().toString().equals("") || etNilai.getText().toString().equals("")) {
                AlertDialogCustom ad = new AlertDialogCustom(TambahRealisasi.this);
                ad.simple("Peringatan", "Data harus terisi semua", R.drawable.info_danger, null);
                return;
            }
            try {
                revenue_realisasi_nilai = etRevenue.getText().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (Double.parseDouble(etNilai.getText().toString()) > Double.parseDouble(getIntent().getExtras().getString("target_real"))) {
                AlertDialogCustom ad = new AlertDialogCustom(TambahRealisasi.this);
                ad.simple("Peringatan", "Nilai realisasi harus kurang atau sama dengan " + getIntent().getExtras().getString("target_real"), R.drawable.info_danger, null);
                return;
            }

            if (Double.parseDouble(etRevenue.getText().toString()) > Double.parseDouble(getIntent().getExtras().getString("target_real_revenue"))) {
                AlertDialogCustom ad = new AlertDialogCustom(TambahRealisasi.this);
                ad.simple("Peringatan", "Nilai realisasi revenue harus kurang atau sama dengan " + getIntent().getExtras().getString("target_real_revenue"), R.drawable.info_danger, null);
                return;
            }
        }

        if (tanggal_realisasi.equals("")) {
            AlertDialogCustom ad = new AlertDialogCustom(TambahRealisasi.this);
            ad.simple("Peringatan", "Data harus terisi semua", R.drawable.info_danger, null);
            return;
        }

        uploadRealisasi(
                tanggal_realisasi,
                keterangan,
                realisasi_nilai,
                revenue_realisasi_nilai
        );

    }

    private void uploadRealisasi(final String tanggal_realisasi,
                                 final String keterangan,
                                 final String realisasi_nilai,
                                 final String revenue_realisasi_nilai) {
        button.setEnabled(false);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
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


        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, Config.URL_POST_REALISASI_TARGET + token, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                try {
                    JSONObject jObject = new JSONObject(resultResponse);
                    String status = jObject.getString("status");
                    if (status.equals("post-success")) {
                        Toast toast = Toast.makeText(TambahRealisasi.this, "Sukses Menambahkan Realisasi", Toast.LENGTH_LONG);
                        toast.show();
                        finish();
                    } else if (status.equals("wrong-id")) {
                        Toast.makeText(TambahRealisasi.this, "Aktivitas tidak ada", Toast.LENGTH_LONG).show();
                    } else if (status.equals("post-failed")) {
                        Toast.makeText(TambahRealisasi.this, "Post data gagal", Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);
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

                params.put("id_target", id_aktifitas + "");
                params.put("tanggal_realisasi", tanggal_realisasi);
                params.put("keterangan", keterangan);
                params.put("realisasi_nilai", realisasi_nilai);
                params.put("revenue_realisasi_nilai", revenue_realisasi_nilai);
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


    private void uploadRealisasi2(
            String tanggal_realisasi,
            String keterangan,
            String realisasi_nilai,
            String revenue_realisasi_nilai) {
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
                        Toast toast = Toast.makeText(TambahRealisasi.this, "Sukses Menambahkan Realisasi", Toast.LENGTH_LONG);
                        toast.show();
                        finish();
                    } else if (status.equals("wrong-id")) {
                        Toast.makeText(TambahRealisasi.this, "Aktivitas tidak ada", Toast.LENGTH_LONG).show();
                    } else if (status.equals("post-failed")) {
                        Toast.makeText(TambahRealisasi.this, "Post data gagal", Toast.LENGTH_LONG).show();
                    } else {
                        GetToken k = new GetToken(TambahRealisasi.this);
                        k.setCallback(new GetToken.callback() {
                            @Override
                            public void action(boolean success) {
                                tambah_realisasi();
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
        }, Config.URL_POST_REALISASI_TARGET + token, new String[]{
                "id_target" + "|" + id_aktifitas,
                "tanggal_realisasi" + "|" + tanggal_realisasi,
                "keterangan" + "|" + keterangan,
                "realisasi_nilai" + "|" + realisasi_nilai,
                "revenue_realisasi_nilai" + "|" + revenue_realisasi_nilai
        });
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}