package com.rujian.mobileassistant;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rujian.mobileassistant.adapter.AppListAdapter;
import com.rujian.mobileassistant.domain.AppInfo;
import com.rujian.mobileassistant.engine.AppInfoProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stars on 2015/6/14.
 */
public class AppManagerActivity extends Activity{
    private ListView appList;
//    private ExpandableListView appList;
    private TextView userBar;//灰色用户程序数量Bar
    private TextView memorySpace,sdCardSpace;//内存可用与SD卡可用文字
    private LinearLayout loading;
    private List<AppInfo> appListInfo;//所有程序
    private List<AppInfo> userAppListInfo;//用户程序
    private List<AppInfo> sysAppListInfo;//系统程序
    private AppInfo appInfo;//点击的应用信息
    private AppListAdapter appListAdapter;
    private static int recordxpand;//记录展开的位置

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            loading.setVisibility(View.GONE);
            userBar.setText("用户程序(" + userAppListInfo.size() + ")");
            appListAdapter = new AppListAdapter(AppManagerActivity.this,userAppListInfo,sysAppListInfo);
            appList.setAdapter(appListAdapter);
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
        memorySpace = (TextView)findViewById(R.id.tv_memory_available_space);
        sdCardSpace = (TextView)findViewById(R.id.tv_SD_available_space);
    }

    /**
     * @init
     * @descript
     */
    private void init() {
        //显示存储空间大小
        showAvailableSpace();
        //获取手机应用的信息，并保存在List
        loadAppDataFromSys();

        appList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (userAppListInfo != null && sysAppListInfo != null) {
                    if (firstVisibleItem > userAppListInfo.size()) {
                        userBar.setText("系统程序(" + sysAppListInfo.size() + ")");
                    } else {
                        userBar.setText("用户程序(" + userAppListInfo.size() + ")");
                    }
                }
            }
        });

        appList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0 && position != (userAppListInfo.size() + 1)) {
                    LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_app_expand);
                    TextView tv_uninstall = (TextView) view.findViewById(R.id.tv_uninstall);
                    TextView tv_open = (TextView) view.findViewById(R.id.tv_open);
                    TextView tv_share = (TextView) view.findViewById(R.id.tv_share);

                    tv_uninstall.setOnClickListener(new MyOnclickListener());
                    tv_open.setOnClickListener(new MyOnclickListener());
                    tv_share.setOnClickListener(new MyOnclickListener());

                    //显示隐藏的bar
                    if(ll.getVisibility() ==View.GONE){
                        ll.setVisibility(View.VISIBLE);
                    }else {
                        ll.setVisibility(View.GONE);
                    }

                    if (position < (userAppListInfo.size() + 1)) {
                        appInfo = userAppListInfo.get(position - 1);
                    } else {
                        appInfo = sysAppListInfo.get(position-(userAppListInfo.size()+2));
                    }
                }
            }
        });
    }

    private class MyOnclickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.tv_uninstall: //卸载
                    uninstall();
                    break;
                case R.id.tv_open: //打开
                    startApplication();
                    break;
                case R.id.tv_share: //分享
                    shareApplication();
                    break;
                default:
                    break;
            }
        }
    }

    //显示可用空间
    private void showAvailableSpace() {
        long memSize = getAvailableSpace(Environment.getDataDirectory().getAbsolutePath());//手机内存大小
        long sdSize = getAvailableSpace(Environment.getExternalStorageDirectory().getAbsolutePath());//外存大小
        memorySpace.setText("内存可用空间："+ Formatter.formatFileSize(this,memSize));
        sdCardSpace.setText("SD卡可用空间："+Formatter.formatFileSize(this,sdSize));
    }

    //获取某个目录的可用空间
    private long getAvailableSpace(String path) {
        StatFs statfs = new StatFs(path);
        long size = statfs.getBlockSize();
        long count = statfs.getAvailableBlocks();
        return size*count;
    }

    //获取手机应用的信息，并保存在List
    private void loadAppDataFromSys(){
        loading.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                appListInfo = AppInfoProvider.getInstance().getAppInfos(getApplicationContext());
                userAppListInfo = new ArrayList<>();
                sysAppListInfo = new ArrayList<>();
                for(AppInfo appInfo:appListInfo){
                    if(appInfo.isUserApp()){
                        userAppListInfo.add(appInfo);
                    }else{
                        sysAppListInfo.add(appInfo);
                    }
                }

                handler.sendEmptyMessage(0);//刷新列表
            }
        }.start();
    }

    //开启应用
    private void startApplication() {
        Intent intent = new Intent();
        String packname = appInfo.getPackname();
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_ACTIVITIES);
            ActivityInfo[] activityInfos = packinfo.activities;
            if(activityInfos!=null&&activityInfos.length>0){
                ActivityInfo activityinfo = activityInfos[0];
                intent.setClassName(packname, activityinfo.name);
                startActivity(intent);
            }else{
                Toast.makeText(this, "哎呀，这个应用程序没界面", Toast.LENGTH_SHORT).show();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "没法开这个应用。", Toast.LENGTH_SHORT).show();
        }
    }

    //分享应用程序
    private void shareApplication() {
        Intent intent = new Intent();
        // <action android:name="android.intent.action.SEND" />
        // <category android:name="android.intent.category.DEFAULT" />
        // <data android:mimeType="text/plain" />
        intent.setAction("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        intent.putExtra(
                Intent.EXTRA_TEXT,
                "推荐你使用一个软件，软件的名称叫："
                        + appInfo.getName()
                        + "，下载地址：https://play.google.com/store/apps/details?id="+appInfo.getPackname());
        startActivity(intent);
    }

    //卸载
    private void uninstall() {
        if (appInfo.isUserApp()) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DELETE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + appInfo.getPackname()));
            startActivityForResult(intent, 0);
        } else {
            Toast.makeText(this, "系统应用需要有root权限后才能卸载", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(0 == requestCode){
            loadAppDataFromSys();
        }
    }
}
