package ua.lviv.navpil.semaphores;

import java.util.ArrayList;
import java.util.List;

public class ConcurrentRunner {


    private final List<Runnable> list = new ArrayList<>();
    private int i;

    public ConcurrentRunner(int i) {
        this.i = i;
    }
    public ConcurrentRunner() {

    }

    public static ConcurrentRunner create() {
        return new ConcurrentRunner();
    }

    public ConcurrentRunner add(Runnable runnable) {
        list.add(runnable);
        return this;
    }

    public void start() throws InterruptedException {
        RunConcurrently.runAll(list, i == 0 ? list.size() : i);
    }

}
