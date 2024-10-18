// Evento para agregar producto al carrito
document.getElementById('add-to-cart').addEventListener('click', function() {
    const productId = this.getAttribute('data-product-id');
    const cartId = 1;
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

// Evento para eliminar producto del carrito
document.querySelectorAll('.remove-from-cart').forEach(form => {
    form.addEventListener('submit', function(event) {
        event.preventDefault(); // Prevenir el envío predeterminado del formulario

        const productId = this.querySelector('input[name="productId"]').value;
        const cartId = this.querySelector('input[name="cartId"]').value;
        const customerId = this.querySelector('input[name="customerId"]').value; // Obtener el customerId

        // Enviar una solicitud para eliminar el producto del carrito
        fetch(`/cart/remove?productId=${productId}&cartId=${cartId}&customerId=${customerId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log('Producto eliminado del carrito:', data);
            location.reload(); // Recargar la página para ver los cambios
        })
        .catch(error => {
            console.error('Error al eliminar producto del carrito:', error);
        });
    });
});

// Evento para proceder al pago
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
