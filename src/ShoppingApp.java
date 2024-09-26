import models.Customer;
import factories.ProductFactory;
import models.ShoppingCart;
import services.ShoppingCartServices;

import java.time.LocalDate;

public class ShoppingApp {
    public static void main(String[] args) {
        // Create customer using the Builder
        Customer customer = new Customer.CustomerBuilder("John", "Smith")
                .setBirthDate(LocalDate.of(1990, 1, 1))
                .setEmail("john@gmail.com")
                .setPhone("123456789")
                .setIdentificationNumber("DNI123")
                .build();

        // Create cart
        ShoppingCart cart = new ShoppingCart(customer);

    // Add products to the cart

    // ELECTRONIC Products
        cart.getProducts().add(ProductFactory.createProduct("ELECTRONICS", "CPU I5", 200.00));
        cart.getProducts().add(ProductFactory.createProduct("ELECTRONICS", "CPU I7", 500.40));
        cart.getProducts().add(ProductFactory.createProduct("ELECTRONICS", "GPU RTX 3060", 150.00));

    // LIBRARY Products
        cart.getProducts().add(ProductFactory.createProduct("BOOK", "Harry Potter", 15.40));
        cart.getProducts().add(ProductFactory.createProduct("BOOK", "1984", 20.30));
        cart.getProducts().add(ProductFactory.createProduct("BOOK", "Java 8", 120.00));

    // OTHER Products
        cart.getProducts().add(ProductFactory.createProduct("CLOTHING", "T-shirt", 12.40));
        cart.getProducts().add(ProductFactory.createProduct("CLOTHING", "Mug", 5.99));
        cart.getProducts().add(ProductFactory.createProduct("CLOTHING", "Backpack", 30.00));


        // Instantiate ShoppingCartServices
        ShoppingCartServices shoppingCartService = new ShoppingCartServices();

        // Call methods
        shoppingCartService.printLibraryProductsWithPriceOver100(cart);

        System.out.println();

        double totalPrice = shoppingCartService.calculateTotalPrice(cart);
        System.out.println("Total sum of prices of all products: " + totalPrice);

        System.out.println();

        double totalElectronics = shoppingCartService.calculateTotalPriceForElectronicProducts(cart);
        System.out.println("Total sum of prices of electronic products: " + totalElectronics);

        System.out.println();

        shoppingCartService.printCartInfoSortedByPrice(cart);
    }
}
