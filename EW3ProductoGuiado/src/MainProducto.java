public class MainProducto {


    public static void main(String[] args) {

       Producto pro1 = new Producto();

       pro1.setActivo(true);

       pro1.setNombre("Sensor de humedad");

       pro1.setTipo("Humedad");

       System.out.println("El nombre es: " + pro1.getNombre());

       System.out.println("El tipo es: " + pro1.getTipo());

       System.out.println("Está activo? " + pro1.isActivo());



       System.out.println("\nPruebas con valores inválidos:");
       pro1.setNombre("");
       pro1.setNombre(null);
       pro1.setTipo("");

       System.out.println("Nombre: " + pro1.getNombre());
       System.out.println("Tipo: " + pro1.getTipo());
    }


}
