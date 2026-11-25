package interfaz;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;

/**
 * Métodos de apoyo que usan todos los menús para leer datos del teclado con Scanner
 * y mostrar mensajes. Así cada menú no repite el mismo código.
 *
 * Los métodos que reciben "valorActual" se usan al editar: muestran el valor actual
 * entre corchetes y, si el usuario solo presiona Enter, lo conservan.
 */
public class Consola {

    // STRICT hace que se rechacen fechas que no existen, como 31/02/2026
    public static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    public static final String[] CURSOS = {"8vo EGB", "9no EGB", "10mo EGB", "1ro BGU", "2do BGU", "3ro BGU"};
    public static final String[] PARALELOS = {"A", "B", "C", "D"};

    // Un solo Scanner para todo el programa
    private static Scanner teclado = new Scanner(System.in);

    // ------------------------------------------------------------------
    // Texto
    // ------------------------------------------------------------------

    public static String leerTexto(String mensaje) {
        System.out.print(mensaje + ": ");
        return teclado.nextLine().trim();
    }

    public static String leerTexto(String mensaje, String valorActual) {
        // Si no hay valor actual (o está vacío) se pregunta normalmente
        if (valorActual == null || valorActual.isEmpty()) {
            return leerTexto(mensaje);
        }
        System.out.print(mensaje + " [" + valorActual + "]: ");
        String texto = teclado.nextLine().trim();
        if (texto.isEmpty()) {
            return valorActual;
        }
        return texto;
    }

    // ------------------------------------------------------------------
    // Números: repiten la pregunta hasta que se escriba un número válido
    // ------------------------------------------------------------------

    public static int leerEntero(String mensaje) {
        return leerEntero(mensaje, null);
    }

    public static int leerEntero(String mensaje, String valorActual) {
        int numero = 0;
        boolean valido = false;
        while (!valido) {
            String texto = leerTexto(mensaje, valorActual);
            try {
                numero = Integer.parseInt(texto);
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("  Debe escribir un número entero.");
            }
        }
        return numero;
    }

    public static double leerDecimal(String mensaje) {
        return leerDecimal(mensaje, null);
    }

    /** Acepta coma o punto como separador decimal. */
    public static double leerDecimal(String mensaje, String valorActual) {
        double numero = 0;
        boolean valido = false;
        while (!valido) {
            String texto = leerTexto(mensaje, valorActual);
            try {
                numero = Double.parseDouble(texto.replace(',', '.'));
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("  Debe escribir un número (por ejemplo 350,50).");
            }
        }
        return numero;
    }

    // ------------------------------------------------------------------
    // Fechas (formato dd/mm/aaaa)
    // ------------------------------------------------------------------

    public static LocalDate leerFecha(String mensaje) {
        return leerFecha(mensaje, null);
    }

    public static LocalDate leerFecha(String mensaje, String valorActual) {
        LocalDate fecha = null;
        while (fecha == null) {
            String texto = leerTexto(mensaje + " (dd/mm/aaaa)", valorActual);
            try {
                fecha = LocalDate.parse(texto, FORMATO_FECHA);
            } catch (DateTimeParseException e) {
                System.out.println("  La fecha no es válida. Use el formato dd/mm/aaaa.");
            }
        }
        return fecha;
    }

    /** Igual que leerFecha, pero si se deja vacío devuelve null (filtros opcionales). */
    public static LocalDate leerFechaOpcional(String mensaje) {
        LocalDate fecha = null;
        boolean terminado = false;
        while (!terminado) {
            String texto = leerTexto(mensaje + " (dd/mm/aaaa, Enter para omitir)");
            if (texto.isEmpty()) {
                terminado = true;
            } else {
                try {
                    fecha = LocalDate.parse(texto, FORMATO_FECHA);
                    terminado = true;
                } catch (DateTimeParseException e) {
                    System.out.println("  La fecha no es válida. Use el formato dd/mm/aaaa.");
                }
            }
        }
        return fecha;
    }

    public static String formatear(LocalDate fecha) {
        if (fecha == null) {
            return "";
        }
        return fecha.format(FORMATO_FECHA);
    }

    // ------------------------------------------------------------------
    // Elegir de una lista numerada
    // ------------------------------------------------------------------

    /** Muestra las opciones numeradas desde 1 y devuelve la posición elegida (desde 0). */
    public static int elegirPosicion(String mensaje, String[] opciones, String valorActual) {
        System.out.println(mensaje + ":");
        for (int i = 0; i < opciones.length; i++) {
            System.out.println("  " + (i + 1) + ". " + opciones[i]);
        }
        String actual = null;
        if (valorActual != null) {
            for (int i = 0; i < opciones.length; i++) {
                if (opciones[i].equals(valorActual)) {
                    actual = String.valueOf(i + 1);
                }
            }
        }
        int numero = leerEntero("  Número de la opción", actual);
        while (numero < 1 || numero > opciones.length) {
            System.out.println("  Debe elegir un número entre 1 y " + opciones.length + ".");
            numero = leerEntero("  Número de la opción", actual);
        }
        return numero - 1;
    }

    public static String elegirOpcion(String mensaje, String[] opciones) {
        return opciones[elegirPosicion(mensaje, opciones, null)];
    }

    public static String elegirOpcion(String mensaje, String[] opciones, String valorActual) {
        return opciones[elegirPosicion(mensaje, opciones, valorActual)];
    }

    // ------------------------------------------------------------------
    // Preguntas de sí o no
    // ------------------------------------------------------------------

    public static boolean leerSiNo(String mensaje, String valorActual) {
        String respuesta = leerTexto(mensaje + " (s/n)", valorActual);
        while (!respuesta.equalsIgnoreCase("s") && !respuesta.equalsIgnoreCase("n")) {
            System.out.println("  Responda s (sí) o n (no).");
            respuesta = leerTexto(mensaje + " (s/n)", valorActual);
        }
        return respuesta.equalsIgnoreCase("s");
    }

    public static boolean confirmar(String mensaje) {
        return leerSiNo(mensaje, null);
    }

    /** Convierte un boolean en "s" o "n" para mostrarlo como valor actual. */
    public static String siNo(boolean valor) {
        if (valor) {
            return "s";
        }
        return "n";
    }

    // ------------------------------------------------------------------
    // Mensajes
    // ------------------------------------------------------------------

    public static void titulo(String texto) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("  " + texto);
        System.out.println("==================================================");
    }

    public static void mostrarError(String mensaje) {
        System.out.println("ERROR: " + mensaje);
    }

    /** Espera un Enter para que el usuario alcance a leer antes de volver al menú. */
    public static void pausar() {
        System.out.print("\nPresione Enter para continuar...");
        teclado.nextLine();
    }
}
