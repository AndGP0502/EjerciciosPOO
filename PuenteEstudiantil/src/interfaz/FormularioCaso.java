package interfaz;

import modelo.CasoSeguimiento;
import modelo.Estudiante;
import modelo.ProfesionalDece;
import negocio.PuenteException;
import negocio.SistemaPuente;

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
 * Formulario para abrir un caso de seguimiento (RF-07).
 * Si el riesgo del estudiante es Bajo o Medio, el gestor exige una justificación.
 *
 * Distribución (BorderLayout): en el centro los campos en dos columnas (etiqueta | campo)
 * y abajo los botones.
 */
public class FormularioCaso extends JDialog {

    private SistemaPuente sistema;

    private JTextField campoCedula = new JTextField(25);
    private JTextField campoMotivo = new JTextField(25);
    private JComboBox<String> comboProfesional = new JComboBox<>();
    private JTextField campoFecha = new JTextField(25);
    private JTextField campoObservaciones = new JTextField(25);
    private JTextField campoJustificacion = new JTextField(25);

    public FormularioCaso(SistemaPuente sistema) {
        this.sistema = sistema;
        setTitle("Abrir caso de seguimiento (RF-07)");
        setModal(true);   // mientras el formulario está abierto no se puede usar la ventana principal
        setLayout(new BorderLayout());

        for (ProfesionalDece p : sistema.getProfesionales()) {
            comboProfesional.addItem(p.toString());
        }
        campoFecha.setText(UtilidadesInterfaz.formatear(LocalDate.now()));

        // ----- Centro: campos en dos columnas (etiqueta | campo) -----
        JPanel campos = new JPanel(new GridLayout(0, 2, 5, 5));
        campos.add(new JLabel("Cédula del estudiante:"));
        campos.add(campoCedula);
        campos.add(new JLabel("Motivo de la detección:"));
        campos.add(campoMotivo);
        campos.add(new JLabel("Profesional responsable:"));
        campos.add(comboProfesional);
        campos.add(new JLabel("Fecha de apertura (dd/mm/aaaa):"));
        campos.add(campoFecha);
        campos.add(new JLabel("Observaciones:"));
        campos.add(campoObservaciones);
        campos.add(new JLabel("Justificación (si el riesgo es Bajo o Medio):"));
        campos.add(campoJustificacion);
        add(campos, BorderLayout.CENTER);

        // ----- Abajo: botones -----
        JPanel botones = new JPanel(new FlowLayout());
        JButton botonCancelar = new JButton("Cancelar");
        JButton botonGuardar = new JButton("Abrir caso");
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
            Estudiante estudiante = sistema.getGestorEstudiantes().obtenerEstudiante(campoCedula.getText().trim());
            // La posición elegida en el combo es la misma que en la lista de profesionales
            ProfesionalDece profesional = null;
            if (comboProfesional.getSelectedIndex() >= 0) {
                profesional = sistema.getProfesionales().get(comboProfesional.getSelectedIndex());
            }
            LocalDate fecha = UtilidadesInterfaz.leerFecha(campoFecha.getText(), "La fecha de apertura");
            CasoSeguimiento caso = sistema.getGestorCasos().abrirCaso(estudiante, campoMotivo.getText(), profesional,
                    fecha, campoObservaciones.getText().trim(), campoJustificacion.getText().trim());
            UtilidadesInterfaz.mostrarMensaje(this, "Caso N.° " + caso.getNumero() + " abierto para "
                    + estudiante.getNombreCompleto() + " (riesgo " + estudiante.getNivelRiesgo() + ") en estado "
                    + caso.getEstado() + ".");
            dispose();
        } catch (PuenteException | IllegalArgumentException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }
}
