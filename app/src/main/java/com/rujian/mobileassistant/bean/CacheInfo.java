package com.rujian.mobileassistant.bean;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by stars on 2015/6/23.
 */
public class CacheInfo implements Serializable {
    public Drawable icon;
    public String name;
    public long cacheSize;
    public String packName;
}
