package com.ctrip;

import android.app.Activity;

import com.ctrip.model.Finder;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by xgc on 16/7/4.
 */
public class XgcIntentInjector
{
    static final Map<Class<?>, AbstractInjector<Object>> INJECTORS = new LinkedHashMap<Class<?>, AbstractInjector<Object>>();

    public static void inject(Activity activity)
    {
        com.ctrip.AbstractInjector<Object> injector = findInjector(activity);
        injector.inject(Finder.ACTIVITY, activity, activity);
    }

    private static com.ctrip.AbstractInjector<Object> findInjector(Object activity)
    {
        Class<?> clazz = activity.getClass();
        com.ctrip.AbstractInjector<Object> injector = INJECTORS.get(clazz);
        if (injector == null)
        {
            try
            {
                Class injectorClazz = Class.forName(clazz.getName() + "$$"
                        + com.ctrip.ProxyInfo.PROXY);
                injector = (com.ctrip.AbstractInjector<Object>) injectorClazz
                        .newInstance();
                INJECTORS.put(clazz, injector);
            } catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            } catch (InstantiationException e)
            {
                e.printStackTrace();
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
        return injector;
    }
}

