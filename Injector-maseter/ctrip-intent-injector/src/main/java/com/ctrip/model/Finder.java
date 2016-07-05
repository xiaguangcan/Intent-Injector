package com.ctrip.model;

import android.app.Activity;

/**
 * Created by xgc on 16/7/4.
 */
public enum  Finder {
    ACTIVITY{
        @Override
        public Object getIntentSource(Object object, String key) {
            return ((Activity)object).getIntent().getExtras().get(key);
        }
    };

    private Finder() {}

    public <T> T castView(Object obj)
    {
        return (T) obj;
    }

    public abstract Object getIntentSource(Object object,String key);

}

