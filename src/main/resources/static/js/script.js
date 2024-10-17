<!-- HTML -->
<button id="add-to-cart" data-product-id="1">Add to Cart</button>

<script>
document.getElementById('add-to-cart').addEventListener('click', function() {
    const productId = this.getAttribute('data-product-id');
    const cartId = 1; // Replace this with the ID of the cart you want to use

    fetch(`/api/shopping-carts/${cartId}/products/${productId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        console.log('Product added to cart:', data);
        // Here you can update the UI to reflect the change
    })
    .catch(error => {
        console.error('Error adding product to cart:', error);
    });
});
<script>
// Checkout functionality: Redirect to checkout.html
document.getElementById('checkout-button').addEventListener('click', function() {
    const cartId = document.querySelector('input[name="cartId"]').value; // Get the cartId from the hidden input

    // Redirect to checkout page with cartId in the URL
    window.location.href = `/cart/checkout?cartId=${cartId}`;
});
</script>


