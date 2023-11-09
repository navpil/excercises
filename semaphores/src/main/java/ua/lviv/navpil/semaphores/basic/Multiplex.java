package ua.lviv.navpil.semaphores.basic;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static ua.lviv.navpil.semaphores.ExceptionalRunnable.wrap;

public class Multiplex {

    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

        int[] counter = new int[]{0};
        int maxNumberOfThreads = 3;
        Semaphore mutex = new Semaphore(maxNumberOfThreads);

        scheduledExecutorService.schedule(wrap(() -> {
            mutex.acquire();
            System.out.println("A1");
            Thread.sleep(100);
            counter[0]++;
            System.out.println("A2");
            mutex.release();
        }), 1, TimeUnit.MILLISECONDS);

        scheduledExecutorService.schedule(wrap(() -> {
            mutex.acquire();
            System.out.println("B1");
            Thread.sleep(100);
            counter[0]++;
            System.out.println("B2");
            mutex.release();

        }), 1, TimeUnit.MILLISECONDS);

        System.out.println("Hello");
        scheduledExecutorService.shutdown();
        scheduledExecutorService.awaitTermination(2, TimeUnit.SECONDS);
    }

}
