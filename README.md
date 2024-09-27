# Sistema de Carrito de Compras


## Carpeta models
- **Clases**: Contiene las clases `Customer`, `ShoppingCart` y `Product`, que utilizan contadores estáticos para gestionar la asignación de IDs a clientes y productos, asegurando que cada uno tenga un identificador único a lo largo de la aplicación.
- **Interfaz `ShoppingCartInterface`**: Define los métodos que utiliza `ShoppingCartServices` para realizar operaciones relacionadas con el carrito de compras.

## Carpeta services
- **Clase `ShoppingCartServices`**: Esta clase extiende de `ShoppingCartInterface` e implementa la lógica del carrito de compras. Incluye métodos para agregar productos, ver productos en el carrito, calcular totales y filtrar productos según criterios específicos, facilitando así la interacción del usuario con el sistema.

## Clase `ShoppingApp`
- Esta clase instancia los objetos y prueba los métodos implementados, sirviendo como punto de entrada para la aplicación y facilitando la interacción con el sistema.
