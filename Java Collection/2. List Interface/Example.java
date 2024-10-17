import java.util.*;

class ListExample {
    public static void main(String[] args) {
        // Tạo danh sách bằng cách sử dụng List Interface (dùng ArrayList làm đối tượng)
        List<String> fruits = new ArrayList<>();

        // Thêm phần tử vào List
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Mango");

        // Duyệt qua các phần tử trong List
        System.out.println("Các loại trái cây:");
        for (String fruit : fruits) {
            System.out.println(fruit);
        }

        // Xóa phần tử
        fruits.remove("Banana");
        System.out.println("Sau khi xóa Banana: " + fruits);
    }
}

class ArrayListExample {
    public static void main(String[] args) {
        ArrayList<Integer> numbers = new ArrayList<>();
        // Thêm phần tử vào ArrayList
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);

        // Truy cập phần tử theo chỉ số
        System.out.println("Phần tử thứ 2: " + numbers.get(1)); // 2

        // Cập nhật phần tử
        numbers.set(1, 5);
        System.out.println("Sau khi cập nhật: " + numbers);

        // Xóa phần tử
        numbers.remove(0);
        System.out.println("Sau khi xóa phần tử đầu tiên: " + numbers);
    }

}

class LinkedListExample {
    public static void main(String[] args) {
        // Tạo LinkedList bằng cách sử dụng LinkedList Interface
        LinkedList<String> animals = new LinkedList<>();

        // Thêm phần tử vào LinkedList
        animals.add("Cat");
        animals.add("Dog");
        animals.add("Elephant");

        // Duyệt qua các phần tử trong LinkedList
        System.out.println("Danh sách động vật: " + animals);

        // Xóa phần tử
        animals.remove("Dog");
        System.out.println("Sau khi xóa phần tử Dog: " + animals);
    }
}

class VectorExample {
    public static void main(String[] args) {
        Vector<String> cities = new Vector<>();

        // Thêm phần tử vào Vector
        cities.add("Ha Noi");
        cities.add("Ho Chi Minh");
        cities.add("Hai Phong");

        // Duyệt qua Vector
        System.out.println("Danh sách thành phố: " + cities);

        // Kiểm tra kích thước và dung lượng của Vector
        System.out.println("Kích thước: " + cities.size());
        System.out.println("Dung lượng: " + cities.capacity());
    }
}

class StackExample {
    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>();

        // Thêm phần tử vào Stack
        stack.push(10);
        stack.push(20);
        stack.push(30);

        // Duyệt qua Stack
        System.out.println("Stack: " + stack);

        // Lấy và xóa phần tử đầu tiên (LIFO - Last In First Out)
        System.out.println("Phần tử trên đỉnh Stack: " + stack.peek());
        System.out.println("Lấy phần tử ra khỏi Stack: " + stack.pop());

        // Kiểm tra Stack sau khi pop
        System.out.println("Stack còn lại: " + stack);

    }
}