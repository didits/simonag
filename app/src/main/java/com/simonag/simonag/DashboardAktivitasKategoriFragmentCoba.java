package com.simonag.simonag;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.pixplicity.easyprefs.library.Prefs;
import com.simonag.simonag.model.Kategori;
import com.simonag.simonag.utils.Config;

import java.util.ArrayList;

import static java.lang.String.format;

/**
 * Created by diditsepiyanto on 6/16/17.
 */

public class DashboardAktivitasKategoriFragmentCoba extends Fragment {
    private com.github.mikephil.charting.charts.PieChart mChart;
    TextView publikasi, sponsorship, hospitality;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.activity_piechart, container, false);
        publikasi = (TextView) v.findViewById(R.id.publikasi);
        sponsorship = (TextView) v.findViewById(R.id.sponsorship);
        hospitality = (TextView) v.findViewById(R.id.hospitality);

        mChart = (com.github.mikephil.charting.charts.PieChart) v.findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);


        mChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener

        setData(4, 100);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);

        return v;
    }


    private void setData(int count, float range) {

        ArrayList<Kategori> kategoris = ((MainActivity) getActivity()).db_kategori;
        int j = Prefs.getInt(Config.FILTER_KOMISARIS, 0);

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

        if (j == 0) {
            for (Kategori k : kategoris) {
                if (k.getNama().equals("publikasi")) {
                    entries.add(new PieEntry(k.getTotal_aktifitas(), "Publikasi"));
                    publikasi.setText(": " + k.getTotal_aktifitas() + "");
                }
                if (k.getNama().equals("sponsorship")) {
                    entries.add(new PieEntry(k.getTotal_aktifitas(), "Sponsorship"));
                    sponsorship.setText(": " +k.getTotal_aktifitas() + "");
                }
                if (k.getNama().equals("hospitality")) {
                    entries.add(new PieEntry(k.getTotal_aktifitas(), "Hospitality"));
                    hospitality.setText(": " +k.getTotal_aktifitas() + "");
                }
            }
        } else if (j == 1) {
            for (Kategori k : kategoris) {
                if (k.getNama().equals("publikasi")) {
                    entries.add(new PieEntry(k.getTotal_rupiah(), "Publikasi"));
                    publikasi.setText(": Rp. " + format("%,d", k.getTotal_rupiah()).replace(",", "."));
                }
                if (k.getNama().equals("sponsorship")) {
                    entries.add(new PieEntry(k.getTotal_rupiah(), "Sponsorship"));
                    sponsorship.setText(": Rp. " + format("%,d", k.getTotal_rupiah()).replace(",", "."));
                }
                if (k.getNama().equals("hospitality")) {
                    entries.add(new PieEntry(k.getTotal_rupiah(), "Hospitality"));
                    hospitality.setText(": Rp. " + format("%,d", k.getTotal_rupiah()).replace(",", "."));
                }
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);


        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.BLACK);
        //dataSet.setSelectionShift(0f);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

}
