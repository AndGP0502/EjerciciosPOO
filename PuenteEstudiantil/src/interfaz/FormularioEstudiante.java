package interfaz;

import modelo.Estudiante;
import modelo.FichaSocioeconomica;
import modelo.Representante;
import negocio.GestorEstudiantes;
import negocio.PuenteException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
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
 * Formulario para registrar un estudiante nuevo (RF-01) o editar uno existente (RF-02).
 * Al guardar llama al gestor, que valida los datos y calcula el riesgo.
 *
 * Distribución (BorderLayout): en el centro los campos en dos columnas (etiqueta | campo)
 * y abajo los botones.
 */
public class FormularioEstudiante extends JDialog {

    private static final String[] ZONAS = {FichaSocioeconomica.ZONA_URBANA, FichaSocioeconomica.ZONA_RURAL};
    private static final String[] PARENTESCOS = {"Madre", "Padre", "Abuelo/a", "Tío/a", "Hermano/a", "Otro"};

    private GestorEstudiantes gestor;
    private Estudiante existente;

    private JTextField campoCedula = new JTextField(15);
    private JTextField campoNombres = new JTextField(15);
    private JTextField campoApellidos = new JTextField(15);
    private JTextField campoFechaNacimiento = new JTextField(15);
    private JTextField campoTelefono = new JTextField(15);
    private JComboBox<String> comboCurso = new JComboBox<>(UtilidadesInterfaz.CURSOS);
    private JComboBox<String> comboParalelo = new JComboBox<>(UtilidadesInterfaz.PARALELOS);
    private JTextField campoDireccion = new JTextField(15);
    private JTextField campoSector = new JTextField(15);
    private JComboBox<String> comboZona = new JComboBox<>(ZONAS);

    private JTextField campoRepNombres = new JTextField(15);
    private JTextField campoRepApellidos = new JTextField(15);
    private JTextField campoRepTelefono = new JTextField(15);
    private JComboBox<String> comboParentesco = new JComboBox<>(PARENTESCOS);

    private JTextField campoMiembros = new JTextField(15);
    private JTextField campoIngreso = new JTextField(15);
    private JCheckBox checkTrabaja = new JCheckBox("Sí");
    private JCheckBox checkHijos = new JCheckBox("Sí");
    private JCheckBox checkRepitio = new JCheckBox("Sí");
    private JTextField campoObservaciones = new JTextField(15);

    public FormularioEstudiante(GestorEstudiantes gestor, Estudiante existente) {
        this.gestor = gestor;
        this.existente = existente;
        if (existente == null) {
            setTitle("Registrar estudiante (RF-01)");
        } else {
            setTitle("Editar estudiante (RF-02)");
        }
        setModal(true);   // mientras el formulario está abierto no se puede usar la ventana principal
        setLayout(new BorderLayout());

        // ----- Centro: campos en dos columnas (etiqueta | campo) -----
        JPanel campos = new JPanel(new GridLayout(0, 2, 5, 5));
        campos.add(new JLabel("Cédula:"));
        campos.add(campoCedula);
        campos.add(new JLabel("Nombres:"));
        campos.add(campoNombres);
        campos.add(new JLabel("Apellidos:"));
        campos.add(campoApellidos);
        campos.add(new JLabel("Fecha de nacimiento (dd/mm/aaaa):"));
        campos.add(campoFechaNacimiento);
        campos.add(new JLabel("Teléfono (opcional):"));
        campos.add(campoTelefono);
        campos.add(new JLabel("Curso:"));
        campos.add(comboCurso);
        campos.add(new JLabel("Paralelo:"));
        campos.add(comboParalelo);
        campos.add(new JLabel("Dirección:"));
        campos.add(campoDireccion);
        campos.add(new JLabel("Sector:"));
        campos.add(campoSector);
        campos.add(new JLabel("Zona:"));
        campos.add(comboZona);
        campos.add(new JLabel("Nombres del representante:"));
        campos.add(campoRepNombres);
        campos.add(new JLabel("Apellidos del representante:"));
        campos.add(campoRepApellidos);
        campos.add(new JLabel("Teléfono del representante:"));
        campos.add(campoRepTelefono);
        campos.add(new JLabel("Parentesco:"));
        campos.add(comboParentesco);
        campos.add(new JLabel("Miembros del hogar:"));
        campos.add(campoMiembros);
        campos.add(new JLabel("Ingreso mensual del hogar (USD):"));
        campos.add(campoIngreso);
        campos.add(new JLabel("¿Trabaja?"));
        campos.add(checkTrabaja);
        campos.add(new JLabel("¿Tiene hijos o cuida familiares?"));
        campos.add(checkHijos);
        campos.add(new JLabel("¿Ha repetido año?"));
        campos.add(checkRepitio);
        campos.add(new JLabel("Observaciones:"));
        campos.add(campoObservaciones);
        add(campos, BorderLayout.CENTER);

        // ----- Abajo: botones -----
        JPanel botones = new JPanel(new FlowLayout());
        JButton botonCancelar = new JButton("Cancelar");
        JButton botonGuardar = new JButton("Guardar");
        botones.add(botonCancelar);
        botones.add(botonGuardar);
        add(botones, BorderLayout.SOUTH);

        botonGuardar.addActionListener(e -> guardar());
        botonCancelar.addActionListener(e -> dispose());   // dispose() cierra el formulario

        if (existente != null) {
            cargarDatos(existente);
        }
        pack();                        // calcula el tamaño justo para que quepan todos los campos
        setLocationRelativeTo(null);   // centra el formulario en la pantalla
    }

    /** Al editar, llena los campos con los datos actuales del estudiante. */
    private void cargarDatos(Estudiante e) {
        campoCedula.setText(e.getCedula());
        campoCedula.setEditable(false);   // la cédula no se puede cambiar
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
        campoMiembros.setText(String.valueOf(e.getFicha().getMiembrosHogar()));
        campoIngreso.setText(String.format("%.2f", e.getFicha().getIngresoMensualHogar()));
        checkTrabaja.setSelected(e.getFicha().isTrabaja());
        checkHijos.setSelected(e.getFicha().isTieneHijosOCuidaFamiliares());
        checkRepitio.setSelected(e.getFicha().isRepitioAnio());
        campoObservaciones.setText(e.getObservaciones());
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
        int miembros = UtilidadesInterfaz.leerEntero(campoMiembros.getText(), "El número de miembros del hogar");
        double ingreso = UtilidadesInterfaz.leerDecimal(campoIngreso.getText(), "El ingreso mensual del hogar");
        String cedulaRep = "";
        if (existente != null) {
            cedulaRep = existente.getRepresentante().getCedula();
        }
        Representante representante = new Representante(cedulaRep, campoRepNombres.getText().trim(),
                campoRepApellidos.getText().trim(), campoRepTelefono.getText().trim(),
                (String) comboParentesco.getSelectedItem());
        FichaSocioeconomica ficha = new FichaSocioeconomica((String) comboZona.getSelectedItem(), miembros, ingreso,
                checkTrabaja.isSelected(), checkHijos.isSelected(), checkRepitio.isSelected());
        return new Estudiante(campoCedula.getText().trim(), campoNombres.getText().trim(),
                campoApellidos.getText().trim(), campoTelefono.getText().trim(), nacimiento,
                (String) comboCurso.getSelectedItem(), (String) comboParalelo.getSelectedItem(),
                campoDireccion.getText().trim(), campoSector.getText().trim(), campoObservaciones.getText().trim(),
                representante, ficha);
    }
}
