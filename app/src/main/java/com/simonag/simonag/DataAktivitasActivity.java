//package com.simonag.simonag;
//
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.MenuItem;
//import android.widget.EditText;
//
//import com.simonag.simonag.adapter.DataAktivitasAdapter;
//import com.simonag.simonag.model.AktivitasModel;
//import com.simonag.simonag.utils.EditHapusInterface;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class DataAktivitasActivity extends AppCompatActivity implements EditHapusInterface {
//
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.fab_add)
//    FloatingActionButton fabAdd;
//    EditText et_nama,et_duedate,et_target,et_revenue;
//
//    DataAktivitasAdapter adapter;
//    List<AktivitasModel> aktivitasModels;
//    String program_id,program_nama;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_data_aktivitas);
//        ButterKnife.bind(this);
//        program_id=getIntent().getExtras().getString("id");
//        program_nama=getIntent().getExtras().getString("nama");
//        init();
//        setTitle("Data Aktivitas");
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(false);
//
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        rvAktivitas.setLayoutManager(llm);
//        rvAktivitas.setHasFixedSize(true);
//
//        adapter = new DataAktivitasAdapter(this, aktivitasModels, this);
//        rvAktivitas.setAdapter(adapter);
//    }
//
//    private void init() {
//        aktivitasModels = new ArrayList<>();
//        AktivitasModel aktivitas;
//        aktivitas = new AktivitasModel();
//        aktivitas.id = "A1";
//        aktivitas.nama = "ABC";
//        aktivitas.duedate = "01-07-2017";
//        aktivitas.kategori = "kualitas";
//        aktivitas.target="1000";
//        aktivitas.revenue="0";
//        aktivitas.program=program_id;
//        aktivitas.satuan="mbps";
//        aktivitasModels.add(aktivitas);
//        aktivitas = new AktivitasModel();
//        aktivitas.id = "A2";
//        aktivitas.nama = "BCA";
//        aktivitas.duedate = "01-07-2017";
//        aktivitas.kategori = "kuantitas";
//        aktivitas.target="1000";
//        aktivitas.revenue="0";
//        aktivitas.program=program_id;
//        aktivitas.satuan="mbps";
//        aktivitasModels.add(aktivitas);
//        aktivitas = new AktivitasModel();
//        aktivitas.id = "A3";
//        aktivitas.nama = "CAB";
//        aktivitas.duedate = "01-07-2017";
//        aktivitas.kategori = "komersial";
//        aktivitas.target="1000";
//        aktivitas.revenue="0";
//        aktivitas.program=program_id;
//        aktivitas.satuan="mbps";
//        aktivitasModels.add(aktivitas);
//    }
//
//    @OnClick(R.id.fab_add)
//    public void onViewClicked() {
//        tambah();
//    }
//
//    private void tambah() {
//    }
//
//    @Override
//    public void edit(int index) {
//
//    }
//
//    @Override
//    public void hapus(int index) {
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                break;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//}
