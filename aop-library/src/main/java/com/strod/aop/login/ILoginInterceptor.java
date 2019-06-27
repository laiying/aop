package com.strod.aop.login;

/**
 * Created by laiying on 2019/3/21.
 */
public interface ILoginInterceptor {
    /**
     * current is login
     * @return
     */
    public boolean isLogin();

    /**
     * navigation to LoginActivity
     * @param action
     */
    public void navigationLoginUI(int action);
}
