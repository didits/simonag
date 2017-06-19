package com.simonag.simonag;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixplicity.easyprefs.library.Prefs;
import com.simonag.simonag.model.Kategori;
import com.simonag.simonag.utils.Config;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;

/**
 * Created by diditsepiyanto on 6/16/17.
 */

public class DashboardAktivitasKategoriFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.aktifitas_per_kategori, container, false);
        PieChart mPieChart = (PieChart) v.findViewById(R.id.piechart);

        ArrayList<Kategori> kategoris = ((MainActivityKomisaris) getActivity()).db_kategori;
        int j = Prefs.getInt(Config.URL_FILTER_2, 0);
        if (j == 0) {
            for (Kategori k : kategoris) {
                if (k.getNama().equals("publikasi"))
                    mPieChart.addPieSlice(new PieModel(k.getNama(), k.getTotal_aktifitas(), Color.parseColor("#FE6DA8")));
                if (k.getNama().equals("sponsorship"))
                    mPieChart.addPieSlice(new PieModel(k.getNama(), k.getTotal_aktifitas(), Color.parseColor("#FE6DA8")));
                if (k.getNama().equals("hospitality"))
                    mPieChart.addPieSlice(new PieModel(k.getNama(), k.getTotal_aktifitas(), Color.parseColor("#FE6DA8")));
            }
        }else {
            for (Kategori k : kategoris) {
                if (k.getNama().equals("publikasi"))
                    mPieChart.addPieSlice(new PieModel(k.getNama(), k.getTotal_rupiah(), Color.parseColor("#FE6DA8")));
                if (k.getNama().equals("sponsorship"))
                    mPieChart.addPieSlice(new PieModel(k.getNama(), k.getTotal_rupiah(), Color.parseColor("#FE6DA8")));
                if (k.getNama().equals("hospitality"))
                    mPieChart.addPieSlice(new PieModel(k.getNama(), k.getTotal_rupiah(), Color.parseColor("#FE6DA8")));
            }
        }


        mPieChart.startAnimation();

        return v;
    }
}
