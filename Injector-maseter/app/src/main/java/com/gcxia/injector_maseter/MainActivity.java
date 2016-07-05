package com.gcxia.injector_maseter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        User user = new User();
        user.id = "123";
        user.name = "github";
        startActivity(new Intent(this, Main2Activity.class).putExtra("A", "1111111").putExtra("B", 100).putExtra("C", user));
    }
}
