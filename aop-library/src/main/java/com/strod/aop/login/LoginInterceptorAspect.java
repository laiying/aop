package com.strod.aop.login;

import android.util.Log;

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
public class LoginInterceptorAspect {

    /**
     * 定义切点,标记切点为所有被@NeedLogin注解的方法
     * 注意:NeedLogin为类的全路径
     */
    @Pointcut("execution(@com.strod.aop.login.NeedLogin * *(..))")
    public void methodAnnotated(){}


    /**
     * 定义一个切面方法，包裹切点方法
     * @param joinPoint
     * @throws Throwable
     */
    @Around("methodAnnotated()")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable{

        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            throw new AnnotationFormatError("NeedLogin注解只能用于方法上");
        }

        MethodSignature methodSignature = (MethodSignature) signature;

        Method method = methodSignature.getMethod();

        if (!method.isAnnotationPresent(NeedLogin.class)){
            return;
        }

        NeedLogin needLogin = method.getAnnotation(NeedLogin.class);

        if (needLogin == null){
            return;
        }

        int action = needLogin.loginAction();
        ILoginInterceptor loginInterceptor = AopManager.getInstance().getILoginInterceptor();
        if (loginInterceptor == null){
            throw new RuntimeException("AopManager类没有设置ILoginInterceptor");
        }

        if (loginInterceptor.isLogin()){
            joinPoint.proceed();
        }else {
            if (AopManager.isDebug()){
                Log.d("LoginInterceptorAspect", "navigationLoginUI");
            }
            loginInterceptor.navigationLoginUI(action);
        }

    }
}
