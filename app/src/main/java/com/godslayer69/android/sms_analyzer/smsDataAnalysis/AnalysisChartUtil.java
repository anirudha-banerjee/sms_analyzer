package com.godslayer69.android.sms_analyzer.smsDataAnalysis;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;

/**
 * Created by root on 5/18/17.
 * Project: ELE_SMS_ANIRUDHA
 */

class AnalysisChartUtil implements OnChartValueSelectedListener {

    private static final String TAG = "AnalysisChartUtil";

    private Context context;
    private PieChart dataAnalysisChart;
    private long rowCount, totalRowCount;
    private int count = 0;
    private float range = 0;

    private Typeface tf;

    AnalysisChartUtil(Context context, long rowCount, long totalRowCount, PieChart analysisChart){

        this.context = context;
        this.rowCount = rowCount;
        this.totalRowCount = totalRowCount;
        dataAnalysisChart = analysisChart;

        count = (int) rowCount;
        range = (float) totalRowCount;

        tf = Typeface.createFromAsset(context.getAssets(), "fonts/" + "Roboto-Medium.ttf");

        initializeChart();

    }

    private void initializeChart(){

        dataAnalysisChart.setUsePercentValues(true);
        dataAnalysisChart.getDescription().setEnabled(false);
        dataAnalysisChart.setExtraOffsets(5, 10, 5, 5);

        dataAnalysisChart.setDragDecelerationFrictionCoef(0.95f);

        dataAnalysisChart.setCenterTextTypeface(tf);
        dataAnalysisChart.setCenterText("ELE_SMS_ANIRUDHA");

        dataAnalysisChart.setDrawHoleEnabled(true);
        dataAnalysisChart.setHoleColor(Color.WHITE);

        dataAnalysisChart.setTransparentCircleColor(Color.WHITE);
        dataAnalysisChart.setTransparentCircleAlpha(110);

        dataAnalysisChart.setHoleRadius(58f);
        dataAnalysisChart.setTransparentCircleRadius(61f);

        dataAnalysisChart.setDrawCenterText(true);

        dataAnalysisChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        dataAnalysisChart.setRotationEnabled(true);
        dataAnalysisChart.setHighlightPerTapEnabled(true);

        // dataAnalysisChart.setUnit(" €");
        // dataAnalysisChart.setDrawUnitsInChart(true);

        // add a selection listener
        dataAnalysisChart.setOnChartValueSelectedListener(this);

        setData(count, range);

        dataAnalysisChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // dataAnalysisChart.spin(2000, 0, 360);

        Legend l = dataAnalysisChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        dataAnalysisChart.setEntryLabelColor(Color.WHITE);
        dataAnalysisChart.setEntryLabelTypeface(tf);
        dataAnalysisChart.setEntryLabelTextSize(12f);

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;
        Log.d(TAG,
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());

    }

    @Override
    public void onNothingSelected() {

    }

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count ; i++) {
            entries.add(new PieEntry((float) ((Math.random() * mult) + mult / 5), mult));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Message Analysis Report");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(tf);
        dataAnalysisChart.setData(data);

        // undo all highlights
        dataAnalysisChart.highlightValues(null);

        dataAnalysisChart.invalidate();
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("ELE_SMS_ANIRUDHA");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

}
