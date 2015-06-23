package com.rujian.mobileassistant;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.rujian.mobileassistant.adapter.AppLockAdapter;
import com.rujian.mobileassistant.bean.AppInfo;
import com.rujian.mobileassistant.db.AppLockDao;
import com.rujian.mobileassistant.engine.AppInfoProvider;

import java.util.ArrayList;
import java.util.List;

public class AppLockActivity extends BaseHeadActivity {

    private Button btlock;//程序锁开关
    private TextView appitem;//
    private ListView applocklist;

    private AppLockDao dao;

    private List<AppInfo> appInfos;//所有程序
    private List<AppInfo> unlockAppInfos;//没加锁程序
    private List<AppInfo> lockedAppInfos;//加锁程序

    private AppLockAdapter appLockAdapter;
    private boolean btonclick =true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock);
        dao = new AppLockDao(this);
        //注册组件
        assignView();
        //初始化组件
        initView();

        btlock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btonclick == true){
                    view.setBackgroundResource(R.mipmap.phone_public_toggle_on);
                    btonclick = false;
                }else{
                    view.setBackgroundResource(R.mipmap.phone_public_toggle_off);
                    btonclick = true;
                }
            }
        });
        appLockAdapter = new AppLockAdapter(AppLockActivity.this, unlockAppInfos, lockedAppInfos);
        applocklist.setAdapter(appLockAdapter);

        applocklist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView View, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(unlockAppInfos !=null && lockedAppInfos !=null){
                    if(firstVisibleItem <lockedAppInfos.size()){
                        appitem.setText("加锁软件");
                    }else {
                        appitem.setText("没加锁软件");
                    }
                }

            }
        });

    }
    private void assignView() {
        applocklist = (ListView)findViewById(R.id.lv_applock);
        appitem =(TextView)findViewById(R.id.tv_lockapp_item);
        btlock = (Button)findViewById(R.id.bt_lock);


    }

    private void initView(){
        setTitle(getResources().getString(R.string.app_lock));
        // 获取所有的应用程序信息的集合。//下面的逻辑最好放在子线程。
        setBackButtonOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        appInfos = AppInfoProvider.getAppInfos(this);
        // 过滤所有应用程序的集合 把未加锁的和已加锁的appinfo给区分出来。
        unlockAppInfos = new ArrayList<AppInfo>();
        lockedAppInfos = new ArrayList<AppInfo>();
        for (AppInfo appInfo : appInfos) {
            if (dao.find(appInfo.getPackname())) {
                lockedAppInfos.add(appInfo);
            } else {
                unlockAppInfos.add(appInfo);
            }
        }






    }

}
