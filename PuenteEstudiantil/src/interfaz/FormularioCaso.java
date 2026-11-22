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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Formulario para abrir un caso de seguimiento (RF-07).
 * Si el riesgo del estudiante es Bajo o Medio, avisa que la justificación es obligatoria.
 */
public class FormularioCaso extends JDialog {

    private SistemaPuente sistema;
    private ArrayList<Estudiante> activos;

    private JComboBox<String> comboEstudiante;
    private JLabel etiquetaRiesgo = new JLabel(" ");
    private JTextField campoMotivo = new JTextField(28);
    private JComboBox<String> comboProfesional = new JComboBox<>();
    private JTextField campoFecha = new JTextField(UtilidadesInterfaz.formatear(LocalDate.now()), 10);
    private JTextArea areaObservaciones = new JTextArea(3, 28);
    private JTextArea areaJustificacion = new JTextArea(3, 28);

    public FormularioCaso(Window padre, SistemaPuente sistema) {
        super(padre, "Abrir caso de seguimiento (RF-07)", ModalityType.APPLICATION_MODAL);
        this.sistema = sistema;
        this.activos = sistema.getGestorEstudiantes().listarActivos();

        String[] nombres = new String[activos.size()];
        for (int i = 0; i < activos.size(); i++) {
            nombres[i] = activos.get(i).getNombreCompleto() + " – " + activos.get(i).getCursoCompleto();
        }
        comboEstudiante = new JComboBox<>(nombres);
        comboEstudiante.addActionListener(e -> mostrarRiesgo());
        for (ProfesionalDece p : sistema.getProfesionales()) {
            comboProfesional.addItem(p.toString());
        }
        areaObservaciones.setLineWrap(true);
        areaObservaciones.setWrapStyleWord(true);
        areaJustificacion.setLineWrap(true);
        areaJustificacion.setWrapStyleWord(true);

        JPanel datos = UtilidadesInterfaz.crearSeccion("Datos del caso");
        UtilidadesInterfaz.agregarCampo(datos, 0, 0, "Estudiante:", comboEstudiante);
        UtilidadesInterfaz.agregarCampo(datos, 1, 0, "Nivel de riesgo:", etiquetaRiesgo);
        UtilidadesInterfaz.agregarCampo(datos, 2, 0, "Motivo de la detección:", campoMotivo);
        UtilidadesInterfaz.agregarCampo(datos, 3, 0, "Profesional responsable:", comboProfesional);
        UtilidadesInterfaz.agregarCampo(datos, 4, 0, "Fecha (dd/mm/aaaa):", campoFecha);
        UtilidadesInterfaz.agregarCampo(datos, 5, 0, "Observaciones:", new JScrollPane(areaObservaciones));
        UtilidadesInterfaz.agregarCampo(datos, 6, 0, "Justificación:", new JScrollPane(areaJustificacion));

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.add(datos, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonGuardar = UtilidadesInterfaz.crearBoton("Abrir caso");
        JButton botonCancelar = UtilidadesInterfaz.crearBoton("Cancelar");
        botonGuardar.addActionListener(e -> guardar());
        botonCancelar.addActionListener(e -> dispose());
        botones.add(botonCancelar);
        botones.add(botonGuardar);

        setLayout(new BorderLayout());
        add(contenido, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
        mostrarRiesgo();
        pack();
        setLocationRelativeTo(padre);
    }

    private void mostrarRiesgo() {
        int indice = comboEstudiante.getSelectedIndex();
        if (indice < 0) {
            etiquetaRiesgo.setText("No hay estudiantes activos.");
            return;
        }
        Estudiante e = activos.get(indice);
        if (e.tieneRiesgoAltoOCritico()) {
            etiquetaRiesgo.setText(e.getNivelRiesgo() + " – el caso se abre de forma directa.");
        } else {
            etiquetaRiesgo.setText(e.getNivelRiesgo() + " – escriba una justificación (obligatoria).");
        }
    }

    private void guardar() {
        try {
            int indiceEstudiante = comboEstudiante.getSelectedIndex();
            int indiceProfesional = comboProfesional.getSelectedIndex();
            Estudiante estudiante = indiceEstudiante < 0 ? null : activos.get(indiceEstudiante);
            ProfesionalDece profesional = indiceProfesional < 0 ? null : sistema.getProfesionales().get(indiceProfesional);
            LocalDate fecha = UtilidadesInterfaz.leerFecha(campoFecha.getText(), "La fecha de apertura");
            CasoSeguimiento caso = sistema.getGestorCasos().abrirCaso(estudiante, campoMotivo.getText(), profesional,
                    fecha, areaObservaciones.getText().trim(), areaJustificacion.getText().trim());
            UtilidadesInterfaz.mostrarMensaje(this, "Caso N.° " + caso.getNumero() + " abierto para "
                    + estudiante.getNombreCompleto() + " en estado " + caso.getEstado() + ".");
            dispose();
        } catch (PuenteException | IllegalArgumentException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }
}
