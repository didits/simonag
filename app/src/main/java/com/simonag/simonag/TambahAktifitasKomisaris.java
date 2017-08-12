package com.simonag.simonag;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;
import com.simonag.simonag.model.AktifitasKomisaris;
import com.simonag.simonag.utils.AlertDialogCustom;
import com.simonag.simonag.utils.Config;
import com.simonag.simonag.utils.GetToken;
import com.simonag.simonag.utils.VolleyClass2;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
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

public class TambahAktifitasKomisaris extends AppCompatActivity {

    AktifitasKomisaris aktifitas;
    SimpleDateFormat dateFormatter;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.et_nama)
    EditText etNama;
    @BindView(R.id.sp_kategori)
    Spinner spKategori;
    @BindView(R.id.b_capture)
    Button bCapture;
    @BindView(R.id.tv_tanggal)
    TextView tvTanggal;
    @BindView(R.id.et_nilai)
    EditText etNilai;
    @BindView(R.id.et_keterangan)
    EditText etKeterangan;
    @BindView(R.id.IV_capture)
    ImageView IVcapture;
    @BindView(R.id.button)
    Button button;
    DatePickerDialog datepicker;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    private Bitmap bitmap = null;

    //Uri to store the image uri
    private Uri filePath;

    String capture = "";

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
        setContentView(R.layout.activity_tambah_aktifitas_komisaris);
        ButterKnife.bind(this);
        avi.hide();
        showActionBar();
        setTitle("Tambah Sponsorship");
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        getKategori();
        if (getIntent().hasExtra("aktifitas")) {
            button.setText("Simpan");
            setTitle("Edit Aktivfitas");
            aktifitas = Parcels.unwrap(getIntent().getParcelableExtra("aktifitas"));
            etNama.setText(aktifitas.getNamaAktivitas());
            if (aktifitas.getIdKategori() >= 0)
                spKategori.setSelection(aktifitas.getIdKategori() - 1);
            tvTanggal.setText(aktifitas.getAkhirPelaksanaan());
            etNilai.setText(aktifitas.getNilaiRupiah() + "");
            etKeterangan.setText(aktifitas.getKeterangan());
            etKeterangan.setText(aktifitas.getKeterangan());
            capture = aktifitas.getCapture();
            if (capture != null) {
                new DownloadImageTask(IVcapture).execute(Config.URL_CAPTURE + capture);
            }
//                String url = Config.URL_CAPTURE+capture;
//                Log.d("[DEBUG]",url);
//                Glide.with(IVcapture.getContext())
//                        .load(url)
//                        .into(IVcapture);
//            }
        }
        //Requesting storage permission
        requestStoragePermission();
    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            bmImage.setImageBitmap(result);
        }
    }

    private void getKategori() {
        String[] kategoriArray = new String[2];
        kategoriArray[0] = "Cash";
        kategoriArray[1] = "In Kind";
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategoriArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKategori.setAdapter(adapter);
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


    private void tambah_aktifitas() {
        BigInteger nilai_rupiah = new BigInteger("0");
        int id_perusahaan=0;
        String nama_aktivitas="",awal_pelaksanaan="",akhir_pelaksanaan="",keterangan="",jenis_media="",url="";
        String isi_capture = "";
        int id_kategori2 = spKategori.getSelectedItemPosition() + 1;
        try {
            nama_aktivitas = etNama.getText().toString();
            awal_pelaksanaan = "";
            akhir_pelaksanaan = tvTanggal.getText().toString();
            keterangan = etKeterangan.getText().toString();
            nilai_rupiah = new BigInteger(etNilai.getText().toString());
            id_perusahaan = Prefs.getInt(Config.ID_BUMN, 0);
            if (bitmap != null) {
                isi_capture = getStringImage(bitmap);
                capture = String.valueOf(filePath);
            }
        } catch (Exception e) {

        }


        AlertDialogCustom ad = new AlertDialogCustom(this);
        if (id_kategori2 == 0) {
            if (nama_aktivitas.equals("")||awal_pelaksanaan.equals("")||akhir_pelaksanaan.equals("")||url.equals("")) {
                ad.simple("Peringatan", "Data harus terisi semua", R.drawable.info_danger, null);
                return;
            }
        } else if (id_kategori2 == 1) {
            if (nama_aktivitas.equals("")||akhir_pelaksanaan.equals("")||etNilai.getText().toString().equals("")) {
                ad.simple("Peringatan", "Data harus terisi semua", R.drawable.info_danger, null);
                return;
            }else {
                awal_pelaksanaan = akhir_pelaksanaan;
            }
        }else if(id_kategori2 == 2){
            if (nama_aktivitas.equals("") || akhir_pelaksanaan.equals("")||etNilai.getText().toString().equals("") ) {
                ad.simple("Peringatan", "Data harus terisi semua", R.drawable.info_danger, null);
                return;
            }else {
                awal_pelaksanaan = akhir_pelaksanaan;
            }
        }
        if (getIntent().hasExtra("aktifitas")) {
            editAktifitas(
                    nama_aktivitas, awal_pelaksanaan, akhir_pelaksanaan, keterangan, nilai_rupiah, id_kategori2, id_perusahaan, jenis_media, url, capture, isi_capture
            );
        } else
            uploadAktifitas(
                    nama_aktivitas, awal_pelaksanaan, akhir_pelaksanaan, keterangan, nilai_rupiah, id_kategori2, id_perusahaan, jenis_media, url, capture, isi_capture
            );
    }


    private void editAktifitas(
            String nama_aktivitas, String awal_pelaksanaan, String akhir_pelaksanaan, String keterangan, BigInteger nilai_rupiah, int id_kategori2, int id_perusahaan, String jenis_media, String url, String capture, String isi_capture
    ) {
        avi.show();
        String token = Prefs.getString(Config.TOKEN_BUMN, "");
        Map<String, String> params = new HashMap<>();
        params.put("nama_aktivitas", nama_aktivitas);
        params.put("awal_pelaksanaan", awal_pelaksanaan);
        params.put("akhir_pelaksanaan", akhir_pelaksanaan);
        params.put("keterangan", keterangan);
        params.put("nilai_rupiah", nilai_rupiah + "");
        params.put("id_kategori2", id_kategori2 + "");
        params.put("id_perusahaan", id_perusahaan + "");
        params.put("jenis_media", jenis_media + "");
        params.put("url", url + "");
        params.put("capture", capture + "");
        params.put("isi_capture", isi_capture + "");
        params.put("id_aktivitas", aktifitas.getIdAktivitas() + "");
        Log.d("parame", String.valueOf(params));
        VolleyClass2 cek = new VolleyClass2(this, true);
        cek.get_data_from_server(new VolleyClass2.VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                avi.hide();
                try {
                    JSONObject jObject = new JSONObject(response);
                    String status = jObject.getString("status");
                    if (status.equals("edit-success")) {
                        Toast toast = Toast.makeText(TambahAktifitasKomisaris.this, "Sukses Mengedit Aktivitas", Toast.LENGTH_LONG);
                        toast.show();
                        onBackPressed();
                    } else if (status.equals("wrong-id")) {
                        Toast.makeText(TambahAktifitasKomisaris.this, "Aktivitas tidak ada", Toast.LENGTH_LONG).show();
                    } else if (status.equals("edit-failed")) {
                        Toast.makeText(TambahAktifitasKomisaris.this, "Edit data gagal", Toast.LENGTH_LONG).show();
                    } else {
                        GetToken k = new GetToken(TambahAktifitasKomisaris.this);
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
        }, Config.URL_EDIT_TARGET_PROGRAM_2 + token, params);
    }

    private void uploadAktifitas(
            String nama_aktivitas, String awal_pelaksanaan, String akhir_pelaksanaan, String keterangan, BigInteger nilai_rupiah, int id_kategori2, int id_perusahaan, String jenis_media, String url, String capture, String isi_capture
    ) {
        avi.show();
        String tokena = Prefs.getString(Config.TOKEN_BUMN, "");
        Log.d("ffww", Config.URL_POST_TARGET_PROGRAM_2 + tokena);
        VolleyClass2 cek = new VolleyClass2(this, true);
        Map<String, String> params = new HashMap<>();
        params.put("nama_aktivitas", nama_aktivitas);
        params.put("awal_pelaksanaan", awal_pelaksanaan);
        params.put("akhir_pelaksanaan", akhir_pelaksanaan);
        params.put("keterangan", keterangan);
        params.put("nilai_rupiah", nilai_rupiah + "");
        params.put("id_kategori2", id_kategori2 + "");
        params.put("id_perusahaan", id_perusahaan + "");
        params.put("jenis_media", jenis_media + "");
        params.put("url", url + "");
        params.put("capture", capture + "");
        params.put("isi_capture", isi_capture + "");
        cek.get_data_from_server(new VolleyClass2.VolleyCallback() {
                                     @Override
                                     public void onSuccess(String response) {
                                         avi.hide();
                                         Log.d("respon onSuccess", response);
                                         try {
                                             JSONObject jObject = new JSONObject(response);
                                             String status = jObject.getString("status");
                                             if (status.equals("post-success")) {
                                                 Toast toast = Toast.makeText(TambahAktifitasKomisaris.this, "Sukses Menambahkan Aktivitas", Toast.LENGTH_LONG);
                                                 toast.show();
                                                 onBackPressed();
                                             } else if (status.equals("wrong-id")) {
                                                 Toast.makeText(TambahAktifitasKomisaris.this, "Aktivitas tidak ada", Toast.LENGTH_LONG).show();
                                             } else if (status.equals("post-failed")) {
                                                 Toast.makeText(TambahAktifitasKomisaris.this, "Post data gagal", Toast.LENGTH_LONG).show();
                                             } else {
                                                 GetToken k = new GetToken(TambahAktifitasKomisaris.this);
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
                                 }, Config.URL_POST_TARGET_PROGRAM_2 + tokena, params
        );
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @OnClick({R.id.tv_tanggal,R.id.button, R.id.b_capture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_tanggal:
                getdate();
                break;
            case R.id.button:
                tambah_aktifitas();
                break;
            case R.id.b_capture:
                showFileChooser();
                break;
        }
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                IVcapture.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}