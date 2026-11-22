package pruebas;

import modelo.Estudiante;
import modelo.FichaSocioeconomica;
import modelo.Representante;

import java.time.LocalDate;

/**
 * Herramientas sencillas para las pruebas: cuenta los casos que pasan y los que fallan,
 * y crea estudiantes de prueba con los factores de riesgo que se necesiten.
 */
public class Verificador {

    /** Acción que debería lanzar una excepción (se escribe como lambda en las pruebas). */
    public interface Accion {
        void ejecutar() throws Exception;
    }

    private static int aprobadas = 0;
    private static int fallidas = 0;

    public static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }

    public static void verificar(boolean condicion, String descripcion) {
        if (condicion) {
            aprobadas++;
            System.out.println("  [OK]    " + descripcion);
        } else {
            fallidas++;
            System.out.println("  [FALLA] " + descripcion);
        }
    }

    /** La prueba pasa si la acción lanza una excepción (es decir, la regla de negocio la rechazó). */
    public static void esperarError(Accion accion, String descripcion) {
        try {
            accion.ejecutar();
            verificar(false, descripcion + " (no se lanzó la excepción esperada)");
        } catch (Exception e) {
            verificar(true, descripcion + " -> «" + e.getMessage() + "»");
        }
    }

    public static int getAprobadas() {
        return aprobadas;
    }

    public static int getFallidas() {
        return fallidas;
    }

    /** Crea un estudiante de prueba con la edad y los factores de riesgo indicados. */
    public static Estudiante crearEstudiante(String cedula, int edad, double ingreso, int miembros, boolean trabaja,
                                             boolean hijos, boolean rural, boolean repitio) {
        Representante rep = new Representante("", "Ana", "Pérez", "0991234567", "Madre");
        FichaSocioeconomica ficha = new FichaSocioeconomica(rural ? FichaSocioeconomica.ZONA_RURAL
                : FichaSocioeconomica.ZONA_URBANA, miembros, ingreso, trabaja, hijos, repitio);
        LocalDate nacimiento = LocalDate.now().minusYears(edad).minusDays(10);
        return new Estudiante(cedula, "Estudiante", "Prueba " + cedula.substring(6), "", nacimiento, "1ro BGU", "A",
                "Calle 1", "Quitumbe", "", rep, ficha);
    }
}
