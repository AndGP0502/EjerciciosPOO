package interfaz;

import javax.swing.JOptionPane;
import java.awt.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Métodos de apoyo que comparten todas las pantallas: lectura de fechas y números
 * y mensajes al usuario. Así cada panel no repite el mismo código.
 */
public class UtilidadesInterfaz {

    // STRICT hace que se rechacen fechas que no existen, como 31/02/2026
    public static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    public static final String[] CURSOS = {"8vo EGB", "9no EGB", "10mo EGB", "1ro BGU", "2do BGU", "3ro BGU"};
    public static final String[] PARALELOS = {"A", "B", "C", "D"};

    // ------------------------------------------------------------------
    // Lectura de datos escritos por el usuario
    // ------------------------------------------------------------------

    public static LocalDate leerFecha(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " es obligatoria (formato dd/mm/aaaa).");
        }
        try {
            return LocalDate.parse(texto.trim(), FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(campo + " no es válida. Use el formato dd/mm/aaaa.");
        }
    }

    /** Igual que leerFecha, pero si el campo está vacío devuelve null (filtros opcionales). */
    public static LocalDate leerFechaOpcional(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }
        return leerFecha(texto, campo);
    }

    public static String formatear(LocalDate fecha) {
        if (fecha == null) {
            return "";
        }
        return fecha.format(FORMATO_FECHA);
    }

    public static int leerEntero(String texto, String campo) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(campo + " debe ser un número entero.");
        }
    }

    /** Acepta coma o punto como separador decimal. */
    public static double leerDecimal(String texto, String campo) {
        try {
            return Double.parseDouble(texto.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(campo + " debe ser un número (por ejemplo 350,50).");
        }
    }

    // ------------------------------------------------------------------
    // Mensajes
    // ------------------------------------------------------------------

    public static void mostrarMensaje(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Puente Estudiantil", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void mostrarError(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean confirmar(Component padre, String mensaje) {
        int opcion = JOptionPane.showConfirmDialog(padre, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION);
        return opcion == JOptionPane.YES_OPTION;
    }
}
