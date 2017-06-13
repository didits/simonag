package com.simonag.simonag;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

import butterknife.BindView;
import butterknife.ButterKnife;

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
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.logo_text_bg)
                .addProfiles(
                        new ProfileDrawerItem().withName("Andi").withEmail("andi@gmail.com").withIcon(ContextCompat.getDrawable(this, R.drawable.p1))
                )
                .withSelectionListEnabledForSingleProfile(false)
                .build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(1).withName("Dashboard").withIcon(FontAwesome.Icon.faw_bar_chart),
                        new PrimaryDrawerItem().withIdentifier(2).withName("Semua").withIcon(FontAwesome.Icon.faw_users),
                        new PrimaryDrawerItem().withIdentifier(3).withName("BUMN").withIcon(FontAwesome.Icon.faw_user_secret),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withIdentifier(4).withName("Tentang").withIcon(FontAwesome.Icon.faw_info),
                        new PrimaryDrawerItem().withIdentifier(5).withName("Keluar").withIcon(FontAwesome.Icon.faw_sign_out)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Fragment fragment = null;
                        Class fragmentClass = null;
                        int flag=0;
                        switch((int) drawerItem.getIdentifier()) {
                            case 1:
                                flag=1;
                                fragmentClass = DashboardFragment.class;
                                break;
                            case 2:
                                startActivity(new Intent(MainActivity.this, DataProgramActivity.class));
                                break;
                            case 3:
                                break;
                            case 4:
                                startActivity(new Intent(MainActivity.this, TentangActivity.class));
                                break;
                            case 5:
                                out();
                                break;
                            default:
                                fragmentClass = DashboardFragment.class;
                        }
                        if(flag==1){
                            try {
                                fragment = (Fragment) fragmentClass.newInstance();
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        return false;
                    }
                })
                .build();
        result.setSelection(1, true);
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
}
