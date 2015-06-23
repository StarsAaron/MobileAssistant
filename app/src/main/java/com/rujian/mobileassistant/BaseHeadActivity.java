package com.rujian.mobileassistant;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by stars on 2015/6/23.
 */
public class BaseHeadActivity extends Activity {
    private RelativeLayout rl_title_bar,rl_container;
    private TextView tv_title;
    private ImageButton img_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_basehead);
        assignview();
    }

    @Override
    public void setContentView(View view) {
        setContentView(view,view.getLayoutParams());
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        rl_container.addView(view,params);
    }

    @Override
    public void setContentView(int layoutResID) {
        View v = getLayoutInflater().inflate(layoutResID,rl_container,false);
        setContentView(v);
    }

    private void assignview() {
        rl_title_bar = (RelativeLayout)findViewById(R.id.rl_title_bar);
        rl_container = (RelativeLayout)findViewById(R.id.rl_container);
        tv_title = (TextView)findViewById(R.id.tv_title);
        img_button = (ImageButton)findViewById(R.id.img_button);
    }
    //设置标题
    protected void setTitle(String title){
        tv_title.setText(title);
    }
    //隐藏左按钮
    protected void hideBackButton(){
        img_button.setVisibility(View.GONE);
    }
    //显示左按钮
    protected void showBackButton(){
        img_button.setVisibility(View.VISIBLE);
    }
    //添加左按钮点击事件
    protected void setBackButtonOnClickListener(View.OnClickListener listener){
        img_button.setOnClickListener(listener);
    }

    //设置标题字体颜色
    protected void setTitleTextColor(int color){
        tv_title.setTextColor(color);
    }

    //设置标题颜色
    protected void setTitleBarColor(int color){
        rl_title_bar.setBackgroundColor(color);
    }
}
