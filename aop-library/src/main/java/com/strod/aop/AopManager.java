package com.strod.aop;

import com.strod.aop.checknetwork.INetwork;
import com.strod.aop.login.ILoginInterceptor;

/**
 * Created by laiying on 2019/3/21.
 */
public class AopManager {
    private static AopManager sInstance;

    public static AopManager getInstance() {
        if (null == sInstance) {
            synchronized (AopManager.class) {
                if (null == sInstance) {
                    sInstance = new AopManager();
                }
            }
        }
        return sInstance;
    }

    private AopManager() {
    }

    private static boolean debug = false;
    /** Control whether debug logging is enabled. */
    public static void setDebug(boolean debug) {
        AopManager.debug = debug;
    }

    public static boolean isDebug() {
        return debug;
    }

    private INetwork mINetwork;
    private ILoginInterceptor mILoginInterceptor;

    public INetwork getINetwork() {
        return mINetwork;
    }

    public void setINetwork(INetwork INetwork) {
        mINetwork = INetwork;
    }

    public ILoginInterceptor getILoginInterceptor() {
        return mILoginInterceptor;
    }

    public void setILoginInterceptor(ILoginInterceptor ILoginInterceptor) {
        mILoginInterceptor = ILoginInterceptor;
    }
}