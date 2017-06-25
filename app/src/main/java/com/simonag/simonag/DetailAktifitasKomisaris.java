package com.simonag.simonag;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.simonag.simonag.model.AktifitasKomisaris;
import com.wang.avi.AVLoadingIndicatorView;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by diditsepiyanto on 6/14/17.
 */

public class DetailAktifitasKomisaris extends AppCompatActivity {

    AktifitasKomisaris aktifitas;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.tv_nama)
    TextView tvNama;
    @BindView(R.id.tv_kategori)
    TextView tvKategori;
    @BindView(R.id.judul_tanggal_mulai)
    TextView judulTanggalMulai;
    @BindView(R.id.tv_tanggal_mulai)
    TextView tvTanggalMulai;
    @BindView(R.id.judul_tanggal_selesai)
    TextView judulTanggalSelesai;
    @BindView(R.id.tv_tanggal_selesai)
    TextView tvTanggalSelesai;
    @BindView(R.id.judul_nilai)
    TextView judulNilai;
    @BindView(R.id.tv_nilai)
    TextView tvNilai;
    @BindView(R.id.judul_keterangan)
    TextView judulKeterangan;
    @BindView(R.id.tv_keterangan)
    TextView tvKeterangan;
    @BindView(R.id.judul_jenis_media)
    TextView judulJenisMedia;
    @BindView(R.id.tv_jenis_media)
    TextView tvJenisMedia;
    @BindView(R.id.judul_url)
    TextView judulUrl;
    @BindView(R.id.tv_url)
    TextView tvUrl;
    @BindView(R.id.IV_capture)
    ImageView IVCapture;
    String capture = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Asap-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_detail_aktifitas_komisaris);
        ButterKnife.bind(this);
        avi.hide();
        showActionBar();
        if (getIntent().hasExtra("aktifitas")) {
            aktifitas = Parcels.unwrap(getIntent().getParcelableExtra("aktifitas"));
            tvNama.setText(aktifitas.getNamaAktivitas());
            if (aktifitas.getIdKategori() >= 0) tvKategori.setText(aktifitas.getNama_kategori());
            if (!aktifitas.getJenisMedia().isEmpty()) tvJenisMedia.setText(aktifitas.getJenisMedia());
            tvUrl.setText(aktifitas.getUrl());
            tvTanggalMulai.setText(aktifitas.getAwalPelaksanaan());
            tvTanggalSelesai.setText(aktifitas.getAkhirPelaksanaan());
            tvNilai.setText(aktifitas.getNilaiRupiah() + "");
            tvKeterangan.setText(aktifitas.getKeterangan());
            tvKeterangan.setText(aktifitas.getKeterangan());
            capture = aktifitas.getCapture();
            Log.d("capture",capture);
            Glide.with(this)
                    .load(capture)
                    .fitCenter()
                    .into(IVCapture);
            getKategori(aktifitas.getNama_kategori());
        }
    }

    private void getKategori(String text) {
        if (text.contentEquals("publikasi")) {
            tvUrl.setVisibility(View.VISIBLE);
            tvJenisMedia.setVisibility(View.VISIBLE);
            tvTanggalMulai.setVisibility(View.VISIBLE);
            judulUrl.setVisibility(View.VISIBLE);
            judulJenisMedia.setVisibility(View.VISIBLE);
            judulTanggalMulai.setVisibility(View.VISIBLE);
            judulTanggalSelesai.setText("Tanggal Selesai");
        } else if (text.contentEquals("sponsorship")) {
            tvUrl.setVisibility(View.GONE);
            tvJenisMedia.setVisibility(View.GONE);
            tvTanggalMulai.setVisibility(View.GONE);
            judulUrl.setVisibility(View.GONE);
            judulJenisMedia.setVisibility(View.GONE);
            judulTanggalMulai.setVisibility(View.GONE);
            judulTanggalSelesai.setText("Tanggal Pelaksanaan");
        } else {
            tvUrl.setVisibility(View.GONE);
            tvJenisMedia.setVisibility(View.GONE);
            tvTanggalMulai.setVisibility(View.GONE);
            judulUrl.setVisibility(View.GONE);
            judulJenisMedia.setVisibility(View.GONE);
            judulTanggalMulai.setVisibility(View.GONE);
            judulTanggalSelesai.setText("Tanggal Pelaksanaan");
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

}