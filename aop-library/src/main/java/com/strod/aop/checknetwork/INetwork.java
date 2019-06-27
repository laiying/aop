package com.strod.aop.checknetwork;

/**
 * Created by laiying on 2019/3/21.
 */
public interface INetwork {
    boolean isNetworkOnline();
    void onNetworkTips(int type);
}
