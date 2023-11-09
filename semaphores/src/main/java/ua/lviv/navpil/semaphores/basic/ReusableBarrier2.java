package ua.lviv.navpil.semaphores.basic;

import ua.lviv.navpil.semaphores.ConcurrentRunner;
import ua.lviv.navpil.semaphores.ThreadWaiter;

import java.util.concurrent.Semaphore;

public class ReusableBarrier2 {

    /*
    This is my solution (it's also in the book though)
     */
    public static void main(String[] args) throws InterruptedException {
        int x = 6;

        ReusableBarrier2 reusableBarrier2 = new ReusableBarrier2(x);

        ConcurrentRunner concurrentRunner = new ConcurrentRunner();
        for (int i = 0; i < x; i++) {
            concurrentRunner.add(new ThreadWaiter(i) {
                @Override
                public void unsafeRun() throws InterruptedException{
                    for (int j = 0; j < 10; j++) {
                        System.out.println(getNumber() + "-1(" + j  + ")");

                        reusableBarrier2.waitForEveryone();
                        System.out.println("Critical section, counter is " + reusableBarrier2.getCounter());
                        //Critical section - Should be executed together

                        reusableBarrier2.recycle();

                        System.out.println(getNumber() + "-2(" + j + ")");
                        System.out.println("Post-critical section, counter is "  + reusableBarrier2.getCounter());
                    }
                }
            });
        }

        concurrentRunner.start();
    }

    private int getCounter() {
        return counter[0];
    }

    int n;
    final int[] counter = new int[] {0};
    final Semaphore barrier = new Semaphore(0);
    final Semaphore barrier2 = new Semaphore(0);
    final Semaphore mutex = new Semaphore(1);

    public ReusableBarrier2(int n) {
        this.n = n;
    }

    public void waitForEveryone() throws InterruptedException {
        mutex.acquire();
        counter[0]++;
        if (counter[0] == n) {
            for (int k = 0; k < n; k++) {
                barrier.release();
            }
        }
        mutex.release();
        barrier.acquire();

    }

    public void recycle() throws InterruptedException {
        mutex.acquire();
        counter[0]--;
        if (counter[0] == 0) {
            for (int k = 0; k < n; k++) {
                barrier2.release();
            }
        }
        mutex.release();
        barrier2.acquire();
    }



}
