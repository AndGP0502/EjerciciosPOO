package modelo;

// Desafío: se agrega esta clase sin modificar las demás
public class ArchivoAudio extends Archivo {
    private int duracionMinutos;

    public ArchivoAudio(String nombre, double tamanoMB, int duracionMinutos) {
        super(nombre, tamanoMB);
        this.duracionMinutos = duracionMinutos;
    }

    // Al procesar el audio se necesita 1.2 veces el tamaño original
    @Override
    public double calcularEspacioProcesado() {
        return getTamanoMB() * 12 / 10;
    }

    @Override
    public String mostrarInformacion() {
        return "[Audio] " + super.mostrarInformacion() + " | Duracion: " + duracionMinutos + " min";
    }
}
