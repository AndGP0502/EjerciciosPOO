package interfaz;

import modelo.AsignacionApoyo;
import modelo.Estudiante;
import modelo.ProgramaApoyo;
import negocio.GestorEstudiantes;
import negocio.GestorProgramas;
import negocio.PuenteException;
import negocio.SistemaPuente;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Pantalla del Módulo 2: programas de apoyo (RF-04 y RF-05) y sus asignaciones,
 * incluida la lista de espera ordenada por nivel de riesgo (RF-06).
 */
public class PanelProgramas extends JPanel {

    private static final String[] COLUMNAS_PROGRAMAS = {"Código", "Programa", "Tipo", "Cupos", "Asignados",
            "Disponibles", "En espera", "Vigencia", "Nivel mínimo", "Estado"};
    private static final String[] COLUMNAS_ASIGNACIONES = {"Estudiante", "Cédula", "Curso", "Nivel de riesgo",
            "Fecha de solicitud", "Fecha de asignación", "Estado", "Posición en espera"};

    private GestorProgramas gestor;
    private GestorEstudiantes gestorEstudiantes;
    private ArrayList<ProgramaApoyo> listaProgramas;
    private ArrayList<AsignacionApoyo> listaAsignaciones;

    private DefaultTableModel modeloProgramas;
    private DefaultTableModel modeloAsignaciones;
    private JTable tablaProgramas;
    private JTable tablaAsignaciones;
    private JComboBox<String> comboCriterio;
    private JTextField campoBuscar;
    private JLabel etiquetaAsignaciones;

    public PanelProgramas(SistemaPuente sistema) {
        this.gestor = sistema.getGestorProgramas();
        this.gestorEstudiantes = sistema.getGestorEstudiantes();
        this.listaProgramas = new ArrayList<>();
        this.listaAsignaciones = new ArrayList<>();
        setLayout(new BorderLayout(8, 8));

        add(crearBarraBusqueda(), BorderLayout.NORTH);

        modeloProgramas = UtilidadesInterfaz.crearModelo(COLUMNAS_PROGRAMAS);
        tablaProgramas = UtilidadesInterfaz.crearTabla(modeloProgramas);
        tablaProgramas.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaProgramas.getColumnModel().getColumn(1).setPreferredWidth(190);
        tablaProgramas.getColumnModel().getColumn(7).setPreferredWidth(170);
        tablaProgramas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarAsignaciones();
            }
        });

        modeloAsignaciones = UtilidadesInterfaz.crearModelo(COLUMNAS_ASIGNACIONES);
        tablaAsignaciones = UtilidadesInterfaz.crearTabla(modeloAsignaciones);
        tablaAsignaciones.getColumnModel().getColumn(0).setPreferredWidth(200);

        JPanel parteSuperior = new JPanel(new BorderLayout(6, 6));
        parteSuperior.add(new JScrollPane(tablaProgramas), BorderLayout.CENTER);
        parteSuperior.add(crearBotonesProgramas(), BorderLayout.SOUTH);

        JPanel parteInferior = new JPanel(new BorderLayout(6, 6));
        etiquetaAsignaciones = new JLabel("Asignaciones del programa seleccionado");
        parteInferior.add(etiquetaAsignaciones, BorderLayout.NORTH);
        parteInferior.add(new JScrollPane(tablaAsignaciones), BorderLayout.CENTER);
        parteInferior.add(crearBotonesAsignaciones(), BorderLayout.SOUTH);

        JSplitPane division = new JSplitPane(JSplitPane.VERTICAL_SPLIT, parteSuperior, parteInferior);
        division.setResizeWeight(0.5);
        add(division, BorderLayout.CENTER);

        actualizar();
    }

    private JPanel crearBarraBusqueda() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        comboCriterio = new JComboBox<>(GestorProgramas.CRITERIOS_BUSQUEDA);
        campoBuscar = new JTextField(16);
        JButton botonBuscar = UtilidadesInterfaz.crearBoton("Buscar");
        JButton botonTodos = UtilidadesInterfaz.crearBoton("Mostrar todos");
        botonBuscar.addActionListener(e -> actualizar());
        campoBuscar.addActionListener(e -> actualizar());
        botonTodos.addActionListener(e -> {
            campoBuscar.setText("");
            actualizar();
        });
        barra.add(new JLabel("Buscar por:"));
        barra.add(comboCriterio);
        barra.add(campoBuscar);
        barra.add(botonBuscar);
        barra.add(botonTodos);
        return barra;
    }

    private JPanel crearBotonesProgramas() {
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        JButton botonNuevo = UtilidadesInterfaz.crearBoton("Nuevo programa");
        JButton botonEditar = UtilidadesInterfaz.crearBoton("Editar programa");
        JButton botonCerrar = UtilidadesInterfaz.crearBoton("Cerrar programa");
        botonNuevo.addActionListener(e -> nuevoPrograma());
        botonEditar.addActionListener(e -> editarPrograma());
        botonCerrar.addActionListener(e -> cerrarPrograma());
        botones.add(botonNuevo);
        botones.add(botonEditar);
        botones.add(botonCerrar);
        return botones;
    }

    private JPanel crearBotonesAsignaciones() {
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        JButton botonAsignar = UtilidadesInterfaz.crearBoton("Asignar a estudiante");
        JButton botonFinalizar = UtilidadesInterfaz.crearBoton("Finalizar asignación");
        JButton botonEspera = UtilidadesInterfaz.crearBoton("Dar cupo al primero en espera");
        botonAsignar.addActionListener(e -> asignarEstudiante());
        botonFinalizar.addActionListener(e -> finalizarAsignacion());
        botonEspera.addActionListener(e -> asignarPrimeroEnEspera());
        botones.add(botonAsignar);
        botones.add(botonFinalizar);
        botones.add(botonEspera);
        return botones;
    }

    /** Recarga la tabla de programas y mantiene seleccionado el programa que estaba elegido. */
    public void actualizar() {
        ProgramaApoyo seleccionado = obtenerProgramaSeleccionado(false);
        listaProgramas = gestor.buscar((String) comboCriterio.getSelectedItem(), campoBuscar.getText());
        modeloProgramas.setRowCount(0);
        for (ProgramaApoyo p : listaProgramas) {
            modeloProgramas.addRow(new Object[]{p.getCodigo(), p.getNombre(), p.getTipo(), p.getCupos(),
                    p.contarAsignacionesVigentes(), p.estaVigente() ? String.valueOf(p.getCuposDisponibles()) : "–",
                    p.getListaEspera().size(),
                    UtilidadesInterfaz.formatear(p.getFechaInicio()) + " – " + UtilidadesInterfaz.formatear(p.getFechaFin()),
                    p.getNivelMinimo(), p.getEstado()});
        }
        int indice = listaProgramas.indexOf(seleccionado);
        if (indice >= 0) {
            tablaProgramas.setRowSelectionInterval(indice, indice);
        } else if (!listaProgramas.isEmpty()) {
            tablaProgramas.setRowSelectionInterval(0, 0);
        }
        cargarAsignaciones();
    }

    /** Muestra primero las vigentes, luego la lista de espera en orden y al final el historial. */
    private void cargarAsignaciones() {
        modeloAsignaciones.setRowCount(0);
        listaAsignaciones = new ArrayList<>();
        ProgramaApoyo p = obtenerProgramaSeleccionado(false);
        if (p == null) {
            etiquetaAsignaciones.setText("Asignaciones del programa seleccionado");
            return;
        }
        for (AsignacionApoyo a : p.getAsignaciones()) {
            if (a.estaVigente()) {
                listaAsignaciones.add(a);
            }
        }
        listaAsignaciones.addAll(p.getListaEspera());
        for (AsignacionApoyo a : p.getAsignaciones()) {
            if (!a.estaVigente() && !a.estaEnEspera()) {
                listaAsignaciones.add(a);
            }
        }
        for (AsignacionApoyo a : listaAsignaciones) {
            Estudiante e = a.getEstudiante();
            int posicion = a.estaEnEspera() ? gestor.obtenerPosicionEnEspera(e, p) : 0;
            modeloAsignaciones.addRow(new Object[]{e.getNombreCompleto(), e.getCedula(), e.getCursoCompleto(),
                    e.getNivelRiesgo(), UtilidadesInterfaz.formatear(a.getFechaSolicitud()),
                    UtilidadesInterfaz.formatear(a.getFechaAsignacion()), a.getEstado(),
                    posicion > 0 ? String.valueOf(posicion) : ""});
        }
        etiquetaAsignaciones.setText("Asignaciones de «" + p.getNombre() + "» – vigentes: "
                + p.contarAsignacionesVigentes() + " de " + p.getCupos() + ", en lista de espera: "
                + p.getListaEspera().size());
    }

    private ProgramaApoyo obtenerProgramaSeleccionado(boolean avisar) {
        int fila = tablaProgramas == null ? -1 : tablaProgramas.getSelectedRow();
        if (fila < 0 || fila >= listaProgramas.size()) {
            if (avisar) {
                UtilidadesInterfaz.mostrarError(this, "Seleccione un programa de la tabla.");
            }
            return null;
        }
        return listaProgramas.get(fila);
    }

    private void nuevoPrograma() {
        new FormularioPrograma(SwingUtilities.getWindowAncestor(this), gestor, null).setVisible(true);
        actualizar();
    }

    private void editarPrograma() {
        ProgramaApoyo p = obtenerProgramaSeleccionado(true);
        if (p == null) {
            return;
        }
        if (!p.estaVigente()) {
            UtilidadesInterfaz.mostrarError(this, "No se puede modificar un programa cerrado.");
            return;
        }
        new FormularioPrograma(SwingUtilities.getWindowAncestor(this), gestor, p).setVisible(true);
        actualizar();
    }

    private void cerrarPrograma() {
        ProgramaApoyo p = obtenerProgramaSeleccionado(true);
        if (p == null) {
            return;
        }
        if (!UtilidadesInterfaz.confirmar(this, "¿Cerrar el programa «" + p.getNombre()
                + "»? Las asignaciones vigentes pasarán a Finalizada y la lista de espera se cancelará.")) {
            return;
        }
        try {
            UtilidadesInterfaz.mostrarMensaje(this, gestor.cerrarPrograma(p.getCodigo()));
            actualizar();
        } catch (PuenteException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }

    private void asignarEstudiante() {
        ProgramaApoyo p = obtenerProgramaSeleccionado(true);
        if (p == null) {
            return;
        }
        ArrayList<Estudiante> activos = gestorEstudiantes.listarActivos();
        if (activos.isEmpty()) {
            UtilidadesInterfaz.mostrarError(this, "No hay estudiantes activos registrados.");
            return;
        }
        String[] opciones = new String[activos.size()];
        for (int i = 0; i < activos.size(); i++) {
            Estudiante e = activos.get(i);
            opciones[i] = e.getNombreCompleto() + " – " + e.getCursoCompleto() + " – riesgo " + e.getNivelRiesgo();
        }
        JComboBox<String> comboEstudiantes = new JComboBox<>(opciones);
        JPanel panel = new JPanel(new GridLayout(0, 1, 4, 4));
        panel.add(new JLabel("Programa: " + p.getNombre() + " (nivel mínimo: " + p.getNivelMinimo()
                + ", cupos disponibles: " + p.getCuposDisponibles() + ")"));
        panel.add(new JLabel("Estudiante:"));
        panel.add(comboEstudiantes);
        int opcion = JOptionPane.showConfirmDialog(this, panel, "Asignar apoyo (RF-06)", JOptionPane.OK_CANCEL_OPTION);
        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            Estudiante elegido = activos.get(comboEstudiantes.getSelectedIndex());
            UtilidadesInterfaz.mostrarMensaje(this, gestor.asignarApoyo(elegido, p, LocalDate.now()));
            actualizar();
        } catch (PuenteException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }

    private void finalizarAsignacion() {
        int fila = tablaAsignaciones.getSelectedRow();
        if (fila < 0) {
            UtilidadesInterfaz.mostrarError(this, "Seleccione una asignación de la tabla inferior.");
            return;
        }
        try {
            UtilidadesInterfaz.mostrarMensaje(this, gestor.finalizarAsignacion(listaAsignaciones.get(fila)));
            actualizar();
        } catch (PuenteException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }

    private void asignarPrimeroEnEspera() {
        ProgramaApoyo p = obtenerProgramaSeleccionado(true);
        if (p == null) {
            return;
        }
        try {
            UtilidadesInterfaz.mostrarMensaje(this, gestor.asignarPrimeroEnEspera(p, LocalDate.now()));
            actualizar();
        } catch (PuenteException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }
}
