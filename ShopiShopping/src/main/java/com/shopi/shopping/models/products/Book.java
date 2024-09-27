package main.java.com.shopi.shopping.models.products;

public class Book extends Product {

    public Book(double price, String name, String description) {
        super(price, name, description);
    }

    @Override
    public String getType() {
        return "LIBRARY";
    }
}

