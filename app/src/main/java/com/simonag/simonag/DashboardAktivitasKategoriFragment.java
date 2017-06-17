package com.simonag.simonag;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.simonag.simonag.model.Kategori;

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

        ArrayList<Kategori> kategoris = ((MainActivityBuDevy) getActivity()).db_kategori;
        int j = 0;
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
