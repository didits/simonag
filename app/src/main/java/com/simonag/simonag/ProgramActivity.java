package com.simonag.simonag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.android.volley.DefaultRetryPolicy;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.pixplicity.easyprefs.library.Prefs;

import com.simonag.simonag.model.Program;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProgramActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "id_bumn";
    String value;
    LinearLayout loading;

    RecyclerView rv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("KEY");
        }

        setContentView(R.layout.activity_data_program);
        loading = (LinearLayout) findViewById(R.id.loading);
        rv = (RecyclerView) findViewById(R.id.rv_program);
        getProgram();

        showActionBar();
        final Button tambah_program = (Button) findViewById(R.id.tambah_program);
        tambah_program.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambah_program();
            }
        });
    }

    private void tambah_program() {
        EditText tambah_program = (EditText) findViewById(R.id.program);
        String program = tambah_program.getText().toString();
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
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(RecyclerView recyclerView, ArrayList<Program> p) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(this,
                p));
    }


    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private ArrayList<Program> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ArrayList<Program> mBoundString;

            public final View mView;
            @BindView(R.id.tv_no)
            TextView tvNo;
            @BindView(R.id.tv_nama)
            TextView tvNama;
            @BindView(R.id.tv_menu)
            ImageView tvMenu;

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

        public SimpleStringRecyclerViewAdapter(Context context, ArrayList<Program> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_nama_program, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.tvNo.setText(mValues.get(position).getNo()+"");
            holder.tvNama.setText(mValues.get(position).getNama_program());
            holder.tvMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, AktifitasActivity.class);
                    intent.putExtra(AktifitasActivity.EXTRA_NAME, holder.mBoundString);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }

    private void getProgram() {
        String tokena = Prefs.getString(Config.TOKEN_BUMN, "");
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d("tokena", tokena);
        final String url = Config.URL_GET_PROGRAM_PER + tokena + "/" + Prefs.getInt(Config.ID_BUMN, 0);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("token", response.toString());
                        loading.setVisibility(View.GONE);
                        try {
                            if (response.getString("status").equals("success")) {
                                setupRecyclerView(rv, jsonDecodeProgram(response.getString("program")));
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
                            i+1,
                            jObject.getInt("id_program"),
                            jObject.getString("nama_program")
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
        loading.setVisibility(View.VISIBLE);
        String token = Prefs.getString(Config.TOKEN_BUMN, "");

        VolleyClass cek = new VolleyClass(this, true);
        cek.get_data_from_server(new VolleyClass.VolleyCallback() {
            @Override
            public void onSuccess(String response) {

                Log.d("respon onSuccess", response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    String status = jObject.getString("status");
                    if (status.equals("post-success")) {
                        Toast toast = Toast.makeText(ProgramActivity.this, "Sukses Menambahkan Program", Toast.LENGTH_LONG);
                        toast.show();
                        getProgram();
                    } else if (status.equals("wrong-id")) {
                        Toast.makeText(ProgramActivity.this, "Perusahaan tidak ada", Toast.LENGTH_LONG).show();
                    } else if (status.equals("post-failed")) {
                        Toast.makeText(ProgramActivity.this, "Post data gagal", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ProgramActivity.this, "Token salah", Toast.LENGTH_LONG).show();}

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError() {

            }
        }, Config.URL_POST_PROGRAM_PER + token, new String[]{
                "nama_program" + "|" + nama_program,
                "keterangan" + "|" + "null",
                "id_perusahaan" + "|" + Prefs.getInt(Config.ID_BUMN, 0)

        });
    }
}
