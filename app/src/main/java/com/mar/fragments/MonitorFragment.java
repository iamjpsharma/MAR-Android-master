package com.mar.fragments;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mar.MainSwipeableActivity;
import com.mar.R;
import com.mar.adapters.MonitorAdapter;
import com.mar.model.AppModel;
import com.mar.utils.AppUsageDataUtils;
import com.mar.utils.MultipleListUtil;

import java.util.ArrayList;
import java.util.List;

public class MonitorFragment extends Fragment {
    private static final String TAG = "MonitorFragment";
    private ListView monitorListView;
    private MonitorAdapter monitorAdapter;
    private ArrayList<AppModel> monitorData;
    private List<ApplicationInfo> restrictedAppFromSystem;
    private AppUsageDataUtils appUsageDataUtils;
    private Context mContext;

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: Monitor Fragment started");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onStart: Monitor Fragment Resumed");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View monitorFragmentView = inflater.inflate(R.layout.fragment_monitor, container, false);
        mContext = getContext();
        monitorListView = monitorFragmentView.findViewById(R.id.monitor_listview);
        prepareDataForMonitorListView();
        return monitorFragmentView;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onStart: Monitor Fragment Paused");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStart: Monitor Fragment Stopped");

    }

    private void prepareDataForMonitorListView() {
        monitorData = new ArrayList<AppModel>();
        restrictedAppFromSystem = new ArrayList<>();
       /* ArrayList<MonitorItem> dataSamples = new ArrayList<>();
        dataSamples.add(new MonitorItem("3.00 Hr", "High", "", null, 75));
        dataSamples.add(new MonitorItem("0.33 Hr", "Low", "", null, 8));
        dataSamples.add(new MonitorItem("2.30 Hr", "High", "", null, 57));
        dataSamples.add(new MonitorItem("0.40 Hr", "Low", "", null, 10));
        dataSamples.add(new MonitorItem("3.30 Hr", "Extreme", "", null, 82));
        dataSamples.add(new MonitorItem("2.15 Hr", "High", "", null, 53));
        dataSamples.add(new MonitorItem("3.13 Hr", "Extreme", "", null, 78));
        dataSamples.add(new MonitorItem("1.20 Hr", "Medium", "", null, 30));
        dataSamples.add(new MonitorItem("0.42 Hr", "Low", "", null, 10));
        dataSamples.add(new MonitorItem("3.30 Hr", "Extreme", "", null, 82));
        dataSamples.add(new MonitorItem("1.36 Hr", "Medium", "", null, 34));
        dataSamples.add(new MonitorItem("0.27 Hr", "Low", "", null, 6));
        dataSamples.add(new MonitorItem("3.34 Hr", "Extreme", "", null, 83));
        dataSamples.add(new MonitorItem("1.46 Hr", "Medium", "", null, 36));
        dataSamples.add(new MonitorItem("3.54 Hr", "Extreme", "", null, 88));
        dataSamples.add(new MonitorItem("2.33 Hr", "High", "", null, 58));
        for (int i = 0; i < restrictedAppFromSystem.size(); i++) {
            ApplicationInfo a = restrictedAppFromSystem.get(i);
            MonitorItem m = dataSamples.get(i);
            m.setAppName(appUsageDataUtils.getPackageManager().getApplicationLabel(a).toString());
            m.setAppIcon(a.loadIcon(appUsageDataUtils.getPackageManager()));
            monitorData.add(m);
            Log.i(TAG, "onCreateView: " + m.getLogForItem());
        }*/
        monitorData.addAll(MainSwipeableActivity.persistenceData);
        monitorAdapter = new MonitorAdapter(getContext(), monitorData);
        monitorListView.setAdapter(monitorAdapter);
        MultipleListUtil.setListViewHeightBasedOnChildren(monitorListView);
        monitorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppModel item = monitorData.get(position);
                Toast.makeText(getContext(), "" + item.getAppName(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
