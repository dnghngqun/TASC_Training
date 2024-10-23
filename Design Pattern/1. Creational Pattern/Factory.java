
// Interface cho sản phẩm
interface Product {
    void showInfo();
}

// Các sản phẩm cụ thể
class ProductA implements Product {
    public void showInfo() {
        System.out.println("This is Product A");
    }
}

class ProductB implements Product {
    public void showInfo() {
        System.out.println("This is Product B");
    }
}

// Factory Method
abstract class Creator {
    public abstract Product createProduct();
}

// Các creator cụ thể
class CreatorA extends Creator {
    public Product createProduct() {
        //  Đinh nghia doi tuong cu the
        return new ProductA();
    }
}

class CreatorB extends Creator {
    public Product createProduct() {
        return new ProductB();
    }
}

// Main để kiểm tra
 class Factory {
    public static void main(String[] args) {
        Creator creatorA = new CreatorA();
        Product productA = creatorA.createProduct();
        productA.showInfo();

        Creator creatorB = new CreatorB();
        Product productB = creatorB.createProduct();
        productB.showInfo();
    }
}
