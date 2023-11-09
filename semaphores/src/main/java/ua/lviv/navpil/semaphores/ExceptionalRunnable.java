package ua.lviv.navpil.semaphores;

public interface ExceptionalRunnable {

    void run() throws InterruptedException;

    static Runnable wrap(ExceptionalRunnable e) {
        return () -> {
            try {
                e.run();
            } catch (InterruptedException ex) {
            }
        };
    }

}
