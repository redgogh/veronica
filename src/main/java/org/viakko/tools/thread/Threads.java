package org.Viakko.tools.thread;

import org.Viakko.tools.exception.UncheckedThreadException;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.Viakko.tools.string.StringUtils.strhas;
import static org.Viakko.tools.string.StringUtils.strfmt;

/**
 * 线程工具类，提供线程创建、执行、管理和查询的便捷方法。
 *
 * <p>主要功能包括：
 * <ul>
 *   <li>基于命名规则创建普通线程和守护线程工厂。</li>
 *   <li>快速创建并启动线程，支持同步和异步任务执行。</li>
 *   <li>基于 CompletableFuture 的异步任务执行封装。</li>
 *   <li>创建固定和调度线程池，支持线程命名。</li>
 *   <li>优雅关闭线程池，响应中断异常。</li>
 *   <li>线程休眠和等待(join)的中断处理封装。</li>
 *   <li>查询所有活动线程及按名称筛选线程集合。</li>
 * </ul>
 *
 * @author Ekko
 * @since 1.0
 */
@SuppressWarnings("UnusedReturnValue")
public class Threads {

    /** 非守护线程工厂，线程名前缀为 worker[%d] */
    public static final ThreadFactory workerThreadFactory = namedThreadFactory("user-worker", false);

    /** 守护线程工厂，线程名前缀为 daemon[%d] */
    public static final ThreadFactory workerDaemonThreadFactory = namedThreadFactory("user-daemon", true);

    /**
     * 创建一个非守护线程执行指定任务
     *
     * @param task 要执行的 Runnable 任务
     * @return 新建线程实例
     */
    public static Thread create(Runnable task) {
        return workerThreadFactory.newThread(task);
    }

    /**
     * 创建并启动一个守护线程执行指定任务
     *
     * @param task 要执行的 Runnable 任务
     * @return 启动后的守护线程实例
     */
    public static Thread runDaemon(Runnable task) {
        Thread t = workerDaemonThreadFactory.newThread(task);
        t.start();
        return t;
    }

    /**
     * 异步运行无返回值任务，使用默认线程池
     *
     * @param task 要异步执行的任务
     * @return CompletableFuture 以便链式操作
     */
    public static CompletableFuture<Void> runAsync(Runnable task) {
        return CompletableFuture.runAsync(task);
    }

    /**
     * 异步运行无返回值任务，使用指定执行器
     *
     * @param task 要异步执行的任务
     * @param executor 线程池执行器
     * @return CompletableFuture 以便链式操作
     */
    public static CompletableFuture<Void> runAsync(Runnable task, Executor executor) {
        return CompletableFuture.runAsync(task, executor);
    }

    /**
     * 异步运行有返回值任务，使用默认线程池
     *
     * @param supplier 任务提供者
     * @param <T> 返回值类型
     * @return CompletableFuture 包裹结果
     */
    public static <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier);
    }

    /**
     * 异步运行有返回值任务，使用指定执行器
     *
     * @param supplier 任务提供者
     * @param executor 线程池执行器
     * @param <T> 返回值类型
     * @return CompletableFuture 包裹结果
     */
    public static <T> CompletableFuture<T> supplyAsync(Supplier<T> supplier, Executor executor) {
        return CompletableFuture.supplyAsync(supplier, executor);
    }

    /**
     * 创建固定线程数的线程池，线程名称带指定前缀
     *
     * @param nThreads 线程数
     * @param prefix 线程名前缀格式，如 "worker-%d"
     * @return 线程池执行器
     */
    public static ExecutorService newFixedThreadPool(int nThreads, String prefix) {
        return Executors.newFixedThreadPool(nThreads, namedThreadFactory(prefix, false));
    }

    /**
     * 创建固定线程数的调度线程池，线程名称带指定前缀
     *
     * @param nThreads 线程数
     * @param prefix 线程名前缀格式，如 "scheduler-%d"
     * @return 调度线程池执行器
     */
    public static ScheduledExecutorService newScheduledThreadPool(int nThreads, String prefix) {
        return Executors.newScheduledThreadPool(nThreads, namedThreadFactory(prefix, false));
    }

    /**
     * 创建指定命名规则的线程工厂
     *
     * @param prefix 线程名格式，支持 `%d` 占位递增编号
     * @param daemon 是否守护线程
     * @return 线程工厂实例
     */
    public static ThreadFactory namedThreadFactory(String prefix, boolean daemon) {
        AtomicInteger count = new AtomicInteger(1);
        return r -> {
            Thread t = new Thread(r, strfmt(prefix.concat("-%d"), count.getAndIncrement()));
            t.setDaemon(daemon);
            return t;
        };
    }

    /**
     * 优雅关闭线程池，先调用 shutdown，再等待最多 10 秒，超时则强制关闭
     *
     * @param executor 线程池执行器
     * @throws UncheckedThreadException 中断时封装异常抛出
     */
    public static void shutdownGracefully(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            interrupt();
            throw new UncheckedThreadException(e);
        }
    }

    /**
     * 当前线程中断标志位设置
     */
    public static void interrupt() {
        Thread.currentThread().interrupt();
    }

    /**
     * 让当前线程睡眠指定毫秒数，捕获中断时设置中断标志并抛出运行时异常
     *
     * @param millis 睡眠时间（毫秒）
     * @throws UncheckedThreadException 中断时抛出
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            interrupt();
            throw new UncheckedThreadException(e);
        }
    }

    /**
     * 等待指定线程结束，捕获中断时设置中断标志并抛出运行时异常
     *
     * @param thread 目标线程
     * @throws UncheckedThreadException 中断时抛出
     */
    public static void join(Thread thread) {
        try {
            thread.join();
        } catch (InterruptedException e) {
            interrupt();
            throw new UncheckedThreadException(e);
        }
    }

    /**
     * @return 返回当前线程名称
     */
    public static String getName() {
        return Thread.currentThread().getName();
    }

    /**
     * 获取系统中所有活动线程集合
     *
     * <p>从根线程组开始递归获取所有活动线程，防止遗漏。
     *
     * @return 当前 JVM 所有活动线程的集合
     */
    public static Set<Thread> allThreads() {
        ThreadGroup root = Thread.currentThread().getThreadGroup();
        while (root.getParent() != null) {
            root = root.getParent();
        }
        int estimatedSize = root.activeCount() * 2;
        Thread[] threads = new Thread[estimatedSize];
        int actualSize = root.enumerate(threads, true);
        Set<Thread> result = new HashSet<>(actualSize);
        for (int i = 0; i < actualSize; i++) {
            if (threads[i] != null) {
                result.add(threads[i]);
            }
        }
        return result;
    }

    /**
     * 根据线程名称模糊匹配，返回符合条件的线程集合
     *
     * @param name 线程名包含该字符串即匹配
     * @return 匹配线程集合
     */
    public static Set<Thread> threadsByName(String name) {
        Set<Thread> all = allThreads();
        Set<Thread> filtered = new HashSet<>();
        for (Thread t : all) {
            if (strhas(t.getName(), name)) {
                filtered.add(t);
            }
        }
        return filtered;
    }

}