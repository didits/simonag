package com.simonag.simonag;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.simonag.simonag.adapter.DataProgramAdapter;
import com.simonag.simonag.model.ProgramModel;
import com.simonag.simonag.utils.EditHapusInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DataProgramActivity extends AppCompatActivity implements EditHapusInterface {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_program)
    RecyclerView rvProgram;
    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;

    DataProgramAdapter adapter;
    List<ProgramModel> programModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_program);
        ButterKnife.bind(this);
        init();
        setTitle("Data Program");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvProgram.setLayoutManager(llm);
        rvProgram.setHasFixedSize(true);

        adapter = new DataProgramAdapter(this, programModels, this);
        rvProgram.setAdapter(adapter);

    }

    private void init() {
        programModels = new ArrayList<>();
        ProgramModel program;
        program = new ProgramModel();
        program.id = "P1";
        program.nama = "ABC";
        programModels.add(program);
        program = new ProgramModel();
        program.id = "P2";
        program.nama = "BCA";
        programModels.add(program);
        program = new ProgramModel();
        program.id = "P3";
        program.nama = "CAB";
        programModels.add(program);
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
