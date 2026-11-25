package interfaz;

/**
 * Menú del Módulo 4 (Juyani): alertas y reportes de permanencia (RF-10, RF-11 y RF-12).
 *
 * ESTADO: en desarrollo. La lógica irá en negocio.GestorAlertas, que ya tiene definidos
 * sus atributos y métodos. Este menú se completará cuando el gestor esté listo.
 */
public class MenuAlertas {

    public void mostrar() {
        Consola.titulo("4. ALERTAS Y REPORTES DE PERMANENCIA");
        System.out.println("Responsable: Juyani. Este módulo se encuentra en desarrollo.");
        System.out.println();
        System.out.println("Requerimientos que cubrirá:");
        System.out.println("- RF-10 Registrar inasistencias.");
        System.out.println("- RF-11 Generar alertas de riesgo de abandono.");
        System.out.println("- RF-12 Generar reporte de permanencia.");
        Consola.pausar();
    }
}
