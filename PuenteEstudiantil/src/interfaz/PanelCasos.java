package interfaz;

import modelo.CasoSeguimiento;
import modelo.Intervencion;
import modelo.ProfesionalDece;
import negocio.GestorCasos;
import negocio.SistemaPuente;

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
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Pantalla del Módulo 3: casos de seguimiento. Permite abrir casos (RF-07), registrar
 * intervenciones y cambiar el estado (RF-08) y consultar el historial con filtros (RF-09).
 */
public class PanelCasos extends JPanel {

    private static final String[] COLUMNAS_CASOS = {"N.°", "Estudiante", "Curso", "Nivel de riesgo", "Profesional",
            "Apertura", "Estado", "Intervenciones", "Última intervención"};
    private static final String[] COLUMNAS_INTERVENCIONES = {"Fecha", "Tipo de intervención", "Resultado",
            "Observaciones"};

    private SistemaPuente sistema;
    private GestorCasos gestor;
    private ArrayList<CasoSeguimiento> listaCasos;

    private DefaultTableModel modeloCasos;
    private DefaultTableModel modeloIntervenciones;
    private JTable tablaCasos;
    private JTextField campoEstudiante;
    private JComboBox<String> comboProfesional;
    private JTextField campoDesde;
    private JTextField campoHasta;
    private JComboBox<String> comboEstado;
    private JLabel etiquetaTotales;
    private JLabel etiquetaDetalle;
    private JButton botonAvisos;

    public PanelCasos(SistemaPuente sistema) {
        this.sistema = sistema;
        this.gestor = sistema.getGestorCasos();
        this.listaCasos = new ArrayList<>();
        setLayout(new BorderLayout(8, 8));

        add(crearFiltros(), BorderLayout.NORTH);

        modeloCasos = UtilidadesInterfaz.crearModelo(COLUMNAS_CASOS);
        tablaCasos = UtilidadesInterfaz.crearTabla(modeloCasos);
        tablaCasos.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaCasos.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaCasos.getColumnModel().getColumn(4).setPreferredWidth(160);
        tablaCasos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarIntervenciones();
            }
        });

        modeloIntervenciones = UtilidadesInterfaz.crearModelo(COLUMNAS_INTERVENCIONES);
        JTable tablaIntervenciones = UtilidadesInterfaz.crearTabla(modeloIntervenciones);
        tablaIntervenciones.getColumnModel().getColumn(0).setPreferredWidth(80);
        tablaIntervenciones.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaIntervenciones.getColumnModel().getColumn(3).setPreferredWidth(420);

        JPanel parteSuperior = new JPanel(new BorderLayout(6, 6));
        parteSuperior.add(new JScrollPane(tablaCasos), BorderLayout.CENTER);
        parteSuperior.add(crearBotones(), BorderLayout.SOUTH);

        JPanel parteInferior = new JPanel(new BorderLayout(6, 6));
        etiquetaDetalle = new JLabel("Intervenciones del caso seleccionado");
        parteInferior.add(etiquetaDetalle, BorderLayout.NORTH);
        parteInferior.add(new JScrollPane(tablaIntervenciones), BorderLayout.CENTER);

        JSplitPane division = new JSplitPane(JSplitPane.VERTICAL_SPLIT, parteSuperior, parteInferior);
        division.setResizeWeight(0.55);
        add(division, BorderLayout.CENTER);

        etiquetaTotales = new JLabel(" ");
        add(etiquetaTotales, BorderLayout.SOUTH);

        actualizar();
    }

    private JPanel crearFiltros() {
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        campoEstudiante = new JTextField(12);
        comboProfesional = new JComboBox<>();
        campoDesde = new JTextField(8);
        campoHasta = new JTextField(8);
        String[] estados = new String[CasoSeguimiento.ESTADOS.length + 1];
        estados[0] = "Todos";
        System.arraycopy(CasoSeguimiento.ESTADOS, 0, estados, 1, CasoSeguimiento.ESTADOS.length);
        comboEstado = new JComboBox<>(estados);
        JButton botonFiltrar = UtilidadesInterfaz.crearBoton("Consultar");
        JButton botonLimpiar = UtilidadesInterfaz.crearBoton("Limpiar");
        botonFiltrar.addActionListener(e -> actualizar());
        botonLimpiar.addActionListener(e -> {
            campoEstudiante.setText("");
            comboProfesional.setSelectedIndex(0);
            campoDesde.setText("");
            campoHasta.setText("");
            comboEstado.setSelectedIndex(0);
            actualizar();
        });
        filtros.add(new JLabel("Estudiante:"));
        filtros.add(campoEstudiante);
        filtros.add(new JLabel("Profesional:"));
        filtros.add(comboProfesional);
        filtros.add(new JLabel("Desde:"));
        filtros.add(campoDesde);
        filtros.add(new JLabel("Hasta:"));
        filtros.add(campoHasta);
        filtros.add(new JLabel("Estado:"));
        filtros.add(comboEstado);
        filtros.add(botonFiltrar);
        filtros.add(botonLimpiar);
        return filtros;
    }

    private JPanel crearBotones() {
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        JButton botonAbrir = UtilidadesInterfaz.crearBoton("Abrir caso");
        JButton botonIntervencion = UtilidadesInterfaz.crearBoton("Registrar intervención");
        JButton botonDetalle = UtilidadesInterfaz.crearBoton("Ver detalle del caso");
        JButton botonHistorial = UtilidadesInterfaz.crearBoton("Historial cronológico");
        botonAvisos = UtilidadesInterfaz.crearBoton("Avisos al coordinador");
        botonAbrir.addActionListener(e -> abrirCaso());
        botonIntervencion.addActionListener(e -> registrarIntervencion());
        botonDetalle.addActionListener(e -> verDetalle());
        botonHistorial.addActionListener(e -> verHistorialCronologico());
        botonAvisos.addActionListener(e -> verAvisos());
        botones.add(botonAbrir);
        botones.add(botonIntervencion);
        botones.add(botonDetalle);
        botones.add(botonHistorial);
        botones.add(botonAvisos);
        return botones;
    }

    /** Aplica los filtros (RF-09) y recarga la tabla de casos. */
    public void actualizar() {
        actualizarComboProfesionales();
        CasoSeguimiento seleccionado = obtenerCasoSeleccionado(false);
        try {
            LocalDate desde = UtilidadesInterfaz.leerFechaOpcional(campoDesde.getText(), "La fecha «Desde»");
            LocalDate hasta = UtilidadesInterfaz.leerFechaOpcional(campoHasta.getText(), "La fecha «Hasta»");
            listaCasos = gestor.consultarHistorial(campoEstudiante.getText(), obtenerProfesionalFiltro(), desde, hasta,
                    (String) comboEstado.getSelectedItem());
        } catch (IllegalArgumentException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
            return;
        }
        modeloCasos.setRowCount(0);
        for (CasoSeguimiento c : listaCasos) {
            modeloCasos.addRow(new Object[]{c.getNumero(), c.getEstudiante().getNombreCompleto(),
                    c.getEstudiante().getCursoCompleto(), c.getEstudiante().getNivelRiesgo(),
                    c.getProfesional().getNombreCompleto(), UtilidadesInterfaz.formatear(c.getFechaApertura()),
                    c.getEstado(), c.getIntervenciones().size(),
                    UtilidadesInterfaz.formatear(c.getFechaUltimaIntervencion())});
        }
        int indice = listaCasos.indexOf(seleccionado);
        if (indice >= 0) {
            tablaCasos.setRowSelectionInterval(indice, indice);
        }
        cargarIntervenciones();
        etiquetaTotales.setText("Casos encontrados: " + listaCasos.size() + "   |   Abiertos: "
                + gestor.contarAbiertos(listaCasos) + "   |   Cerrados: " + gestor.contarCerrados(listaCasos));
        botonAvisos.setText("Avisos al coordinador (" + gestor.getAvisosCoordinador().size() + ")");
    }

    private void actualizarComboProfesionales() {
        Object elegido = comboProfesional.getSelectedItem();
        comboProfesional.removeAllItems();
        comboProfesional.addItem("Todos");
        for (ProfesionalDece p : sistema.getProfesionales()) {
            comboProfesional.addItem(p.getNombreCompleto());
        }
        if (elegido != null) {
            comboProfesional.setSelectedItem(elegido);
        }
    }

    private ProfesionalDece obtenerProfesionalFiltro() {
        int indice = comboProfesional.getSelectedIndex();
        if (indice <= 0) {
            return null;
        }
        return sistema.getProfesionales().get(indice - 1);
    }

    private void cargarIntervenciones() {
        modeloIntervenciones.setRowCount(0);
        CasoSeguimiento c = obtenerCasoSeleccionado(false);
        if (c == null) {
            etiquetaDetalle.setText("Intervenciones del caso seleccionado");
            return;
        }
        for (Intervencion i : c.getIntervencionesOrdenadas()) {
            modeloIntervenciones.addRow(new Object[]{UtilidadesInterfaz.formatear(i.getFecha()), i.getTipo(),
                    i.getResultado(), i.getObservaciones()});
        }
        etiquetaDetalle.setText("Intervenciones del caso N.° " + c.getNumero() + " – "
                + c.getEstudiante().getNombreCompleto() + " (" + c.getEstado() + ")");
    }

    private CasoSeguimiento obtenerCasoSeleccionado(boolean avisar) {
        int fila = tablaCasos == null ? -1 : tablaCasos.getSelectedRow();
        if (fila < 0 || fila >= listaCasos.size()) {
            if (avisar) {
                UtilidadesInterfaz.mostrarError(this, "Seleccione un caso de la tabla.");
            }
            return null;
        }
        return listaCasos.get(fila);
    }

    private void abrirCaso() {
        new FormularioCaso(SwingUtilities.getWindowAncestor(this), sistema).setVisible(true);
        actualizar();
    }

    private void registrarIntervencion() {
        CasoSeguimiento c = obtenerCasoSeleccionado(true);
        if (c == null) {
            return;
        }
        if (!c.estaAbierto()) {
            UtilidadesInterfaz.mostrarError(this, "El caso está cerrado (" + c.getEstado()
                    + ") y no admite nuevas intervenciones.");
            return;
        }
        new FormularioIntervencion(SwingUtilities.getWindowAncestor(this), gestor, c).setVisible(true);
        actualizar();
    }

    private void verDetalle() {
        CasoSeguimiento c = obtenerCasoSeleccionado(true);
        if (c == null) {
            return;
        }
        String texto = "Caso N.° " + c.getNumero()
                + "\nEstudiante: " + c.getEstudiante().getNombreCompleto() + " (" + c.getEstudiante().getCedula() + ")"
                + "\nCurso: " + c.getEstudiante().getCursoCompleto() + " – Riesgo: " + c.getEstudiante().getNivelRiesgo()
                + "\nProfesional responsable: " + c.getProfesional()
                + "\nFecha de apertura: " + UtilidadesInterfaz.formatear(c.getFechaApertura())
                + "\nMotivo de la detección: " + c.getMotivoDeteccion()
                + "\nEstado: " + c.getEstado()
                + "\nIntentos seguidos sin respuesta del representante: " + c.contarIntentosSinRespuesta();
        if (c.getJustificacion() != null && !c.getJustificacion().isEmpty()) {
            texto += "\nJustificación (riesgo Bajo o Medio): " + c.getJustificacion();
        }
        if (c.getObservaciones() != null && !c.getObservaciones().isEmpty()) {
            texto += "\nObservaciones: " + c.getObservaciones();
        }
        if (!c.estaAbierto()) {
            texto += "\nFecha de cierre: " + UtilidadesInterfaz.formatear(c.getFechaCierre())
                    + "\nObservación final: " + c.getObservacionFinal();
        }
        mostrarTexto(texto, "Detalle del caso");
    }

    /** RF-09: lista cronológica de las intervenciones de los casos que se ven en la tabla. */
    private void verHistorialCronologico() {
        if (listaCasos.isEmpty()) {
            UtilidadesInterfaz.mostrarError(this, "No hay casos con los filtros actuales.");
            return;
        }
        String texto = "";
        for (CasoSeguimiento c : listaCasos) {
            texto += "CASO N.° " + c.getNumero() + " – " + c.getEstudiante().getNombreCompleto() + " – Estado: "
                    + c.getEstado() + " – Profesional: " + c.getProfesional().getNombreCompleto() + "\n";
            texto += "  " + UtilidadesInterfaz.formatear(c.getFechaApertura()) + "  Apertura del caso: "
                    + c.getMotivoDeteccion() + "\n";
            for (Intervencion i : c.getIntervencionesOrdenadas()) {
                texto += "  " + UtilidadesInterfaz.formatear(i.getFecha()) + "  " + i.getTipo() + " – "
                        + i.getResultado() + ": " + i.getObservaciones() + "\n";
            }
            if (!c.estaAbierto()) {
                texto += "  " + UtilidadesInterfaz.formatear(c.getFechaCierre()) + "  Cierre (" + c.getEstado() + "): "
                        + c.getObservacionFinal() + "\n";
            }
            texto += "\n";
        }
        texto += "Total de casos: " + listaCasos.size() + " | Abiertos: " + gestor.contarAbiertos(listaCasos)
                + " | Cerrados: " + gestor.contarCerrados(listaCasos);
        mostrarTexto(texto, "Historial cronológico (RF-09)");
    }

    private void verAvisos() {
        ArrayList<String> avisos = gestor.getAvisosCoordinador();
        if (avisos.isEmpty()) {
            UtilidadesInterfaz.mostrarMensaje(this, "No hay avisos para el coordinador.");
            return;
        }
        String texto = "";
        for (String aviso : avisos) {
            texto += "• " + aviso + "\n";
        }
        mostrarTexto(texto, "Avisos al coordinador");
    }

    private void mostrarTexto(String texto, String titulo) {
        JTextArea area = new JTextArea(texto, 18, 60);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setCaretPosition(0);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), titulo, JOptionPane.PLAIN_MESSAGE);
    }
}
