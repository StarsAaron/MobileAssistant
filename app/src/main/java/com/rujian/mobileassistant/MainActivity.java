package com.rujian.mobileassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {

    private static final String[] widgetName ={"软件管理","通讯卫士","程序锁","进程管理","缓存清理"
            ,"XXXX","XXXX","XXXX","XXXX"};
    private static final int[] widgetIcons ={
            R.mipmap.widget_assistant_caipiao
            ,R.mipmap.widget_assistant_caipu
            ,R.mipmap.widget_assistant_chongzhi
            ,R.mipmap.widget_assistant_huochepiao
            ,R.mipmap.widget_assistant_jiakao
            ,R.mipmap.widget_assistant_jiemeng
            ,R.mipmap.widget_assistant_kuaidi
            ,R.mipmap.widget_assistant_suanming
            ,R.mipmap.widget_assistant_tianqi};
    private static List<Map<String,Object>> appList;
    private GridView appWidget;
    private RelativeLayout searchBar;
    private TextView concel,searchText;
    private EditText enterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //注册组件
        regist();
        //初始化组件
        init();
    }

    private void regist() {
        appWidget = (GridView) findViewById(R.id.gv_appwidget);
        searchBar = (RelativeLayout)findViewById(R.id.rl_searchbar);
        concel = (TextView)findViewById(R.id.tv_concel);
        searchText = (TextView)findViewById(R.id.tv_searh);
        enterText = (EditText)findViewById(R.id.et_enter_text);
    }

    private void init() {
        if(appList == null) {
            appList = new ArrayList<>();
        }
//        if (widgetName == null) {
//            widgetName = getResources().getStringArray(R.array.widget_names);
//        }
//        if (widgetIcons == null) {
//            widgetIcons = getResources().getIntArray(R.array.widget_icons);
//        }
        for(int i=0;i<widgetName.length;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("icons",widgetIcons[i]);
            map.put("names",widgetName[i]);
            appList.add(map);
        }

        appWidget.setAdapter(new SimpleAdapter(this,appList,R.layout.list_function_item
                ,new String[]{"icons","names"},new int[]{R.id.iv_icons,R.id.tv_appname}));
        appWidget.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent1 = new Intent(MainActivity.this,AppManagerActivity.class);
                        startActivity(intent1);
                        System.out.println("点击了软件管理");
                        break;
                    case 1:
                        Intent intent2 = new Intent(MainActivity.this,AppManagerActivity.class);
                        startActivity(intent2);
                        System.out.println("点击了手机卫士");
                        break;
                    case 2:
                        Intent intent3 = new Intent(MainActivity.this,AppManagerActivity.class);
                        startActivity(intent3);
                        System.out.println("点击了程序锁");
                        break;
                    case 3:
                        Intent intent4 = new Intent(MainActivity.this,AppManagerActivity.class);
                        startActivity(intent4);
                        System.out.println("点击了进程管理");
                        break;
                    case 4:
                        Intent intent5 = new Intent(MainActivity.this,CleanCacheActivity.class);
                        startActivity(intent5);
                        System.out.println("点击了缓存清理");
                        break;
                }
            }
        });

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setVisibility(View.GONE);
                enterText.setVisibility(View.VISIBLE);
                concel.setVisibility(View.VISIBLE);
            }
        });
        concel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setVisibility(View.VISIBLE);
                enterText.setVisibility(View.GONE);
                concel.setVisibility(View.GONE);
            }
        });
    }

}
