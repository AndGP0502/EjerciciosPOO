public class Producto {

    private String nombre;
    private double precio;
    private int stock;


    public String getNombre(){

        return nombre;
    }
    public double getPrecio(){

        return precio;
    }
    public int getStock(){

        return stock;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public void setPrecio(double precio){

        if(validarPrecio(precio)){
            this.precio = precio;
        }
        else
            System.out.println("Ese valor no es válido");
    }
    public void setStock(int stock){
        this.stock = stock;
    }

    public Producto(String nombre, double precio, int stock){

        this.nombre = nombre;
        setPrecio(precio);
        setStock(stock);
    }

    public boolean validarPrecio(double precio){

        if(precio > 0){

            return true;
        }
        else
            return false;
    }

    private double calcularPrecioConDescuento(){

        return precio - (precio * 0.10);
    }


    public void mostrarInfo(){

        System.out.println("Nombre: " + nombre + " " + "precio: " + precio + " " + "Stock: " + stock);


    }
}
