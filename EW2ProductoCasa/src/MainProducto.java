public class MainProducto {

     public static void main(String[] args) {

        Producto producto1 = new Producto();
        Producto producto2 = new Producto();

        producto1.categoria = "landing Pages";
        producto1.mostrarCategoria();

        producto2.categoria = "Software personalizados";
        producto2.mostrarCategoria();

        producto1.precio = 1200;
        producto2.precio = 3200;

        producto1.mostrarPrecio();
        producto2.mostrarPrecio();

        producto1.nombre = "Landing page de André";
        producto2.nombre = "Software personalizado de Miguel";

        producto1.mostrarNombre();
        producto2.mostrarNombre();


    }
}
