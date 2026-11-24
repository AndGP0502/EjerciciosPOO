package interfaz;

import modelo.CasoSeguimiento;
import modelo.Intervencion;
import modelo.ProfesionalDece;
import negocio.GestorCasos;
import negocio.SistemaPuente;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Pantalla del Módulo 3: casos de seguimiento (RF-07), intervenciones y cambio
 * de estado (RF-08) y consulta del historial con filtros (RF-09).
 *
 * Distribución (BorderLayout): arriba los filtros del historial, en el centro
 * un área de texto con los resultados y abajo los botones.
 */
public class PanelCasos extends JPanel {

    private SistemaPuente sistema;
    private GestorCasos gestor;

    private JTextField campoEstudiante;
    private JComboBox<String> comboProfesional;
    private JComboBox<String> comboEstado;
    private JTextField campoDesde;
    private JTextField campoHasta;
    private JTextField campoNumero;
    private JButton botonAvisos;
    private JTextArea area;

    public PanelCasos(SistemaPuente sistema) {
        this.sistema = sistema;
        gestor = sistema.getGestorCasos();
        setLayout(new BorderLayout());

        // ----- Arriba: filtros del historial en dos filas -----
        campoEstudiante = new JTextField(12);
        comboProfesional = new JComboBox<>();
        comboProfesional.addItem("Todos");
        for (ProfesionalDece p : sistema.getProfesionales()) {
            comboProfesional.addItem(p.getNombreCompleto());
        }
        comboEstado = new JComboBox<>();
        comboEstado.addItem("Todos");
        for (String estado : CasoSeguimiento.ESTADOS) {
            comboEstado.addItem(estado);
        }
        JPanel fila1 = new JPanel(new FlowLayout());
        fila1.add(new JLabel("Estudiante (nombre o cédula):"));
        fila1.add(campoEstudiante);
        fila1.add(new JLabel("Profesional:"));
        fila1.add(comboProfesional);
        fila1.add(new JLabel("Estado:"));
        fila1.add(comboEstado);

        campoDesde = new JTextField(8);
        campoHasta = new JTextField(8);
        JButton botonConsultar = new JButton("Consultar historial");
        JButton botonLimpiar = new JButton("Limpiar filtros");
        JPanel fila2 = new JPanel(new FlowLayout());
        fila2.add(new JLabel("Apertura desde (dd/mm/aaaa):"));
        fila2.add(campoDesde);
        fila2.add(new JLabel("hasta:"));
        fila2.add(campoHasta);
        fila2.add(botonConsultar);
        fila2.add(botonLimpiar);

        JPanel arriba = new JPanel(new GridLayout(2, 1));
        arriba.add(fila1);
        arriba.add(fila2);
        add(arriba, BorderLayout.NORTH);

        // ----- Centro: área de texto donde se muestran los resultados -----
        area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        add(new JScrollPane(area), BorderLayout.CENTER);

        // ----- Abajo: botones (los de un caso usan el número escrito) -----
        campoNumero = new JTextField(4);
        JButton botonAbrir = new JButton("Abrir caso");
        JButton botonIntervencion = new JButton("Registrar intervención");
        JButton botonDetalle = new JButton("Ver detalle");
        botonAvisos = new JButton("Avisos al coordinador");
        JPanel abajo = new JPanel(new FlowLayout());
        abajo.add(botonAbrir);
        abajo.add(new JLabel("N.° de caso:"));
        abajo.add(campoNumero);
        abajo.add(botonIntervencion);
        abajo.add(botonDetalle);
        abajo.add(botonAvisos);
        add(abajo, BorderLayout.SOUTH);

        // ----- Qué hace cada botón -----
        botonConsultar.addActionListener(e -> consultarHistorial());
        botonLimpiar.addActionListener(e -> limpiarFiltros());
        botonAbrir.addActionListener(e -> abrirCaso());
        botonIntervencion.addActionListener(e -> registrarIntervencion());
        botonDetalle.addActionListener(e -> verDetalle());
        botonAvisos.addActionListener(e -> verAvisos());

        consultarHistorial();
    }

    /** Escribe un texto en el área y vuelve al inicio para que se lea desde arriba. */
    private void mostrar(String texto) {
        area.setText(texto);
        area.setCaretPosition(0);
    }

    /** RF-09: muestra los casos que cumplen los filtros, cada uno con sus intervenciones en orden de fecha. */
    private void consultarHistorial() {
        ArrayList<CasoSeguimiento> lista;
        try {
            LocalDate desde = UtilidadesInterfaz.leerFechaOpcional(campoDesde.getText(), "La fecha «desde»");
            LocalDate hasta = UtilidadesInterfaz.leerFechaOpcional(campoHasta.getText(), "La fecha «hasta»");
            lista = gestor.consultarHistorial(campoEstudiante.getText(), obtenerProfesionalFiltro(), desde, hasta,
                    (String) comboEstado.getSelectedItem());
        } catch (IllegalArgumentException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
            return;
        }
        String texto = "HISTORIAL DE CASOS (RF-09)\n\n";
        for (CasoSeguimiento c : lista) {
            texto += "CASO N.° " + c.getNumero() + " – " + c.getEstudiante().getNombreCompleto() + " – "
                    + c.getEstudiante().getCursoCompleto() + " – Riesgo " + c.getEstudiante().getNivelRiesgo()
                    + " – Estado: " + c.getEstado() + " – Profesional: " + c.getProfesional().getNombreCompleto() + "\n";
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
        texto += "Total de casos: " + lista.size() + " | Abiertos: " + gestor.contarAbiertos(lista)
                + " | Cerrados: " + gestor.contarCerrados(lista);
        mostrar(texto);
        botonAvisos.setText("Avisos al coordinador (" + gestor.getAvisosCoordinador().size() + ")");
    }

    private void limpiarFiltros() {
        campoEstudiante.setText("");
        comboProfesional.setSelectedIndex(0);
        comboEstado.setSelectedIndex(0);
        campoDesde.setText("");
        campoHasta.setText("");
        consultarHistorial();
    }

    /** La posición 0 del combo es "Todos"; las demás siguen el orden de la lista de profesionales. */
    private ProfesionalDece obtenerProfesionalFiltro() {
        int indice = comboProfesional.getSelectedIndex();
        if (indice <= 0) {
            return null;
        }
        return sistema.getProfesionales().get(indice - 1);
    }

    /** Busca el caso con el número escrito abajo. Devuelve null y avisa si no existe. */
    private CasoSeguimiento leerCaso() {
        int numero;
        try {
            numero = UtilidadesInterfaz.leerEntero(campoNumero.getText(), "El número de caso");
        } catch (IllegalArgumentException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
            return null;
        }
        for (CasoSeguimiento c : gestor.getCasos()) {
            if (c.getNumero() == numero) {
                return c;
            }
        }
        UtilidadesInterfaz.mostrarError(this, "No existe el caso N.° " + numero + ".");
        return null;
    }

    private void abrirCaso() {
        FormularioCaso formulario = new FormularioCaso(sistema);
        formulario.setVisible(true);
        consultarHistorial();
    }

    private void registrarIntervencion() {
        CasoSeguimiento c = leerCaso();
        if (c == null) {
            return;
        }
        if (!c.estaAbierto()) {
            UtilidadesInterfaz.mostrarError(this, "El caso está cerrado (" + c.getEstado()
                    + ") y no admite nuevas intervenciones.");
            return;
        }
        FormularioIntervencion formulario = new FormularioIntervencion(gestor, c);
        formulario.setVisible(true);
        verDetalle();
    }

    private void verDetalle() {
        CasoSeguimiento c = leerCaso();
        if (c == null) {
            return;
        }
        String texto = "DETALLE DEL CASO N.° " + c.getNumero()
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
        texto += "\n\nINTERVENCIONES (de la más antigua a la más reciente)\n";
        for (Intervencion i : c.getIntervencionesOrdenadas()) {
            texto += "  " + UtilidadesInterfaz.formatear(i.getFecha()) + "  " + i.getTipo() + " – "
                    + i.getResultado() + ": " + i.getObservaciones() + "\n";
        }
        if (!c.estaAbierto()) {
            texto += "\nFecha de cierre: " + UtilidadesInterfaz.formatear(c.getFechaCierre())
                    + "\nObservación final: " + c.getObservacionFinal();
        }
        mostrar(texto);
        botonAvisos.setText("Avisos al coordinador (" + gestor.getAvisosCoordinador().size() + ")");
    }

    private void verAvisos() {
        ArrayList<String> avisos = gestor.getAvisosCoordinador();
        String texto = "AVISOS AL COORDINADOR\n\n";
        if (avisos.isEmpty()) {
            texto += "No hay avisos para el coordinador.";
        }
        for (String aviso : avisos) {
            texto += "- " + aviso + "\n";
        }
        mostrar(texto);
    }
}
