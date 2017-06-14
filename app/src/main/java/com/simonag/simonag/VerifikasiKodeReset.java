package com.simonag.simonag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.simonag.simonag.utils.Config;
import com.simonag.simonag.utils.VolleyClass;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by root on 6/14/17.
 */

public class VerifikasiKodeReset extends AppCompatActivity {

    @BindView(R.id.ET_email_verifikasi)
    EditText email_verifikasi;
    @BindView(R.id.ET_kode_verifikasi)
    EditText kode_verifikasi;
    @BindView(R.id.B_verifikasi)
    Button verifikasiButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Asap-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.verifikasi_kode);
        ButterKnife.bind(this);
        Intent in = getIntent();
        if(in.getExtras() != null){
            email_verifikasi.setText(in.getExtras().getString("email"));
        }
        showActionBar();
    }

    @OnClick({R.id.B_verifikasi })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.B_verifikasi:
                sendCode();
                break;
        }
    }

    private void move(String id_user) {
        Intent i = new Intent(VerifikasiKodeReset.this, GantiPassword.class);
        i.putExtra("id_user",id_user);
        i.putExtra("email",email_verifikasi.getText().toString());
        i.putExtra("code",kode_verifikasi.getText().toString());
        startActivity(i);
        finish();
    }

    private void sendCode() {
        VolleyClass cek = new VolleyClass(this, true);
        cek.get_data_from_server(new VolleyClass.VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d("respon onSuccess", response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    String status = jObject.getString("status");
                    String id_user = jObject.getString("id_user");
                    if (status.equals("success")) {
                        Toast.makeText(VerifikasiKodeReset.this, "Berhasil mengirim", Toast.LENGTH_LONG).show();
                        move(id_user);
                    } else if (status.equals("failed")) {
                        Toast.makeText(VerifikasiKodeReset.this, "Kode salah", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError() {

            }
        }, Config.URL_VERIFY_CODE_EMAIL, new String[]{
                "email" + "|" + email_verifikasi.getText().toString(),
                "code" + "|" + kode_verifikasi.getText().toString()
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
        }

        return super.onOptionsItemSelected(item);
    }


}
