package modelo;

// Clase padre: datos comunes de todos los archivos
public class Archivo {
    private String nombre;
    private double tamanoMB;

    public Archivo(String nombre, double tamanoMB) {
        this.nombre = nombre;
        this.tamanoMB = tamanoMB;
    }

    public String getNombre() {
        return nombre;
    }

    public double getTamanoMB() {
        return tamanoMB;
    }

    // Por defecto el espacio procesado es igual al tamaño original
    public double calcularEspacioProcesado() {
        return tamanoMB;
    }

    public String mostrarInformacion() {
        return "Archivo: " + nombre + " | Tamano: " + tamanoMB
                + " MB | Espacio procesado: " + calcularEspacioProcesado() + " MB";
    }
}
