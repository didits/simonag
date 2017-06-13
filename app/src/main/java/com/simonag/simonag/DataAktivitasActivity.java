package com.simonag.simonag;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.simonag.simonag.model.AktivitasModel;
import com.simonag.simonag.utils.EditHapusInterface;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DataAktivitasActivity extends AppCompatActivity implements EditHapusInterface {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_aktivitas)
    RecyclerView rvAktivitas;
    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;

//    DataAktivitasAdapter adapter;
    List<AktivitasModel> aktivitasModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_aktivitas);
        ButterKnife.bind(this);

        setTitle("Data Aktivitas");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    @OnClick(R.id.fab_add)
    public void onViewClicked() {
        tambah();
    }

    private void tambah() {
    }

    @Override
    public void edit(int index) {

    }

    @Override
    public void hapus(int index) {

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
}
