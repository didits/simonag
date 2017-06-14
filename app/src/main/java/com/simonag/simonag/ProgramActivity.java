package com.simonag.simonag;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

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
    ArrayList<Program> db = new ArrayList<>();
    @BindView(R.id.tv_program)
    EditText tvProgram;
    @BindView(R.id.add)
    Button add;
    SimpleStringRecyclerViewAdapter adapter;
    RecyclerView rv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_program);
        ButterKnife.bind(this);
        showActionBar();
        rv = (RecyclerView) findViewById(R.id.rv_program);
        getProgram();
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

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        adapter =new SimpleStringRecyclerViewAdapter(this, db);
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.add)
    public void onViewClicked() {
        addprogram();
    }


    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private ArrayList<Program> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public ArrayList<Program> mBoundString;

            @BindView(R.id.tv_no)
            TextView tvNo;
            @BindView(R.id.tv_nama)
            TextView tvNama;
            @BindView(R.id.tv_menu)
            ImageView tvMenu;

            public ViewHolder(View view) {
                super(view);
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
            holder.tvNama.setText(mValues.get(position).getNama_program());
            holder.tvMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            holder.tvNama.setOnClickListener(new View.OnClickListener() {
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
        int id_perusahaan = Prefs.getInt(Config.ID_BUMN, 0);
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = Config.URL_GET_PROGRAM_PER + tokena + "/" + id_perusahaan;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            db = jsonDecodeBilling(response.getString("program"));
                        } catch (JSONException E) {
                            Log.e("json_error", E.toString());
                        }
                        setupRecyclerView(rv);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        queue.add(getRequest);
    }

    public ArrayList<Program> jsonDecodeBilling(String jsonStr) {
        ArrayList<Program> billing = new ArrayList<>();
        if (jsonStr != null) {
            try {
                JSONArray transaksi = new JSONArray(jsonStr);
                for (int i = 0; i < transaksi.length(); i++) {
                    JSONObject jObject = transaksi.getJSONObject(i);
                    Program d = new Program(
                            i,
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

    private void addprogram() {
        VolleyClass cek = new VolleyClass(this, true);
        cek.get_data_from_server(new VolleyClass.VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jObject = new JSONObject(response);
                    String status = jObject.getString("status");
                    if (status.equals("success")) {
                        getProgram();
                    } else if (status.equals("wrong-id")) {
                        Toast.makeText(ProgramActivity.this, "Perusahaan tidak ada", Toast.LENGTH_LONG).show();
                    } else if (status.equals("post-failed")) {
                        Toast.makeText(ProgramActivity.this, "Post data gagal", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ProgramActivity.this, "Token salah", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError() {

            }
        }, Config.URL_LOGIN, new String[]{
                "nama_program" + "|" + tvProgram.getText().toString(),
                "keterangan" + "|" + tvProgram.getText().toString(),
                "id_perusahaan" + "|" + Prefs.getString(Config.ID_BUMN,"")
        });
    }
}
