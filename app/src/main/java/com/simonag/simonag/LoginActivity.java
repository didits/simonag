package com.simonag.simonag;

import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
                Prefs.putBoolean("logged", true);
                move();
                break;
            case R.id.login_lupa:
                resetdialog();
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
}
