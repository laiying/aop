package com.strod.aop.timetrace;

import android.util.Log;

import com.strod.aop.AopManager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by laiying on 2019/3/21.
 */
@Aspect
public class TimeTraceAspect {

    private static final String TAG = TimeTraceAspect.class.getSimpleName();

    @Pointcut("execution(@com.strod.aop.timetrace.TimeTrace * *(..))")
    public void methodAnnotated() {
    }

    @Around("methodAnnotated()")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        if (AopManager.isDebug()) {
            // 方法执行前先记录时间
            long start = System.currentTimeMillis();
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            // 获取注解
            TimeTrace annotation = methodSignature.getMethod().getAnnotation(TimeTrace.class);
            if (annotation == null) {
                return;
            }
            String value = annotation.name();

            // 执行原方法体
            joinPoint.proceed();

            // 方法执行完成后，记录时间，打印日志
            long end = System.currentTimeMillis();
            StringBuilder sb = new StringBuilder();
            sb.append(value)
                    .append(" proceed,used times:")
                    .append(end - start)
                    .append("ms=>")
                    .append((end - start) / 1000f)
                    .append("s");

            Log.d(TAG, sb.toString());
        }
    }
}
