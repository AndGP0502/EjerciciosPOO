package interfaz;

import modelo.Estudiante;
import modelo.ProgramaApoyo;
import negocio.GestorProgramas;
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
 * Formulario para registrar un programa de apoyo (RF-04) o editar uno vigente (RF-05).
 *
 * Distribución (BorderLayout): en el centro los campos en dos columnas (etiqueta | campo)
 * y abajo los botones.
 */
public class FormularioPrograma extends JDialog {

    private GestorProgramas gestor;
    private ProgramaApoyo existente;

    private JTextField campoNombre = new JTextField(20);
    private JComboBox<String> comboTipo = new JComboBox<>(ProgramaApoyo.TIPOS);
    private JTextField campoDescripcion = new JTextField(20);
    private JTextField campoCupos = new JTextField(20);
    private JTextField campoInicio = new JTextField(20);
    private JTextField campoFin = new JTextField(20);
    private JComboBox<String> comboNivel = new JComboBox<>(Estudiante.NIVELES);

    public FormularioPrograma(GestorProgramas gestor, ProgramaApoyo existente) {
        this.gestor = gestor;
        this.existente = existente;
        if (existente == null) {
            setTitle("Registrar programa de apoyo (RF-04)");
        } else {
            setTitle("Editar programa de apoyo (RF-05)");
        }
        setModal(true);   // mientras el formulario está abierto no se puede usar la ventana principal
        setLayout(new BorderLayout());

        // ----- Centro: campos en dos columnas (etiqueta | campo) -----
        JPanel campos = new JPanel(new GridLayout(0, 2, 5, 5));
        campos.add(new JLabel("Nombre:"));
        campos.add(campoNombre);
        campos.add(new JLabel("Tipo de apoyo:"));
        campos.add(comboTipo);
        campos.add(new JLabel("Descripción:"));
        campos.add(campoDescripcion);
        campos.add(new JLabel("Cupos:"));
        campos.add(campoCupos);
        campos.add(new JLabel("Fecha de inicio (dd/mm/aaaa):"));
        campos.add(campoInicio);
        campos.add(new JLabel("Fecha de fin (dd/mm/aaaa):"));
        campos.add(campoFin);
        campos.add(new JLabel("Nivel mínimo de riesgo:"));
        campos.add(comboNivel);
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

        if (existente == null) {
            comboNivel.setSelectedItem(Estudiante.NIVEL_MEDIO);   // valor por defecto según RF-04
            campoInicio.setText(UtilidadesInterfaz.formatear(LocalDate.now()));
        } else {
            campoNombre.setText(existente.getNombre());
            comboTipo.setSelectedItem(existente.getTipo());
            campoDescripcion.setText(existente.getDescripcion());
            campoCupos.setText(String.valueOf(existente.getCupos()));
            campoInicio.setText(UtilidadesInterfaz.formatear(existente.getFechaInicio()));
            campoFin.setText(UtilidadesInterfaz.formatear(existente.getFechaFin()));
            comboNivel.setSelectedItem(existente.getNivelMinimo());
        }
        pack();
        setLocationRelativeTo(null);
    }

    private void guardar() {
        try {
            int cupos = UtilidadesInterfaz.leerEntero(campoCupos.getText(), "El número de cupos");
            LocalDate inicio = UtilidadesInterfaz.leerFecha(campoInicio.getText(), "La fecha de inicio");
            LocalDate fin = UtilidadesInterfaz.leerFecha(campoFin.getText(), "La fecha de fin");
            ProgramaApoyo datos = new ProgramaApoyo(campoNombre.getText().trim(), (String) comboTipo.getSelectedItem(),
                    campoDescripcion.getText().trim(), cupos, inicio, fin, (String) comboNivel.getSelectedItem());
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
