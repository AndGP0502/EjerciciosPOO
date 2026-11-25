package modelo;

public class ArchivoImagen extends Archivo {
    private String resolucion;

    public ArchivoImagen(String nombre, double tamanoMB, String resolucion) {
        super(nombre, tamanoMB);
        this.resolucion = resolucion;
    }

    // Al procesar la imagen se generan miniaturas: 1.5 veces el tamaño original
    @Override
    public double calcularEspacioProcesado() {
        return getTamanoMB() * 3 / 2;
    }

    @Override
    public String mostrarInformacion() {
        return "[Imagen] " + super.mostrarInformacion() + " | Resolucion: " + resolucion;
    }
}
