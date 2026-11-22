package interfaz;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.BorderLayout;

/**
 * Pantalla del Módulo 4 (Juyani): alertas y reportes de permanencia (RF-10, RF-11 y RF-12).
 *
 * ESTADO: en desarrollo. La lógica irá en negocio.GestorAlertas, que ya tiene definidos
 * sus atributos y métodos. Esta pantalla se completará cuando el gestor esté listo.
 */
public class PanelAlertas extends JPanel {

    public PanelAlertas() {
        setLayout(new BorderLayout());
        JTextArea mensaje = new JTextArea("Módulo 4: Alertas y reportes de permanencia\n"
                + "Responsable: Juyani. Este módulo se encuentra en desarrollo.\n\n"
                + "Requerimientos que cubrirá:\n"
                + "- RF-10 Registrar inasistencias.\n"
                + "- RF-11 Generar alertas de riesgo de abandono.\n"
                + "- RF-12 Generar reporte de permanencia.");
        mensaje.setEditable(false);
        add(mensaje, BorderLayout.CENTER);
    }
}
