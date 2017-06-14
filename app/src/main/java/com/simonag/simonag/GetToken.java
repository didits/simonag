package com.simonag.simonag;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by diditsepiyanto on 6/14/17.
 */

public class GetToken {
    private callback callback_variable;
    public GetToken(Context c){


        VolleyClass cek = new VolleyClass(c, true);
        cek.get_data_from_server(new VolleyClass.VolleyCallback() {
            @Override
            public void onSuccess(String response) {

                try {
                    JSONObject jObject = new JSONObject(response);
                    String status = jObject.getString("status");
                    if(status.equals("success")){
                        Prefs.putString(Config.TOKEN_BUMN, jObject.getString("token"));
                        if (callback_variable != null) {
                            callback_variable.action(true);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError() {

            }
        }, Config.URL_GET_TOKEN, new String[]{
                "email" + "|" + Prefs.getString(Config.EMAIL_BUMN, null),
                "password" + "|" + Prefs.getString(Config.PASSWORD_BUMN, "")

        });
    }

    public void setCallback(callback callback) {
        this.callback_variable = callback;
    }

    public interface callback {
        public void action(boolean success);
    }
}
