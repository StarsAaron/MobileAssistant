package com.rujian.mobileassistant.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rujian.mobileassistant.R;
import com.rujian.mobileassistant.bean.AppInfo;
import com.rujian.mobileassistant.db.AppLockDao;

import java.util.List;

/**
 * Created by lanlan on 2015/6/22.
 */
public class AppLockAdapter extends BaseAdapter {
    private Context context;
    private List<AppInfo> unlockAppInfos;//没加锁程序
    private List<AppInfo> lockedAppInfos;//加锁程序
    private boolean unlockflag;
    private AppLockDao dao;

    public AppLockAdapter(Context context, List<AppInfo> unlockAppInfos, List<AppInfo> lockedAppInfos) {
        this.context = context;
        this.unlockAppInfos = unlockAppInfos;
        this.lockedAppInfos = lockedAppInfos;
        dao = new AppLockDao(context);

    }

    @Override
    public int getCount() {
        return unlockAppInfos.size() + 1 + lockedAppInfos.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        AppInfo appInfo;
        if(position == 0){
            return null;
        }else if(position == (lockedAppInfos.size() + 1)){
            return null;
        }else if(position <= lockedAppInfos.size()){
            int newposition =position - 1;
            appInfo = lockedAppInfos.get(newposition);
        }else {
            int newposition = position - 1 -lockedAppInfos.size()-1;
            appInfo = unlockAppInfos.get(newposition);
        }


        return appInfo;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final AppInfo appInfo ;
        if (position == 0){
            TextView tv =new TextView(context);
            tv.setText("加锁软件");
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(18);
            tv.setBackgroundColor(Color.GRAY);
            return tv;
        }else if(position == lockedAppInfos.size() + 1){
            TextView tv = new TextView(context);
            tv.setText("没加锁软件");
            tv.setTextColor(Color.WHITE);
            tv.setTextSize(18);
            tv.setBackgroundColor(Color.GRAY);
            return tv;
        }else if(position <= lockedAppInfos.size()){
            int newposition = position - 1;
            appInfo = lockedAppInfos.get(newposition);
            unlockflag = false;

        }else {
            int newposition = position - 1 - lockedAppInfos.size() - 1;
            appInfo = unlockAppInfos.get(newposition);
            unlockflag = true;

        }

        final View view;
        ViewHolder holder;
        if (convertView != null && convertView instanceof RelativeLayout) {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        } else {
            view =View.inflate(context, R.layout.list_applock_item,null);
            holder = new ViewHolder();
            holder.iv_lock_app =(ImageView)view.findViewById(R.id.iv_lock_app);
            holder.tv_applock_name = (TextView)view.findViewById(R.id.tv_applock_name);
            holder.tv_applock_version = (TextView)view.findViewById(R.id.tv_applock_version);
            holder.iv_iflock = (ImageView)view.findViewById(R.id.iv_iflock);
            holder.tv_orlock = (TextView)view.findViewById(R.id.tv_orlock);
            view.setTag(holder);
        }
        if (unlockflag) {
            holder.iv_iflock.setBackgroundResource(R.mipmap.lock);
            holder.tv_orlock.setText("加锁");

        }else {
            holder.iv_iflock.setBackgroundResource(R.mipmap.unlock);
            holder.tv_orlock.setText("解锁");

        }
        holder.iv_lock_app.setImageDrawable(appInfo.getIcon());
        holder.tv_applock_name.setText(appInfo.getName());
        holder.tv_applock_version.setText(appInfo.getVersionName());
        holder.iv_iflock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dao.find(appInfo.getPackname()) ==false){
                    TranslateAnimation ta = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0,
                            Animation.RELATIVE_TO_SELF, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0,
                            Animation.RELATIVE_TO_SELF, 0);
                    ta.setDuration(500);
                    view.startAnimation(ta);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //会在主线程里面执行。
                            unlockAppInfos.remove(position-lockedAppInfos.size()-2);
                            dao.add(appInfo.getPackname());
                            lockedAppInfos.add(appInfo);
                            AppLockAdapter.this.notifyDataSetChanged();
                        }
                    }, 500);

                }else {
                    TranslateAnimation ta = new TranslateAnimation(
                            Animation.RELATIVE_TO_SELF, 0,
                            Animation.RELATIVE_TO_SELF, -1.0f,
                            Animation.RELATIVE_TO_SELF, 0,
                            Animation.RELATIVE_TO_SELF, 0);
                    ta.setDuration(500);
                    view.startAnimation(ta);
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            lockedAppInfos.remove(position-1);
                            dao.delete(appInfo.getPackname());
                            unlockAppInfos.add(appInfo);
                            AppLockAdapter.this.notifyDataSetChanged();
                        }

                    }, 500);

                }
            }
        });
        return view;
    }
    static class ViewHolder {

        ImageView iv_lock_app;
        TextView tv_applock_name;
        TextView tv_applock_version;
        ImageView iv_iflock;
        TextView tv_orlock;

    }
}
