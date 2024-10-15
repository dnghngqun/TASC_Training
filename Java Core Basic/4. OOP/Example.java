
class Animal{
    private int age; // tính đóng gói, class ngoài không thể truy cập biến age mà phải qua get set
    private int weight;

    final void sleep(){
        System.out.println("sleep");
    }

    public static void run(){
        System.out.println("run");
    }


    public void walk(){
        System.out.println("walk");
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}

interface Action{
    void sound();
}

class Dog extends Animal implements Action{

    @Override
    public void walk() {
        System.out.println("Dog walk"); //tính trừu tượng
    }

   @Override
    public void sound() {
        System.out.println("Gâu gâu");
    }

    /* Không thể override method static
    public void run(){
        System.out.println("Dog run");
    }
    */

}

class Cat extends Animal implements Action, Action2{

    //nếu implement 2 interface cùng phương thức nhưng khác kiểu dữ liệu sẽ lỗi
    @Override
    public void sound() {
        System.out.println("Meo meo");
    }

    public Cat() {
        super();//gọi constructor lớp cha
    }
}



interface Action2{
//    String sound();
}

public class Example {
    public static void main(String[] args) {
        Dog dog = new Dog();
        Cat cat = new Cat();
        dog.sleep();// class dog vẫn kế thừa method final sleep() trong lớp Animal
        dog.sound();
        cat.sound();
    }

}
