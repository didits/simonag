package com.simonag.simonag;

import android.app.DatePickerDialog;
import android.content.Context;
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
import com.simonag.simonag.model.AktifitasKomisaris;
import com.simonag.simonag.utils.Config;
import com.simonag.simonag.utils.GetToken;
import com.simonag.simonag.utils.VolleyClass;
import com.simonag.simonag.utils.VolleyClass2;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

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
    int id_program;
    SimpleDateFormat dateFormatter;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.et_nama)
    EditText etNama;
    @BindView(R.id.sp_kategori)
    Spinner spKategori;
    @BindView(R.id.sp_jenis_media)
    Spinner spJenisMedia;
    @BindView(R.id.et_url)
    EditText etUrl;
    @BindView(R.id.tv_capture)
    TextView tvCapture;
    @BindView(R.id.tv_tanggal_mulai)
    TextView tvTanggalMulai;
    @BindView(R.id.tv_tanggal_selesai)
    TextView tvTanggalSelesai;
    @BindView(R.id.et_nilai)
    EditText etNilai;
    @BindView(R.id.et_keterangan)
    EditText etKeterangan;
    @BindView(R.id.button)
    Button button;
    DatePickerDialog datepicker;
    HashMap<String, Integer> satuanMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Asap-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_tambah_aktifitas_komisaris);
        ButterKnife.bind(this);
        avi.hide();
        showActionBar();
        setTitle("Tambah Aktifitas");
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        getJenisMedia();
        getKategori();
        id_program = getIntent().getExtras().getInt("id_program");
        if (getIntent().hasExtra("aktifitas")) {
            button.setText("Simpan");
            setTitle("Edit Aktivfitas");
            aktifitas = Parcels.unwrap(getIntent().getParcelableExtra("aktifitas"));
            etNama.setText(aktifitas.getNamaAktivitas());
            if(aktifitas.getIdKategori()>=0) spKategori.setSelection(aktifitas.getIdKategori()-1);
            if(aktifitas.getJenisMedia().contentEquals("Offline"))spJenisMedia.setSelection(0);
            else if(aktifitas.getJenisMedia().contentEquals("Online"))spJenisMedia.setSelection(1);
            else spJenisMedia.setSelection(2);
            etUrl.setText(aktifitas.getUrl());
            tvCapture.setText(aktifitas.getCapture());
            tvTanggalMulai.setText(aktifitas.getAwalPelaksanaan());
            tvTanggalSelesai.setText(aktifitas.getAkhirPelaksanaan());
            etNilai.setText(aktifitas.getNilaiRupiah()+"");
            etKeterangan.setText(aktifitas.getKeterangan());
        }
    }

    private void getKategori() {
        String[] kategoriArray = new String[3];
        kategoriArray[0] = "Publikasi";
        kategoriArray[1] = "Sponsorship";
        kategoriArray[2] = "Hospitality";
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kategoriArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKategori.setAdapter(adapter);
    }

    private void getJenisMedia(){
        String[] jenismediaArray = new String[3];
        jenismediaArray[0] = "Offline";
        jenismediaArray[1] = "Online";
        jenismediaArray[2] = "";
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jenismediaArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spJenisMedia.setAdapter(adapter);
    }
    private void getdateawal() {
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        datepicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tvTanggalMulai.setText(dateFormatter.format(newDate.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datepicker.show();
    }

    private void getdateakhir() {
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Calendar newCalendar = Calendar.getInstance();
        datepicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tvTanggalSelesai.setText(dateFormatter.format(newDate.getTime()));
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
        String nama_aktivitas=etNama.getText().toString();
        String awal_pelaksanaan=tvTanggalMulai.getText().toString();
        String akhir_pelaksanaan=tvTanggalSelesai.getText().toString();
        String keterangan=etKeterangan.getText().toString();
        int nilai_rupiah = 0;
        try {
             nilai_rupiah=Integer.parseInt(etNilai.getText().toString());
        }catch (Exception e){

        }
        int id_kategori2=spKategori.getSelectedItemPosition()+1;
        int id_perusahaan=Prefs.getInt(Config.ID_BUMN,0);
        String jenis_media=spJenisMedia.getSelectedItem().toString();
        String url=etUrl.getText().toString();
        String capture=tvCapture.getText().toString();
        if (getIntent().hasExtra("aktifitas"))
            editAktifitas(
                    nama_aktivitas,awal_pelaksanaan,akhir_pelaksanaan,keterangan,nilai_rupiah,id_kategori2,id_perusahaan,jenis_media,url,capture
            );
        else
            uploadAktifitas(
                    nama_aktivitas,awal_pelaksanaan,akhir_pelaksanaan,keterangan,nilai_rupiah,id_kategori2,id_perusahaan,jenis_media,url,capture
            );
    }


    private void editAktifitas(
            String nama_aktivitas,String awal_pelaksanaan,String akhir_pelaksanaan,String keterangan,int nilai_rupiah,int id_kategori2,int id_perusahaan,String jenis_media,String url,String capture
    ) {
        avi.show();
        String token = Prefs.getString(Config.TOKEN_BUMN, "");
        Map<String, String> params = new HashMap<>();
        params.put("nama_aktivitas", nama_aktivitas);
        params.put("awal_pelaksanaan", awal_pelaksanaan);
        params.put("akhir_pelaksanaan", akhir_pelaksanaan);
        params.put("keterangan", keterangan);
        params.put("nilai_rupiah", nilai_rupiah+"");
        params.put("id_kategori2", id_kategori2+"");
        params.put("id_perusahaan", id_perusahaan+"");
        params.put("jenis_media", jenis_media+"");
        params.put("url", url+"");
        params.put("capture", capture+"");
        VolleyClass2 cek = new VolleyClass2(this, true);
        cek.get_data_from_server(new VolleyClass2.VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                avi.hide();
                try {
                    JSONObject jObject = new JSONObject(response);
                    String status = jObject.getString("status");
                    if (status.equals("edit-success")) {
                        Toast toast = Toast.makeText(TambahAktifitasKomisaris.this, "Sukses Mengedit Program", Toast.LENGTH_LONG);
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
            String nama_aktivitas,String awal_pelaksanaan,String akhir_pelaksanaan,String keterangan,int nilai_rupiah,int id_kategori2,int id_perusahaan,String jenis_media,String url,String capture
    ) {
        avi.show();
        String tokena = Prefs.getString(Config.TOKEN_BUMN, "");
        Log.d("ffww",Config.URL_POST_TARGET_PROGRAM_2 + tokena);
        VolleyClass2 cek = new VolleyClass2(this, true);
        Map<String, String> params = new HashMap<>();
        params.put("nama_aktivitas", nama_aktivitas);
        params.put("awal_pelaksanaan", awal_pelaksanaan);
        params.put("akhir_pelaksanaan", akhir_pelaksanaan);
        params.put("keterangan", keterangan);
        params.put("nilai_rupiah", nilai_rupiah+"");
        params.put("id_kategori2", id_kategori2+"");
        params.put("id_perusahaan", id_perusahaan+"");
        params.put("jenis_media", jenis_media+"");
        params.put("url", url+"");
        params.put("capture", capture+"");
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
        }, Config.URL_POST_TARGET_PROGRAM_2 + tokena,params
        );
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }



    @OnClick({R.id.tv_tanggal_mulai, R.id.tv_tanggal_selesai, R.id.button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_tanggal_mulai:
                getdateawal();
                break;
            case R.id.tv_tanggal_selesai:
                getdateakhir();
                break;
            case R.id.button:
                tambah_aktifitas();
                break;
        }
    }
}