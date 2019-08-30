package com.mar.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.mar.MainSwipeableActivity;
import com.mar.R;
import com.mar.adapters.AnalysisAdapter;
import com.mar.model.AppModel;
import com.mar.utils.AppUsageDataUtils;
import com.mar.utils.MultipleListUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AnalysisFragment extends Fragment {
    private static final String TAG = "AnalysisFragment";
    private AppUsageDataUtils appUsageDataUtils;
    protected Spinner timeSpinner;
    protected BarChart barChart;
    protected ListView safeUsageListView;
    protected ListView unsafeUsageListView;
    public static ArrayList<AppModel> safeApps;
    public static ArrayList<AppModel> unsafeApps;
    private Context mContext;
    List<AppModel> appsInfoFromSystem;
    private List<AppModel> data;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_analysis, container, false);
        mContext = getContext();
        assert mContext != null;
        appUsageDataUtils = new AppUsageDataUtils(mContext);
        //add view element initialization code in this block
        initView(fragmentView);
        prepareTimeRangeSpinner();
        prepareBarChartData();
        appsInfoFromSystem = MainSwipeableActivity.persistenceData;
        prepareSafeUsageAppsListData();
        prepareUnsafeUsageAppsListData();

        return fragmentView;
    }

    private void initView(View rootView) {
        timeSpinner = rootView.findViewById(R.id.time_span_selector_spinner);
        barChart = rootView.findViewById(R.id.app_usage_bar_chart);
        safeUsageListView = rootView.findViewById(R.id.safe_usage_listview);
        unsafeUsageListView = rootView.findViewById(R.id.unsafe_usage_listview);
    }

    public void prepareTimeRangeSpinner() {
        String[] timeSpanOptions = getResources().getStringArray(R.array.time_span_range);
        ArrayAdapter<String> timeSpanAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, timeSpanOptions);
        timeSpanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeSpanAdapter);
        Log.i(TAG, "prepareTimeRangeSpinner: Spinner setup is good to use !");
    }

    public void prepareBarChartData() {
        List<BarEntry> NoOfApp = new ArrayList<BarEntry>();

        data = MainSwipeableActivity.persistenceData;
        for (int i = 0; i < data.size(); i++) {
            AppModel am = data.get(i);
            /*Drawable ab = am.getAppIcon();
            Bitmap bitmapDrawable = ((BitmapDrawable) ab).getBitmap();
            Bitmap b = Bitmap.createScaledBitmap(bitmapDrawable, 56, 56, false);
            ab = new BitmapDrawable(getResources(), b);*/
            NoOfApp.add(new BarEntry(i + 1, am.getUsageInPercent()));
        }

        BarDataSet barDataSet = new BarDataSet(NoOfApp, "App Usage");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextSize(16);
        barDataSet.setDrawValues(false);
        barDataSet.setHighlightEnabled(false);
        barDataSet.setDrawIcons(false);


        BarData barData = new BarData(barDataSet);
//        barData.setBarWidth(5);
        barData.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                DecimalFormat decimalFormat = new DecimalFormat();
                return decimalFormat.format(value) + " %";
            }
        });

        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
//        barChart.setMaxVisibleValueCount(14);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.setFitBars(true);
        barChart.setPinchZoom(false);
        barChart.setScaleEnabled(false);

        XAxis x = barChart.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis yl = barChart.getAxisLeft();
        yl.setDrawAxisLine(false);
        yl.setDrawLabels(false);
//        yl.setLabelCount(10);

        YAxis yr = barChart.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setDrawLabels(true);
        yr.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                DecimalFormat decimalFormat = new DecimalFormat();
                return decimalFormat.format(value) + " %";
            }
        });
//        yr.setLabelCount(10);

        Legend l = barChart.getLegend();
        l.setEnabled(false);

        Log.i(TAG, "prepareBarChartData: BarChart DataSet Good To Go !!!");
    }

    public void prepareSafeUsageAppsListData() {
        safeApps = new ArrayList<AppModel>();
        for (AppModel am : appsInfoFromSystem) {
            if (am.getUsageOfApp().equals("Low") || am.getUsageOfApp().equals("Medium")) {
                safeApps.add(am);
            }
        }
        AnalysisAdapter safeAdapter = new AnalysisAdapter(mContext, safeApps, AnalysisAdapter.AnalysisListType.SAFE);
        safeUsageListView.setAdapter(safeAdapter);
        MultipleListUtil.setListViewHeightBasedOnChildren(safeUsageListView);
        Log.i(TAG, "prepareSafeUsageAppsListData: Safe Apps Data set is Generated");
    }

    public void prepareUnsafeUsageAppsListData() {
        unsafeApps = new ArrayList<AppModel>();
        for (AppModel am : appsInfoFromSystem) {
            if (am.getUsageOfApp().equals("High") || am.getUsageOfApp().equals("Extreme")) {
                unsafeApps.add(am);
            }
        }
        AnalysisAdapter unsafeAdapter = new AnalysisAdapter(mContext, unsafeApps, AnalysisAdapter.AnalysisListType.UNSAFE);
        unsafeUsageListView.setAdapter(unsafeAdapter);
        MultipleListUtil.setListViewHeightBasedOnChildren(unsafeUsageListView);
        Log.i(TAG, "prepareSafeUsageAppsListData: Unsafe Apps Data set is Generated");

    }

}