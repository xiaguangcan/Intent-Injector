package com.ctrip;

import com.ctrip.model.Finder;

/**
 * Created by xgc on 16/7/4.
 */
public interface AbstractInjector<T>
{

    void inject(Finder finder, T target, Object source);

}
