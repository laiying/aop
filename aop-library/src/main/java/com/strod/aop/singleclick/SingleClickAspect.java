package com.strod.aop.singleclick;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;

import com.strod.aop.AopManager;
import com.strod.aop.BuildConfig;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.Method;

/**
 * Created by laiying on 2019/3/20.
 */
@Aspect
public class SingleClickAspect {
    private static final long DEFAULT_TIME_INTERVAL = 5000;


    @Pointcut("execution(* onClick(..))")
    public void onClickPointcut(){}

    /**
     * 定义切点,标记切点为所有被@SingleClick注解的方法
     * 注意:SingleClick为类的全路径
     */
    @Pointcut("execution(@com.strod.aop.singleclick.SingleClick * *(..))")
    public void methodAnnotated(){}

    @Pointcut("execution(@butterknife.OnClick * *(..))")
    public void butterKnifePointcut(){}

    /**
     * 定义一个切面方法，包裹切点方法
     * @param joinPoint
     * @throws Throwable
     */
    @Around("methodAnnotated() || butterKnifePointcut()")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable{

        View view = null;
        for (Object arg : joinPoint.getArgs()){
            if (arg instanceof View){
                view = (View)arg;
                break;
            }
        }

        if (view == null){
            throw new AnnotationFormatError("@SingleClick 注解的方法必须有View参数");
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //检查方法是否有注解
        boolean hasAnnotation = method != null && method.isAnnotationPresent(SingleClick.class);

        long interval = 500L;

        //注解排除某个控件不防止双击
        if (hasAnnotation) {
            SingleClick annotation = method.getAnnotation(SingleClick.class);
            interval = annotation.value();
            //按id值排除不防止双击的按钮点击
            int id = view.getId();
            int[] except = annotation.except();
            for (int i : except) {
                if (i == id) {
                    joinPoint.proceed();

                    if (AopManager.isDebug()){
                        Log.d("SingleClickAspect", "except view:"+view.getId());
                    }
                    return;
                }
            }
            //按id名排除不防止双击的按钮点击（非app模块）
            String[] idName = annotation.moduleExceptIdName();
            Resources resources = view.getResources();
            for (String name : idName) {
                int resId = resources.getIdentifier(name, "id", view.getContext().getPackageName());
                if (resId == id) {
                    joinPoint.proceed();
                    if (BuildConfig.DEBUG){
                        Log.d("SingleClickAspect", "module exceptIdName view:"+view.getId());
                    }
                    return;
                }
            }
        }

        //判断是否快速点击
        if (!SingleClickUtils.isFastDoubleClick(view, interval)){
            //不是快速点击，执行原方法
            joinPoint.proceed();
        }else {
            if (AopManager.isDebug()){
                Log.d("SingleClickAspect", "view:"+view.getId()+" 快速点击了");
            }
        }

    }
}
