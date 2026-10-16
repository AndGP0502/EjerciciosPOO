public class Sensor {

    private int id;
    private String nombre;
    private String tipo;
    private double lectura;

    // Contador compartido por toda la clase
    private static int contador = 0;

    public Sensor(String nombre, String tipo){
        // Cada sensor nuevo suma al contador y toma ese número como ID
        contador++;
        this.id = contador;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public int getId(){
        return id;
    }

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getTipo(){
        return tipo;
    }

    public void setTipo(String tipo){
        this.tipo = tipo;
    }

    public double getLectura(){
        return lectura;
    }

    public void setLectura(double lectura){
        // Validación: la lectura no puede ser negativa
        if(lectura >= 0) {
            this.lectura = lectura;
        } else {
            System.out.println("La lectura debe ser 0 o mayor que 0");
        }
    }

    public void mostrarInformacion(){
        System.out.println("ID: " + id);
        System.out.println("Nombre: " + nombre);
        System.out.println("Tipo: " + tipo);
        System.out.println("Lectura: " + lectura);
    }

    public static int getTotalCreados(){
        return contador;
    }

    public static void mostrarTotal(){
        System.out.println("Total: " + getTotalCreados());
    }
}