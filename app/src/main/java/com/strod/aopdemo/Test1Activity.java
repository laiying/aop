package com.strod.aopdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by laiying on 2019/6/27.
 */
public class Test1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.sIsUserLogin = false;
                Toast.makeText(Test1Activity.this, "已退出登录",Toast.LENGTH_SHORT).show();
                Test1Activity.this.finish();
            }
        });
    }
}
