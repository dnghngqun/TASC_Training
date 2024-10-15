class StaticExample{
    static int count = 0;
    public static void increment(){
        count++;
        System.out.println("Count: " + count);
    }
}

final class FinalExample{
    public final double PI = 3.14;
    public int age = 18;


}

public class Example {
    public static void main(String[] args) {
        StaticExample.increment();
        final double PI = 3.14;
        System.out.println(PI);
        //PI = 3.1415;// false


        FinalExample finalExample = new FinalExample();
        //finalExample.PI = 3.1415;// false
        finalExample.age = 19; //true
        System.out.println(finalExample.age);
    }
}
