package com.simonag.simonag;

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
import android.view.View;

import com.ToxicBakery.viewpager.transforms.ZoomOutSlideTransformer;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//import com.mikepenz.fontawesome_typeface_library.FontAwesome;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    public AccountHeader headerResult;
    public Drawer result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);



        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            viewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.logo_text_bg)
                .addProfiles(
                        new ProfileDrawerItem().withName(Prefs.getString(Config.NAMA_BUMN,"")).withEmail(Prefs.getString(Config.EMAIL_BUMN,"")).withIcon(FontAwesome.Icon.faw_user_secret)
                )
                .withSelectionListEnabledForSingleProfile(false)
                .build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(1).withName("Dashboard").withIcon(FontAwesome.Icon.faw_bar_chart),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withIdentifier(2).withName("Semua").withIcon(FontAwesome.Icon.faw_users),
                        new PrimaryDrawerItem().withIdentifier(3).withName("BUMN").withIcon(FontAwesome.Icon.faw_user_secret),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withIdentifier(4).withName("Tentang").withIcon(FontAwesome.Icon.faw_info),
                        new PrimaryDrawerItem().withIdentifier(5).withName("Keluar").withIcon(FontAwesome.Icon.faw_sign_out)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch((int) drawerItem.getIdentifier()) {
                            case 1:
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            case 4:
                                startActivity(new Intent(MainActivity.this, TentangActivity.class));
                                break;
                            case 5:
                                out();
                                break;
                        }
                        return false;
                    }
                })
                .build();

        //result.setSelection(1, true);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new DashboardFragment(), "Kualitas");
        adapter.addFragment(new DashboardFragment(), "Kapasitas");
        adapter.addFragment(new DashboardFragment(), "Komersial");
        viewPager.setAdapter(adapter);
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
}
