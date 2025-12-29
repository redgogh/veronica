package org.varketh.tools.test.thread;

/* -------------------------------------------------------------------------------- *\
|*                                                                                  *|
|*    Copyright (C) 2019-2024 Varketh Nockrath  All rights reserved.                *|
|*                                                                                  *|
|*    Licensed under the Apache License, Version 2.0 (the "License");               *|
|*    you may not use this file except in compliance with the License.              *|
|*    You may obtain a copy of the License at                                       *|
|*                                                                                  *|
|*        http://www.apache.org/licenses/LICENSE-2.0                                *|
|*                                                                                  *|
|*    Unless required by applicable law or agreed to in writing, software           *|
|*    distributed under the License is distributed on an "AS IS" BASIS,             *|
|*    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.      *|
|*    See the License for the specific language governing permissions and           *|
|*    limitations under the License.                                                *|
|*                                                                                  *|
\* -------------------------------------------------------------------------------- */

import org.junit.Test;
import org.varketh.tools.thread.Threads;

import java.util.concurrent.*;

@SuppressWarnings("ALL")
public class ThreadsTest {

    @Test
    public void testCreateThread() {
        Thread t = Threads.create(() -> System.out.println(Threads.getName()));
        t.start();
        t.interrupt();
    }

    @Test
    public void testRunDaemon() {
        Threads.runDaemon(() -> System.out.println("111"));
        Threads.sleep(3000);
        System.out.println("end");
    }

    @Test
    public void testRunAsync() {
        ExecutorService executor = Threads.newFixedThreadPool(1, "fixed-pool");
        Threads.runAsync(() -> System.out.println(Threads.getName()), executor);
        Threads.shutdownGracefully(executor);
    }

    @Test
    public void testRunSupplyAsync() throws ExecutionException, InterruptedException {
        ExecutorService executor = Threads.newFixedThreadPool(1, "fixed-pool");
        CompletableFuture<Integer> future = Threads.supplyAsync(() -> {
            Threads.sleep(3);
            return 10;
        }, executor);
        System.out.println("current thread name: " + Threads.getName());
        Integer ret = future.get();
        System.out.println("ret = " + ret);
        Threads.shutdownGracefully(executor);
    }

    @Test
    public void testSchduleThread() {
        ScheduledExecutorService executor =
                Threads.newScheduledThreadPool(1, "sche-pool");
        executor.schedule(() -> System.out.println(Threads.getName()), 3, TimeUnit.SECONDS);
        Threads.shutdownGracefully(executor);
    }

    @Test
    public void testJoin() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            Threads.sleep(500);
            System.out.println("I am Thread-1");
        });

        Thread t2 = new Thread(() -> {
            System.out.println("I am Thread-2");
        });

        t1.start();
        t1.join();

        t2.start();
        t2.join();

        System.out.println("All done");
    }

}
