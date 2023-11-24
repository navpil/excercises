package ua.lviv.navpil.semaphores.basic;

import ua.lviv.navpil.semaphores.UnsafeNumberedRunnable;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class ReusableBarrier {

    /*
    The solution from the book
     */
    public static void main(String[] args) throws InterruptedException {
        int x = 6;
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(x);

        int n = x;
        int[] counter = new int[] {0};
        Semaphore barrier = new Semaphore(0);
        Semaphore barrier2 = new Semaphore(1);
        Semaphore mutex = new Semaphore(1);

        for (int i = 0; i < x; i++) {
            UnsafeNumberedRunnable unsafeRunnable = new UnsafeNumberedRunnable(i) {
                @Override
                public void unsafeRun() throws InterruptedException{
                    for (int j = 0; j < 10; j++) {
                        System.out.println(getNumber() + "-1(" + j  + ")");

                        mutex.acquire();
                        counter[0]++;
                        if (counter[0] == n) {
                            barrier2.acquire();
                            barrier.release();
                        }
                        mutex.release();
                        barrier.acquire();
                        barrier.release();

                        //Critical section - Should be executed together

                        mutex.acquire();
                        counter[0]--;
                        if (counter[0] == 0) {
                            barrier.acquire();
                            barrier2.release();
                        }
                        mutex.release();
                        barrier2.acquire();
                        barrier2.release();

                        System.out.println(getNumber() + "-2(" + j + ")");
                    }
                }
            };
            scheduledExecutorService.schedule(unsafeRunnable, 1, TimeUnit.MILLISECONDS);
        }

        scheduledExecutorService.shutdown();
        scheduledExecutorService.awaitTermination(2, TimeUnit.SECONDS);
    }


}
