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
import android.view.View;
import android.widget.EditText;

import com.simonag.simonag.adapter.DataProgramAdapter;
import com.simonag.simonag.model.ProgramModel;
import com.simonag.simonag.utils.EditHapusInterface;
import com.simonag.simonag.utils.TextEmptyUtils;

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
    EditText et_nama;

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
        final AlertDialog dialog = buildDialog("Tambah ProgramModel");
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty()) return;
                String nama = et_nama.getText().toString();
                ProgramModel program = new ProgramModel();
                program.nama=nama;
                program.id="P"+(programModels.size()+1);
                programModels.add(program);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void edit(int index) {
        final ProgramModel program = programModels.get(index);
        final AlertDialog dialog = buildDialog("Edit Program");
        et_nama.setText(program.nama);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty()) return;
                String nama = et_nama.getText().toString();
                program.nama=nama;
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

    }

    private boolean isEmpty() {
        List<EditText> editTexts = new ArrayList<>();
        editTexts.add(et_nama);
        return TextEmptyUtils.setEmptyErrorMessage(editTexts, "Wajib diisi");
    }

    @Override
    public void hapus(final int index) {
        AlertDialog.Builder pilihan = new AlertDialog.Builder(this);
        pilihan.setMessage("Anda ingin menghapus?");
        pilihan.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                programModels.remove(index);
                adapter.notifyDataSetChanged();
            }
        });
        pilihan.setNegativeButton("Tidak", null);
        AlertDialog alert = pilihan.create();
        alert.show();
    }

    private AlertDialog buildDialog(String title) {
        AlertDialog.Builder result = new AlertDialog.Builder(this);
        View alertView = getLayoutInflater().inflate(R.layout.dialog_data_program, null);
        et_nama = (EditText) alertView.findViewById(R.id.et_nama);
        result.setTitle(title)
                .setView(alertView)
                .setPositiveButton("Simpan", null)
                .setNegativeButton("Batal", null);
        AlertDialog dialog = result.create();
        dialog.show();
        return dialog;
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
