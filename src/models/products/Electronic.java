package models.products;

public class Electronic extends Product {

    public Electronic(double price, String name, String description) {
        super(price, name, description);
    }

    @Override
    public String getType() {
        return "ELECTRONIC";
    }
}