import modelo.ErrorSoftware;
import modelo.AnalizadorErrores;

public class Main {

    public static void main(String[] args) {

        ErrorSoftware[] errores = new ErrorSoftware[7];

        errores[0] = new ErrorSoftware(
                "E01", "Error de inicio de sesion", "ALTA", false);

        errores[1] = new ErrorSoftware(
                "E02", "Boton desalineado", "BAJA", true);

        errores[2] = new ErrorSoftware(
                "E03", "Error al guardar datos", "ALTA", false);

        errores[3] = new ErrorSoftware(
                "E04", "Carga lenta", "MEDIA", false);

        errores[4] = new ErrorSoftware(
                "E05", "Error de visualizacion", "BAJA", true);

        errores[5] = new ErrorSoftware(
                "E06", "Fallo en reportes", "MEDIA", false);

        errores[6] = new ErrorSoftware(
                "E07", "Error de autenticacion", "ALTA", true);

        AnalizadorErrores analizador =
                new AnalizadorErrores(errores);

        System.out.println("=== ERRORES PENDIENTES ===");
        System.out.println(analizador.contarPendientes());

        System.out.println("=== CONTEO POR SEVERIDAD ===");
        System.out.println("ALTA: " +
                analizador.contarPorSeveridad("ALTA"));
        System.out.println("MEDIA: " +
                analizador.contarPorSeveridad("MEDIA"));
        System.out.println("BAJA: " +
                analizador.contarPorSeveridad("BAJA"));

        System.out.println("=== ERRORES DE SEVERIDAD ALTA ===");
        System.out.println(analizador.listarPorSeveridad("ALTA"));

        System.out.println("=== BUSCAR ERROR EXISTENTE ===");

        ErrorSoftware encontrado =
                analizador.buscarPorCodigo("E03");

        if (encontrado != null) {
            System.out.println(encontrado.mostrarInformacion());
        } else {
            System.out.println("Error no encontrado");
        }

        System.out.println("=== BUSCAR ERROR INEXISTENTE ===");

        ErrorSoftware inexistente =
                analizador.buscarPorCodigo("E10");

        if (inexistente != null) {
            System.out.println(inexistente.mostrarInformacion());
        } else {
            System.out.println("Error no encontrado");
        }

        System.out.println("=== PRIMER PENDIENTE ALTA ===");

        ErrorSoftware pendiente =
                analizador.obtenerPrimerPendienteAlta();

        if (pendiente != null) {
            System.out.println(pendiente.mostrarInformacion());
        } else {
            System.out.println("No existen errores pendientes de severidad ALTA");
        }
    }
}