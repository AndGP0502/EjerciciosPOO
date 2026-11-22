package interfaz;

import modelo.Estudiante;
import modelo.FichaSocioeconomica;
import modelo.Representante;
import negocio.GestorEstudiantes;
import negocio.PuenteException;
import negocio.SistemaPuente;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Pantalla del Módulo 1: lista de estudiantes con su nivel de riesgo,
 * búsqueda (RF-02), registro y edición (RF-01 y RF-02), baja lógica (RF-02)
 * y cálculo del riesgo con umbral configurable (RF-03).
 */
public class PanelEstudiantes extends JPanel {

    private static final String[] COLUMNAS = {"Cédula", "Estudiante", "Edad", "Curso", "Zona", "Ingreso/persona",
            "Puntaje", "Nivel de riesgo", "Estado"};

    private GestorEstudiantes gestor;
    private ArrayList<Estudiante> listaActual;

    private DefaultTableModel modelo;
    private JTable tabla;
    private JComboBox<String> comboCriterio;
    private JTextField campoBuscar;
    private JLabel etiquetaUmbral;
    private JLabel etiquetaResumen;

    public PanelEstudiantes(SistemaPuente sistema) {
        this.gestor = sistema.getGestorEstudiantes();
        this.listaActual = new ArrayList<>();
        setLayout(new BorderLayout(8, 8));

        add(crearBarraBusqueda(), BorderLayout.NORTH);

        modelo = UtilidadesInterfaz.crearModelo(COLUMNAS);
        tabla = UtilidadesInterfaz.crearTabla(modelo);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(230);
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    verFicha();
                }
            }
        });
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        add(crearBotones(), BorderLayout.EAST);

        etiquetaResumen = new JLabel(" ");
        add(etiquetaResumen, BorderLayout.SOUTH);

        actualizar();
    }

    private JPanel crearBarraBusqueda() {
        JPanel barra = new JPanel(new BorderLayout());

        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        comboCriterio = new JComboBox<>(GestorEstudiantes.CRITERIOS_BUSQUEDA);
        campoBuscar = new JTextField(16);
        JButton botonBuscar = UtilidadesInterfaz.crearBoton("Buscar");
        JButton botonTodos = UtilidadesInterfaz.crearBoton("Mostrar todos");
        botonBuscar.addActionListener(e -> actualizar());
        campoBuscar.addActionListener(e -> actualizar());
        botonTodos.addActionListener(e -> {
            campoBuscar.setText("");
            actualizar();
        });
        izquierda.add(new JLabel("Buscar por:"));
        izquierda.add(comboCriterio);
        izquierda.add(campoBuscar);
        izquierda.add(botonBuscar);
        izquierda.add(botonTodos);

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        etiquetaUmbral = new JLabel();
        JButton botonUmbral = UtilidadesInterfaz.crearBoton("Cambiar umbral");
        botonUmbral.addActionListener(e -> cambiarUmbral());
        derecha.add(etiquetaUmbral);
        derecha.add(botonUmbral);

        barra.add(izquierda, BorderLayout.WEST);
        barra.add(derecha, BorderLayout.EAST);
        return barra;
    }

    private JPanel crearBotones() {
        JPanel botones = new JPanel(new GridLayout(0, 1, 0, 8));
        JButton botonNuevo = UtilidadesInterfaz.crearBoton("Nuevo estudiante");
        JButton botonEditar = UtilidadesInterfaz.crearBoton("Editar datos");
        JButton botonBaja = UtilidadesInterfaz.crearBoton("Dar de baja");
        JButton botonFicha = UtilidadesInterfaz.crearBoton("Ver ficha y riesgo");
        JButton botonRecalcular = UtilidadesInterfaz.crearBoton("Recalcular riesgo");
        botonNuevo.addActionListener(e -> nuevoEstudiante());
        botonEditar.addActionListener(e -> editarEstudiante());
        botonBaja.addActionListener(e -> darDeBaja());
        botonFicha.addActionListener(e -> verFicha());
        botonRecalcular.addActionListener(e -> recalcular());
        botones.add(botonNuevo);
        botones.add(botonEditar);
        botones.add(botonBaja);
        botones.add(botonFicha);
        botones.add(botonRecalcular);

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setPreferredSize(new Dimension(170, 10));
        contenedor.add(botones, BorderLayout.NORTH);
        return contenedor;
    }

    /** Vuelve a buscar con el filtro actual y recarga la tabla. */
    public void actualizar() {
        listaActual = gestor.buscar((String) comboCriterio.getSelectedItem(), campoBuscar.getText());
        modelo.setRowCount(0);
        int activos = 0;
        for (Estudiante e : listaActual) {
            modelo.addRow(new Object[]{e.getCedula(), e.getNombreCompleto(), e.calcularEdad(), e.getCursoCompleto(),
                    e.getFicha().getZona(), String.format("$ %.2f", e.getFicha().calcularIngresoPorPersona()),
                    e.getPuntajeRiesgo(), e.getNivelRiesgo(), e.getEstado()});
            if (e.estaActivo()) {
                activos++;
            }
        }
        etiquetaUmbral.setText("Umbral de ingreso por persona: USD "
                + String.format("%.2f", gestor.getUmbralIngresoPorPersona()));
        etiquetaResumen.setText("Mostrando " + listaActual.size() + " estudiantes (" + activos
                + " activos). Doble clic en una fila para ver la ficha completa.");
    }

    private Estudiante obtenerSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            UtilidadesInterfaz.mostrarError(this, "Seleccione un estudiante de la tabla.");
            return null;
        }
        return listaActual.get(fila);
    }

    private void nuevoEstudiante() {
        FormularioEstudiante formulario = new FormularioEstudiante(SwingUtilities.getWindowAncestor(this), gestor, null);
        formulario.setVisible(true);
        actualizar();
    }

    private void editarEstudiante() {
        Estudiante e = obtenerSeleccionado();
        if (e == null) {
            return;
        }
        if (!e.estaActivo()) {
            UtilidadesInterfaz.mostrarError(this, "No se puede modificar un estudiante inactivo.");
            return;
        }
        FormularioEstudiante formulario = new FormularioEstudiante(SwingUtilities.getWindowAncestor(this), gestor, e);
        formulario.setVisible(true);
        actualizar();
    }

    private void darDeBaja() {
        Estudiante e = obtenerSeleccionado();
        if (e == null) {
            return;
        }
        JComboBox<String> comboMotivo = new JComboBox<>(GestorEstudiantes.MOTIVOS_BAJA);
        JTextField campoFecha = new JTextField(UtilidadesInterfaz.formatear(LocalDate.now()), 10);
        JPanel panel = new JPanel(new GridLayout(0, 1, 4, 4));
        panel.add(new JLabel("Estudiante: " + e.getNombreCompleto()));
        panel.add(new JLabel("Motivo de la baja:"));
        panel.add(comboMotivo);
        panel.add(new JLabel("Fecha de la baja (dd/mm/aaaa):"));
        panel.add(campoFecha);
        int opcion = JOptionPane.showConfirmDialog(this, panel, "Dar de baja", JOptionPane.OK_CANCEL_OPTION);
        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            LocalDate fecha = UtilidadesInterfaz.leerFecha(campoFecha.getText(), "La fecha de baja");
            String mensaje = gestor.darDeBaja(e.getCedula(), (String) comboMotivo.getSelectedItem(), fecha);
            UtilidadesInterfaz.mostrarMensaje(this, mensaje + "\nSu historial se conserva en el sistema.");
            actualizar();
        } catch (PuenteException | IllegalArgumentException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }

    private void verFicha() {
        Estudiante e = obtenerSeleccionado();
        if (e == null) {
            return;
        }
        FichaSocioeconomica f = e.getFicha();
        Representante r = e.getRepresentante();
        String texto = "DATOS DEL ESTUDIANTE\n" + e.mostrarDatos()
                + "\nFecha de nacimiento: " + UtilidadesInterfaz.formatear(e.getFechaNacimiento())
                + " (" + e.calcularEdad() + " años)"
                + "\nDirección: " + e.getDireccion() + " – Sector: " + e.getSector()
                + "\n\nREPRESENTANTE\n" + r.mostrarDatos()
                + "\n\nFICHA SOCIOECONÓMICA"
                + "\nZona: " + f.getZona()
                + "\nMiembros del hogar: " + f.getMiembrosHogar()
                + "\nIngreso mensual del hogar: USD " + String.format("%.2f", f.getIngresoMensualHogar())
                + "\nIngreso por persona: USD " + String.format("%.2f", f.calcularIngresoPorPersona())
                + "\nTrabaja: " + (f.isTrabaja() ? "Sí" : "No")
                + "\nTiene hijos o cuida familiares: " + (f.isTieneHijosOCuidaFamiliares() ? "Sí" : "No")
                + "\nHa repetido año: " + (f.isRepitioAnio() ? "Sí" : "No")
                + "\n\nCÁLCULO DEL RIESGO (RF-03)"
                + "\nPuntaje: " + e.getPuntajeRiesgo() + " de 10 – Nivel: " + e.getNivelRiesgo()
                + "\nFactores que sumaron: " + e.getDetalleRiesgo();
        if (!e.estaActivo()) {
            texto += "\n\nBAJA\nFecha: " + UtilidadesInterfaz.formatear(e.getFechaBaja()) + " – Motivo: " + e.getMotivoBaja();
        }
        if (e.getObservaciones() != null && !e.getObservaciones().isEmpty()) {
            texto += "\n\nObservaciones: " + e.getObservaciones();
        }
        JTextArea area = new JTextArea(texto, 22, 52);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Ficha del estudiante", JOptionPane.PLAIN_MESSAGE);
    }

    private void recalcular() {
        UtilidadesInterfaz.mostrarMensaje(this, gestor.recalcularTodos());
        actualizar();
    }

    private void cambiarUmbral() {
        String texto = JOptionPane.showInputDialog(this,
                "Nuevo umbral de ingreso mensual por persona (USD).\nSe recalculará el riesgo de todos los estudiantes:",
                String.format("%.2f", gestor.getUmbralIngresoPorPersona()));
        if (texto == null) {
            return;
        }
        try {
            String mensaje = gestor.cambiarUmbral(UtilidadesInterfaz.leerDecimal(texto, "El umbral"));
            UtilidadesInterfaz.mostrarMensaje(this, mensaje);
            actualizar();
        } catch (PuenteException | IllegalArgumentException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }
}
