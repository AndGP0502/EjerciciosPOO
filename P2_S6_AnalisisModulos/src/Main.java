import modelo.Modulo;
import modelo.AnalizadorProyecto;

public class Main {

    public static void main(String[] args) {

        Modulo[] modulos = new Modulo[5];

        modulos[0] = new Modulo("Autenticación", "Java", 100, true);
        modulos[1] = new Modulo("Inventario", "Java", 75, false);
        modulos[2] = new Modulo("Facturación", "Python", 50, false);
        modulos[3] = new Modulo("Reportes", "Java", 100, true);
        modulos[4] = new Modulo("Base de Datos", "SQL", 25, false);

        AnalizadorProyecto analizador =
                new AnalizadorProyecto(modulos);

        System.out.println("=== MODULOS DEL PROYECTO ===");
        System.out.println(analizador.listarModulos());

        System.out.println("=== MODULOS TERMINADOS ===");
        System.out.println(analizador.contarTerminados());

        System.out.println("=== PROGRESO PROMEDIO ===");
        System.out.println(
                analizador.calcularProgresoPromedio() + "%"
        );

        System.out.println("=== BUSCAR MODULO ===");

        Modulo encontrado = analizador.buscarModulo("Autenticación");

        if (encontrado != null) {
            System.out.println(encontrado.mostrarInformacion());
        } else {
            System.out.println("Modulo no encontrado");
        }

        System.out.println("=== BUSCAR MODULO INEXISTENTE ===");

        Modulo inexistente = analizador.buscarModulo("Seguridad");

        if (inexistente != null) {
            System.out.println(inexistente.mostrarInformacion());
        } else {
            System.out.println("Modulo no encontrado");
        }

        System.out.println("=== MODULO CON MENOR PROGRESO ===");

        Modulo menor = analizador.obtenerMenorProgreso();

        if (menor != null) {
            System.out.println(menor.mostrarInformacion());
        }
    }
}
