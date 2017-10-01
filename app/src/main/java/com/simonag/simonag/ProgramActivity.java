package com.simonag.simonag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.pixplicity.easyprefs.library.Prefs;
import com.simonag.simonag.model.Aktifitas;
import com.simonag.simonag.model.ProgramKedua;
import com.simonag.simonag.utils.AlertDialogCustom;
import com.simonag.simonag.utils.Config;
import com.simonag.simonag.utils.GetToken;
import com.simonag.simonag.utils.VolleyClass;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.math.BigInteger;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static java.lang.String.format;

public class ProgramActivity extends AppCompatActivity {
    public BottomSheetBehavior bottomSheetBehavior;
    public BottomSheetBehavior bottomSheetBehavior2;
    String value, nama_perusahaan, nama_gambar;
    ProgramKedua temp_progam;
    @BindView(R.id.program)
    EditText program_text;
    @BindView(R.id.tambah_program)
    LinearLayout tambahProgram;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.rv_program)
    RecyclerView rv;
    @BindView(R.id.edit)
    LinearLayout edit;
    @BindView(R.id.add)
    LinearLayout add;
    @BindView(R.id.hapus)
    LinearLayout hapus;
    @BindView(R.id.action)
    RelativeLayout action;
    @BindView(R.id.action2)
    RelativeLayout action2;
    EditText edit_nama;
    Aktifitas temp_aktivitas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Prefs.Builder()
                .setContext(this)
                .setMode(Context.MODE_PRIVATE)
                .setPrefsName(Config.SHARED_USER)
                .setUseDefaultSharedPreference(true)
                .build();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Asap-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        setContentView(R.layout.activity_data_program);
        ButterKnife.bind(this);
        final LinearLayout tambah_program = (LinearLayout) findViewById(R.id.tambah_program_layout);
        TextView nama_bumn = (TextView) findViewById(R.id.nama_bumn);
        ImageView gambar_bumn = (ImageView) findViewById(R.id.gambar_bumn);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nama_gambar = extras.getString("GAMBAR_PERUSAHAAN");
            nama_perusahaan = extras.getString("NAMA_PERUSAHAAN");
            value = extras.getString("KEY");
            nama_bumn.setText(extras.getString("NAMA_PERUSAHAAN"));
            String url = Config.URL_GAMBAR + extras.getString("GAMBAR_PERUSAHAAN");

            Glide.with(gambar_bumn.getContext())
                    .load(url)
                    .fitCenter()
                    .into(gambar_bumn);
        }


        if (Prefs.getInt(Config.ID_BUMN, 0) == Integer.parseInt(value)) {
            tambah_program.setVisibility(View.VISIBLE);
        } else {
            setTitle("Progress Program");
            tambah_program.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
            hapus.setVisibility(View.GONE);
        }

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setView("hidden");
            }
        });
        action2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setView("hidden");
            }
        });
        tambahProgram.setEnabled(false);
        tambahProgram.setBackground(getResources().getDrawable(R.drawable.button_disabled));
        program_text.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!program_text.getText().toString().equals("")) {
                    tambahProgram.setEnabled(true);
                    tambahProgram.setBackground(getResources().getDrawable(R.drawable.button));
                } else {
                    tambahProgram.setEnabled(false);
                    tambahProgram.setBackground(getResources().getDrawable(R.drawable.button_disabled));
                }
            }
        });
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));
        bottomSheetBehavior2 = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout2));
        showActionBar();
    }


    public void setView(@NonNull String state) {
        switch (state) {
            case "collapsed":
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case "expanded":
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case "hidden":
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case "settling":
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_SETTLING);
                break;
        }

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                // Check Logs to see how bottom sheets behaves
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.e("Bottom Sheet Behaviour", "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.e("Bottom Sheet Behaviour", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.e("Bottom Sheet Behaviour", "STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        setView("collapsed");
                        Log.e("Bottom Sheet Behaviour", "STATE_HIDDEN");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.e("Bottom Sheet Behaviour", "STATE_SETTLING");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    public void setView2(@NonNull String state) {
        switch (state) {
            case "collapsed":
                bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case "expanded":
                bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case "hidden":
                bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_HIDDEN);
                break;
            case "settling":
                bottomSheetBehavior2.setState(BottomSheetBehavior.STATE_SETTLING);
                break;
        }

        bottomSheetBehavior2.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                // Check Logs to see how bottom sheets behaves
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.e("Bottom Sheet Behaviour", "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.e("Bottom Sheet Behaviour", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.e("Bottom Sheet Behaviour", "STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        setView("collapsed");
                        Log.e("Bottom Sheet Behaviour", "STATE_HIDDEN");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.e("Bottom Sheet Behaviour", "STATE_SETTLING");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void tambah_program() {
        String program = program_text.getText().toString();
        uploadProgram(program);
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

    private void setupRecyclerView(RecyclerView recyclerView, ArrayList<ProgramKedua> p) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        if (p.size() == 0) {
            LinearLayout info = (LinearLayout) findViewById(R.id.info_program);
            info.setVisibility(View.VISIBLE);
        }
        SimpleStringRecyclerViewAdapter k = new SimpleStringRecyclerViewAdapter(this, p, value, nama_perusahaan, nama_gambar);
        k.setCallback(new SimpleStringRecyclerViewAdapter.callback() {
            @Override
            public void action(Context c, ProgramKedua program, String action) {
                temp_progam = program;
                //setView("expanded");
                if (action.equals("edit")){
                    editProgram();
                }else if(action.equals("tambah")){
                    Intent intent;
                    intent = new Intent(ProgramActivity.this, TambahAktifitas.class);
                    intent.putExtra("id_program", temp_progam.getId_program());
                    startActivity(intent);
                }else if(action.equals("hapus")){
                    final AlertDialogCustom ad = new AlertDialogCustom(c);
                    ad.konfirmasi("KONFIRMASI", "Apakah anda yakin akan menghapus data?", R.drawable.trash, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteProgram();
                            ad.dismiss();
                        }
                    }, "YA", "TIDAK");
                }
            }

            public void action2(Context c,ProgramKedua program, Aktifitas aktivitas, String action) {
                temp_progam = program;
                temp_aktivitas = aktivitas;
                if(action.equals("update_aktivitas")){
                    Intent intent3 = new Intent(c, TambahRealisasi.class);
                    intent3.putExtra("id_aktivitas", temp_aktivitas.getId());
                    intent3.putExtra("id_kategori", temp_aktivitas.getIdKategori());
                    intent3.putExtra("id_program", temp_progam.getId_program());
                    intent3.putExtra("nama_program", temp_progam.getNama_program());
                    intent3.putExtra("nama_aktivitas", temp_aktivitas.getNama());
                    intent3.putExtra("due_date", "due: "+temp_aktivitas.getDuedate());
                    intent3.putExtra("target", format("%,d", temp_aktivitas.getRealisasi()).replace(",", ".") + "/" + format("%,d", temp_aktivitas.getTarget()).replace(",", ".") + " " + temp_aktivitas.getSatuan());
                    intent3.putExtra("realisasi_persen", temp_aktivitas.getRealisasi_persen()+"%");
                    intent3.putExtra("revenue", "Rp. " + format("%,d", temp_aktivitas.getRealisasi_revenue()).replace(",", ".") + " / Rp." + format("%,d", temp_aktivitas.getTarget_revenue()).replace(",", "."));
                    intent3.putExtra("kategori", temp_aktivitas.getKategori());
                    startActivity(intent3);
                }else if(action.equals("edit")){
                    Intent intent2 = new Intent(ProgramActivity.this, TambahAktifitas.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id_program", temp_progam.getId_program());
                    bundle.putParcelable("aktifitas", Parcels.wrap(temp_aktivitas));
                    intent2.putExtras(bundle);
                    startActivity(intent2);
                }else if(action.equals("hapus")){
                    final AlertDialogCustom ads = new AlertDialogCustom(c);
                    ads.konfirmasi("KONFIRMASI", "Apakah anda yakin akan menghapus data?", R.drawable.trash, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteAktifitas();
                            ads.dismiss();
                        }
                    }, "YA","TIDAK");
                }
                //setView2("expanded");
            }
        });
        recyclerView.setAdapter(k);
    }


    private void getProgram() {
        avi.show();
        String tokena = Prefs.getString(Config.TOKEN_BUMN, "");
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = Config.URL_GET_PROGRAM_PER + tokena + "/" + value;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("respon_program", response.toString());
                        avi.hide();
                        try {
                            if (response.getString("status").equals("success")) {
                                setupRecyclerView(rv, jsonDecodeProgram(response.getString("program")));
                            } else if (response.getString("status").equals("invalid-token")) {
                                GetToken k = new GetToken(ProgramActivity.this);
                                k.setCallback(new GetToken.callback() {
                                    @Override
                                    public void action(boolean success) {
                                        getProgram();
                                    }
                                });
                            }

                        } catch (JSONException E) {
                            Log.e("json_error", E.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(ProgramActivity.this);
        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(getRequest);
        queue.add(getRequest);
    }

    private void deleteProgram() {
        avi.show();
        String tokena = Prefs.getString(Config.TOKEN_BUMN, "");
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = Config.URL_DELETE_PROGRAM_PER + tokena + "/" + temp_progam.getId_program();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        avi.hide();
                        try {
                            if (response.getString("status").equals("delete-success")) {
                                getProgram();
                                Toast toast = Toast.makeText(ProgramActivity.this, "Sukses menghapus program", Toast.LENGTH_LONG);
                                toast.show();
                            } else if (response.getString("status").equals("invalid-token")) {
                                GetToken k = new GetToken(ProgramActivity.this);
                                k.setCallback(new GetToken.callback() {
                                    @Override
                                    public void action(boolean success) {
                                        deleteProgram();
                                    }
                                });
                            }

                        } catch (JSONException E) {
                            Log.e("json_error", E.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(ProgramActivity.this);
        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(getRequest);
        queue.add(getRequest);
    }

    private double convert(String data) {
        try {
            return Double.parseDouble(data);
        } catch (Exception e) {
            return -1;
        }

    }

    public ArrayList<ProgramKedua> jsonDecodeProgram(String jsonStr) {
        ArrayList<ProgramKedua> billing = new ArrayList<>();
        if (jsonStr != null) {
            try {
                JSONArray transaksi = new JSONArray(jsonStr);
                for (int i = 0; i < transaksi.length(); i++) {
                    JSONObject jObject = transaksi.getJSONObject(i);
                    JSONObject jsonArray = new JSONObject(jObject.getString("last_update"));
                    ProgramKedua d = new ProgramKedua(
                            i + 1,
                            jObject.getInt("id_program"),
                            jObject.getString("nama_program"),
                            cek_data(jObject.getString("realisasi_persen")),
                            jsonArray.getString("date"),
                            cek_data_target(jObject.getString("total_target")),
                            jObject.getString("target")
                    );
                    billing.add(d);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return billing;
    }

    private double cek_data(String data) {
        try {
            return Double.parseDouble(data);
        } catch (Exception e) {
            return 0;
        }
    }

    private int cek_data_target(String data) {
        try {
            return Integer.parseInt(data);
        } catch (Exception e) {
            return 0;
        }
    }

    private void uploadProgram(String nama_program) {
        avi.show();
        String token = Prefs.getString(Config.TOKEN_BUMN, "");
        VolleyClass cek = new VolleyClass(this, true);
        cek.get_data_from_server(new VolleyClass.VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                avi.hide();
                try {
                    JSONObject jObject = new JSONObject(response);
                    String status = jObject.getString("status");
                    if (status.equals("post-success")) {
                        program_text.setText("");
                        Toast toast = Toast.makeText(ProgramActivity.this, "Sukses Menambahkan Program", Toast.LENGTH_LONG);
                        toast.show();
                        LinearLayout info = (LinearLayout) findViewById(R.id.info_program);
                        info.setVisibility(View.GONE);
                        getProgram();
                    } else if (status.equals("wrong-id")) {
                        Toast.makeText(ProgramActivity.this, "Perusahaan tidak ada", Toast.LENGTH_LONG).show();
                    } else if (status.equals("post-failed")) {
                        Toast.makeText(ProgramActivity.this, "Post data gagal", Toast.LENGTH_LONG).show();
                    } else {
                        GetToken k = new GetToken(ProgramActivity.this);
                        k.setCallback(new GetToken.callback() {
                            @Override
                            public void action(boolean success) {
                                tambah_program();
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError() {
                avi.hide();
            }
        }, Config.URL_POST_PROGRAM_PER + token, new String[]{
                "nama_program" + "|" + nama_program,
                "keterangan" + "|" + "null",
                "id_perusahaan" + "|" + Prefs.getInt(Config.ID_BUMN, 0)

        });
    }

    @OnClick({R.id.tambah_program, R.id.edit, R.id.hapus, R.id.add, R.id.edit_aktivitas,R.id.hapus_aktivitas, R.id.update_aktivitas})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tambah_program:
                tambah_program();
                break;
            case R.id.edit:
                editProgram();
                setView("hidden");
                break;
            case R.id.add:
                Intent intent;
                intent = new Intent(ProgramActivity.this, TambahAktifitas.class);
                intent.putExtra("id_program", temp_progam.getId_program());
                startActivity(intent);
                setView("hidden");
                break;
            case R.id.hapus:
                final AlertDialogCustom ad = new AlertDialogCustom(this);
                ad.konfirmasi("KONFIRMASI", "Apakah anda yakin akan menghapus data?", R.drawable.trash, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteProgram();
                        ad.dismiss();
                    }
                }, "YA", "TIDAK");
                setView("hidden");
                break;
            case R.id.edit_aktivitas:
                Intent intent2 = new Intent(ProgramActivity.this, TambahAktifitas.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id_program", temp_progam.getId_program());
                bundle.putParcelable("aktifitas", Parcels.wrap(temp_aktivitas));
                intent2.putExtras(bundle);
                startActivity(intent2);
                setView2("hidden");
                break;
            case R.id.hapus_aktivitas:
                final AlertDialogCustom ads = new AlertDialogCustom(this);
                ads.konfirmasi("KONFIRMASI", "Apakah anda yakin akan menghapus data?", R.drawable.trash, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteAktifitas();
                        ads.dismiss();
                    }
                }, "YA","TIDAK");

                setView2("hidden");
                break;
            case R.id.update_aktivitas:
                Intent intent3 = new Intent(this, TambahRealisasi.class);
                intent3.putExtra("id_aktivitas", temp_aktivitas.getId());
                intent3.putExtra("id_kategori", temp_aktivitas.getIdKategori());
                intent3.putExtra("id_program", temp_progam.getId_program());
                intent3.putExtra("nama_program", temp_progam.getNama_program());
                intent3.putExtra("nama_aktivitas", temp_aktivitas.getNama());
                intent3.putExtra("due_date", "due: "+temp_aktivitas.getDuedate());
                intent3.putExtra("target", format("%,d", temp_aktivitas.getRealisasi()).replace(",", ".") + "/" + format("%,d", temp_aktivitas.getTarget()).replace(",", ".") + " " + temp_aktivitas.getSatuan());
                intent3.putExtra("target_asli", format("%,d", temp_aktivitas.getTarget()).replace(",", "."));
                intent3.putExtra("realisasi_persen", temp_aktivitas.getRealisasi_persen()+"%");
                intent3.putExtra("revenue", "Rp. " + format("%,d", temp_aktivitas.getRealisasi_revenue()).replace(",", ".") + " / Rp." + format("%,d", temp_aktivitas.getTarget_revenue()).replace(",", "."));
                intent3.putExtra("kategori", temp_aktivitas.getKategori());
                this.startActivity(intent3);
                break;
        }
    }

    private void deleteAktifitas() {
        avi.show();
        String tokena = Prefs.getString(Config.TOKEN_BUMN, "");
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = Config.URL_DELETE_TARGET_PROGRAM + tokena + "/" + temp_aktivitas.getId();
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        avi.hide();
                        try {
                            if (response.getString("status").equals("delete-success")) {
                                getProgram();
                                Toast.makeText(ProgramActivity.this, "Aktivitas sukses terhapus", Toast.LENGTH_LONG).show();
                            } else if (response.getString("status").equals("invalid-token")) {
                                GetToken k = new GetToken(ProgramActivity.this);
                                k.setCallback(new GetToken.callback() {
                                    @Override
                                    public void action(boolean success) {
                                        getProgram();
                                    }
                                });
                            }

                        } catch (JSONException E) {
                            Log.e("json_error", E.toString());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(ProgramActivity.this);
        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(getRequest);
        queue.add(getRequest);
    }

    private void posteditProgram(String program) {
        avi.show();
        String token = Prefs.getString(Config.TOKEN_BUMN, "");
        VolleyClass cek = new VolleyClass(this, true);
        cek.get_data_from_server(new VolleyClass.VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                avi.hide();
                try {
                    JSONObject jObject = new JSONObject(response);
                    String status = jObject.getString("status");
                    if (status.equals("edit-success")) {
                        program_text.setText("");
                        Toast toast = Toast.makeText(ProgramActivity.this, "Sukses Mengedit Program", Toast.LENGTH_LONG);
                        toast.show();
                        getProgram();
                    } else if (status.equals("wrong-id")) {
                        Toast.makeText(ProgramActivity.this, "Program tidak ada", Toast.LENGTH_LONG).show();
                    } else if (status.equals("edit-failed")) {
                        Toast.makeText(ProgramActivity.this, "Edit data gagal", Toast.LENGTH_LONG).show();
                    } else {
                        GetToken k = new GetToken(ProgramActivity.this);
                        k.setCallback(new GetToken.callback() {
                            @Override
                            public void action(boolean success) {
                                editProgram();
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {
                avi.hide();
            }
        }, Config.URL_EDIT_PROGRAM_PER + token, new String[]{
                "nama_program" + "|" + program,
                "keterangan" + "|" + "null",
                "id_perusahaan" + "|" + Prefs.getInt(Config.ID_BUMN, 0),
                "id_program" + "|" + temp_progam.getId_program()
        });
    }

    private void editProgram() {
        final AlertDialog dialog = buildDialog("Edit nama program");
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = edit_nama.getText().toString();
                posteditProgram(nama);
                dialog.dismiss();
            }
        });
    }

    private AlertDialog buildDialog(String title) {
        AlertDialog.Builder result = new AlertDialog.Builder(this);
        View alertView = getLayoutInflater().inflate(R.layout.dialog_edit_program, null);
        edit_nama = (EditText) alertView.findViewById(R.id.edit_nama);
        edit_nama.setText(temp_progam.getNama_program());
        result.setTitle(title)
                .setView(alertView)
                .setPositiveButton("Simpan", null)
                .setNegativeButton("Batal", null);
        AlertDialog dialog = result.create();
        dialog.show();
        return dialog;
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {
        private callback callback_variable;
        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private ArrayList<ProgramKedua> mValues;
        private String val, nama_per, gambar_per;
        Activity c;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ArrayList<ProgramKedua> mBoundString;

            public final View mView;
            @BindView(R.id.tv_no)
            TextView tvNo;
            @BindView(R.id.tv_nama)
            TextView tvNama;
            @BindView(R.id.total_aktivitas)
            TextView tvTotalAktivitas;
            @BindView(R.id.tv_menu)
            LinearLayout tvMenu;
            @BindView(R.id.progres_program)
            DonutProgress pgProgram;
            @BindView(R.id.last_update)
            TextView tvLastUpdate;
            @BindView(R.id.expanding)
            LinearLayout lnExpand;
            @BindView(R.id.table_lay)
            LinearLayout table_lay;

            @BindView(R.id.gambar_tambah)
            ImageView gambar_tambah;
            @BindView(R.id.gambar_edit)
            ImageView gambar_edit;
            @BindView(R.id.gambar_hapus)
            ImageView gambar_hapus;


            /*@BindView(R.id.progress)

            NumberProgressBar progress;
            @BindView(R.id.progress_kualitas)
            NumberProgressBar progress_kualitas;
            @BindView(R.id.progress_kapasitas)
            NumberProgressBar progress_kapasitas;
            @BindView(R.id.progress_komersial)
            NumberProgressBar progress_komersial;*/

            public ViewHolder(View view) {
                super(view);
                mView = view;
                ButterKnife.bind(this, view);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + tvNama.getText();
            }
        }

        public SimpleStringRecyclerViewAdapter(Activity context, ArrayList<ProgramKedua> items, String value, String nama_perusahaan,
                                               String gambar_perusahaan) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
            val = value;
            nama_per = nama_perusahaan;
            gambar_per = gambar_perusahaan;
            this.c = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_program_kedua, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.setIsRecyclable(false);
            holder.tvNo.setText("#" + mValues.get(position).getNo() + "");
            holder.tvTotalAktivitas.setText(mValues.get(position).getTotal_aktivitas() + " aktivitas");
            holder.pgProgram.setProgress((int) mValues.get(position).getRealisasi_target());


            String[] separated = mValues.get(position).getLast_update().split(" ");
            String[] bulan_tahun = separated[0].split("-");
            String[] jam = separated[1].split(":");
            holder.tvLastUpdate.setText("Last Update: " + bulan_tahun[2] + " " + bulan(bulan_tahun[1]) + " " + bulan_tahun[0] + " " + jam[0] + ":" + jam[1]);

            holder.tvNama.setText(mValues.get(position).getNama_program());
            if (Prefs.getInt(Config.ID_BUMN, 0) != Integer.parseInt(val)) {
                holder.tvMenu.setVisibility(View.GONE);
            }
            /*else {
                holder.tvMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (callback_variable != null) {
                            callback_variable.action(mValues.get(position), null);
                        }
                    }
                });
            }*/

            holder.gambar_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callback_variable != null) {
                        callback_variable.action(c, mValues.get(position), "edit");
                    }
                }
            });

            holder.gambar_tambah.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callback_variable != null) {
                        callback_variable.action(c, mValues.get(position), "tambah");
                    }
                }
            });

            holder.gambar_hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callback_variable != null) {
                        callback_variable.action(c, mValues.get(position), "hapus");
                    }
                }
            });


            //holder.progress.setProgress((int) mValues.get(position).getRealisasi_persen());
            //holder.progress_kualitas.setProgress((int) mValues.get(position).getKualitas_persen());
            //holder.progress_kapasitas.setProgress((int) mValues.get(position).getKuantitas_persen());
            //holder.progress_komersial.setProgress((int) mValues.get(position).getKomersial_persen());

            int jumlah_aktivitas = 0;
            try {
                JSONArray transaksi = new JSONArray(mValues.get(position).getTarget());
                for (int i = 0; i < transaksi.length(); i++) {

                    jumlah_aktivitas += 1;
                    JSONObject jObject = transaksi.getJSONObject(i);

                    final Aktifitas d = new Aktifitas(
                            i + 1,
                            jObject.getInt("id_target"),
                            jObject.getString("nama_aktivitas"),
                            jObject.getString("nama_kategori"),
                            new BigInteger(jObject.getString("target_nilai")),
                            new BigInteger(jObject.getString("revenue_target_nilai")),
                            new BigInteger(jObject.getString("realisasi")),
                            new BigInteger(jObject.getString("realisasi_revenue")),
                            jObject.getString("due_date"),
                            jObject.getString("nama_satuan"),
                            jObject.getDouble("realisasi_persen"),
                            jObject.getInt("status_revenue"),
                            jObject.getInt("id_kategori"),
                            jObject.getInt("id_satuan")
                    );

                    LayoutInflater li = LayoutInflater.from(c);
                    View inputnya = li.inflate(R.layout.inflate_aktivitas, null);
                    int nomor = i + 1;
                    TextView no = (TextView) inputnya.findViewById(R.id.nomor);
                    no.setText(nomor + "");

                    TextView nama_akvitas = (TextView) inputnya.findViewById(R.id.nama_aktivitas);
                    nama_akvitas.setText(jObject.getString("nama_aktivitas"));

                    TextView due_date = (TextView) inputnya.findViewById(R.id.due_date);
                    due_date.setText("Due: " + jObject.getString("due_date"));

                    TextView nama_kategori = (TextView) inputnya.findViewById(R.id.nama_kategori);
                    nama_kategori.setText(jObject.getString("nama_kategori"));

                    String text = "";
                    text+= format("%,d", new BigInteger(jObject.getString("realisasi"))).replace(",", ".") + "/" + format("%,d", new BigInteger(jObject.getString("target_nilai"))).replace(",", ".") + " " + jObject.getString("nama_satuan");
                    text+= " | "+jObject.getString("realisasi_persen") + "%";
                    //TextView realisasi = (TextView) inputnya.findViewById(R.id.realisasi);
                    //realisasi.setText(format("%,d", new BigInteger(jObject.getString("realisasi"))).replace(",", ".") + "/" + format("%,d", new BigInteger(jObject.getString("target_nilai"))).replace(",", ".") + " " + jObject.getString("nama_satuan"));

                    //TextView percentase = (TextView) inputnya.findViewById(R.id.persentase);
                    //percentase.setText(jObject.getString("realisasi_persen") + "%");

                    LinearLayout action_aktivitas = (LinearLayout) inputnya.findViewById(R.id.action_aktivitas);
                    if (Prefs.getInt(Config.ID_BUMN, 0) != Integer.parseInt(val)) {
                        action_aktivitas.setVisibility(View.GONE);
                    }

                    ImageView update_aktivitas = (ImageView) inputnya.findViewById(R.id.update_aktivitas);
                    ImageView edit_aktivitas = (ImageView) inputnya.findViewById(R.id.edit_aktivitas);
                    ImageView hapus_aktivitas = (ImageView) inputnya.findViewById(R.id.hapus_aktivitas);

                    update_aktivitas.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (callback_variable != null) {
                                callback_variable.action2(c,mValues.get(position),d,"update_aktivitas");
                            }
                        }
                    });

                    edit_aktivitas.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (callback_variable != null) {
                                callback_variable.action2(c,mValues.get(position),d,"edit");
                            }
                        }
                    });

                    hapus_aktivitas.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (callback_variable != null) {
                                callback_variable.action2(c,mValues.get(position),d,"hapus");
                            }
                        }
                    });


                    /*action_aktivitas.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });*/


                    if (jObject.getString("nama_kategori").equals("komersial")) {
                        //FrameLayout garis = (FrameLayout) inputnya.findViewById(R.id.garis);
                        //garis.setVisibility(View.VISIBLE);
                        text+=" | Rp. " + format("%,d", new BigInteger(jObject.getString("realisasi_revenue"))).replace(",", ".") + " / Rp." + format("%,d", new BigInteger(jObject.getString("revenue_target_nilai"))).replace(",", ".");
                        //TextView realisasi_revenue = (TextView) inputnya.findViewById(R.id.revenue);
                        //realisasi_revenue.setVisibility(View.VISIBLE);
                        //realisasi_revenue.setText("Rp. " + format("%,d", new BigInteger(jObject.getString("realisasi_revenue"))).replace(",", ".") + " / Rp." + format("%,d", new BigInteger(jObject.getString("revenue_target_nilai"))).replace(",", "."));
                    }

                    TextView realisasi_revenuse = (TextView) inputnya.findViewById(R.id.scrollingtext);
                    realisasi_revenuse.setText(text);
                    realisasi_revenuse.setSelected(true);

                    holder.table_lay.addView(inputnya);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (jumlah_aktivitas > 0) {
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.lnExpand.getVisibility() == View.GONE) {
                            holder.lnExpand.setVisibility(View.VISIBLE);
                            expand(holder.lnExpand);
                        } else {
                            collapse(holder.lnExpand);
                        }

                    /*Context context = v.getContext();
                    Intent intent;
                    intent = new Intent(context, AktifitasActivity.class);
                    intent.putExtra("id_program", mValues.get(position).getId_program());
                    intent.putExtra("ID_BUMN", val);
                    intent.putExtra("NAMA_PERUSAHAAN", nama_per);
                    intent.putExtra("NAMA_PROGRAM", mValues.get(position).getNama_program());
                    intent.putExtra("GAMBAR_PERUSAHAAN", gambar_per);
                    context.startActivity(intent);*/

                    }
                });
            }
        }

        public static void expand(final View v) {
            v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            final int targetHeight = v.getMeasuredHeight();

            // Older versions of android (pre API 21) cancel animations for views with a height of 0.
            v.getLayoutParams().height = 1;
            v.setVisibility(View.VISIBLE);
            Animation a = new Animation()
            {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    v.getLayoutParams().height = interpolatedTime == 1
                            ? LinearLayout.LayoutParams.WRAP_CONTENT
                            : (int)(targetHeight * interpolatedTime);
                    v.requestLayout();
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            // 1dp/ms
            a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density)+100);
            v.startAnimation(a);
        }

        public static void collapse(final View v) {
            final int initialHeight = v.getMeasuredHeight();

            Animation a = new Animation()
            {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    if(interpolatedTime == 1){
                        v.setVisibility(View.GONE);
                    }else{
                        v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                        v.requestLayout();
                    }
                }

                @Override
                public boolean willChangeBounds() {
                    return true;
                }
            };

            // 1dp/ms
            a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density)+100);
            v.startAnimation(a);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public void setCallback(callback callback) {
            this.callback_variable = callback;
        }

        public interface callback {
            public void action(Context c, ProgramKedua temp_progam, String action);
            public void action2(Context c, ProgramKedua temp_progam, Aktifitas temp_aktivitas, String action);
        }


        public String[] bulan_ = {"", "Januari ", "Februari ", "Maret ", "April ", "Mei ",
                "Juni ", "Juni ", "Agustus ", "Sepember ", "Oktober ", "November ", "Desember "};

        public String bulan(String bulan) {
            String bulan_sekarang = "";
            if (bulan.equals("01")) bulan_sekarang = bulan_[1];
            else if (bulan.equals("02")) bulan_sekarang = bulan_[2];
            else if (bulan.equals("03")) bulan_sekarang = bulan_[3];
            else if (bulan.equals("04")) bulan_sekarang = bulan_[4];
            else if (bulan.equals("05")) bulan_sekarang = bulan_[5];
            else if (bulan.equals("06")) bulan_sekarang = bulan_[6];
            else if (bulan.equals("07")) bulan_sekarang = bulan_[7];
            else if (bulan.equals("08")) bulan_sekarang = bulan_[8];
            else if (bulan.equals("09")) bulan_sekarang = bulan_[9];
            else if (bulan.equals("10")) bulan_sekarang = bulan_[10];
            else if (bulan.equals("11")) bulan_sekarang = bulan_[11];
            else if (bulan.equals("12")) bulan_sekarang = bulan_[12];
            return bulan_sekarang;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProgram();
    }


}
