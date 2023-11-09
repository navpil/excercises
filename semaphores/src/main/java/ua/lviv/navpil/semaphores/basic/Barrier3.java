package ua.lviv.navpil.semaphores.basic;

import ua.lviv.navpil.semaphores.ThreadWaiter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Barrier3 {

    /*
    The solution from the book
     */
    public static void main(String[] args) throws InterruptedException {
        int x = 6;
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(x);

        int n = x;
        int[] counter = new int[] {0};
        Semaphore barrier = new Semaphore(0);
        Semaphore mutex = new Semaphore(1);

        for (int i = 0; i < x; i++) {
            ThreadWaiter threadWaiter = new ThreadWaiter(i) {
                @Override
                public void unsafeRun() throws InterruptedException{
                    System.out.println(getNumber() + "-1");
                    mutex.acquire();
                    counter[0]++;
                    if (counter[0] == n) {
                        barrier.release();
                    }
                    mutex.release();

                    //turnstile
                    barrier.acquire();
                    barrier.release();

                    System.out.println(getNumber() + "-2");
                }
            };
            scheduledExecutorService.schedule(threadWaiter, 1, TimeUnit.MILLISECONDS);
        }

        scheduledExecutorService.shutdown();
        scheduledExecutorService.awaitTermination(2, TimeUnit.SECONDS);
    }

}
