package com.zerody.user.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author  DaBai
 * @date  2022/2/25 16:51
 */

public class SingletonThreadPoolUtil {

    //根据cpu的数量动态的配置核心线程数和最大线程数
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //核心线程数 = CPU核心数 + 1
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    //线程池最大线程数 = CPU核心数 * 2 + 1
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    //非核心线程闲置时间 = 超时1s
    private static final int KEEP_ALIVE = 1;
    // 队列容量
    private static final int CAPACITY = 3000;


    // 要确保该类只有一个实例对象，避免产生过多对象消费资源，所以采用单例模式
    private SingletonThreadPoolUtil() {
    }
    public static ThreadPoolExecutor getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        /**
         * corePoolSize:核心线程数
         * maximumPoolSize：线程池所容纳最大线程数(workQueue队列满了之后才开启)
         * keepAliveTime：非核心线程闲置时间超时时长
         * unit：keepAliveTime的单位
         * workQueue：等待队列，存储还未执行的任务
         * threadFactory：线程创建的工厂
         * handler：异常处理机制:
         * 1.CallerRunsPolicy ：这个策略重试添加当前的任务，他会自动重复调用 execute() 方法，直到成功。
         * 2. AbortPolicy ：对拒绝任务抛弃处理，并且抛出异常。
         * 3. DiscardPolicy ：对拒绝任务直接无声抛弃，没有异常信息。
         * 4. DiscardOldestPolicy ：对拒绝任务不抛弃，而是抛弃队列里面等待最久的一个线程，然后把拒绝任务加到队列。
         */
        public final static ThreadPoolExecutor instance = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(CAPACITY),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());

    }
}
