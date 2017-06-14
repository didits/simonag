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
 * Created by diditsepiyanto on 6/14/17.
 */

public class ResetPassword extends AppCompatActivity {

    @BindView(R.id.ET_email)
    EditText email;
    @BindView(R.id.B_reset)
    Button resetButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Asap-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.reset_password);
        ButterKnife.bind(this);
        showActionBar();

    }

    @OnClick({R.id.B_reset })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.B_reset:
                sendCode();
                break;
        }
    }

    private void move() {
        Intent i = new Intent(ResetPassword.this, VerifikasiKodeReset.class);
        i.putExtra("email",email.getText().toString());
        startActivity(i);
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
                    if (status.equals("send-success")) {
                        Toast.makeText(ResetPassword.this, "Berhasil mengirim", Toast.LENGTH_LONG).show();
                        move();
                    } else if (status.equals("not-exist")) {
                        Toast.makeText(ResetPassword.this, "email tidak ada", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError() {

            }
        }, Config.URL_SEND_EMAIL_FORGOT_PASSWORD, new String[]{
                "email" + "|" + email.getText().toString()
        });
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
