package com.songsy.iframe.core.persistence.provider;

/**
 * @author songshuiyang
 * @date 2018/10/28 11:47
 */
public class MyBatisProviderContext {

    private ProviderContext providerContext;

    private static ThreadLocal<MyBatisProviderContext> threadLocal = new ThreadLocal<MyBatisProviderContext>();

    public static MyBatisProviderContext get() {
        if (threadLocal.get() == null) {
            MyBatisProviderContext threadLocalContext = new MyBatisProviderContext();
            threadLocal.set(threadLocalContext);
        }
        return threadLocal.get();
    }

    public void remove() {
        this.providerContext = null;
        threadLocal.remove();
    }

    public ProviderContext getProviderContext() {
        return providerContext;
    }

    public void setProviderContext(ProviderContext providerContext) {
        this.providerContext = providerContext;
    }
}
