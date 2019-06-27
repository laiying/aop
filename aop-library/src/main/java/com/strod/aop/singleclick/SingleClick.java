package com.strod.aop.singleclick;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by laiying on 2019/3/20.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SingleClick {
    /**
     * single click time
     * @return
     */
    long value() default 500;

    int[] except() default {};

    String[] moduleExceptIdName() default {};
}
