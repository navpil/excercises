package ua.lviv.navpil.semaphores.basic;

import ua.lviv.navpil.semaphores.UnsafeNumberedRunnable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Barrier2 {

    /*
    My other solution - this time it will signal N times
     */
    public static void main(String[] args) throws InterruptedException {
        int x = 6;
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(x);

        int n = x;
        int[] counter = new int[] {0};
        Semaphore barrier = new Semaphore(0);
        Semaphore mutex = new Semaphore(1);

        for (int i = 0; i < x; i++) {
            UnsafeNumberedRunnable unsafeRunnable = new UnsafeNumberedRunnable(i) {
                @Override
                public void unsafeRun() throws InterruptedException{
                    System.out.println(getNumber() + "-1");
                    mutex.acquire();
                    counter[0]++;
                    if (counter[0] == n) {
                        for (int j = 0; j < n; j++) {
                            barrier.release();
                        }
                    }
                    mutex.release();
                    barrier.acquire();
                    System.out.println(getNumber() + "-2");
                }
            };
            scheduledExecutorService.schedule(unsafeRunnable, 1, TimeUnit.MILLISECONDS);
        }

        scheduledExecutorService.shutdown();
        scheduledExecutorService.awaitTermination(2, TimeUnit.SECONDS);
    }

}
