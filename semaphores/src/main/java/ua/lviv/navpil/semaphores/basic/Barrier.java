package ua.lviv.navpil.semaphores.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * This is my solution, which uses many semaphors.
 */
public class Barrier {

    public static void main(String[] args) throws InterruptedException {
        int x = 6;
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(x);

        Barrier barrier = new Barrier(x);

        for (int i = 0; i < x; i++) {
            ThreadWaiter threadWaiter = new ThreadWaiter(barrier, i);
            scheduledExecutorService.schedule(threadWaiter, 1, TimeUnit.MILLISECONDS);
        }

        scheduledExecutorService.shutdown();
        scheduledExecutorService.awaitTermination(2, TimeUnit.SECONDS);
    }

    public static class ThreadWaiter implements Runnable {

        private final Barrier barrier;
        private final int number;

        public ThreadWaiter(Barrier barrier, int number) {
            this.barrier = barrier;
            this.number = number;
        }

        @Override
        public void run() {
            System.out.println(number + "-1");
            barrier.unlock(number);

            try {
//                Thread.sleep(100);
                barrier.lock();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(number + "-2");
        }
    }


    private final List<Semaphore> semaphores = new ArrayList<>();
    public Barrier(int threads) {
        for (int i = 0; i < threads; i++) {
            semaphores.add(new Semaphore(0));
        }
    }

    public void unlock(int x) {
        for (int i = 0; i < semaphores.size(); i++) {
            semaphores.get(x).release();
        }
    }

    public void lock() throws InterruptedException {
        for (Semaphore semaphore : semaphores) {
            semaphore.acquire();
        }
    }

}
