package com.czh.po.server;

import java.util.concurrent.ThreadFactory;

/**
 * 线程factory
 * @author chenzhuohong
 */
public class ServerThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r);
    }
}
