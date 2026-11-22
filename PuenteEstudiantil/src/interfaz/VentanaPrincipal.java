package interfaz;

import negocio.SistemaPuente;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.Dimension;

/**
 * Ventana principal. Tiene una pestaña por cada módulo del sistema;
 * cada pestaña es un panel que trabaja con el mismo objeto SistemaPuente.
 */
public class VentanaPrincipal extends JFrame {

    private PanelEstudiantes panelEstudiantes;
    private PanelProgramas panelProgramas;
    private PanelCasos panelCasos;
    private PanelAlertas panelAlertas;
    private PanelRendimiento panelRendimiento;

    public VentanaPrincipal(SistemaPuente sistema) {
        super("Puente Estudiantil – DECE");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 650));

        panelEstudiantes = new PanelEstudiantes(sistema);
        panelProgramas = new PanelProgramas(sistema);
        panelCasos = new PanelCasos(sistema);
        panelAlertas = new PanelAlertas();
        panelRendimiento = new PanelRendimiento(sistema);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("1. Estudiantes y riesgo", panelEstudiantes);
        pestanas.addTab("2. Programas de apoyo", panelProgramas);
        pestanas.addTab("3. Casos de seguimiento", panelCasos);
        pestanas.addTab("4. Alertas y reportes", panelAlertas);
        pestanas.addTab("5. Rendimiento académico", panelRendimiento);
        // Al cambiar de pestaña se recargan los datos, porque otro módulo pudo modificarlos
        pestanas.addChangeListener(e -> actualizarPestana(pestanas.getSelectedIndex()));
        add(pestanas);

        pack();
        setLocationRelativeTo(null);
    }

    private void actualizarPestana(int indice) {
        if (indice == 0) {
            panelEstudiantes.actualizar();
        } else if (indice == 1) {
            panelProgramas.actualizar();
        } else if (indice == 2) {
            panelCasos.actualizar();
        } else if (indice == 4) {
            panelRendimiento.actualizar();
        }
    }
}
