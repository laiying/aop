package com.strod.aopdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by laiying on 2019/6/27.
 */
public class LoginActivity extends AppCompatActivity {
    
    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.sIsUserLogin = true;
                Toast.makeText(LoginActivity.this, "已登录",Toast.LENGTH_SHORT).show();
                LoginActivity.this.finish();
            }
        });
    }
}
