package com.strod.aop.checknetwork;


import com.strod.aop.AopManager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
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
public class CheckNetworkAspect {

    private static final String TAG = CheckNetworkAspect.class.getSimpleName();

    @Pointcut("execution(@com.strod.aop.checknetwork.CheckNetwork * *(..))")
    public void methodAnnotated(){}

    @Around("methodAnnotated()")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable{
        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            throw new AnnotationFormatError("CheckNetwork注解只能用于方法上");
        }

        MethodSignature methodSignature = (MethodSignature) signature;

        Method method = methodSignature.getMethod();

        if (!method.isAnnotationPresent(CheckNetwork.class)){
            return;
        }

        CheckNetwork checkNetwork = method.getAnnotation(CheckNetwork.class);
        if (checkNetwork == null){
            return;
        }

        INetwork network = AopManager.getInstance().getINetwork();
        if (network == null){
            throw new RuntimeException("AopManager类没有设置INetwork");
        }

        //判断有无网络
        if (network.isNetworkOnline()){
            joinPoint.proceed();
        }else {
            int type = checkNetwork.tipsType();

            network.onNetworkTips(type);
        }

    }
}
