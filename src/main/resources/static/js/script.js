document.getElementById('add-to-cart').addEventListener('click', function() {
    const productId = this.getAttribute('data-product-id');
    const cartId = 1; // Asegúrate de establecer esto dinámicamente si es necesario
    const customerId = document.getElementById('customer-id').value; // Obtener el customerId desde el campo oculto

    fetch(`/api/shopping-carts/${cartId}/products/${productId}?customerId=${customerId}`, {
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
        console.log('Producto añadido al carrito:', data);
    })
    .catch(error => {
        console.error('Error al agregar producto al carrito:', error);
    });
});
document.getElementById('checkout-button').addEventListener('click', function() {
    const cartId = document.querySelector('input[name="cartId"]').value; // Obtener cartId
    const customerId = document.querySelector('input[name="customerId"]').value; // Obtener customerId

    // Verificar que ambos ID están disponibles
    if (cartId && customerId) {
        // Enviar el formulario
        document.getElementById('checkout-form').submit();
    } else {
        alert("Cart ID or Customer ID is missing.");
    }
});


