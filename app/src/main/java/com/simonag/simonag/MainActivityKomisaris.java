package com.simonag.simonag;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.pixplicity.easyprefs.library.Prefs;
import com.simonag.simonag.model.DashboardKomisaris;
import com.simonag.simonag.model.Kategori;
import com.simonag.simonag.model.Pertanggal;
import com.simonag.simonag.utils.AlertDialogCustom;
import com.simonag.simonag.utils.Config;
import com.simonag.simonag.utils.GetToken;
import com.simonag.simonag.utils.VolleyClass;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivityKomisaris extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    public AccountHeader headerResult;
    public Drawer result;
    ArrayList<DashboardKomisaris> dbkom = new ArrayList<>();
    ArrayList<Kategori> db_kategori = new ArrayList<>();
    ArrayList<Pertanggal> db_tanggal = new ArrayList<>();
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    int tab_selected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        setContentView(R.layout.activity_main_komisaris);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getDashboard();
        showActionBar();
    }

    private void showActionBar() {
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayUseLogoEnabled(true);
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
    }


    private void getTabSelected(){
        try {
            if(tabLayout.getTabAt(0).isSelected()){
                tab_selected=0;
            }else if(tabLayout.getTabAt(1).isSelected()){
                tab_selected=1;
            }else if(tabLayout.getTabAt(2).isSelected()){
                tab_selected=2;
            }
        }catch (Exception ignore){

        }

    }

    private void createTabIcons() {
        LinearLayout tabOne = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView judul = (TextView) tabOne.findViewById(R.id.tab);
        TextView persentase = (TextView) tabOne.findViewById(R.id.percent);
        judul.setText("Sponsorship");
        persentase.setText("Per BUMN");
        tabLayout.getTabAt(0).setCustomView(tabOne);

        LinearLayout tabTwo = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView judulTwo = (TextView) tabTwo.findViewById(R.id.tab);
        TextView persentaseTwo = (TextView) tabTwo.findViewById(R.id.percent);
        judulTwo.setText("Sponsorship");
        persentaseTwo.setText("Per Kategori");
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        LinearLayout tabThree = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView judulThree = (TextView) tabThree.findViewById(R.id.tab);
        TextView persentaseThree = (TextView) tabThree.findViewById(R.id.percent);
        judulThree.setText("Sponsorship");
        persentaseThree.setText("Per Tanggal");
        tabLayout.getTabAt(2).setCustomView(tabThree);

        if(tab_selected==0){
            tabLayout.getTabAt(0).select();
        }else if(tab_selected==1){
            tabLayout.getTabAt(1).select();
        }else if(tab_selected==2){
            tabLayout.getTabAt(2).select();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new DashboardAktivitasBUMNFragment(), "Kualitas");
        adapter.addFragment(new DashboardAktivitasKategoriFragmentCoba(), "Kapasitas");
        adapter.addFragment(new DashboardAktivitasTanggalFragment(), "Komersial");
        try {
            viewPager.setAdapter(adapter);
        }catch (Exception e){

        }

    }

    private void out() {
        AlertDialog.Builder pilihan = new AlertDialog.Builder(this);
        pilihan.setMessage("Anda ingin keluar?");
        pilihan.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Prefs.clear();
                startActivity(new Intent(MainActivityKomisaris.this, LoginActivity.class));
                finish();
            }
        });
        pilihan.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alert = pilihan.create();
        alert.show();
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }


    private void getDashboard() {
        avi.show();
        String tokena = Prefs.getString(Config.TOKEN_BUMN, "");
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = Config.URL_GET_DASHBOARD_2 + tokena;
        Log.d("dead", url);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("yes", response.getString("kategori2"));
                            if(response.getString("status").equals("success")){
                                dbkom = jsonDecodeBilling(response.getString("perusahaan"));
                                db_kategori = jsonDecodeAllKategori(response.getString("kategori2"));
                                db_tanggal = jsonPertanggal(response.getString("pertanggal"));

                                if (viewPager != null) {
                                    viewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
                                    setupViewPager(viewPager);
                                }
                                tabLayout.setupWithViewPager(viewPager);
                                createTabIcons();
                                avi.hide();
                            }else if(response.getString("status").equals("invalid-token")){
                                GetToken k = new GetToken(MainActivityKomisaris.this);
                                k.setCallback(new GetToken.callback() {
                                    @Override
                                    public void action(boolean success) {
                                        getDashboard();
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
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivityKomisaris.this);
        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(getRequest);
        queue.add(getRequest);
    }

    private void getDashboardFilter(final String tanggal_awal, final  String tanggal_akhir) {
        avi.show();
        VolleyClass cek = new VolleyClass(this, true);
        cek.get_data_from_server(new VolleyClass.VolleyCallback() {
            @Override
            public void onSuccess(String s) {
                try {
                    JSONObject response = new JSONObject(s);
                    if(response.getString("status").equals("success")){
                        dbkom = jsonDecodeBilling(response.getString("perusahaan"));
                        db_kategori = jsonDecodeAllKategori(response.getString("kategori2"));
                        db_tanggal = jsonPertanggal(response.getString("pertanggal"));
                        if (viewPager != null) {
                            viewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
                            setupViewPager(viewPager);
                        }
                        tabLayout.setupWithViewPager(viewPager);
                        createTabIcons();
                        avi.hide();
                    }else if(response.getString("status").equals("invalid-token")){
                        GetToken k = new GetToken(MainActivityKomisaris.this);
                        k.setCallback(new GetToken.callback() {
                            @Override
                            public void action(boolean success) {
                                getDashboardFilter(tanggal_awal, tanggal_akhir);
                            }
                        });
                    }

                } catch (JSONException E) {
                    Log.e("json_error", E.toString());
                }
            }

            @Override
            public void onError() {

            }
        }, Config.URL_FILTER_2 + Prefs.getString(Config.TOKEN_BUMN, ""), new String[]{
                "tanggal_awal" + "|" + tanggal_awal,
                "tanggal_akhir" + "|" + tanggal_akhir
        });
    }


    public ArrayList<DashboardKomisaris> jsonDecodeBilling(String jsonStr) {
        ArrayList<DashboardKomisaris> billing = new ArrayList<>();

        if (jsonStr != null) {
            try {
                JSONArray transaksi = new JSONArray(jsonStr);
                for (int i = 0; i < transaksi.length(); i++) {
                    JSONObject jObject = transaksi.getJSONObject(i);
                    DashboardKomisaris d = new DashboardKomisaris(
                            jObject.getInt("id_perusahaan"),
                            jObject.getString("nama_perusahaan"),
                            jObject.getString("keterangan"),
                            new BigInteger(jObject.getString("total_rupiah")),
                            jObject.getInt("total_aktivitas"),
                            jObject.getString("image")
                    );
                    billing.add(d);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return billing;
    }

    public ArrayList<Pertanggal> jsonPertanggal(String jsonStr) {
        ArrayList<Pertanggal> billing = new ArrayList<>();

        if (jsonStr != null) {
            try {
                JSONArray transaksi = new JSONArray(jsonStr);
                for (int i = 0; i < transaksi.length(); i++) {
                    JSONObject jObject = transaksi.getJSONObject(i);
                    Pertanggal d = new Pertanggal(
                            i,
                            jObject.getString("tanggal"),
                            jObject.getInt("total_aktivitas"),
                            new BigInteger(jObject.getString("total_biaya"))
                    );
                    billing.add(d);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return billing;
    }

    public ArrayList<Kategori> jsonDecodeAllKategori(String jsonStr) {
        ArrayList<Kategori> billing = new ArrayList<>();

        if (jsonStr != null) {
            try {
                JSONArray transaksi = new JSONArray(jsonStr);
                for (int i = 0; i < transaksi.length(); i++) {
                    JSONObject jObject = transaksi.getJSONObject(i);
                    Kategori d = new Kategori(
                            jObject.getInt("id_kategori"),
                            jObject.getString("nama_kategori"),
                            jObject.getInt("status"),
                            change_null(jObject.getString("total_rupiah")),
                            jObject.getInt("total_aktivitas")
                    );
                    billing.add(d);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return billing;
    }

    private BigInteger change_null(String data){
        try {
            return new BigInteger(data);
        }catch (Exception e){
            return new BigInteger("0");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tanggal:
                getTabSelected();
                final AlertDialogCustom ad = new AlertDialogCustom(this);
                ad.tanggal_awal_akhir(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView tanggal_awal = (TextView) view.getRootView().findViewById(R.id.tanggal_awal);
                        TextView tanggal_akhir = (TextView) view.getRootView().findViewById(R.id.tanggal_akhir);
                        getDashboardFilter(tanggal_awal.getText().toString(), tanggal_akhir.getText().toString());
                        ad.dismiss();
                    }
                });
                return true;
            case android.R.id.home:
                finish();
            case R.id.aktivitas:
                Prefs.putInt(Config.FILTER_KOMISARIS, 0);
                getTabSelected();
                getDashboard();
                return true;
            case R.id.biaya:
                Prefs.putInt(Config.FILTER_KOMISARIS, 1);
                getTabSelected();
                getDashboard();
                return true;
            case R.id.refresh:
                getTabSelected();
                getDashboard();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bu_devy, menu);
        return true;
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}