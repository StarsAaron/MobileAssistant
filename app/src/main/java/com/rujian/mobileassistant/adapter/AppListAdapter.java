package com.rujian.mobileassistant.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rujian.mobileassistant.R;
import com.rujian.mobileassistant.domain.AppInfo;

import java.util.List;

/**
 * Created by stars on 2015/6/15.
 */
public class AppListAdapter extends BaseAdapter {
    private List<AppInfo> userAppInfos;
    private List<AppInfo> systemAppInfos;
    private Context context;
    private int visiablePosition;

    public AppListAdapter(Context context,List<AppInfo> l1,List<AppInfo> l2) {
        this.context = context;
        this.userAppInfos = l1;
        this.systemAppInfos = l2;
    }

    @Override
    public int getCount() {
        //+1因为多了TextView
        return userAppInfos.size() + 1 + systemAppInfos.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        AppInfo appInfo;
        if (position == 0) {// 显示一个textview告诉用户有多少个用户应用
            return null;
        } else if (position == (userAppInfos.size() + 1)) {
            return null;
        } else if (position <= userAppInfos.size()) {
            // 用户程序。
            int newposition = position - 1;
            appInfo = userAppInfos.get(newposition);
        } else {
            // 系统程序
            int newposition = position - 1 - userAppInfos.size() - 1;
            appInfo = systemAppInfos.get(newposition);
        }
        return appInfo;
    }

    @Override
    public long getItemId(final int position) {
        visiablePosition = position;
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        AppInfo appInfo;

        if (position == 0) {// 显示一个textview告诉用户有多少个用户应用
            TextView tv = new TextView(context);
            tv.setText("用户程序  (" + userAppInfos.size() + ")");
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(18);
            tv.setBackgroundColor(Color.GRAY);
            return tv;
        } else if (position == (userAppInfos.size() + 1)) {
            TextView tv = new TextView(context);
            tv.setText("系统程序  (" + systemAppInfos.size() + ")");
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(18);
            tv.setBackgroundColor(Color.GRAY);
            return tv;
        } else if (position <= userAppInfos.size()) {
            // 用户程序。
            int newposition = position - 1;
            appInfo = userAppInfos.get(newposition);
        } else {
            // 系统程序
            int newposition = position - 1 - userAppInfos.size() - 1;
            appInfo = systemAppInfos.get(newposition);
        }
        View view;
        ViewHolder holder;
        if (convertView != null && convertView instanceof RelativeLayout) {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        } else {
            view = View.inflate(context,R.layout.list_app_item, null);
            holder = new ViewHolder();
            holder.iv = (ImageView) view.findViewById(R.id.iv_icon);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder.tv_location = (TextView) view.findViewById(R.id.tv_location);

            holder.tv_uninstall = (TextView) view.findViewById(R.id.tv_uninstall);
            holder.tv_open = (TextView) view.findViewById(R.id.tv_open);
            holder.tv_share = (TextView) view.findViewById(R.id.tv_share);

            view.setTag(holder);
        }
        //赋值
        holder.iv.setImageDrawable(appInfo.getIcon());
        holder.tv_name.setText(appInfo.getName());
        if (appInfo.isInRom()) {
            holder.tv_location.setText("手机内存");
        } else {
            holder.tv_location.setText("外部存储卡");
        }
        return view;
    }

    static class ViewHolder {
        ImageView iv;
        TextView tv_name;
        TextView tv_location;
        //隐藏的Bar
        TextView tv_uninstall;
        TextView tv_open;
        TextView tv_share;
    }
}
