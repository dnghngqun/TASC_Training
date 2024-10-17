import java.util.*;

class PriorityQueueExample {
    public static void main(String[] args) {
        // Tạo hàng đợi ưu tiên (min-heap)
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        // Thêm phần tử vào hàng đợi
        pq.add(10);
        pq.add(20);
        pq.add(5);

        // Lấy và xóa phần tử theo thứ tự ưu tiên (số nhỏ nhất trước)
        System.out.println("Phần tử đầu tiên: " + pq.peek()); // 5
        System.out.println("Lấy và xóa phần tử: " + pq.poll()); // 5
        System.out.println("Hàng đợi sau khi xóa: " + pq); // [10, 20]
    }
}

class LinkedListQueueExample{
    public static void main(String[] args) {
        // Tạo lớp Queue
        Queue<Integer> queue = new LinkedList<>();

        // Thêm phần tử với lớp Queue
        queue.add(10);
        queue.add(20);
        queue.add(5);

        // Lọc phần tử theo thứ tự
        System.out.println("Phần tử đầu: " + queue.peek()); // 10
        System.out.println("Lọc phần tử: " + queue.poll()); // 10
        System.out.println("Hợp đồi sau khi lọc: " + queue); // [20, 5]
    }
}

class LinkedListDequeExample{
    public static void main(String[] args) {
        Deque<String> deque = new LinkedList<>();

        // Thêm phần tử vào đầu và cuối
        deque.addFirst("A");
        deque.addLast("B");
        deque.addLast("C");

        System.out.println("Deque ban đầu: " + deque); // [A, B, C]

        // Lấy và xóa phần tử đầu và cuối
        System.out.println("Phần tử đầu tiên: " + deque.pollFirst()); // A
        System.out.println("Phần tử cuối cùng: " + deque.pollLast()); // C

        System.out.println("Deque sau khi xóa: " + deque); // [B]
    }
}

class ArrayDequeExample{
    public static void main(String[] args) {
        ArrayDeque<String> deque = new ArrayDeque<>();

        // Thêm phần tử vào đầu và cuối
        deque.addFirst("A");
        deque.addLast("B");
        deque.addLast("C");

        System.out.println("Deque ban đầu: " + deque); // [A, B, C]

        // Lấy và xóa phần tử đầu và cuối
        System.out.println("Phần tử đầu tiên: " + deque.pollFirst()); // A
        System.out.println("Phần tử cuối cùng: " + deque.pollLast()); // C

        System.out.println("Deque sau khi xóa: " + deque); // [B]
    }
}