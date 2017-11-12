package com.simonag.simonag;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pixplicity.easyprefs.library.Prefs;
import com.simonag.simonag.utils.Config;
import com.simonag.simonag.utils.GetToken;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static android.os.Build.VERSION_CODES.M;

/**
 * Created by diditsepiyanto on 11/12/17.
 */

public class ViewRealisasi extends AppCompatActivity {
    String blob;
    AVLoadingIndicatorView avi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_realisasi);
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        try {
            String id_target = getIntent().getExtras().getString("id_target");
            getRealisasi(id_target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        showActionBar();
    }

    private void getRealisasi(final String id_target) {
        avi.show();
        String tokena = Prefs.getString(Config.TOKEN_BUMN, "");
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = Config.URL_GET_REALISASI + tokena + "/" + id_target;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        avi.hide();
                        Log.d("dataku", response.toString());
                        try {
                            if (response.getString("status").equals("success")) {
                                final JSONObject realisasi = new JSONObject(response.getString("realisasi"));
                                if (realisasi.getString("file_type").equals("jpg") || realisasi.getString("file_type").equals("jpeg") || realisasi.getString("file_type").equals("png")
                                        || realisasi.getString("file_type").equals("gif")) {
                                    final ImageView gambar = (ImageView) findViewById(R.id.gambar);

                                    new Thread(new Runnable() {
                                        public void run() {
                                            try {
                                                byte[] decodedString = Base64.decode(realisasi.getString("file"), Base64.DEFAULT);
                                                final Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                                gambar.post(new Runnable() {
                                                    public void run() {
                                                        gambar.setImageBitmap(decodedByte);
                                                    }
                                                });
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();

                                } else  {
                                    try {
                                        String sdCardDir = Environment.getExternalStorageDirectory() + "/Simonag/realisasi";
                                        File folder = new File(sdCardDir);
                                        folder.mkdirs();
                                        String filename = realisasi.getString("id_target")+realisasi.getString("file_name");

                                        File saveFile = new File(sdCardDir, filename);
                                        if (saveFile.exists()) {
                                            saveFile.delete();
                                            saveFile = new File(sdCardDir, filename);
                                        }

                                        byte[] decodedString = Base64.decode(realisasi.getString("file"), Base64.DEFAULT);
                                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(saveFile));
                                        bos.write(decodedString);
                                        bos.flush();
                                        bos.close();

                                        Uri path;

                                        if (Build.VERSION.SDK_INT > M) {
                                            path = FileProvider.getUriForFile(ViewRealisasi.this,
                                                    BuildConfig.APPLICATION_ID + ".provider",
                                                    saveFile);
                                        } else {
                                            path = Uri.fromFile(saveFile);
                                        }

                                        MimeTypeMap myMime = MimeTypeMap.getSingleton();
                                        Intent newIntent = new Intent(Intent.ACTION_VIEW);
                                        String mimeType = myMime.getMimeTypeFromExtension(fileExt(filename).substring(1));
                                        newIntent.setDataAndType(path,mimeType);
                                        newIntent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
                                        try {
                                            ViewRealisasi.this.startActivity(newIntent);
                                            finish();
                                        } catch (ActivityNotFoundException e) {
                                            Toast.makeText(ViewRealisasi.this, "No handler for this type of file.", Toast.LENGTH_LONG).show();
                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(ViewRealisasi.this, "Tidak ada data realisasi terbaru", Toast.LENGTH_LONG).show();
                                        finish();
                                    }

                                }

                                if(response.getString("realisasi")==null){
                                    Toast.makeText(ViewRealisasi.this, "Tidak ada data realisasi terbaru", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            } else if (response.getString("status").equals("invalid-token")) {
                                GetToken k = new GetToken(ViewRealisasi.this);
                                k.setCallback(new GetToken.callback() {
                                    @Override
                                    public void action(boolean success) {
                                        getRealisasi(id_target);
                                    }
                                });
                            }

                        } catch (JSONException E) {
                            Log.e("json_error", E.toString());
                            Toast.makeText(ViewRealisasi.this, "Tidak ada data realisasi terbaru", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(getRequest);
        queue.add(getRequest);
    }

    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
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
        }

        return super.onOptionsItemSelected(item);
    }
}
