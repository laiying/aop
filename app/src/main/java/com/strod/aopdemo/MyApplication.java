package com.strod.aopdemo;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.strod.aop.AopManager;
import com.strod.aop.checknetwork.INetwork;
import com.strod.aop.login.ILoginInterceptor;
import com.strod.aopdemo.utils.NetMonitorUtils;

/**
 * Created by laiying on 2019/6/27.
 */
public class MyApplication extends Application {
    private static Application sInstance;

    public static boolean sIsUserLogin = false;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        //设置打印日志
        AopManager.setDebug(BuildConfig.DEBUG);

        //设置全局的无网络提示
        AopManager.getInstance().setINetwork(new INetwork() {
            @Override
            public boolean isNetworkOnline() {
                return NetMonitorUtils.isNetworkConnected(sInstance);
            }

            @Override
            public void onNetworkTips(int type) {
                switch (type){
                    case 0:
                        Toast.makeText(sInstance, "暂无网络", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        //dialog or other,custom define
                        break;
                }
            }
        });

        //设置全局的登录拦截
        AopManager.getInstance().setILoginInterceptor(new ILoginInterceptor() {
            @Override
            public boolean isLogin() {
                //这里判断当前是否登录
                return sIsUserLogin;
            }

            @Override
            public void navigationLoginUI(int action) {
                //这里可以设置跳转到登录页面,可根据action做相关的操作设置
                Log.d("LoginInterceptor", "navToLoginUI action:"+ action);
                LoginActivity.start(sInstance);
            }
        });
    }

}
