package com.simonag.simonag;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
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

import java.math.BigInteger;
import java.util.ArrayList;

import butterknife.BindView;

import static java.lang.String.format;

/**
 * Created by diditsepiyanto on 6/16/17.
 */

public class DashboardAktivitasKategoriFragmentCoba extends Fragment {
    private com.github.mikephil.charting.charts.PieChart mChart;
    TextView cash, inkind, cash_text, in_kind_text;
    LinearLayout cash_kanan, cash_kiri, in_kind_kanan, in_kind_kiri;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_piechart, container, false);
        cash = (TextView) v.findViewById(R.id.cash);
        inkind = (TextView) v.findViewById(R.id.inkind);

        cash_kanan = (LinearLayout) v.findViewById(R.id.cash_kanan);
        cash_kiri = (LinearLayout) v.findViewById(R.id.cash_kiri);
        in_kind_kiri = (LinearLayout) v.findViewById(R.id.in_kind_kiri);
        in_kind_kanan = (LinearLayout) v.findViewById(R.id.in_kind_kanan);

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


        ArrayList<Kategori> kategoris = ((MainActivityKomisaris) getActivity()).db_kategori;
        int j = Prefs.getInt(Config.FILTER_KOMISARIS, 0);
        double total = 0;
        if (j == 0) {
            for (Kategori k : kategoris) {
                total = total+k.getTotal_aktifitas();
            }
            mChart.setCenterText(generateCenterSpannableText(total+""));
        } else if (j == 1) {
            for (Kategori k : kategoris) {
                total = total+k.getTotal_rupiah().doubleValue();
            }
            mChart.setCenterText(generateCenterSpannableText("Rp. " + format("%,d", total).replace(",", ".")));
        }

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

        ArrayList<Kategori> kategoris = ((MainActivityKomisaris) getActivity()).db_kategori;
        int j = Prefs.getInt(Config.FILTER_KOMISARIS, 0);

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

        float total = 0;
        if (j == 0) {
            for (Kategori k : kategoris) {
                total = total+k.getTotal_aktifitas();
            }
        } else if (j == 1) {
            for (Kategori k : kategoris) {
                total = total+k.getTotal_rupiah().floatValue();
            }
        }

        if (j == 0) {
            for (Kategori k : kategoris) {
                if (k.getNama().equals("cash")) {
                    entries.add(new PieEntry(k.getTotal_aktifitas(), "Cash"));
                    cash.setText(": " + k.getTotal_aktifitas() + "");
                    float hasil = (k.getTotal_aktifitas()*1.f)/total;
                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50,1-hasil);
                    LinearLayout.LayoutParams q = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50,hasil);
                    cash_kanan.setLayoutParams(p);
                    cash_kiri.setLayoutParams(q);
                }
                if (k.getNama().equals("in kind")) {
                    entries.add(new PieEntry(k.getTotal_aktifitas(), "In Kind"));
                    inkind.setText(": " +k.getTotal_aktifitas() + "");
                    float hasil = (k.getTotal_aktifitas()*1.f)/total;
                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50,1-hasil);
                    LinearLayout.LayoutParams q = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50,hasil);
                    in_kind_kanan.setLayoutParams(p);
                    in_kind_kiri.setLayoutParams(q);
                }
            }
        } else if (j == 1) {
            for (Kategori k : kategoris) {
                if (k.getNama().equals("cash")) {
                    entries.add(new PieEntry(k.getTotal_rupiah().floatValue(), "Cash"));
                    cash.setText(": Rp. " + format("%,d", k.getTotal_rupiah()).replace(",", "."));
                    float hasil = (k.getTotal_rupiah().floatValue()*1.f)/total;
                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50,1-hasil);
                    LinearLayout.LayoutParams q = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50,hasil);
                    cash_kanan.setLayoutParams(p);
                    cash_kiri.setLayoutParams(q);
                }
                if (k.getNama().equals("in kind")) {
                    entries.add(new PieEntry(k.getTotal_rupiah().floatValue(), "In Kind"));
                    inkind.setText(": Rp. " + format("%,d", k.getTotal_rupiah()).replace(",", "."));
                    float hasil = (k.getTotal_rupiah().floatValue()*1.f)/total;
                    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50,1-hasil);
                    LinearLayout.LayoutParams q = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50,hasil);
                    in_kind_kanan.setLayoutParams(p);
                    in_kind_kiri.setLayoutParams(q);
                }
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        //for (int c : ColorTemplate.COLORFUL_COLORS)
         //   colors.add(c);


        //colors.add(ColorTemplate.getHoloBlue());
        colors.add(Color.rgb(255,211,2));
        colors.add(Color.rgb(103,183,221));

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

    private SpannableString generateCenterSpannableText(String text) {
        SpannableString s = new SpannableString(text);
        s.setSpan(new RelativeSizeSpan(2f), s.length(), s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), s.length(), s.length(), 0);
        return s;
    }
}
