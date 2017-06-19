package com.simonag.simonag;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pixplicity.easyprefs.library.Prefs;
import com.simonag.simonag.model.Kategori;
import com.simonag.simonag.utils.Config;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;

import static java.lang.String.format;

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
        int j = Prefs.getInt(Config.FILTER_KOMISARIS, 0);
        if (j == 0) {
            Log.d("kategori", kategoris.size()+"");
            for (Kategori k : kategoris) {
                if (k.getNama().equals("publikasi"))
                    mPieChart.addPieSlice(new PieModel(k.getNama() + ": "+ k.getTotal_aktifitas(), k.getTotal_aktifitas(), Color.parseColor("#FE6DA8")));
                if (k.getNama().equals("sponsorship"))
                    mPieChart.addPieSlice(new PieModel(k.getNama() + ": "+ k.getTotal_aktifitas(), k.getTotal_aktifitas(), Color.parseColor("#56B7F1")));
                if (k.getNama().equals("hospitality"))
                    mPieChart.addPieSlice(new PieModel(k.getNama() + ": "+ k.getTotal_aktifitas(), k.getTotal_aktifitas(), Color.parseColor("#CDA67F")));
            }
        }else {
            for (Kategori k : kategoris) {
                if (k.getNama().equals("publikasi"))
                    mPieChart.addPieSlice(new PieModel(k.getNama() + ": Rp. "+ format("%,d", k.getTotal_rupiah()).replace(",", "."), k.getTotal_rupiah(), Color.parseColor("#FE6DA8")));
                if (k.getNama().equals("sponsorship"))
                    mPieChart.addPieSlice(new PieModel(k.getNama() + ": Rp. "+ format("%,d", k.getTotal_rupiah()).replace(",", "."), k.getTotal_rupiah(), Color.parseColor("#56B7F1")));
                if (k.getNama().equals("hospitality"))
                    mPieChart.addPieSlice(new PieModel(k.getNama() + ": Rp. "+ format("%,d", k.getTotal_rupiah()).replace(",", "."), k.getTotal_rupiah(), Color.parseColor("#CDA67F")));
            }
        }


        mPieChart.startAnimation();

        return v;
    }
}
