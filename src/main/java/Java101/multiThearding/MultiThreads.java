package Java101.multiThearding;

import lombok.Data;

/**
* @Race_Condition
* A race condition in Java (and in concurrent programming in general) occurs
* when two or more threads can access shared data and they try to change it at the same time.
*/
public class MultiThreads {

    static class Counter {
        public int counter = 0;

        // Synchronized prevent increment() from triggered by multiple thread and the same time
        public synchronized void increment() {
            counter++;
//            System.out.println(counter++);
        }
    }


    public static void main(String[] args) throws InterruptedException {

        Counter counter = new Counter();

        Runnable runnable1 = () -> {
            for (int i = 1; i <= 1000; i++) {
                counter.increment();
            }
        };

        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                for (int i = 1; i <= 1000; i++) {
                    counter.increment();
                }
            }
        };


        Thread t1 = new Thread(runnable1);
        Thread t2 = new Thread(runnable2);

        t1.start();
        t2.start();

        // join() allow main thread to wait until t1 and t2 complete their task
        t1.join();
        t2.join();

        System.out.println(counter.counter);

        System.out.println("Hello world");


//        Thread thread = Thread.currentThread();
//
//        System.out.println("Current Thread is : " + thread.getName());
//
//        thread.setName("my_custom_thread");
//
//        System.out.println("Current Thread is : " + thread.getName());
//
//        Thread.sleep(30_000);
    }
}
