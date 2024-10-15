
class Person{
    //tham chiếu String name được lưu trong stack
    public void hello(String name){
        String s = "Hello " + name;
        //String được lưu trong giá trị heap, s là tham chiếu được lưu trong stack
        System.out.println(s);
    }
}

public class Example {

    public static void main(String[] args) {
        int x = 10; //biến x được lưu vào bộ nhơ stack
        //Person được lưu vào bộ nhớ Heap, cứ new Person() là tạo đối tượng, và lưu tại heap
        //person là tham chiếu tới đối tượng, được lưu trong stack
        Person person = new Person();
        person.hello("Quan");
    }
}
