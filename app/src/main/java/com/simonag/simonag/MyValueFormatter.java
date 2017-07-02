package com.simonag.simonag;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by diditsepiyanto on 7/1/17.
 */

public class MyValueFormatter implements IAxisValueFormatter {
    private DecimalFormat mFormat;

    public MyValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0"); // use one decimal
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return "" + ((int) value);
    }
}
