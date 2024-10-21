
class ClientEagerTest{
    public static void main(String[] args) {
        EagerInitialization eager1 = EagerInitialization.getInstance();
        eager1.setName("Eager1");
        System.out.println("Eager1: " + eager1.getName());

        EagerInitialization eager2 = EagerInitialization.getInstance();
        //eager1
        System.out.println("Eager2: " + eager2.getName());

        //cả 2 đều trả về Eager1 => Đối tượng chỉ tạo 1 lần duy nhất
    }
}

class EagerInitialization {
    // thể hiện class duy nhất
   private static final EagerInitialization INSTANCE = new EagerInitialization();

   private EagerInitialization(){}

    public static EagerInitialization getInstance(){
        return INSTANCE;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class LazyInitialization {
    private static LazyInitialization INSTANCE;
    private LazyInitialization() {
    }
    public static LazyInitialization getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new LazyInitialization();
        }
        return INSTANCE;
    }
}

class ThreadSafeSingleton {
    private static ThreadSafeSingleton INSTANCE;

    private ThreadSafeSingleton() {
    }

    public static synchronized ThreadSafeSingleton getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ThreadSafeSingleton();
        }
        return INSTANCE;
    }
}

class DoubleCheckedLockingSingleton {
    private static volatile DoubleCheckedLockingSingleton INSTANCE;
    private DoubleCheckedLockingSingleton(){
    }

    private static DoubleCheckedLockingSingleton getInstance() {
        if(INSTANCE == null){
            synchronized (DoubleCheckedLockingSingleton.class){
                if (INSTANCE == null) {
                    INSTANCE = new DoubleCheckedLockingSingleton();
                }
            }
        }
        return INSTANCE;
    }

}



