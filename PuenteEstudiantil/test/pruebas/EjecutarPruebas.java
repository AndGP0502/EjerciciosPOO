package pruebas;

import modelo.ProgramaApoyo;
import negocio.DatosDeEjemplo;
import negocio.SistemaPuente;

import static pruebas.Verificador.titulo;
import static pruebas.Verificador.verificar;

/**
 * Ejecuta todas las pruebas de los módulos terminados y muestra el resumen.
 * En IntelliJ: clic derecho sobre esta clase > Run 'EjecutarPruebas.main()'.
 */
public class EjecutarPruebas {

    public static void main(String[] args) {
        try {
            PruebasModulo1.ejecutar();
            PruebasModulo2.ejecutar();
            PruebasModulo3.ejecutar();
            PruebasModulo5.ejecutar();
            probarDatosDeEjemplo();
        } catch (Exception e) {
            System.out.println("\nUna prueba se detuvo por un error inesperado: " + e.getMessage());
            e.printStackTrace();
            Verificador.verificar(false, "Ejecución completa de las pruebas");
        }
        System.out.println();
        System.out.println("==============================================");
        System.out.println("Pruebas aprobadas: " + Verificador.getAprobadas());
        System.out.println("Pruebas fallidas:  " + Verificador.getFallidas());
        System.out.println("==============================================");
        if (Verificador.getFallidas() > 0) {
            System.exit(1);
        }
    }

    // Los datos de ejemplo pasan por las mismas reglas de negocio, así que también sirven de prueba
    private static void probarDatosDeEjemplo() throws Exception {
        titulo("INTEGRACIÓN – Datos de ejemplo");
        SistemaPuente sistema = new SistemaPuente();
        DatosDeEjemplo.cargar(sistema);
        verificar(sistema.getGestorEstudiantes().getEstudiantes().size() == 12
                && sistema.getGestorEstudiantes().listarActivos().size() == 11, "12 estudiantes cargados (11 activos)");
        verificar(sistema.getGestorProgramas().getProgramas().size() == 5, "5 programas de apoyo cargados");
        ProgramaApoyo refrigerio = sistema.getGestorProgramas().getProgramas().get(1);
        verificar(refrigerio.getCuposDisponibles() == 0 && refrigerio.getListaEspera().size() == 2,
                "El refrigerio está lleno y tiene 2 estudiantes en lista de espera");
        verificar(sistema.getGestorCasos().getCasos().size() == 4
                && sistema.getGestorCasos().contarAbiertos(sistema.getGestorCasos().getCasos()) == 3,
                "4 casos de seguimiento (3 abiertos)");
        verificar(sistema.getGestorRendimiento().getCalificaciones().size() == 28, "28 notas del primer periodo");
    }
}
