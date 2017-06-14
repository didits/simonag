package com.simonag.simonag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.login_username)
    EditText loginUsername;
    @BindView(R.id.login_password)
    EditText loginPassword;
    @BindView(R.id.login_button)
    Button loginButton;
    @BindView(R.id.login_lupa)
    TextView loginLupa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Asap-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        new Prefs.Builder()
                .setContext(this)
                .setMode(Context.MODE_PRIVATE)
                .setPrefsName(Config.SHARED_USER)
                .setUseDefaultSharedPreference(true)
                .build();

        if (Prefs.getBoolean(Config.STATUS_LOGIN, false)) {
            move();
        }

    }

    @OnClick({R.id.login_button, R.id.login_lupa})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                login();
                break;
            case R.id.login_lupa:
                Intent i = new Intent(LoginActivity.this, ResetPassword.class);
                startActivity(i);
                break;
        }
    }

        private void move() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private void login() {
        VolleyClass cek = new VolleyClass(this, true);
        cek.get_data_from_server(new VolleyClass.VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d("respon onSuccess", response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    String status = jObject.getString("status");
                    if (status.equals("success")) {
                        Prefs.putBoolean(Config.STATUS_LOGIN, true);
                        Prefs.putString(Config.EMAIL_BUMN, jObject.getString("status_daftar"));
                        Prefs.putString(Config.PASSWORD_BUMN, jObject.getString("status_daftar"));
                        Prefs.putString(Config.NAMA_BUMN, jObject.getString("nama"));
                        Prefs.putInt(Config.ID_BUMN, jObject.getInt("id_perusahaan"));
                        Prefs.putInt(Config.STATUS_BUMN, jObject.getInt("status_daftar"));
                        Prefs.putString(Config.TOKEN_BUMN, jObject.getString("token"));
                        move();
                    } else if (status.equals("failed")) {
                        Toast.makeText(LoginActivity.this, "Periksa email atau password Anda", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login bermasalah", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError() {

            }
        }, Config.URL_LOGIN, new String[]{
                "email" + "|" + loginUsername.getText().toString(),
                "password" + "|" + loginPassword.getText().toString()
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }



}
