package ua.lviv.navpil.semaphores.basic;

import ua.lviv.navpil.semaphores.ConcurrentRunner;
import ua.lviv.navpil.semaphores.UnsafeNumberedRunnable;

import java.util.concurrent.Semaphore;

/*
Solution in the book is more complicated.
Mine also works.
I don't know what the complication in the books brings.
 */
public class QueueFollowersLeaders {

    public static void main(String[] args) throws InterruptedException {

        Semaphore leaders = new Semaphore(0);
        Semaphore followers = new Semaphore(0);

        Semaphore leadersMutex = new Semaphore(1);
        Semaphore followersMutex = new Semaphore(1);

        ConcurrentRunner concurrentRunner = new ConcurrentRunner();

        for (int i = 0; i < 10; i++) {
            //leaders
              concurrentRunner.add(new UnsafeNumberedRunnable(i) {
                  @Override
                  public void unsafeRun() throws InterruptedException {
                      leadersMutex.acquire();
                      followers.release();
                      leaders.acquire();
                      System.out.println("I'm leader: " + getNumber() + " dancing");
                      leadersMutex.release();
                  }
              });
        }
        for (int i = 0; i < 10; i++) {
              concurrentRunner.add(new UnsafeNumberedRunnable(i) {
                  @Override
                  public void unsafeRun() throws InterruptedException {
                      followersMutex.acquire();
                      leaders.release();
                      followers.acquire();
                      System.out.println("I'm follower: " + getNumber() + " dancing");
                      followersMutex.release();
                  }
              });
        }

        concurrentRunner.start();

    }
}
