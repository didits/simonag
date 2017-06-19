package com.simonag.simonag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
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
import com.pixplicity.easyprefs.library.Prefs;
import com.simonag.simonag.model.Aktifitas;
import com.simonag.simonag.utils.AlertDialogCustom;
import com.simonag.simonag.utils.Config;
import com.simonag.simonag.utils.GetToken;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static java.lang.String.format;

/**
 * Created by diditsepiyanto on 6/13/17.
 */

public class AktifitasActivity extends AppCompatActivity {
    public static final String EXTRA_NAME = "id_program";
    public BottomSheetBehavior bottomSheetBehavior;
    String value, nama, pic, program;
    Aktifitas temp_aktivitas;
    @BindView(R.id.tambah_aktifitas)
    LinearLayout tambahAktifitas;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.rv_aktifitas)
    RecyclerView rv;
    @BindView(R.id.edit)
    LinearLayout edit;
    @BindView(R.id.hapus)
    LinearLayout hapus;
    @BindView(R.id.action)
    RelativeLayout action;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Asap-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_data_aktifitas);
        TextView nama_bumn = (TextView) findViewById(R.id.nama_bumn);
        TextView nama_program = (TextView) findViewById(R.id.nama_program);
        ImageView gambar_bumn = (ImageView) findViewById(R.id.gambar_bumn);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("ID_BUMN");
            nama = extras.getString("NAMA_PERUSAHAAN");
            program = extras.getString("NAMA_PROGRAM");
            pic = extras.getString("GAMBAR_PERUSAHAAN");
        }



        nama_bumn.setText(nama);
        nama_program.setText("Program: " + program);
        String url = Config.URL_GAMBAR + pic;
        Log.d("urlnih",url);
        Glide.with(gambar_bumn.getContext())
                .load(url)
                .fitCenter()
                .into(gambar_bumn);

        ButterKnife.bind(this);
        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setView("hidden");
            }
        });
        if (Prefs.getInt(Config.ID_BUMN, 0) != Integer.parseInt(value)) {
            tambahAktifitas.setVisibility(View.GONE);
            setTitle("Progress Aktifitas");
        }
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));
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

    private void setupRecyclerView(RecyclerView recyclerView, ArrayList<Aktifitas> p, String value) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        if(p.size()==0){
            LinearLayout info = (LinearLayout) findViewById(R.id.info_program);
            info.setVisibility(View.VISIBLE);
        }
        SimpleStringRecyclerViewAdapter k = new SimpleStringRecyclerViewAdapter(this, p, value);
        k.setCallback(new SimpleStringRecyclerViewAdapter.callback() {
            @Override
            public void action(Aktifitas aktifitas) {
                temp_aktivitas = aktifitas;
                setView("expanded");
            }
        });
        recyclerView.setAdapter(k);
    }


    @OnClick({R.id.tambah_aktifitas, R.id.edit, R.id.hapus})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tambah_aktifitas:
                Intent intent = new Intent(AktifitasActivity.this, TambahAktifitas.class);
                intent.putExtra("id_program", getIntent().getExtras().getInt("id_program"));
                startActivity(intent);
                break;
            case R.id.edit:
                Intent intent2 = new Intent(AktifitasActivity.this, TambahAktifitas.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id_program", getIntent().getExtras().getInt("id_program"));
                bundle.putParcelable("aktifitas", Parcels.wrap(temp_aktivitas));
                intent2.putExtras(bundle);
                startActivity(intent2);
                setView("hidden");
                break;
            case R.id.hapus:
                final AlertDialogCustom ad = new AlertDialogCustom(this);
                ad.konfirmasi("KONFIRMASI", "Apakah anda yakin akan menghapus data?", R.drawable.trash, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteAktifitas();
                        ad.dismiss();
                    }
                }, "YA","TIDAK");

                setView("hidden");
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
                                getAktifitas();
                                Toast.makeText(AktifitasActivity.this, "Aktivitas sukses terhapus", Toast.LENGTH_LONG).show();
                            } else if (response.getString("status").equals("invalid-token")) {
                                GetToken k = new GetToken(AktifitasActivity.this);
                                k.setCallback(new GetToken.callback() {
                                    @Override
                                    public void action(boolean success) {
                                        getAktifitas();
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
        RequestQueue requestQueue = Volley.newRequestQueue(AktifitasActivity.this);
        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(getRequest);
        queue.add(getRequest);
    }


    private void getAktifitas() {
        avi.show();
        String tokena = Prefs.getString(Config.TOKEN_BUMN, "");
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = Config.URL_GET_TARGET_PROGRAM + tokena + "/" + getIntent().getExtras().getInt("id_program");
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        avi.hide();
                        try {
                            Log.d("statuss", response.toString());
                            if (response.getString("status").equals("success")) {
                                setupRecyclerView(rv, jsonDecodeAktifitas(response.getString("target")), value);
                            } else if (response.getString("status").equals("invalid-token")) {
                                GetToken k = new GetToken(AktifitasActivity.this);
                                k.setCallback(new GetToken.callback() {
                                    @Override
                                    public void action(boolean success) {
                                        getAktifitas();
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
        RequestQueue requestQueue = Volley.newRequestQueue(AktifitasActivity.this);
        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(getRequest);
        queue.add(getRequest);
    }


    public ArrayList<Aktifitas> jsonDecodeAktifitas(String jsonStr) {
        ArrayList<Aktifitas> billing = new ArrayList<>();
        if (jsonStr != null) {
            try {
                JSONArray transaksi = new JSONArray(jsonStr);
                if(transaksi.length()!=0){
                    LinearLayout info = (LinearLayout) findViewById(R.id.info_program);
                    info.setVisibility(View.GONE);
                }
                for (int i = 0; i < transaksi.length(); i++) {
                    JSONObject jObject = transaksi.getJSONObject(i);
                    Aktifitas d = new Aktifitas(
                            i + 1,
                            jObject.getInt("id_target"),
                            jObject.getString("nama_aktivitas"),
                            jObject.getString("nama_kategori"),
                            jObject.getInt("target_nilai"),
                            jObject.getInt("revenue_target_nilai"),
                            jObject.getInt("realisasi"),
                            jObject.getInt("realisasi_revenue"),
                            jObject.getString("due_date"),
                            jObject.getString("nama_satuan"),
                            jObject.getDouble("realisasi_persen"),
                            jObject.getInt("status_revenue"),
                            jObject.getInt("id_kategori"),
                            jObject.getInt("id_satuan")
                    );
                    billing.add(d);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return billing;
    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {
        private callback callback_variable;
        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private ArrayList<Aktifitas> mValues;
        private String val;
        Activity c;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            @BindView(R.id.tv_no)
            TextView tvNo;
            @BindView(R.id.tv_nama)
            TextView tvNama;
            @BindView(R.id.tv_persen)
            TextView tvPersen;
            @BindView(R.id.tv_duedate)
            TextView tvDuedate;
            @BindView(R.id.tv_target)
            TextView tvTarget;
            @BindView(R.id.tv_realisasi)
            TextView tvRealisasi;
            @BindView(R.id.tv_kategori)
            TextView tvKategori;
            @BindView(R.id.view_detail)
            LinearLayout viewDetail;
            @BindView(R.id.tv_menu)
            LinearLayout tvMenu;
            @BindView(R.id.gambar_kategori)
            ImageView imgGambarKategori;

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

        public SimpleStringRecyclerViewAdapter(Activity context, ArrayList<Aktifitas> items, String value) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
            this.val = value;
            this.c = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_aktivitas, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.tvNo.setText(mValues.get(position).getNo() + "");
            holder.tvNama.setText(mValues.get(position).getNama());
            holder.tvPersen.setText((int)mValues.get(position).getRealisasi_persen()+"%");
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            SimpleDateFormat format2 = new SimpleDateFormat("dd MMM yyyy", Locale.US);

            try {
                holder.tvDuedate.setText("Due Date: "+format2.format(format1.parse(mValues.get(position).getDuedate())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.tvKategori.setText("Kategori: "+mValues.get(position).getKategori());
            if(mValues.get(position).getKategori().equals("kualitas")){
                holder.imgGambarKategori.setImageResource(R.drawable.percentage);
                holder.tvRealisasi.setVisibility(View.GONE);
                holder.tvTarget.setText("Realisasi: " + mValues.get(position).getRealisasi() + "/" + mValues.get(position).getTarget() + " (%)");
            }else if(mValues.get(position).getKategori().equals("kapasitas")){
                holder.imgGambarKategori.setImageResource(R.drawable.warehouse);
                holder.tvRealisasi.setVisibility(View.GONE);
                holder.tvTarget.setText("Realisasi: " + mValues.get(position).getRealisasi() +"/" + mValues.get(position).getTarget() +" ("+ mValues.get(position).getSatuan()+")");
            }else if(mValues.get(position).getKategori().equals("komersial")){
                holder.imgGambarKategori.setImageResource(R.drawable.coin);
                holder.tvTarget.setText("Realisasi: " + mValues.get(position).getRealisasi() +"/"+ mValues.get(position).getTarget() +" ("+ mValues.get(position).getSatuan()+")");
                holder.tvRealisasi.setText("Revenue: " + format("%,d", mValues.get(position).getTarget_revenue()).replace(",", ".")
                        + "/" + format("%,d", mValues.get(position).getRealisasi_revenue()).replace(",", ".")+" (Rupiah)");
            }


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

            holder.viewDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Prefs.getInt(Config.ID_BUMN, 0) == Integer.parseInt(val)) {
                        Context context = v.getContext();
                        Intent intent2 = new Intent(context, TambahRealisasi.class);
                        intent2.putExtra("id_aktivitas", mValues.get(position).getId());
                        intent2.putExtra("id_kategori", mValues.get(position).getIdKategori());
                        intent2.putExtra("id_program", ((AktifitasActivity) context).getIntent().getExtras().getInt("id_program"));
                        context.startActivity(intent2);
                    }
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
            public void action(Aktifitas aktifitas);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAktifitas();
    }
}
