package interfaz;

import negocio.DatosDeEjemplo;
import negocio.PuenteException;
import negocio.SistemaPuente;

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

        VentanaPrincipal ventana = new VentanaPrincipal(sistema);
        ventana.setVisible(true);
    }
}
