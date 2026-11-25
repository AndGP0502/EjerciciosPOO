
import modelo.VersionSoftware;
import modelo.AnalizadorVersiones;

public class Main {

    public static void main(String[] args) {

        VersionSoftware[] versiones = new VersionSoftware[5];

        versiones[0] = new VersionSoftware(
                "1.0", "10/01/2026", true, 15);

        versiones[1] = new VersionSoftware(
                "1.1", "15/02/2026", true, 20);

        versiones[2] = new VersionSoftware(
                "1.2", "20/03/2026", false, 35);

        versiones[3] = new VersionSoftware(
                "2.0", "10/05/2026", true, 50);

        versiones[4] = new VersionSoftware(
                "2.1", "15/06/2026", false, 30);

        AnalizadorVersiones analizador =
                new AnalizadorVersiones(versiones);

        System.out.println("=== VERSIONES REGISTRADAS ===");

        for (int i = 0; i < versiones.length; i++) {
            if (versiones[i] != null) {
                System.out.println(versiones[i].mostrarInformacion());
            }
        }

        System.out.println("\n=== VERSIONES ESTABLES ===");
        System.out.println(analizador.contarEstables());

        System.out.println("\n=== TOTAL DE CAMBIOS ===");
        System.out.println(analizador.sumarCambios());

        System.out.println("\n=== BUSCAR VERSION EXISTENTE ===");

        VersionSoftware encontrada =
                analizador.buscarVersion("1.2");

        if (encontrada != null) {
            System.out.println(encontrada.mostrarInformacion());
        } else {
            System.out.println("Version no encontrada");
        }

        System.out.println("\n=== BUSCAR VERSION INEXISTENTE ===");

        VersionSoftware inexistente =
                analizador.buscarVersion("3.0");

        if (inexistente != null) {
            System.out.println(inexistente.mostrarInformacion());
        } else {
            System.out.println("Version no encontrada");
        }

        System.out.println("\n=== VERSION CON MAS CAMBIOS ===");

        VersionSoftware mayor =
                analizador.obtenerVersionMasCambios();

        if (mayor != null) {
            System.out.println(mayor.mostrarInformacion());
        } else {
            System.out.println("No existen versiones registradas");
        }

        System.out.println("\n=== PROMEDIO DE CAMBIOS ===");
        System.out.println(analizador.calcularPromedioCambios());
    }
}

