package com.simonag.simonag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;
import com.simonag.simonag.utils.Config;
import com.simonag.simonag.utils.RegexInput;
import com.simonag.simonag.utils.VolleyClass;

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

        loginButton.setEnabled(false);
        loginButton.setBackground(getResources().getDrawable(R.drawable.button_disabled));

        loginUsername.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                RegexInput regex = new RegexInput();
                if (regex.EmailValidator(loginUsername.getText().toString()) && !loginPassword.getText().toString().equals("")) {
                    loginButton.setEnabled(true);
                    loginButton.setBackground(getResources().getDrawable(R.drawable.button));
                } else {
                    loginButton.setEnabled(false);
                    loginButton.setBackground(getResources().getDrawable(R.drawable.button_disabled));
                }
            }
        });

        loginPassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                RegexInput regex = new RegexInput();
                if (!loginPassword.getText().toString().equals("") && regex.EmailValidator(loginUsername.getText().toString())) {
                    loginButton.setEnabled(true);
                    loginButton.setBackground(getResources().getDrawable(R.drawable.button));
                } else {
                    loginButton.setEnabled(false);
                    loginButton.setBackground(getResources().getDrawable(R.drawable.button_disabled));
                }
            }
        });

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
                        Prefs.putInt(Config.ID_BUMN, jObject.getInt("id_perusahaan"));
                        Prefs.putInt(Config.ID_ROLE, jObject.getInt("id_role"));
                        Prefs.putString(Config.NAMA_BUMN, jObject.getString("nama"));
                        Prefs.putInt(Config.STATUS_BUMN, jObject.getInt("active"));
                        Prefs.putString(Config.TOKEN_BUMN, jObject.getString("token"));
                        Prefs.putString(Config.EMAIL_BUMN, loginUsername.getText().toString());
                        Prefs.putString(Config.PASSWORD_BUMN, loginPassword.getText().toString());
                        Prefs.putBoolean(Config.STATUS_LOGIN, true);
                        move();
                    } else if (status.equals("failed")) {
                        Toast.makeText(LoginActivity.this, "username / pass salah", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "username tidak ada", Toast.LENGTH_LONG).show();
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
