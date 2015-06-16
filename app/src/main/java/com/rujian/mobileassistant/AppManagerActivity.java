package com.rujian.mobileassistant;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rujian.mobileassistant.adapter.AppListAdapter;
import com.rujian.mobileassistant.domain.AppInfo;
import com.rujian.mobileassistant.engine.AppInfoProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stars on 2015/6/14.
 */
public class AppManagerActivity extends Activity {
    private ListView appList;
    private TextView userBar;
    private LinearLayout loading;
    private List<AppInfo> AppList;//所有程序
    private List<AppInfo> userAppList;//用户程序
    private List<AppInfo> sysAppList;//系统程序
    private AppInfoProvider appInfoProvider;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            loading.setVisibility(View.GONE);
            appList.setAdapter(new AppListAdapter(AppManagerActivity.this,userAppList,sysAppList));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appmanager);
        //注册组件
        regist();
        //初始化组件
        init();
    }

    /**
     * @regis
     * @descript
     */
    private void regist() {
        appList = (ListView)findViewById(R.id.lv_appman);
        userBar = (TextView)findViewById(R.id.tv_appman_userbar);
        loading = (LinearLayout)findViewById(R.id.ll_loading);
    }

    /**
     * @init
     * @descript
     */
    private void init() {
        //获取手机应用的信息，并保存在List
        loadAppDataFromSys();
    }

    /**
     * @loadAppDataFromSys
     * @descript 获取手机应用的信息，并保存在List
     */
    private void loadAppDataFromSys(){
        loading.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                AppList = AppInfoProvider.getAppInfos(getApplicationContext());
                userAppList = new ArrayList<AppInfo>();
                sysAppList = new ArrayList<AppInfo>();
                for(AppInfo appInfo:AppList){
                    if(appInfo.isUserApp()){
                        userAppList.add(appInfo);
                    }else{
                        sysAppList.add(appInfo);
                    }
                }

                handler.sendEmptyMessage(0);//刷新列表
            }
        }.start();
    }
}
