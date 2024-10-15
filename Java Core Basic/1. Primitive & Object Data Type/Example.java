public class Example {
    public static void main(String[] args) {
        //primitive
        int x = 5;
        int y = 10;
        double z = 3.123;
        System.out.println("x = " + x + " y = " + y + " z = " + z);

        //object
        Dog dog = new Dog(); //create a new Dog object
        dog.bark();

        //boxing
        Integer obj = x; // Autoboxing: chuyển int thành Integer

        //unboxing
        int value = obj;

        //so sánh primitive với object wrapper
        if(x == obj){
            System.out.println("x == obj");
        }else{
            System.out.println("x != obj");
        }

        //so sánh primitive với object
        /* trường hợp này luôn sai, primitive ko thể so sánh với object
        if(x == dog) {
            System.out.println("x == dog");
        }
        */

    }
}

class Dog{
    String name;
    int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Dog() {
    }

    public Dog(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public void bark() {
        System.out.println("Woof");
    }
}