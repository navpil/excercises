package ua.lviv.navpil.semaphores.classic;

import ua.lviv.navpil.semaphores.ConcurrentRunner;
import ua.lviv.navpil.semaphores.UnsafeNumberedRunnable;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerConsumer {

    public static void main(String[] args) throws InterruptedException {

        ConcurrentRunner concurrentRunner = new ConcurrentRunner();
        EventFactory eventFactory = new EventFactory();
        Buffer<Event> buffer = new Buffer<>();

        for (int i = 0; i < 10; i++) {
            //producers
            concurrentRunner.add(new UnsafeNumberedRunnable(i) {
                @Override
                public void unsafeRun() throws InterruptedException {
                    for (int j = 0; j < 10; j++) {
                        Event event = eventFactory.waitForEvent();
                        buffer.add(event);
                        System.out.println("Added " + event);
                    }
                }
            });
        }

        for (int i = 0; i < 10; i++) {
            concurrentRunner.add(new UnsafeNumberedRunnable(i) {
                @Override
                public void unsafeRun() throws InterruptedException {
                    for (int j = 0; j < 10; j++) {
                        Thread.sleep(100);
                        Event event = buffer.get();
                        event.process();
                    }
                }
            });
        }

        concurrentRunner.start();
    }

    public static class Buffer<T> {

        private final Semaphore addGetSemaphore = new Semaphore(0);
        private final Semaphore mutex = new Semaphore(1);
        private final Semaphore maxBufferSize = new Semaphore(5);
        private final Queue<T> que = new LinkedList<>();

        public void add(T t) throws InterruptedException {
            maxBufferSize.acquire();
            mutex.acquire();
            que.add(t);
            mutex.release();
            addGetSemaphore.release();
        }

        public T get() throws InterruptedException {
            addGetSemaphore.acquire();
            mutex.acquire();
            T event = que.poll();
            mutex.release();
            maxBufferSize.release();
            return event;
        }

    }

    public static class EventFactory {
        private Random random = new Random();
        private AtomicInteger atomicInteger = new AtomicInteger(0);

        public Event waitForEvent() {
            try {
                Thread.sleep(random.nextInt(50));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return new Event("" + atomicInteger.incrementAndGet());
        }
    }

    public static class Event {

        private final String name;

        public Event(String name) {
            this.name = name;
        }

        public void process() {
            System.out.println("I'm processed: " + name);
        }

        @Override
        public String toString() {
            return "Event:" + name;
        }
    }


}
