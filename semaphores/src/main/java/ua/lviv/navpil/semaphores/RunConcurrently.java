package ua.lviv.navpil.semaphores;

import ua.lviv.navpil.semaphores.basic.Barrier;

import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RunConcurrently {

    public static void runAll(Collection<Runnable> runnables) throws InterruptedException {
        runAll(runnables, runnables.size());
    }
    public static void runAll(Collection<Runnable> runnables, int threadpoolSize) throws InterruptedException {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(threadpoolSize);

        for (Runnable runnable : runnables) {
            scheduledExecutorService.schedule(runnable, 1, TimeUnit.MILLISECONDS);
        }

        scheduledExecutorService.shutdown();
        scheduledExecutorService.awaitTermination(2, TimeUnit.SECONDS);

    }
}
