public class Main {

    public static void main(String[] args) {

        // Crear tres videojuegos
        Videojuego juego1 = new Videojuego();
        Videojuego juego2 = new Videojuego();
        Videojuego juego3 = new Videojuego();

        // Asignar valores diferentes a cada uno
        juego1.nombre = "Friv";
        juego1.genero = "Variado";
        juego1.version = "1.21";
        juego1.activo = false;

        juego2.nombre = "GTA 6";
        juego2.genero = "Mundo abierto";
        juego2.version = "2.0";
        juego2.activo = false;

        juego3.nombre = "Call of Duty";
        juego3.genero = "Battle Royale";
        juego3.version = "31.10";
        juego3.activo = false;

        // Mostrar los tres videojuegos
        System.out.println("=== juego 1 ");
        juego1.mostrarInformacion();
        System.out.println();

        System.out.println(" juego 2 ");
        juego2.mostrarInformacion();
        System.out.println();

        System.out.println(" juego 3 ");
        juego3.mostrarInformacion();
        System.out.println();

        // Usar los métodos solo con el primer videojuego
        System.out.println(" Se inicia solo el juego 1 ");
        juego1.iniciar();
        juego1.mostrarVersion();
        System.out.println();

        // Comprobar que cada objeto mantenga sus valores
        System.out.println("Estado para después de iniciar el juego 1");
        juego1.mostrarInformacion();
        System.out.println();
        juego2.mostrarInformacion();
        System.out.println();
        juego3.mostrarInformacion();
        System.out.println();

        // Cerrar el juego 1
        juego1.cerrar();
        juego1.mostrarInformacion();
    }
}