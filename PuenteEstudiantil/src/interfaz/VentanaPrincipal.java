package interfaz;

import negocio.SistemaPuente;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 * Ventana principal. Tiene una pestaña por cada módulo del sistema;
 * cada pestaña es un panel que trabaja con el mismo objeto SistemaPuente.
 */
public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal(SistemaPuente sistema) {
        setTitle("Puente Estudiantil – DECE");
        setSize(1150, 650);
        setLocationRelativeTo(null);                       // centra la ventana en la pantalla
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    // al cerrar la ventana termina el programa

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("1. Estudiantes y riesgo", new PanelEstudiantes(sistema));
        pestanas.addTab("2. Programas de apoyo", new PanelProgramas(sistema));
        pestanas.addTab("3. Casos de seguimiento", new PanelCasos(sistema));
        pestanas.addTab("4. Alertas y reportes", new PanelAlertas());
        pestanas.addTab("5. Rendimiento académico", new PanelRendimiento(sistema));
        add(pestanas);
    }
}
