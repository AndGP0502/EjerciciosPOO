package interfaz;

import modelo.Estudiante;
import modelo.FichaSocioeconomica;
import modelo.Representante;
import negocio.GestorEstudiantes;
import negocio.PuenteException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import java.awt.GridLayout;
import java.awt.Window;
import java.time.LocalDate;

/**
 * Formulario para registrar un estudiante nuevo (RF-01) o editar uno existente (RF-02).
 * Al guardar llama al gestor, que valida los datos y calcula el riesgo.
 */
public class FormularioEstudiante extends JDialog {

    private static final String[] ZONAS = {FichaSocioeconomica.ZONA_URBANA, FichaSocioeconomica.ZONA_RURAL};
    private static final String[] PARENTESCOS = {"Madre", "Padre", "Abuelo/a", "Tío/a", "Hermano/a", "Otro"};

    private GestorEstudiantes gestor;
    private Estudiante existente;

    private JTextField campoCedula = new JTextField(12);
    private JTextField campoNombres = new JTextField(14);
    private JTextField campoApellidos = new JTextField(14);
    private JTextField campoFechaNacimiento = new JTextField(10);
    private JTextField campoTelefono = new JTextField(10);
    private JComboBox<String> comboCurso = new JComboBox<>(UtilidadesInterfaz.CURSOS);
    private JComboBox<String> comboParalelo = new JComboBox<>(UtilidadesInterfaz.PARALELOS);
    private JTextField campoDireccion = new JTextField(14);
    private JTextField campoSector = new JTextField(14);
    private JComboBox<String> comboZona = new JComboBox<>(ZONAS);

    private JTextField campoRepNombres = new JTextField(14);
    private JTextField campoRepApellidos = new JTextField(14);
    private JTextField campoRepTelefono = new JTextField(10);
    private JComboBox<String> comboParentesco = new JComboBox<>(PARENTESCOS);

    private JSpinner spinnerMiembros = new JSpinner(new SpinnerNumberModel(4, 1, 20, 1));
    private JTextField campoIngreso = new JTextField(8);
    private JCheckBox checkTrabaja = new JCheckBox("Trabaja");
    private JCheckBox checkHijos = new JCheckBox("Tiene hijos o cuida familiares");
    private JCheckBox checkRepitio = new JCheckBox("Ha repetido año");
    private JTextArea areaObservaciones = new JTextArea(3, 30);

    public FormularioEstudiante(Window padre, GestorEstudiantes gestor, Estudiante existente) {
        super(padre, existente == null ? "Registrar estudiante (RF-01)" : "Editar estudiante (RF-02)",
                ModalityType.APPLICATION_MODAL);
        this.gestor = gestor;
        this.existente = existente;

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));

        JPanel datos = UtilidadesInterfaz.crearSeccion("Datos del estudiante");
        UtilidadesInterfaz.agregarCampo(datos, 0, 0, "Cédula:", campoCedula);
        UtilidadesInterfaz.agregarCampo(datos, 0, 1, "Fecha de nacimiento (dd/mm/aaaa):", campoFechaNacimiento);
        UtilidadesInterfaz.agregarCampo(datos, 1, 0, "Nombres:", campoNombres);
        UtilidadesInterfaz.agregarCampo(datos, 1, 1, "Apellidos:", campoApellidos);
        UtilidadesInterfaz.agregarCampo(datos, 2, 0, "Curso:", comboCurso);
        UtilidadesInterfaz.agregarCampo(datos, 2, 1, "Paralelo:", comboParalelo);
        UtilidadesInterfaz.agregarCampo(datos, 3, 0, "Dirección:", campoDireccion);
        UtilidadesInterfaz.agregarCampo(datos, 3, 1, "Sector:", campoSector);
        UtilidadesInterfaz.agregarCampo(datos, 4, 0, "Zona:", comboZona);
        UtilidadesInterfaz.agregarCampo(datos, 4, 1, "Teléfono (opcional):", campoTelefono);
        contenido.add(datos);

        JPanel representante = UtilidadesInterfaz.crearSeccion("Representante");
        UtilidadesInterfaz.agregarCampo(representante, 0, 0, "Nombres:", campoRepNombres);
        UtilidadesInterfaz.agregarCampo(representante, 0, 1, "Apellidos:", campoRepApellidos);
        UtilidadesInterfaz.agregarCampo(representante, 1, 0, "Teléfono:", campoRepTelefono);
        UtilidadesInterfaz.agregarCampo(representante, 1, 1, "Parentesco:", comboParentesco);
        contenido.add(representante);

        JPanel ficha = UtilidadesInterfaz.crearSeccion("Ficha socioeconómica");
        UtilidadesInterfaz.agregarCampo(ficha, 0, 0, "Miembros del hogar:", spinnerMiembros);
        UtilidadesInterfaz.agregarCampo(ficha, 0, 1, "Ingreso mensual del hogar (USD):", campoIngreso);
        JPanel checks = new JPanel(new GridLayout(1, 3));
        checks.add(checkTrabaja);
        checks.add(checkHijos);
        checks.add(checkRepitio);
        UtilidadesInterfaz.agregarCampo(ficha, 1, 0, "Situación:", checks);
        contenido.add(ficha);

        JPanel observaciones = UtilidadesInterfaz.crearSeccion("Observaciones");
        areaObservaciones.setLineWrap(true);
        areaObservaciones.setWrapStyleWord(true);
        UtilidadesInterfaz.agregarCampo(observaciones, 0, 0, "", new JScrollPane(areaObservaciones));
        contenido.add(observaciones);

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

        if (existente != null) {
            cargarDatos(existente);
        }
        pack();
        setLocationRelativeTo(padre);
    }

    private void cargarDatos(Estudiante e) {
        campoCedula.setText(e.getCedula());
        campoCedula.setEditable(false);
        campoNombres.setText(e.getNombres());
        campoApellidos.setText(e.getApellidos());
        campoFechaNacimiento.setText(UtilidadesInterfaz.formatear(e.getFechaNacimiento()));
        campoTelefono.setText(e.getTelefono());
        comboCurso.setSelectedItem(e.getCurso());
        comboParalelo.setSelectedItem(e.getParalelo());
        campoDireccion.setText(e.getDireccion());
        campoSector.setText(e.getSector());
        comboZona.setSelectedItem(e.getFicha().getZona());
        campoRepNombres.setText(e.getRepresentante().getNombres());
        campoRepApellidos.setText(e.getRepresentante().getApellidos());
        campoRepTelefono.setText(e.getRepresentante().getTelefono());
        comboParentesco.setSelectedItem(e.getRepresentante().getParentesco());
        spinnerMiembros.setValue(e.getFicha().getMiembrosHogar());
        campoIngreso.setText(String.format("%.2f", e.getFicha().getIngresoMensualHogar()));
        checkTrabaja.setSelected(e.getFicha().isTrabaja());
        checkHijos.setSelected(e.getFicha().isTieneHijosOCuidaFamiliares());
        checkRepitio.setSelected(e.getFicha().isRepitioAnio());
        areaObservaciones.setText(e.getObservaciones());
    }

    private void guardar() {
        try {
            Estudiante datos = construirEstudiante();
            String mensaje;
            if (existente == null) {
                mensaje = gestor.registrarEstudiante(datos);
            } else {
                mensaje = gestor.actualizarEstudiante(existente.getCedula(), datos);
            }
            UtilidadesInterfaz.mostrarMensaje(this, mensaje);
            dispose();
        } catch (PuenteException | IllegalArgumentException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }

    // Arma el objeto con lo que escribió el usuario; las reglas las revisa el gestor
    private Estudiante construirEstudiante() {
        LocalDate nacimiento = UtilidadesInterfaz.leerFecha(campoFechaNacimiento.getText(), "La fecha de nacimiento");
        double ingreso = UtilidadesInterfaz.leerDecimal(campoIngreso.getText(), "El ingreso mensual del hogar");
        String cedulaRep = existente == null ? "" : existente.getRepresentante().getCedula();
        Representante representante = new Representante(cedulaRep, campoRepNombres.getText().trim(),
                campoRepApellidos.getText().trim(), campoRepTelefono.getText().trim(),
                (String) comboParentesco.getSelectedItem());
        FichaSocioeconomica ficha = new FichaSocioeconomica((String) comboZona.getSelectedItem(),
                (Integer) spinnerMiembros.getValue(), ingreso, checkTrabaja.isSelected(), checkHijos.isSelected(),
                checkRepitio.isSelected());
        return new Estudiante(campoCedula.getText().trim(), campoNombres.getText().trim(),
                campoApellidos.getText().trim(), campoTelefono.getText().trim(), nacimiento,
                (String) comboCurso.getSelectedItem(), (String) comboParalelo.getSelectedItem(),
                campoDireccion.getText().trim(), campoSector.getText().trim(), areaObservaciones.getText().trim(),
                representante, ficha);
    }
}
