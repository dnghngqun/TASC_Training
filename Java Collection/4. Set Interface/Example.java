import java.util.*;

public class Example {
    public static void main(String[] args) {
        //student
        Student std1 = new Student("Nam", 20);
        Student std2 = new Student("Huy", 19);
        Student std3 = new Student("Quan", 17);
        Student std4 = new Student("Anh", 22);
        Student std5 = new Student("Duc", 25);



        //hashset
        HashSet<Student> set = new HashSet<>();

        //add
        set.add(std1);
        set.add(std2);
        set.add(std3);
        set.add(std4);
        set.add(std5);
        //add duplicate value
        set.add(std1);


        System.out.println("HashSet: ");
        for(Student std : set){
            System.out.println(std.toString());
        }


        //LinkedHashSet
        LinkedHashSet<Student> set2 = new LinkedHashSet<>();

        set2.add(std1);
        set2.add(std2);
        set2.add(std3);
        set2.add(std4);
        set2.add(std5);

        System.out.println("LinkedHashSet: ");
        for(Student std : set2){
            System.out.println(std.toString());
        }

        //TreeSet
        TreeSet<Integer> set3 = new TreeSet<>();

        set3.add(5);
        set3.add(3);
        set3.add(2);
        set3.add(1);
        set3.add(4);

        //add duplicate value
        set3.add(4);

        System.out.println("TreeSet: " + set3);


        //class student required comparable
        TreeSet<Student> set4 = new TreeSet<>();
        set4.add(std4);
        set4.add(std1);
        set4.add(std2);
        set4.add(std5);

        set4.add(std3);

        System.out.println("TreeSet: ");
        for(Student std : set4){
            System.out.println(std.toString());
        }

        Set<days> set7 = EnumSet.allOf(days.class);

        Iterator<days> iter = set7.iterator();
        while (iter.hasNext())
            System.out.println(iter.next());
    }

}

enum days {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
}

class Student implements Comparable<Student> {
    private String name;
    private int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

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

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public int compareTo(Student std) {
        return this.getName().compareTo(std.getName());
    }
}
