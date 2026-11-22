package interfaz;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Métodos de apoyo que comparten todas las pantallas: lectura de fechas y números,
 * creación de tablas, botones y mensajes. Así cada panel no repite el mismo código.
 */
public class UtilidadesInterfaz {

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
        return fecha == null ? "" : fecha.format(FORMATO_FECHA);
    }

    public static int leerEntero(String texto, String campo) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException(campo + " debe ser un número entero.");
        }
    }

    /** Acepta coma o punto como separador decimal. */
    public static double leerDecimal(String texto, String campo) {
        try {
            return Double.parseDouble(texto.trim().replace(',', '.'));
        } catch (NumberFormatException | NullPointerException e) {
            throw new IllegalArgumentException(campo + " debe ser un número (por ejemplo 350,50).");
        }
    }

    // ------------------------------------------------------------------
    // Componentes
    // ------------------------------------------------------------------

    /** Modelo de tabla que no deja editar las celdas con doble clic. */
    public static DefaultTableModel crearModelo(String[] columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
    }

    /** Tabla en la que solo se puede seleccionar una fila a la vez. */
    public static JTable crearTabla(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return tabla;
    }

    public static JButton crearBoton(String texto) {
        return new JButton(texto);
    }

    /** Panel con un título para agrupar los campos de un formulario. */
    public static JPanel crearSeccion(String titulo) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(titulo));
        return panel;
    }

    /** Agrega una fila "etiqueta: campo" en un panel con GridBagLayout. */
    public static void agregarCampo(JPanel panel, int fila, int columna, String etiqueta, JComponent campo) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 4, 3, 4);
        c.gridy = fila;
        c.gridx = columna * 2;
        c.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel(etiqueta), c);
        c.gridx = columna * 2 + 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        panel.add(campo, c);
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
