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

public class GantiPassword extends AppCompatActivity {

    @BindView(R.id.ET_password)
    EditText inputPassword;
    @BindView(R.id.ET_passcode)
    EditText inputPasscode;
    @BindView(R.id.B_reset_password)
    Button resetButton;

    private String email="";
    private String id_user="";
    private String code="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Asap-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.new_password);
        ButterKnife.bind(this);
        Intent in = getIntent();
        if(in.getExtras() != null){
            email =in.getExtras().getString("email");
            id_user =in.getExtras().getString("id_user");
            code =in.getExtras().getString("code");
        }
        showActionBar();

    }

    @OnClick({R.id.B_reset_password })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.B_reset_password:
                if(inputPasscode.getText().toString().trim().equals(inputPassword.getText().toString().trim())){
                    Toast.makeText(GantiPassword.this, "Password Tidak Sama", Toast.LENGTH_LONG).show();
                }else{
                    sendCode();
                }
                break;
        }
    }

    private void move() {
        Intent i = new Intent(GantiPassword.this, LoginActivity.class);
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
                    if (status.equals("success")) {
                        Toast.makeText(GantiPassword.this, "Berhasil mengirim", Toast.LENGTH_LONG).show();
                        move();
                    } else if (status.equals("failed")) {
                        Toast.makeText(GantiPassword.this, "Gagal mengganti", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError() {

            }
        }, Config.URL_CHANGE_PASSWORD, new String[]{
                "email" + "|" + email,
                "code" + "|" + code,
                "id_user" + "|" + id_user,
                "password" + "|" + inputPassword.getText(),
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
