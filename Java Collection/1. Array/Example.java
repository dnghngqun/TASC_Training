public class Example {
    public static void main(String[] args) {
        // Khai báo mảng kiểu int
        int[] numbers;  // Cách 1
        int numbers1[];  // Cách 2

        // Khởi tạo mảng với kích thước 5
        numbers = new int[5];

        // Hoặc khai báo và khởi tạo cùng lúc
        int[] numbers2 = new int[5];

        // Khởi tạo mảng với giá trị
        int[] numbers3 = {1, 2, 3, 4, 5};

        int firstNumber = numbers3[0];  // Lấy giá trị đầu tiên trong mảng

        //lặp mảng
        for(int i : numbers3) {
            System.out.println(i);
        }
    }

}
