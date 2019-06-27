package com.strod.aop.checknetwork;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by laiying on 2019/3/20.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CheckNetwork {

    public static final int TOAST = 0;
    public static final int DIALOG = 1;

    int tipsType() default 0;
}
