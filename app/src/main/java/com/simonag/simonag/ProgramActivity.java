package com.simonag.simonag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.pixplicity.easyprefs.library.Prefs;
import com.simonag.simonag.model.Program;
import com.simonag.simonag.utils.AlertDialogCustom;
import com.simonag.simonag.utils.Config;
import com.simonag.simonag.utils.GetToken;
import com.simonag.simonag.utils.VolleyClass;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProgramActivity extends AppCompatActivity {
    public BottomSheetBehavior bottomSheetBehavior;
    String value, nama_perusahaan, nama_gambar;
    Program temp_progam;
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
    @BindView(R.id.hapus)
    LinearLayout hapus;
    @BindView(R.id.action)
    RelativeLayout action;
    EditText edit_nama;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Asap-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        setContentView(R.layout.activity_data_program);
        final LinearLayout tambah_program = (LinearLayout) findViewById(R.id.tambah_program_layout);
        TextView nama_bumn = (TextView) findViewById(R.id.nama_bumn);
        ImageView gambar_bumn =(ImageView) findViewById(R.id.gambar_bumn);

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
            tambah_program.setVisibility(View.GONE);
        }

        ButterKnife.bind(this);
        action.setOnClickListener(new View.OnClickListener() {
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
        showActionBar();
        getProgram();
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

    private void setupRecyclerView(RecyclerView recyclerView, ArrayList<Program> p) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        SimpleStringRecyclerViewAdapter k = new SimpleStringRecyclerViewAdapter(this, p, value, nama_perusahaan, nama_gambar);
        k.setCallback(new SimpleStringRecyclerViewAdapter.callback() {
            @Override
            public void action(Program program) {
                temp_progam = program;
                setView("expanded");
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

    public ArrayList<Program> jsonDecodeProgram(String jsonStr) {
        ArrayList<Program> billing = new ArrayList<>();
        if (jsonStr != null) {
            try {
                JSONArray transaksi = new JSONArray(jsonStr);
                for (int i = 0; i < transaksi.length(); i++) {
                    JSONObject jObject = transaksi.getJSONObject(i);
                    Program d = new Program(
                            i + 1,
                            jObject.getInt("id_program"),
                            jObject.getString("nama_program"),
                            jObject.getDouble("realisasi_persen")
                    );
                    billing.add(d);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return billing;
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

    @OnClick({R.id.tambah_program, R.id.edit, R.id.hapus})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tambah_program:
                tambah_program();
                break;
            case R.id.edit:
                editProgram();
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
                }, "YA","TIDAK");

                setView("hidden");
                break;
        }
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
        final AlertDialog dialog = buildDialog("Edit program");
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
        private ArrayList<Program> mValues;
        private String val, nama_per, gambar_per;
        Activity c;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ArrayList<Program> mBoundString;

            public final View mView;
            @BindView(R.id.tv_no)
            TextView tvNo;
            @BindView(R.id.tv_nama)
            TextView tvNama;
            @BindView(R.id.tv_menu)
            LinearLayout tvMenu;
            @BindView(android.R.id.progress)
            NumberProgressBar progress;

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

        public SimpleStringRecyclerViewAdapter(Activity context, ArrayList<Program> items, String value, String nama_perusahaan,
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_program, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.tvNo.setText(mValues.get(position).getNo() + "");

            holder.tvNama.setText(mValues.get(position).getNama_program());
            if (Prefs.getInt(Config.ID_BUMN, 0) != Integer.parseInt(val)) {
                holder.tvMenu.setVisibility(View.GONE);
            } else {
                holder.tvMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (callback_variable != null) {
                            callback_variable.action(mValues.get(position));
                        }
                    }
                });
            }
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    c.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (holder.progress.getProgress() < (int) mValues.get(position).getRealisasi_persen()) {
                                holder.progress.incrementProgressBy(1);
                            }
                        }
                    });
                }
            }, 500, 100);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, AktifitasActivity.class);
                    intent.putExtra("id_program", mValues.get(position).getId_program());
                    intent.putExtra("ID_BUMN", val);
                    intent.putExtra("NAMA_PERUSAHAAN", nama_per);
                    intent.putExtra("NAMA_PROGRAM", mValues.get(position).getNama_program());
                    intent.putExtra("GAMBAR_PERUSAHAAN", gambar_per);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public void setCallback(callback callback) {
            this.callback_variable = callback;
        }

        public interface callback {
            public void action(Program temp_progam);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
