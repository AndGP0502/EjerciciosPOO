package interfaz;

import modelo.Estudiante;
import modelo.ProgramaApoyo;
import negocio.GestorProgramas;
import negocio.PuenteException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.time.LocalDate;

/**
 * Formulario para crear un programa de apoyo (RF-04) o editarlo (RF-05).
 */
public class FormularioPrograma extends JDialog {

    private GestorProgramas gestor;
    private ProgramaApoyo existente;

    private JTextField campoNombre = new JTextField(22);
    private JComboBox<String> comboTipo = new JComboBox<>(ProgramaApoyo.TIPOS);
    private JTextArea areaDescripcion = new JTextArea(3, 22);
    private JSpinner spinnerCupos = new JSpinner(new SpinnerNumberModel(10, 0, 500, 1));
    private JTextField campoInicio = new JTextField(10);
    private JTextField campoFin = new JTextField(10);
    private JComboBox<String> comboNivel = new JComboBox<>(Estudiante.NIVELES);

    public FormularioPrograma(Window padre, GestorProgramas gestor, ProgramaApoyo existente) {
        super(padre, existente == null ? "Registrar programa de apoyo (RF-04)" : "Editar programa de apoyo (RF-05)",
                ModalityType.APPLICATION_MODAL);
        this.gestor = gestor;
        this.existente = existente;

        JPanel datos = UtilidadesInterfaz.crearSeccion("Datos del programa");
        areaDescripcion.setLineWrap(true);
        areaDescripcion.setWrapStyleWord(true);
        UtilidadesInterfaz.agregarCampo(datos, 0, 0, "Nombre:", campoNombre);
        UtilidadesInterfaz.agregarCampo(datos, 1, 0, "Tipo de apoyo:", comboTipo);
        UtilidadesInterfaz.agregarCampo(datos, 2, 0, "Descripción:", new JScrollPane(areaDescripcion));
        UtilidadesInterfaz.agregarCampo(datos, 3, 0, "Cupos:", spinnerCupos);
        UtilidadesInterfaz.agregarCampo(datos, 4, 0, "Fecha de inicio (dd/mm/aaaa):", campoInicio);
        UtilidadesInterfaz.agregarCampo(datos, 5, 0, "Fecha de fin (dd/mm/aaaa):", campoFin);
        UtilidadesInterfaz.agregarCampo(datos, 6, 0, "Nivel mínimo de riesgo:", comboNivel);

        JPanel contenido = new JPanel(new BorderLayout());
        contenido.add(datos, BorderLayout.CENTER);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonGuardar = UtilidadesInterfaz.crearBoton("Guardar");
        JButton botonCancelar = UtilidadesInterfaz.crearBoton("Cancelar");
        botonGuardar.addActionListener(e -> guardar());
        botonCancelar.addActionListener(e -> dispose());
        botones.add(botonCancelar);
        botones.add(botonGuardar);

        setLayout(new BorderLayout());
        add(contenido, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(botonGuardar);

        if (existente == null) {
            comboNivel.setSelectedItem(Estudiante.NIVEL_MEDIO);   // valor por defecto según RF-04
            campoInicio.setText(UtilidadesInterfaz.formatear(LocalDate.now()));
        } else {
            campoNombre.setText(existente.getNombre());
            comboTipo.setSelectedItem(existente.getTipo());
            areaDescripcion.setText(existente.getDescripcion());
            spinnerCupos.setValue(existente.getCupos());
            campoInicio.setText(UtilidadesInterfaz.formatear(existente.getFechaInicio()));
            campoFin.setText(UtilidadesInterfaz.formatear(existente.getFechaFin()));
            comboNivel.setSelectedItem(existente.getNivelMinimo());
        }
        pack();
        setLocationRelativeTo(padre);
    }

    private void guardar() {
        try {
            ProgramaApoyo datos = new ProgramaApoyo(campoNombre.getText().trim(), (String) comboTipo.getSelectedItem(),
                    areaDescripcion.getText().trim(), (Integer) spinnerCupos.getValue(),
                    UtilidadesInterfaz.leerFecha(campoInicio.getText(), "La fecha de inicio"),
                    UtilidadesInterfaz.leerFecha(campoFin.getText(), "La fecha de fin"),
                    (String) comboNivel.getSelectedItem());
            String mensaje;
            if (existente == null) {
                mensaje = gestor.registrarPrograma(datos);
            } else {
                mensaje = gestor.actualizarPrograma(existente.getCodigo(), datos);
            }
            UtilidadesInterfaz.mostrarMensaje(this, mensaje);
            dispose();
        } catch (PuenteException | IllegalArgumentException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }
}
