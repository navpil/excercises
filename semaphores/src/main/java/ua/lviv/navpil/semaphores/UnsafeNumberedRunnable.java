package ua.lviv.navpil.semaphores;

public abstract class UnsafeNumberedRunnable implements Runnable {

    private final int number;

    public UnsafeNumberedRunnable(int number) {
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
