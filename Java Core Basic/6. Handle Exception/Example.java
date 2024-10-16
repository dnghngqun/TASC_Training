import java.io.FileReader;
import java.io.IOException;


class ExampleCustomException extends Exception {
    public ExampleCustomException(String message) {
        super(message);
    }
}

public class Example {

    //throw
    private void sumNumber(int a, int b) {
        //try catch
        try{
            System.out.println(a+b);
        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }

    private void readFile(){
        // try-with-resources
        try (FileReader fr = new FileReader("file.txt")) {
            //chưa làm gì cạ
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void divideNumber(int a, int b) throws ArithmeticException { //ném ra ngoại lệ cho method gọi nó xử lý
        if(b == 0) {
            throw new ArithmeticException("b cannot be 0"); //throw
        }else {
            System.out.println(a/b);
        }
    }


    // custom exception
    public void validateAge (int age) throws ExampleCustomException {
        if(age < 0){
            throw new ExampleCustomException("Invalid age!!!");
        }else{
            System.out.println("Age is valid");
        }
    }
    public static void main (String[] args){
        Example example = new Example();
        try {
            example.divideNumber(1, 0);
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }
    }
}
