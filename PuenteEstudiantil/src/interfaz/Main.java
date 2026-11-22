package interfaz;

import negocio.DatosDeEjemplo;
import negocio.PuenteException;
import negocio.SistemaPuente;

import javax.swing.SwingUtilities;

/**
 * Punto de entrada del programa. Crea el sistema, le carga datos de ejemplo
 * (para poder probarlo sin escribir todo a mano) y abre la ventana principal.
 */
public class Main {

    public static void main(String[] args) {
        SistemaPuente sistema = new SistemaPuente();
        try {
            DatosDeEjemplo.cargar(sistema);
        } catch (PuenteException e) {
            System.out.println("No se pudieron cargar los datos de ejemplo: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> new VentanaPrincipal(sistema).setVisible(true));
    }
}
