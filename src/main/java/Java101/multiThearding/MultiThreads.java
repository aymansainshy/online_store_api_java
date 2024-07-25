package Java101.multiThearding;


public class MultiThreads {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = Thread.currentThread();

        System.out.println("Current Thread is : " + thread.getName());

        thread.setName("my_custom_thread");

        System.out.println("Current Thread is : " + thread.getName());

        Thread.sleep(30_000);
    }
}
