import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class Example {
    public static void main(String[] args) {
        //hashmap
        HashMap<String, Integer> marks = new HashMap<>();
        marks.put("Hoa", 10);
        marks.put("Quang", 9);
        marks.put("Thao", 7);
        marks.put("Thu", null);
        marks.put("Minh", null);
        marks.put(null, 10);

        //add duplicate
        marks.put("Hoa", 10);

        System.out.println("HashMap: "+marks);

        //LinkedHashMap
        LinkedHashMap<String, Integer> marks2 = new LinkedHashMap<>();
        marks2.put("Hoa", 10);
        marks2.put("Quang", 9);
        marks2.put("Thao", 7);
        marks2.put("Thu", null);
        marks2.put("Minh", 9);
        marks2.put(null, 10);


        //add duplicate
        marks.put("Hoa", 10);
        System.out.println("LinkedHashMap: "+marks2);

        TreeMap<String, Integer> marks3 = new TreeMap<>();
        marks3.put("Hoa", 10);
        marks3.put("Quang", 9);
        marks3.put("Thao", 7);
        marks3.put("Anh", 8);
        marks3.put("Minh", 9);
//        marks3.put(null, 10); //error

        System.out.println("TreeMap: " + marks3);

        //hashtable
        Hashtable<String, Integer> marks4 = new Hashtable<>();
        marks4.put("Hoa", 10);
        marks4.put("Quang", 9);
        marks4.put("Thao", 7);
        marks4.put("Anh", 8);
        marks4.put("Minh", 9);
        //marks4.put("Minh", null);//error
        //marks4.put(null, 10); //error
        System.out.println("Hashtable: " + marks4);

        //concurrent hashmap
        ConcurrentHashMap<String, Integer> marks5 = new ConcurrentHashMap<>();
        marks5.put("Hoa", 10);
        marks5.put("Quang", 9);
        marks5.put("Thao", 7);
        marks5.put("Anh", 8);
        marks5.put("Minh", 9);
        //marks5.put("Minh", null);//error
        //marks5.put(null, 10); //error
        System.out.println("ConcurrentHashMap: " + marks5);


    }


}

