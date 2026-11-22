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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.time.LocalDate;

/**
 * Formulario para registrar una intervención en un caso y, si corresponde, cambiar su estado (RF-08).
 * La observación final solo se habilita cuando el caso se va a cerrar.
 */
public class FormularioIntervencion extends JDialog {

    private GestorCasos gestor;
    private CasoSeguimiento caso;

    private JComboBox<String> comboTipo = new JComboBox<>(Intervencion.TIPOS);
    private JTextField campoFecha = new JTextField(UtilidadesInterfaz.formatear(LocalDate.now()), 10);
    private JComboBox<String> comboResultado = new JComboBox<>(Intervencion.RESULTADOS);
    private JTextArea areaObservaciones = new JTextArea(3, 28);
    private JComboBox<String> comboEstado = new JComboBox<>(CasoSeguimiento.ESTADOS);
    private JTextArea areaObservacionFinal = new JTextArea(3, 28);

    public FormularioIntervencion(Window padre, GestorCasos gestor, CasoSeguimiento caso) {
        super(padre, "Registrar intervención (RF-08)", ModalityType.APPLICATION_MODAL);
        this.gestor = gestor;
        this.caso = caso;

        areaObservaciones.setLineWrap(true);
        areaObservaciones.setWrapStyleWord(true);
        areaObservacionFinal.setLineWrap(true);
        areaObservacionFinal.setWrapStyleWord(true);
        // Si el caso recién se abrió, lo normal es que pase a "En seguimiento"
        if (CasoSeguimiento.ESTADO_ABIERTO.equals(caso.getEstado())) {
            comboEstado.setSelectedItem(CasoSeguimiento.ESTADO_EN_SEGUIMIENTO);
        } else {
            comboEstado.setSelectedItem(caso.getEstado());
        }
        comboEstado.addActionListener(e -> habilitarObservacionFinal());

        JPanel datos = UtilidadesInterfaz.crearSeccion("Caso N.° " + caso.getNumero() + " – "
                + caso.getEstudiante().getNombreCompleto());
        UtilidadesInterfaz.agregarCampo(datos, 0, 0, "Estado actual:", new JLabel(caso.getEstado()
                + "   (intentos seguidos sin respuesta: " + caso.contarIntentosSinRespuesta() + ")"));
        UtilidadesInterfaz.agregarCampo(datos, 1, 0, "Tipo de intervención:", comboTipo);
        UtilidadesInterfaz.agregarCampo(datos, 2, 0, "Fecha (dd/mm/aaaa):", campoFecha);
        UtilidadesInterfaz.agregarCampo(datos, 3, 0, "Resultado:", comboResultado);
        UtilidadesInterfaz.agregarCampo(datos, 4, 0, "Observaciones:", new JScrollPane(areaObservaciones));
        UtilidadesInterfaz.agregarCampo(datos, 5, 0, "Nuevo estado del caso:", comboEstado);
        UtilidadesInterfaz.agregarCampo(datos, 6, 0, "Observación final:", new JScrollPane(areaObservacionFinal));

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
        habilitarObservacionFinal();
        pack();
        setLocationRelativeTo(padre);
    }

    private void habilitarObservacionFinal() {
        String estado = (String) comboEstado.getSelectedItem();
        boolean cierra = CasoSeguimiento.ESTADO_RESUELTO.equals(estado) || CasoSeguimiento.ESTADO_ABANDONO.equals(estado);
        areaObservacionFinal.setEnabled(cierra);
    }

    private void guardar() {
        try {
            Intervencion intervencion = new Intervencion(
                    UtilidadesInterfaz.leerFecha(campoFecha.getText(), "La fecha de la intervención"),
                    (String) comboTipo.getSelectedItem(), (String) comboResultado.getSelectedItem(),
                    areaObservaciones.getText().trim());
            String observacionFinal = areaObservacionFinal.isEnabled() ? areaObservacionFinal.getText() : "";
            String mensaje = gestor.registrarIntervencion(caso, intervencion, (String) comboEstado.getSelectedItem(),
                    observacionFinal);
            UtilidadesInterfaz.mostrarMensaje(this, mensaje);
            dispose();
        } catch (PuenteException | IllegalArgumentException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }
}
