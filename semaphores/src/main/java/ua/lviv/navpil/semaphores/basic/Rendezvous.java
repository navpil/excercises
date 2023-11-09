package ua.lviv.navpil.semaphores.basic;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Rendezvous {

    public static void main(String[] args) throws InterruptedException {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);

        Semaphore waitForA = new Semaphore(0);
        Semaphore waitForB = new Semaphore(0);

        scheduledExecutorService.schedule(() -> {
            System.out.println("A1");
            waitForA.release();
            try {
                waitForB.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("A2");

        }, 1, TimeUnit.MILLISECONDS);
        scheduledExecutorService.schedule(() -> {
            System.out.println("B1");
            waitForB.release();
            try {
                waitForA.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("B2");

        }, 1, TimeUnit.MILLISECONDS);

        System.out.println("Hello");
        scheduledExecutorService.shutdown();
        scheduledExecutorService.awaitTermination(2, TimeUnit.SECONDS);
    }
}
