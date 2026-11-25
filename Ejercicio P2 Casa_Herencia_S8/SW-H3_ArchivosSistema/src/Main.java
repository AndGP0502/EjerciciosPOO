import modelo.*;

public class Main {
    public static void main(String[] args) {
        RepositorioArchivos repositorio = new RepositorioArchivos();

        // Referencias de tipo Archivo que apuntan a objetos de las subclases
        Archivo a1 = new ArchivoTexto("informe.txt", 2.0, 1500);
        Archivo a2 = new ArchivoTexto("manual.txt", 4.0, 3200);
        Archivo a3 = new ArchivoImagen("logo.png", 5.0, "1920x1080");
        Archivo a4 = new ArchivoImagen("banner.jpg", 8.0, "3840x2160");
        Archivo a5 = new ArchivoVideo("demo.mp4", 100.0, 5);
        Archivo a6 = new ArchivoVideo("clase.mp4", 250.0, 45);
        Archivo a7 = new ArchivoAudio("podcast.mp3", 10.0, 12);
        Archivo a8 = new ArchivoAudio("entrevista.mp3", 30.0, 35);

        repositorio.agregarArchivo(a1);
        repositorio.agregarArchivo(a2);
        repositorio.agregarArchivo(a3);
        repositorio.agregarArchivo(a4);
        repositorio.agregarArchivo(a5);
        repositorio.agregarArchivo(a6);
        repositorio.agregarArchivo(a7);
        repositorio.agregarArchivo(a8);

        System.out.println("===== PRUEBA 1: Calculos diferentes =====");
        System.out.println(a1.getNombre() + " (Texto) -> " + a1.calcularEspacioProcesado() + " MB");
        System.out.println(a3.getNombre() + " (Imagen) -> " + a3.calcularEspacioProcesado() + " MB");
        System.out.println(a5.getNombre() + " (Video) -> " + a5.calcularEspacioProcesado() + " MB");
        System.out.println(a7.getNombre() + " (Audio) -> " + a7.calcularEspacioProcesado() + " MB");

        System.out.println();
        System.out.println("===== PRUEBA 2: Listado polimorfico =====");
        System.out.println("Archivos en el repositorio: " + repositorio.getCantidadArchivos());
        System.out.print(repositorio.listarArchivos());

        System.out.println();
        System.out.println("===== PRUEBA 3: Suma de espacio procesado =====");
        System.out.println("Espacio total procesado: " + repositorio.calcularEspacioTotalProcesado() + " MB");
    }
}
