package ua.lviv.navpil.semaphores;

public abstract class ThreadWaiter implements Runnable {

    private final int number;

    public ThreadWaiter(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void run() {
        try {
            unsafeRun();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void unsafeRun() throws InterruptedException;
}
