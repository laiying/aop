#简介
使用运行时注解,全局拦截检查网络，是否登录，快速双击过滤，方法调用耗时打印.
以一种更优雅的方式实现这些常用的判断

![demo](https://github.com/laiying/aop/tree/master/Screenshots/Screenshot.jpg)
![demo](/raw/master/Screenshots/Screenshot.jpg)  
更多使用(请参考demo)


#### 二、基础功能
0. gradle环境
```
//root build.gradle
dependencies {
    classpath 'com.android.tools.build:gradle:3.2.1'

    // NOTE: Do not place your application dependencies here; they belong
    // in the individual module build.gradle files
}
repositories {
    google()
    jcenter()
    maven { url "https://jitpack.io" }
}
// gradle/wrapper/gradle-wrapper.properties
distributionUrl=https\://services.gradle.org/distributions/gradle-4.10.1-all.zip
####其它gradle版本请自行配置

1. 添加依赖和配置
``` app->build.gradle

apply plugin: 'com.android.application'
// ajc 编译所需gradle脚本,application适用
apply from: '../aop-library/aspectj-configure-app.gradle'

android {
    defaultConfig {
	...
}

dependencies {

    implementation project(path: ':aop-library')
    ...
}


2. 初始化SDK
``` java
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

```

3. 添加注解
``` java
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
```



4. 添加混淆规则(如果使用了Proguard)
```
# aop注解
-adaptclassstrings
-keepattributes InnerClasses, EnclosingMethod, Signature, *Annotation*
-keepnames @org.aspectj.lang.annotation.Aspect class * {
    ajc* <methods>;
}
-keepclassmembers class ** {
    @com.strod.aop.** <methods>;
}

-dontwarn com.strod.aop.**
-keep class com.strod.aop.**{*;}
```


#作者
laiying email:lai_ying@163.com


