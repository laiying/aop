package com.strod.aopdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.strod.aop.checknetwork.CheckNetwork;
import com.strod.aop.login.NeedLogin;
import com.strod.aop.singleclick.SingleClick;
import com.strod.aop.timetrace.TimeTrace;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.check_netword).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        findViewById(R.id.time_trace).setOnClickListener(this);

        findViewById(R.id.single_click).setOnClickListener(new View.OnClickListener() {

            @SingleClick(value = 1000)
            @Override
            public void onClick(View v) {
                Log.d(TAG, "signClick() log");
            }
        });
    }

    @SingleClick(value = 1000)
    @Override
    public void onClick(View v) {
        Log.d(TAG, "onClick() log");
        switch (v.getId()){
            case R.id.check_netword:
                checkNetword();
                break;
            case R.id.login:
                login();
                break;
            case R.id.time_trace:
                timeTrace();
                break;
        }
    }

    @CheckNetwork
    private void checkNetword(){
        //do something,like net request
        Log.d(TAG, "checkNetword() log");
        Toast.makeText(this, "当前有网络，请开启飞行模式测试无网络",Toast.LENGTH_SHORT).show();
    }

    @NeedLogin
    private void login(){
        //to other activity
        Log.d(TAG, "login() log");
        startActivity(new Intent(this, Test1Activity.class));
    }

    /**
     * 一般注解在onClick(View v)方法上
     * 如果单个方法注解，需要传入view参数
     * @param v
     */
    @SingleClick(value = 1000)
    private void signClick(View v){
        Log.d(TAG, "signClick() log");
    }

    //except是排除不需要双击拦截的控件id,适用于@SingleClick注解在app模块的onClick方法上
    @SingleClick(value = 1000, except = {R.id.time_trace, R.id.check_netword})
    private void signClick2(View v){

    }
    //moduleExceptIdName是排除不需要双击拦截的控件名字,适用于@SingleClick注解在module模块的onClick方法上
    @SingleClick(value = 1000, moduleExceptIdName = {"time_trace", "check_netword"})
    private void signClick3(View v){

    }

    @TimeTrace(name = "timeTrace")
    private void timeTrace(){
        Log.d(TAG, "timeTrace() log");
        //
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
