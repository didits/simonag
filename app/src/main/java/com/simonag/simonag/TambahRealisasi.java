package com.simonag.simonag;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;
import com.simonag.simonag.utils.AlertDialogCustom;
import com.simonag.simonag.utils.Config;
import com.simonag.simonag.utils.GetToken;
import com.simonag.simonag.utils.VolleyClass;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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
    @BindView(R.id.et_nilai)
    EditText etNilai;
    @BindView(R.id.et_revenue)
    EditText etRevenue;
    @BindView(R.id.button)
    Button button;
    DatePickerDialog datepicker;
    @BindView(R.id.tv_tanggal)
    TextView tvTanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Asap-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_tambah_realisasi);
        ButterKnife.bind(this);
        setEditListener();
        avi.hide();
        setTitle("Tambah Realisasi");
        showActionBar();
        id_aktifitas = getIntent().getExtras().getInt("id_aktivitas");
    }

    private void setEditListener() {
        button.setEnabled(false);
        button.setBackground(getResources().getDrawable(R.drawable.button_disabled));

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
        String tanggal_realisasi = tvTanggal.getText().toString();
        String keterangan = "null";
        int realisasi_nilai = 0;
        try {
            realisasi_nilai = Integer.parseInt(etNilai.getText().toString());
        } catch (Exception e) {

        }
        int revenue_realisasi_nilai = 0;
        try {
            revenue_realisasi_nilai = Integer.parseInt(etRevenue.getText().toString());
        } catch (Exception e) {

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

    private void uploadRealisasi(
            String tanggal_realisasi,
            String keterangan,
            int realisasi_nilai,
            int revenue_realisasi_nilai) {
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