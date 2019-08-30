package com.mar.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.mar.R;
import com.mar.model.AppModel;

import java.util.ArrayList;

public class MonitorAdapter extends ArrayAdapter<AppModel> {
    private ArrayList<AppModel> mList;
    private Context mContext;

    public MonitorAdapter(Context context, ArrayList<AppModel> list) {
        super(context, R.layout.item_monitor_listview, list);
        this.mContext = context;
        this.mList = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        AppModel item = getItem(position);
        MonitorViewHolder mvh;
        final View monitorView;
        if (convertView == null) {
            mvh = new MonitorViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_monitor_listview, parent, false);
            mvh.daily_average_value_tv = convertView.findViewById(R.id.daily_usage_value);
            mvh.usage_level_value_tv = convertView.findViewById(R.id.usage_level_value);
            mvh.app_icon_iv = convertView.findViewById(R.id.app_icon_iv);
            mvh.circularProgress = convertView.findViewById(R.id.custom_progress_view);
            monitorView = convertView;
            convertView.setTag(mvh);
        } else {
            mvh = (MonitorViewHolder) convertView.getTag();
            monitorView = convertView;
        }

        assert item != null;
        // TODO: 26-04-2019 Adapt the appModel data to monitor listView item
        mvh.daily_average_value_tv.setText(item.getUsageCount());

        mvh.usage_level_value_tv.setText(item.getUsageLevel());

        mvh.app_icon_iv.setImageDrawable(item.getAppIcon());

        String progress = item.getUsageOfApp();

        if (progress.equals("Low")) {
            mvh.circularProgress.setProgress(item.getUsageInPercent());
            mvh.circularProgress.setTextColor(mContext.getResources().getColor(R.color.low_usage));
            mvh.circularProgress.setFinishedStrokeColor(mContext.getResources().getColor(R.color.low_usage));
        } else if (progress.equals("Medium")) {
            mvh.circularProgress.setProgress(item.getUsageInPercent());
            mvh.circularProgress.setTextColor(mContext.getResources().getColor(R.color.medium_usage));
            mvh.circularProgress.setFinishedStrokeColor(mContext.getResources().getColor(R.color.medium_usage));
        } else if (progress.equals("High")) {
            mvh.circularProgress.setProgress(item.getUsageInPercent());
            mvh.circularProgress.setTextColor(mContext.getResources().getColor(R.color.high_usage));
            mvh.circularProgress.setFinishedStrokeColor(mContext.getResources().getColor(R.color.high_usage));
        } else if (progress.equals("Extreme")) {
            mvh.circularProgress.setProgress(item.getUsageInPercent());
            mvh.circularProgress.setTextColor(mContext.getResources().getColor(R.color.extreme_usage));
            mvh.circularProgress.setFinishedStrokeColor(mContext.getResources().getColor(R.color.extreme_usage));
        }
        return convertView;
    }

    @Override
    public AppModel getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    private static class MonitorViewHolder {
        TextView daily_average_value_tv;
        TextView usage_level_value_tv;
        ImageView app_icon_iv;
        DonutProgress circularProgress;
    }
}
