package com.simonag.simonag;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by diditsepiyanto on 6/14/17.
 */

public class TambahAktifitas extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_nama)
    EditText tvNama;
    @BindView(R.id.tv_kategori)
    EditText tvKategori;
    @BindView(R.id.tv_duedate)
    EditText tvDuedate;
    @BindView(R.id.tv_satuan)
    EditText tvSatuan;
    @BindView(R.id.tv_target)
    EditText tvTarget;
    @BindView(R.id.tv_revenue)
    EditText tvRevenue;
    @BindView(R.id.login_button)
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_aktifitas);
        ButterKnife.bind(this);
        setTitle("Tambah Aktivitas");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.login_button)
    public void onViewClicked() {
    }
}