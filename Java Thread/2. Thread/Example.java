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