package cn.reghao.jutil.jdk.thread;

import java.util.concurrent.*;

/**
 * 线程池包装类
 *
 * @author reghao
 * @date 2019-11-12 16:39:45
 */
public class ThreadPoolWrapper {
    public static ExecutorService threadPool(String name) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat(name + "-pool-%d").build();

        return new ThreadPoolExecutor(10, 20, 200L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(32),
                namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 获取一个线程池
     * TODO 线程池根据机器 CPU 数量设置一个合适的值
     *
     * @param name 线程池名字
     * @return 线程池
     * @date 2019-11-12 下午4:42
     */
    public static ExecutorService threadPool(String name, int size) {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat(name + "-pool-%d").build();
        return new ThreadPoolExecutor(size, size*2, 1800L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(size*10),
                namedThreadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }

    public static ScheduledExecutorService scheduledThreadPool(String name, int size) {
        ThreadFactory namedThreadFactory =
                new ThreadFactoryBuilder().setNameFormat(name + "-scheduled-pool-%d").build();
        return new ScheduledThreadPoolExecutor(size, namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

    public static void shutdownScheduler(ScheduledExecutorService scheduler) {
        scheduler.shutdown();
        int count = 1;
        int max = 30;
        while (count < max && !scheduler.isTerminated()) {
            try {
                count++;
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //log.info("等待 {} 中的任务完成已耗时 {}s...", scheduler.getClass().getSimpleName(), count++);
        }
    }
}
