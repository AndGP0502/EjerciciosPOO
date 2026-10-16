public class Sensor {

    String nombre;

    public String tipo;

    boolean lectura;


    public void mostrarNombre(String nombre){

        this.nombre = nombre;
        System.out.println("El nombre es: " + this.nombre);

    }

    public void mostrartipo(String tipo){

        this.tipo = tipo;

        System.out.println("El tipo es: " + this.tipo);
    }

    public void mostrarActivo(boolean lectura){

        this.lectura = lectura;

        System.out.println("La lectura es: " + this.lectura);
    }

    
}
