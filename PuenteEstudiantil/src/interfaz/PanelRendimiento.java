package interfaz;

import modelo.Calificacion;
import modelo.Estudiante;
import negocio.GestorEstudiantes;
import negocio.GestorRendimiento;
import negocio.PuenteException;
import negocio.SistemaPuente;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Pantalla del Módulo 5: rendimiento académico. Permite registrar notas por periodo (RF-13),
 * consultarlas, corregirlas o eliminarlas (RF-14) y detectar el bajo rendimiento (RF-15).
 */
public class PanelRendimiento extends JPanel {

    private static final String[] COLUMNAS_ESTUDIANTES = {"Estudiante", "Curso", "Riesgo", "Promedio",
            "Notas bajo 7", "Rendimiento"};
    private static final String[] COLUMNAS_NOTAS = {"Asignatura", "Nota", "Situación", "Fecha de registro"};

    private GestorRendimiento gestor;
    private GestorEstudiantes gestorEstudiantes;
    private ArrayList<Estudiante> listaEstudiantes;
    private ArrayList<Calificacion> listaNotas;
    private boolean soloQueRequierenAtencion;

    private DefaultTableModel modeloEstudiantes;
    private DefaultTableModel modeloNotas;
    private JTable tablaEstudiantes;
    private JTable tablaNotas;
    private JTextField campoAnio;
    private JComboBox<String> comboPeriodo;
    private JComboBox<String> comboCurso;
    private JLabel etiquetaNotas;
    private JLabel etiquetaResumen;
    private JTextArea areaRecomendacion;

    public PanelRendimiento(SistemaPuente sistema) {
        this.gestor = sistema.getGestorRendimiento();
        this.gestorEstudiantes = sistema.getGestorEstudiantes();
        this.listaEstudiantes = new ArrayList<>();
        this.listaNotas = new ArrayList<>();
        setLayout(new BorderLayout(8, 8));

        add(crearFiltros(), BorderLayout.NORTH);

        modeloEstudiantes = UtilidadesInterfaz.crearModelo(COLUMNAS_ESTUDIANTES);
        tablaEstudiantes = UtilidadesInterfaz.crearTabla(modeloEstudiantes);
        tablaEstudiantes.getColumnModel().getColumn(0).setPreferredWidth(230);
        tablaEstudiantes.getColumnModel().getColumn(1).setPreferredWidth(85);
        tablaEstudiantes.getColumnModel().getColumn(2).setPreferredWidth(65);
        tablaEstudiantes.getColumnModel().getColumn(3).setPreferredWidth(65);
        tablaEstudiantes.getColumnModel().getColumn(4).setPreferredWidth(80);
        tablaEstudiantes.getColumnModel().getColumn(5).setPreferredWidth(125);
        tablaEstudiantes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarNotas();
            }
        });

        modeloNotas = UtilidadesInterfaz.crearModelo(COLUMNAS_NOTAS);
        tablaNotas = UtilidadesInterfaz.crearTabla(modeloNotas);
        tablaNotas.getColumnModel().getColumn(0).setPreferredWidth(170);
        tablaNotas.getColumnModel().getColumn(2).setPreferredWidth(120);

        JPanel parteDerecha = new JPanel(new BorderLayout(6, 6));
        etiquetaNotas = new JLabel("Notas del estudiante seleccionado");
        parteDerecha.add(etiquetaNotas, BorderLayout.NORTH);
        parteDerecha.add(new JScrollPane(tablaNotas), BorderLayout.CENTER);
        JPanel abajo = new JPanel(new BorderLayout(4, 4));
        abajo.add(crearBotonesNotas(), BorderLayout.NORTH);
        areaRecomendacion = new JTextArea(5, 30);
        areaRecomendacion.setEditable(false);
        areaRecomendacion.setLineWrap(true);
        areaRecomendacion.setWrapStyleWord(true);
        JScrollPane panelRecomendacion = new JScrollPane(areaRecomendacion);
        panelRecomendacion.setBorder(BorderFactory.createTitledBorder("Acciones sugeridas (RF-15)"));
        abajo.add(panelRecomendacion, BorderLayout.CENTER);
        parteDerecha.add(abajo, BorderLayout.SOUTH);

        JSplitPane division = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(tablaEstudiantes),
                parteDerecha);
        division.setResizeWeight(0.55);
        add(division, BorderLayout.CENTER);

        etiquetaResumen = new JLabel(" ");
        add(etiquetaResumen, BorderLayout.SOUTH);

        actualizar();
    }

    private JPanel crearFiltros() {
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        campoAnio = new JTextField("2026-2027", 8);
        comboPeriodo = new JComboBox<>(new String[]{"1", "2", "3"});
        String[] cursos = new String[UtilidadesInterfaz.CURSOS.length + 1];
        cursos[0] = "Todos";
        System.arraycopy(UtilidadesInterfaz.CURSOS, 0, cursos, 1, UtilidadesInterfaz.CURSOS.length);
        comboCurso = new JComboBox<>(cursos);
        JButton botonTodos = UtilidadesInterfaz.crearBoton("Ver todos");
        JButton botonDetectar = UtilidadesInterfaz.crearBoton("Detectar bajo rendimiento");
        botonTodos.addActionListener(e -> {
            soloQueRequierenAtencion = false;
            actualizar();
        });
        botonDetectar.addActionListener(e -> {
            soloQueRequierenAtencion = true;
            actualizar();
        });
        comboPeriodo.addActionListener(e -> actualizar());
        comboCurso.addActionListener(e -> actualizar());
        campoAnio.addActionListener(e -> actualizar());
        filtros.add(new JLabel("Año lectivo:"));
        filtros.add(campoAnio);
        filtros.add(new JLabel("Periodo académico:"));
        filtros.add(comboPeriodo);
        filtros.add(new JLabel("Curso:"));
        filtros.add(comboCurso);
        filtros.add(botonTodos);
        filtros.add(botonDetectar);
        return filtros;
    }

    private JPanel crearBotonesNotas() {
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        JButton botonRegistrar = UtilidadesInterfaz.crearBoton("Registrar nota");
        JButton botonCorregir = UtilidadesInterfaz.crearBoton("Corregir nota");
        JButton botonEliminar = UtilidadesInterfaz.crearBoton("Eliminar nota");
        botonRegistrar.addActionListener(e -> registrarNota());
        botonCorregir.addActionListener(e -> corregirNota());
        botonEliminar.addActionListener(e -> eliminarNota());
        botones.add(botonRegistrar);
        botones.add(botonCorregir);
        botones.add(botonEliminar);
        return botones;
    }

    private String getAnio() {
        return campoAnio.getText().trim();
    }

    private int getPeriodo() {
        return comboPeriodo.getSelectedIndex() + 1;
    }

    private String getCurso() {
        return comboCurso.getSelectedIndex() <= 0 ? "" : (String) comboCurso.getSelectedItem();
    }

    /** Recarga la lista de estudiantes (todos o solo los que requieren atención). */
    public void actualizar() {
        if (modeloEstudiantes == null) {
            return;
        }
        Estudiante seleccionado = obtenerEstudianteSeleccionado(false);
        if (soloQueRequierenAtencion) {
            listaEstudiantes = gestor.detectarBajoRendimiento(gestorEstudiantes.getEstudiantes(), getCurso(), getAnio(),
                    getPeriodo());
        } else {
            listaEstudiantes = new ArrayList<>();
            for (Estudiante e : gestorEstudiantes.listarActivos()) {
                if (getCurso().isEmpty() || e.getCurso().equals(getCurso())) {
                    listaEstudiantes.add(e);
                }
            }
        }
        modeloEstudiantes.setRowCount(0);
        int bajo = 0;
        int observacion = 0;
        for (Estudiante e : listaEstudiantes) {
            String clasificacion = gestor.clasificarRendimiento(e, getAnio(), getPeriodo());
            boolean tieneNotas = !GestorRendimiento.SIN_CALIFICACIONES.equals(clasificacion);
            modeloEstudiantes.addRow(new Object[]{e.getNombreCompleto(), e.getCursoCompleto(),
                    e.getNivelRiesgo(),
                    tieneNotas ? String.format("%.2f", gestor.calcularPromedio(e, getAnio(), getPeriodo())) : "–",
                    tieneNotas ? gestor.contarAsignaturasBajoMinimo(e, getAnio(), getPeriodo()) : "–", clasificacion});
            if (GestorRendimiento.RENDIMIENTO_BAJO.equals(clasificacion)) {
                bajo++;
            } else if (GestorRendimiento.RENDIMIENTO_EN_OBSERVACION.equals(clasificacion)) {
                observacion++;
            }
        }
        int indice = listaEstudiantes.indexOf(seleccionado);
        if (indice >= 0) {
            tablaEstudiantes.setRowSelectionInterval(indice, indice);
        }
        cargarNotas();
        etiquetaResumen.setText((soloQueRequierenAtencion ? "Estudiantes que requieren atención: " : "Estudiantes activos: ")
                + listaEstudiantes.size() + "   |   Bajo rendimiento: " + bajo + "   |   En observación: " + observacion
                + "   |   Nota mínima para aprobar: 7,00");
    }

    private void cargarNotas() {
        modeloNotas.setRowCount(0);
        listaNotas = new ArrayList<>();
        Estudiante e = obtenerEstudianteSeleccionado(false);
        if (e == null) {
            etiquetaNotas.setText("Notas del estudiante seleccionado");
            areaRecomendacion.setText("Seleccione un estudiante para ver sus notas y las acciones sugeridas.");
            return;
        }
        listaNotas = gestor.buscarCalificaciones(e, getAnio(), getPeriodo());
        for (Calificacion c : listaNotas) {
            modeloNotas.addRow(new Object[]{c.getAsignatura(), String.format("%.2f", c.getNota()),
                    c.estaBajoMinimo() ? "Alerta temprana" : "Aprobada",
                    UtilidadesInterfaz.formatear(c.getFechaRegistro())});
        }
        etiquetaNotas.setText("Notas de " + e.getNombreCompleto() + " – periodo " + getPeriodo());
        areaRecomendacion.setText(gestor.clasificarRendimiento(e, getAnio(), getPeriodo()) + ". "
                + gestor.generarRecomendacion(e, getAnio(), getPeriodo()));
    }

    private Estudiante obtenerEstudianteSeleccionado(boolean avisar) {
        int fila = tablaEstudiantes == null ? -1 : tablaEstudiantes.getSelectedRow();
        if (fila < 0 || fila >= listaEstudiantes.size()) {
            if (avisar) {
                UtilidadesInterfaz.mostrarError(this, "Seleccione un estudiante de la tabla.");
            }
            return null;
        }
        return listaEstudiantes.get(fila);
    }

    private Calificacion obtenerNotaSeleccionada() {
        int fila = tablaNotas.getSelectedRow();
        if (fila < 0 || fila >= listaNotas.size()) {
            UtilidadesInterfaz.mostrarError(this, "Seleccione una nota de la tabla de la derecha.");
            return null;
        }
        return listaNotas.get(fila);
    }

    private void registrarNota() {
        Estudiante e = obtenerEstudianteSeleccionado(true);
        if (e == null) {
            return;
        }
        JComboBox<String> comboAsignatura = new JComboBox<>(GestorRendimiento.ASIGNATURAS);
        comboAsignatura.setEditable(true);
        JTextField campoNota = new JTextField(6);
        JPanel panel = new JPanel(new GridLayout(0, 1, 4, 4));
        panel.add(new JLabel("Estudiante: " + e.getNombreCompleto() + " – Año " + getAnio() + ", periodo " + getPeriodo()));
        panel.add(new JLabel("Asignatura:"));
        panel.add(comboAsignatura);
        panel.add(new JLabel("Nota (0 a 10):"));
        panel.add(campoNota);
        int opcion = JOptionPane.showConfirmDialog(this, panel, "Registrar nota (RF-13)", JOptionPane.OK_CANCEL_OPTION);
        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            double nota = UtilidadesInterfaz.leerDecimal(campoNota.getText(), "La nota");
            String asignatura = String.valueOf(comboAsignatura.getSelectedItem());
            UtilidadesInterfaz.mostrarMensaje(this, gestor.registrarCalificacion(e, getAnio(), getPeriodo(), asignatura,
                    nota, LocalDate.now()));
            actualizar();
        } catch (PuenteException | IllegalArgumentException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }

    private void corregirNota() {
        Calificacion c = obtenerNotaSeleccionada();
        if (c == null) {
            return;
        }
        String texto = JOptionPane.showInputDialog(this, "Nueva nota de " + c.getAsignatura() + " (0 a 10):",
                String.format("%.2f", c.getNota()));
        if (texto == null) {
            return;
        }
        try {
            UtilidadesInterfaz.mostrarMensaje(this, gestor.actualizarNota(c, UtilidadesInterfaz.leerDecimal(texto, "La nota")));
            actualizar();
        } catch (PuenteException | IllegalArgumentException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }

    private void eliminarNota() {
        Calificacion c = obtenerNotaSeleccionada();
        if (c == null) {
            return;
        }
        if (!UtilidadesInterfaz.confirmar(this, "¿Eliminar la nota de " + c.getAsignatura() + " ("
                + String.format("%.2f", c.getNota()) + ")?")) {
            return;
        }
        try {
            UtilidadesInterfaz.mostrarMensaje(this, gestor.eliminarCalificacion(c));
            actualizar();
        } catch (PuenteException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }
}
