package com.gcxia.injector_maseter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ctrip.annotation.Parameter;
import com.ctrip.XgcIntentInjector;

/**
 * Created by gcxia on 2016/7/5.
 */
public class Main2Activity extends AppCompatActivity {

    @Parameter("A")
    String test1;

    @Parameter("B")
    int test2;

    @Parameter("C")
    User test3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        XgcIntentInjector.inject(this);
        Log.e("Test",test1);
        Log.e("Test",test2+"");
        Log.e("Test",test3.toString());
    }
}
