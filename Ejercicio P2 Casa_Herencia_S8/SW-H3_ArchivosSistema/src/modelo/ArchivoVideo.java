package modelo;

public class ArchivoVideo extends Archivo {
    private int duracionMinutos;

    public ArchivoVideo(String nombre, double tamanoMB, int duracionMinutos) {
        super(nombre, tamanoMB);
        this.duracionMinutos = duracionMinutos;
    }

    // Al codificar el video se necesitan 3 veces el tamaño original
    @Override
    public double calcularEspacioProcesado() {
        return getTamanoMB() * 3;
    }

    @Override
    public String mostrarInformacion() {
        return "[Video] " + super.mostrarInformacion() + " | Duracion: " + duracionMinutos + " min";
    }
}
