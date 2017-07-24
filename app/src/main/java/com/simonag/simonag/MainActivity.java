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
import com.simonag.simonag.model.Dashboard;
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


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    public AccountHeader headerResult;
    public Drawer result;
    ArrayList<Dashboard> db = new ArrayList<>();
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
    @BindView(R.id.header)
    LinearLayout header;
    @BindView(R.id.subheader)
    LinearLayout subheader;
    @BindView(R.id.jumlah_bumn)
    TextView keterangan;
    boolean dashajs=true;

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
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        String url = Config.URL_GAMBAR + Prefs.getString(Config.FOTO, "");
        final IProfile profile = new ProfileDrawerItem().withName(Prefs.getString(Config.NAMA_BUMN, ""))
                .withEmail(Prefs.getString(Config.EMAIL_BUMN, "")).withIcon(url);
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.latar)
                .addProfiles(
                        profile
                )
                .withSelectionListEnabledForSingleProfile(false)
                .build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withSliderBackgroundColorRes(R.color.colorWhiteTrans)
                .withDrawerWidthDp(200)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(1).withName("Pencapaian 3K").withIcon(FontAwesome.Icon.faw_check_square_o),
                        new PrimaryDrawerItem().withIdentifier(2).withName("Sponsorship").withIcon(FontAwesome.Icon.faw_handshake_o),
                        new PrimaryDrawerItem().withIdentifier(3).withName("Input Program").withIcon(FontAwesome.Icon.faw_plus),
                        new PrimaryDrawerItem().withIdentifier(4).withName("Input Sponsorship").withIcon(FontAwesome.Icon.faw_plus),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withIdentifier(5).withName("Tentang").withIcon(FontAwesome.Icon.faw_info),
                        new PrimaryDrawerItem().withIdentifier(6).withName("Keluar").withIcon(FontAwesome.Icon.faw_sign_out)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Intent i;
                        switch ((int) drawerItem.getIdentifier()) {
                            case 1:
                                dashajs=true;
                                setTitle("Pencapaian 3K");
                                invalidateOptionsMenu();
                                header.setVisibility(View.VISIBLE);
                                subheader.setVisibility(View.VISIBLE);
                                getDashboard();
                                break;
                            case 2:
                                /*dashajs=false;
                                setTitle("Sponsorship");
                                invalidateOptionsMenu();
                                header.setVisibility(View.GONE);
                                subheader.setVisibility(View.GONE);
                                getDashboard();*/
                                i = new Intent(MainActivity.this, MainActivityKomisaris.class);
                                startActivity(i);
                                break;
                            case 3:
                                i = new Intent(MainActivity.this, ProgramActivity.class);
                                i.putExtra("KEY", "" + Prefs.getInt(Config.ID_BUMN, 0));
                                i.putExtra("NAMA_PERUSAHAAN", "" + Prefs.getString(Config.NAMA_BUMN, "").toUpperCase());
                                i.putExtra("GAMBAR_PERUSAHAAN", "" + Prefs.getString(Config.FOTO, ""));
                                startActivity(i);
                                break;
                            case 4:
                                i = new Intent(MainActivity.this, AktifitasActivityKomisaris.class);
                                i.putExtra("ID_BUMN", "" + Prefs.getInt(Config.ID_BUMN, 0));
                                i.putExtra("NAMA_PERUSAHAAN", "" + Prefs.getString(Config.NAMA_BUMN, "").toUpperCase());
                                startActivity(i);
                                break;
                            case 5:
                                i = new Intent(MainActivity.this, TentangActivity.class);
                                startActivity(i);
                                break;
                            case 6:
                                out();
                                break;
                        }
                        return false;
                    }
                })
                .build();
        getDashboard();
    }


    private void createTabIcons(Double kualitas, Double kapasitas, Double komersial) {
        LinearLayout tabOne = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView judul = (TextView) tabOne.findViewById(R.id.tab);
        TextView persentase = (TextView) tabOne.findViewById(R.id.percent);
        if(dashajs){
            judul.setText("KUALITAS");
            persentase.setText(kualitas + "%");
        }else{
            judul.setText("Sponsorship");
            persentase.setText("BUMN");
        }
        tabLayout.getTabAt(0).setCustomView(tabOne);

        LinearLayout tabTwo = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView judulTwo = (TextView) tabTwo.findViewById(R.id.tab);
        TextView persentaseTwo = (TextView) tabTwo.findViewById(R.id.percent);
        if(dashajs){
            judulTwo.setText("KAPASITAS");
            persentaseTwo.setText(kapasitas + "%");
        }else{
            judulTwo.setText("Sponsorship");
            persentaseTwo.setText("Kategori");
        }
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        LinearLayout tabThree = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView judulThree = (TextView) tabThree.findViewById(R.id.tab);
        TextView persentaseThree = (TextView) tabThree.findViewById(R.id.percent);
        if(dashajs){
            judulThree.setText("KOMERSIAL");
            persentaseThree.setText(komersial + "%");
        }else{
            judulThree.setText("Sponsorship");
            persentaseThree.setText("Tanggal");
        }

        tabLayout.getTabAt(2).setCustomView(tabThree);

        if (tab_selected == 0) {
            tabLayout.getTabAt(0).select();
        } else if (tab_selected == 1) {
            tabLayout.getTabAt(1).select();
        } else if (tab_selected == 2) {
            tabLayout.getTabAt(2).select();
        }

    }

    private void setupViewPager(ViewPager viewpager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        if(dashajs){
            adapter.addFragment(new DashboardKualitasFragment(), "Kualitas");
            adapter.addFragment(new DashboardKapasitasFragment(), "Kapasitas");
            adapter.addFragment(new DashboardKomersialFragment(), "Komersial");
        }else{
            adapter.addFragment(new DashboardAktivitasBUMNFragment(), "BUMN");
            adapter.addFragment(new DashboardAktivitasKategoriFragmentCoba(), "Kategori");
            adapter.addFragment(new DashboardAktivitasTanggalFragment(), "Tanggal");
        }
        viewpager.setAdapter(adapter);
    }

    private void out() {
        AlertDialog.Builder pilihan = new AlertDialog.Builder(this);
        pilihan.setMessage("Anda ingin keluar?");
        pilihan.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Prefs.clear();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
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
        String url = "";
        if(dashajs){
            url=Config.URL_GET_DASHBOARD + tokena;
        }else{
            url=Config.URL_GET_DASHBOARD_2 + tokena;
        }
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("success")) {
                                if(dashajs){
                                    db = jsonDecodeBilling(response.getString("perusahaan"));
                                    keterangan.setVisibility(View.VISIBLE);
                                    keterangan.setText("Jumlah BUMN: " + response.getString("jumlah_perusahaan")
                                            + " | Jumlah Program: " + response.getString("jumlah_program"));
                                }else{
                                    dbkom = jsonDecodeBillingKom(response.getString("perusahaan"));
                                    db_kategori = jsonDecodeAllKategori(response.getString("kategori2"));
                                    db_tanggal = jsonPertanggal(response.getString("pertanggal"));
                                }

                                if (viewPager != null) {
                                    Log.d("get_dataajs", "adadds");
                                    viewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
                                    setupViewPager(viewPager);
                                }
                                tabLayout.setupWithViewPager(viewPager);

                                if(dashajs){
                                    jsonDecodePersentaseKategori(response.getString("kategori"));
                                }else{
                                    createTabIcons(0.0,0.0,0.0);
                                }

                                avi.hide();
                            } else if (response.getString("status").equals("invalid-token")) {
                                GetToken k = new GetToken(MainActivity.this);
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
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(getRequest);
        queue.add(getRequest);
    }

    public ArrayList<DashboardKomisaris> jsonDecodeBillingKom(String jsonStr) {
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
                            new BigInteger(jObject.getString("total_rupiah")),
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

    public ArrayList<Dashboard> jsonDecodeBilling(String jsonStr) {
        ArrayList<Dashboard> billing = new ArrayList<>();

        if (jsonStr != null) {
            try {
                JSONArray transaksi = new JSONArray(jsonStr);
                for (int i = 0; i < transaksi.length(); i++) {
                    JSONObject jObject = transaksi.getJSONObject(i);
                    double komersial = -1;
                    double kualitas = -1;
                    double kapasitas = -1;
                    try {
                        komersial = jObject.getDouble("komersial_persen");
                    } catch (Exception e) {
                    }

                    try {
                        kualitas = jObject.getDouble("kualitas_persen");
                    } catch (Exception e) {
                    }

                    try {
                        kapasitas = jObject.getDouble("kapasitas_persen");
                    } catch (Exception e) {
                    }

                    Dashboard d = new Dashboard(
                            i,
                            jObject.getInt("id_perusahaan"),
                            jObject.getString("nama_perusahaan"),
                            komersial,
                            kualitas,
                            kapasitas,
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

    public void jsonDecodePersentaseKategori(String jsonStr) {
        Double kualitas = 0.0, kapasitas = 0.0, komersial = 0.0;

        if (jsonStr != null) {
            try {
                JSONArray transaksi = new JSONArray(jsonStr);
                for (int i = 0; i < transaksi.length(); i++) {
                    JSONObject jObject = transaksi.getJSONObject(i);
                    if (jObject.getString("nama_kategori").equals("kualitas")) {
                        kualitas = jObject.getDouble("realisasi_persen");
                    } else if (jObject.getString("nama_kategori").equals("kapasitas")) {
                        kapasitas = jObject.getDouble("realisasi_persen");
                    } else if (jObject.getString("nama_kategori").equals("komersial")) {
                        komersial = jObject.getDouble("realisasi_persen");
                    }
                }

                createTabIcons(kualitas, kapasitas, komersial);

            } catch (JSONException e) {
                e.printStackTrace();
            }
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

    private void getDashboardFilter(final String tanggal_awal, final String tanggal_akhir) {
        avi.show();
        String url="";
        if(dashajs){
            url=Config.URL_FILTER_1 + Prefs.getString(Config.TOKEN_BUMN, "");
        }else{
            url=Config.URL_FILTER_2 + Prefs.getString(Config.TOKEN_BUMN, "");
        }

        VolleyClass cek = new VolleyClass(this, true);
        cek.get_data_from_server(new VolleyClass.VolleyCallback() {
            @Override
            public void onSuccess(String s) {
                try {
                    JSONObject response = new JSONObject(s);
                    if (response.getString("status").equals("success")) {

                        if(dashajs){
                            db = jsonDecodeBilling(response.getString("perusahaan"));
                        }else{
                            dbkom = jsonDecodeBillingKom(response.getString("perusahaan"));
                            db_kategori = jsonDecodeAllKategori(response.getString("kategori2"));
                            db_tanggal = jsonPertanggal(response.getString("pertanggal"));
                        }

                        if (viewPager != null) {
                            Log.d("get_dataajs", "adadds");
                            viewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
                            setupViewPager(viewPager);
                        }
                        tabLayout.setupWithViewPager(viewPager);

                        if(dashajs){
                            jsonDecodePersentaseKategori(response.getString("kategori"));
                        }else{
                            createTabIcons(0.0,0.0,0.0);
                        }

                        avi.hide();
                    } else if (response.getString("status").equals("invalid-token")) {
                        GetToken k = new GetToken(MainActivity.this);
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
        }, url, new String[]{
                "tanggal_awal" + "|" + tanggal_awal,
                "tanggal_akhir" + "|" + tanggal_akhir
        });
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(dashajs){
            getMenuInflater().inflate(R.menu.menu_pak_ajs, menu);
        }else{
            getMenuInflater().inflate(R.menu.menu_bu_devy, menu);
        }
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