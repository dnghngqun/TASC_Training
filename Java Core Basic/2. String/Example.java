public class Example {

    public static void main(String[] args) {
        String newText = "Welcome";
        String newText2 = "Welcome";
        // newText2 được gán cùng giá trị, nên tái sử dụng đối tượng Spring Pool thay vì tạo mới

        //so sánh dùng "=="
        System.out.println(newText == newText2); // true

        //so sánh dùng equals
        System.out.println(newText.equals(newText2)); // true

        //so sánh dùng compareTo
        System.out.println(newText.compareTo(newText2)); // 0


        //Stringbuilder
        StringBuilder sb = new StringBuilder("Hello");

        // Thêm chuỗi vào cuối
        sb.append(" World");
        System.out.println(sb);  // Hello World

        // Chèn chuỗi vào vị trí chỉ định
        sb.insert(6, "Java ");
        System.out.println(sb);  // Hello Java World

        // Xóa ký tự tại một đoạn cụ thể
        sb.delete(5, 10);
        System.out.println(sb);  // Hello World

        // Đảo ngược chuỗi
        sb.reverse();
        System.out.println(sb);  // dlroW olleH

        //StringBuffer
        StringBuffer sb1 = new StringBuffer("Hello");

        // Thêm chuỗi vào cuối
        sb1.append(" World");
        System.out.println(sb1);  // Hello World

        // Chèn chuỗi vào vị trí chỉ định
        sb1.insert(6, "Java ");
        System.out.println(sb1);  // Hello Java World

        // Xóa chuỗi con
        sb1.delete(5, 10);
        System.out.println(sb1);  // Hello World

        // Đảo ngược chuỗi
        sb1.reverse();
        System.out.println(sb1);  // dlroW olleH





    }
}
