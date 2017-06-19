package com.simonag.simonag.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.simonag.simonag.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by diditsepiyanto on 11/3/16.
 */

public class VolleyClass2 {
    private ProgressDialog progress_dialog;
    private Context context;
    private boolean show_progress;

    public VolleyClass2(Context context, boolean show_progress) {
        if(show_progress){
            if (progress_dialog == null) {
                progress_dialog = new ProgressDialog(context);
                progress_dialog.setMessage("Koneksi ke server");
                progress_dialog.setIndeterminate(true);
            }
            progress_dialog.show();
        }
        this.show_progress = show_progress;
        this.context = context;
    }

    public void get_data_from_server(final VolleyClass2.VolleyCallback callback, final String link, final Map<String, String> p) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(show_progress){
                            if (progress_dialog != null && progress_dialog.isShowing()) {
                                progress_dialog.hide();
                            }
                        }
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(show_progress){
                            if (progress_dialog != null && progress_dialog.isShowing()) {
                                progress_dialog.hide();
                            }
                        }
                        alert();
                        callback.onError();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return p;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public interface VolleyCallback {
        void onSuccess(String result);
        void onError();
    }

    private void alert(){
        AlertDialogCustom ad = new AlertDialogCustom(context);
        ad.simple("KONEKSI GAGAL", "Tidak tersambung ke jaringan. Harap periksa koneksi internet Anda.", R.drawable.broken_link, null);
    }

}
