package models.products;

public class Clothing extends Product {
    public Clothing(double price, String name, String description) {
        super(price, name, description);
    }

    @Override
    public String getType() {
        return "CLOTHES";
    }
}
