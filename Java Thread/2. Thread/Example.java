public class Example {

}

class ThreadExtendExample extends  Thread{
    @Override
    public void run() {
        System.out.println("Start thread");
        long sum = 0L;
        for(long i  = 0L; i < 100000000L; i++){
            sum += i;
        }
        System.out.println("End thread");
        System.out.println("Sum = " + sum);
    }

    public static void main(String[] args) {
        int MAX = 50;

        for (int i = 0; i < MAX; i++) {
            ThreadExtendExample t = new ThreadExtendExample();
            t.start();
        }
        System.out.println("Finish");
    }

}


class ThreadImplementExample implements Runnable{
    @Override
    public void run() {
        System.out.println("Start thread");
        long sum = 0L;
        for(long i  = 0L; i < 100000000L; i++){
            sum += i;
        }
        System.out.println("End thread");
        System.out.println("Sum = " + sum);
    }

    public static void main(String[] args) {
        int MAX = 50;

        for (int i = 0; i < MAX; i++) {
            ThreadImplementExample t = new ThreadImplementExample();
            Thread t1 = new Thread(t);
            t1.start();
        }
        System.out.println("Finish");
    }
}

class TheadLambdaExample{
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            System.out.println("Start thread");
            long sum = 0L;
            for(long i  = 0L; i < 100000000L; i++){
                sum += i;
            }
            System.out.println("End thread");
            System.out.println("Sum = " + sum);
        });
        t.start();
    }
}

class ThreadAnonymousExample{
    public static void main(String[] args) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Start thread");
                long sum = 0L;
                for(long i  = 0L; i < 100000000L; i++){
                    sum += i;
                }
                System.out.println("End thread");
                System.out.println("Sum = " + sum);
            }
        });
        t.start();
    }
}

//callable and future

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class MyCallable implements Callable<String> {
    private String taskName;

    public MyCallable(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public String call() throws Exception {
        // Giả lập một tác vụ mất thời gian
        Thread.sleep(2000);  // Dừng trong 2 giây
        return taskName + " completed!";
    }
}

public class CallableFutureExample {
    public static void main(String[] args) throws Exception {
        // Tạo một thread pool với 2 thread
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Tạo 2 tác vụ Callable
        MyCallable task1 = new MyCallable("Task 1");
        MyCallable task2 = new MyCallable("Task 2");

        // Gửi các tác vụ tới thread pool và nhận lại Future
        Future<String> future1 = executor.submit(task1);
        Future<String> future2 = executor.submit(task2);

        // Lấy kết quả của các tác vụ
        System.out.println("Waiting for task results...");

        // Sử dụng get() để chặn cho đến khi task hoàn thành và lấy kết quả
        String result1 = future1.get();  // Chờ cho task1 hoàn thành
        String result2 = future2.get();  // Chờ cho task2 hoàn thành

        // In ra kết quả
        System.out.println(result1);  // Task 1 completed!
        System.out.println(result2);  // Task 2 completed!

        // Đóng ExecutorService
        executor.shutdown();
    }
}