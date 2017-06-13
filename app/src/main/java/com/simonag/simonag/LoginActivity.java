package com.simonag.simonag;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        if (Prefs.getBoolean("logged", false)) {
            move();
        }

    }

    @OnClick({R.id.login_button, R.id.login_lupa})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                //Prefs.putBoolean("logged", true);
                move();
                break;
            case R.id.login_lupa:
                Intent i = new Intent(LoginActivity.this, ResetPassword.class);
                startActivity(i);
                break;
        }
    }

    private void resetdialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Masukan Email Anda");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                return;
            }
        });

        alert.setNegativeButton("Batal",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        return;
                    }
                });
        alert.show();
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

                        //Creating a shared preference
                        SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_USER, Context.MODE_PRIVATE);

                        //Creating editor to store values to shared preferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putBoolean(Config.STATUS_LOGIN, true);
                        editor.putString(Config.EMAIL_BUMN, jObject.getString("status_daftar"));
                        editor.putString(Config.PASSWORD_BUMN, jObject.getString("status_daftar"));
                        editor.putString(Config.NAMA_BUMN, jObject.getString("nama"));
                        editor.putInt(Config.ID_BUMN, jObject.getInt("id_perusahaan"));
                        editor.putInt(Config.STATUS_BUMN, jObject.getInt("status_daftar"));
                        editor.putString(Config.TOKEN_BUMN, jObject.getString("token"));

                        editor.commit();


                        move();
                    } else if (status.equals("failed")) {
                        Toast.makeText(LoginActivity.this, "Token Failed", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Token Failed", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError() {

            }
        }, Config.URL_LOGIN, new String[]{
                "email" + "|" + loginUsername,
                "password" + "|" + loginUsername
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }



}
