package interfaz;

import modelo.CasoSeguimiento;
import modelo.Intervencion;
import negocio.GestorCasos;
import negocio.PuenteException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;

/**
 * Formulario para registrar una intervención en un caso y elegir su nuevo estado (RF-08).
 * Las reglas (3 intentos sin respuesta, requisitos para resolver) las aplica el gestor.
 *
 * Distribución (BorderLayout): en el centro los campos en dos columnas (etiqueta | campo)
 * y abajo los botones.
 */
public class FormularioIntervencion extends JDialog {

    private GestorCasos gestor;
    private CasoSeguimiento caso;

    private JComboBox<String> comboTipo = new JComboBox<>(Intervencion.TIPOS);
    private JTextField campoFecha = new JTextField(25);
    private JComboBox<String> comboResultado = new JComboBox<>(Intervencion.RESULTADOS);
    private JTextField campoObservaciones = new JTextField(25);
    private JComboBox<String> comboEstado = new JComboBox<>(CasoSeguimiento.ESTADOS);
    private JTextField campoObservacionFinal = new JTextField(25);

    public FormularioIntervencion(GestorCasos gestor, CasoSeguimiento caso) {
        this.gestor = gestor;
        this.caso = caso;
        setTitle("Registrar intervención (RF-08)");
        setModal(true);   // mientras el formulario está abierto no se puede usar la ventana principal
        setLayout(new BorderLayout());

        campoFecha.setText(UtilidadesInterfaz.formatear(LocalDate.now()));
        // Si el caso recién se abrió, lo normal es que pase a "En seguimiento"
        if (CasoSeguimiento.ESTADO_ABIERTO.equals(caso.getEstado())) {
            comboEstado.setSelectedItem(CasoSeguimiento.ESTADO_EN_SEGUIMIENTO);
        } else {
            comboEstado.setSelectedItem(caso.getEstado());
        }

        // ----- Centro: campos en dos columnas (etiqueta | campo) -----
        JPanel campos = new JPanel(new GridLayout(0, 2, 5, 5));
        campos.add(new JLabel("Caso:"));
        campos.add(new JLabel("N.° " + caso.getNumero() + " – " + caso.getEstudiante().getNombreCompleto()));
        campos.add(new JLabel("Estado actual:"));
        campos.add(new JLabel(caso.getEstado() + " (intentos sin respuesta: " + caso.contarIntentosSinRespuesta() + ")"));
        campos.add(new JLabel("Tipo de intervención:"));
        campos.add(comboTipo);
        campos.add(new JLabel("Fecha (dd/mm/aaaa):"));
        campos.add(campoFecha);
        campos.add(new JLabel("Resultado:"));
        campos.add(comboResultado);
        campos.add(new JLabel("Observaciones:"));
        campos.add(campoObservaciones);
        campos.add(new JLabel("Nuevo estado del caso:"));
        campos.add(comboEstado);
        campos.add(new JLabel("Observación final (si se cierra el caso):"));
        campos.add(campoObservacionFinal);
        add(campos, BorderLayout.CENTER);

        // ----- Abajo: botones -----
        JPanel botones = new JPanel(new FlowLayout());
        JButton botonCancelar = new JButton("Cancelar");
        JButton botonGuardar = new JButton("Guardar");
        botones.add(botonCancelar);
        botones.add(botonGuardar);
        add(botones, BorderLayout.SOUTH);

        botonGuardar.addActionListener(e -> guardar());
        botonCancelar.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(null);
    }

    private void guardar() {
        try {
            LocalDate fecha = UtilidadesInterfaz.leerFecha(campoFecha.getText(), "La fecha de la intervención");
            Intervencion intervencion = new Intervencion(fecha, (String) comboTipo.getSelectedItem(),
                    (String) comboResultado.getSelectedItem(), campoObservaciones.getText().trim());
            String mensaje = gestor.registrarIntervencion(caso, intervencion, (String) comboEstado.getSelectedItem(),
                    campoObservacionFinal.getText());
            UtilidadesInterfaz.mostrarMensaje(this, mensaje);
            dispose();
        } catch (PuenteException | IllegalArgumentException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }
}
