package modelo;

public class ArchivoTexto extends Archivo {
    private int cantidadPalabras;

    public ArchivoTexto(String nombre, double tamanoMB, int cantidadPalabras) {
        super(nombre, tamanoMB);
        this.cantidadPalabras = cantidadPalabras;
    }

    // El texto se comprime: ocupa la mitad del tamaño original
    @Override
    public double calcularEspacioProcesado() {
        return getTamanoMB() / 2;
    }

    @Override
    public String mostrarInformacion() {
        return "[Texto] " + super.mostrarInformacion() + " | Palabras: " + cantidadPalabras;
    }
}
