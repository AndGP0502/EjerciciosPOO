public class Videojuego {

        // Atributos public
        public String nombre;
        public String genero;

        // Atributos con acceso por defecto (sin modificador)
        String version;
        boolean activo;

        // Método public: muestra todos los datos del juego
        public void mostrarInformacion() {
            System.out.println("Nombre: " + nombre);
            System.out.println("Género: " + genero);
            System.out.println("Versión: " + version);
            System.out.println("Activo: " + activo);
        }

        // Método public: iniciar el juego
        public void iniciar() {
            activo = true;
            System.out.println(nombre + " ya empezó.");
        }

        // Método con acceso por defecto para cerrar el juego
        void cerrar() {
            activo = false;
            System.out.println(nombre + " se acabó.");
        }

        // Método con acceso por defecto
        void mostrarVersion() {
            System.out.println("Versión de " + nombre + ": " + version);
        }

}
