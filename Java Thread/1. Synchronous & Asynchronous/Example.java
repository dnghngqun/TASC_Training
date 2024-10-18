class Synchronous {
    public static void main(String[] args) {
        System.out.println("Tải dữ liệu...");
        // Chờ cho đến khi hoàn tất tải dữ liệu
        String data = downloadData();
        System.out.println("Dữ liệu đã tải: " + data);
    }

    private static String downloadData() {
        try {
            // Giả sử thời gian tải dữ liệu là 2s
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Dữ liệu";
    }

}

import java.util.concurrent.CompletableFuture;

class Asynchronous {

    public static void main(String[] args) {
        System.out.println("Bắt đầu tải dữ liệu...");
        CompletableFuture<String> futureData = CompletableFuture.supplyAsync(() -> downloadData());

        futureData.thenAccept(data -> {
            System.out.println("Dữ liệu đã tải: " + data);
        });

        System.out.println("Thực hiện các tác vụ khác...");
    }

    private static String downloadData() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Dữ liệu";
    }

}