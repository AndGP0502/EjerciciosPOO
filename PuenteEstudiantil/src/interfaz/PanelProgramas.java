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
 * Pantalla del Módulo 2: programas de apoyo (RF-04 y RF-05) y asignación
 * a estudiantes con lista de espera (RF-06).
 *
 * Distribución (BorderLayout): arriba los datos que usan los botones, en el centro
 * un área de texto con los resultados y abajo los botones (dos filas).
 */
public class PanelProgramas extends JPanel {

    private GestorProgramas gestor;
    private GestorEstudiantes gestorEstudiantes;

    private JComboBox<String> comboCriterio;
    private JTextField campoBuscar;
    private JTextField campoCodigo;
    private JTextField campoCedula;
    private JTextArea area;

    public PanelProgramas(SistemaPuente sistema) {
        gestor = sistema.getGestorProgramas();
        gestorEstudiantes = sistema.getGestorEstudiantes();
        setLayout(new BorderLayout());

        // ----- Arriba: fila 1 para buscar y fila 2 para elegir programa y estudiante -----
        comboCriterio = new JComboBox<>(GestorProgramas.CRITERIOS_BUSQUEDA);
        campoBuscar = new JTextField(15);
        JButton botonBuscar = new JButton("Buscar");
        JButton botonTodos = new JButton("Mostrar todos");
        JPanel fila1 = new JPanel(new FlowLayout());
        fila1.add(new JLabel("Buscar por:"));
        fila1.add(comboCriterio);
        fila1.add(campoBuscar);
        fila1.add(botonBuscar);
        fila1.add(botonTodos);

        campoCodigo = new JTextField(5);
        campoCedula = new JTextField(12);
        JPanel fila2 = new JPanel(new FlowLayout());
        fila2.add(new JLabel("Código del programa:"));
        fila2.add(campoCodigo);
        fila2.add(new JLabel("Cédula del estudiante:"));
        fila2.add(campoCedula);

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

        // ----- Abajo: fila de botones de programas y fila de botones de asignaciones -----
        JButton botonNuevo = new JButton("Nuevo programa");
        JButton botonEditar = new JButton("Editar programa");
        JButton botonCerrar = new JButton("Cerrar programa");
        JButton botonVer = new JButton("Ver asignaciones");
        JPanel botonesProgramas = new JPanel(new FlowLayout());
        botonesProgramas.add(botonNuevo);
        botonesProgramas.add(botonEditar);
        botonesProgramas.add(botonCerrar);
        botonesProgramas.add(botonVer);

        JButton botonAsignar = new JButton("Asignar a estudiante");
        JButton botonFinalizar = new JButton("Finalizar asignación");
        JButton botonEspera = new JButton("Dar cupo al primero en espera");
        JPanel botonesAsignaciones = new JPanel(new FlowLayout());
        botonesAsignaciones.add(botonAsignar);
        botonesAsignaciones.add(botonFinalizar);
        botonesAsignaciones.add(botonEspera);

        JPanel abajo = new JPanel(new GridLayout(2, 1));
        abajo.add(botonesProgramas);
        abajo.add(botonesAsignaciones);
        add(abajo, BorderLayout.SOUTH);

        // ----- Qué hace cada botón -----
        botonBuscar.addActionListener(e -> buscar());
        botonTodos.addActionListener(e -> mostrarTodos());
        botonNuevo.addActionListener(e -> nuevoPrograma());
        botonEditar.addActionListener(e -> editarPrograma());
        botonCerrar.addActionListener(e -> cerrarPrograma());
        botonVer.addActionListener(e -> verAsignaciones());
        botonAsignar.addActionListener(e -> asignarEstudiante());
        botonFinalizar.addActionListener(e -> finalizarAsignacion());
        botonEspera.addActionListener(e -> asignarPrimeroEnEspera());

        mostrarTodos();
    }

    /** Escribe un texto en el área y vuelve al inicio para que se lea desde arriba. */
    private void mostrar(String texto) {
        area.setText(texto);
        area.setCaretPosition(0);
    }

    private void mostrarLista(ArrayList<ProgramaApoyo> lista) {
        String texto = "PROGRAMAS DE APOYO\n\n";
        for (ProgramaApoyo p : lista) {
            String disponibles = "–";   // un programa cerrado ya no tiene cupos disponibles
            if (p.estaVigente()) {
                disponibles = String.valueOf(p.getCuposDisponibles());
            }
            texto += "Código " + p.getCodigo() + " | " + p.getNombre() + " | " + p.getTipo()
                    + " | Cupos: " + p.getCupos() + " (asignados " + p.contarAsignacionesVigentes()
                    + ", disponibles " + disponibles + ") | En espera: " + p.getListaEspera().size()
                    + " | Vigencia: " + UtilidadesInterfaz.formatear(p.getFechaInicio()) + " – "
                    + UtilidadesInterfaz.formatear(p.getFechaFin()) + " | Nivel mínimo: " + p.getNivelMinimo()
                    + " | " + p.getEstado() + "\n";
        }
        texto += "\nTotal: " + lista.size() + " programas.";
        mostrar(texto);
    }

    private void buscar() {
        mostrarLista(gestor.buscar((String) comboCriterio.getSelectedItem(), campoBuscar.getText()));
    }

    private void mostrarTodos() {
        campoBuscar.setText("");
        mostrarLista(gestor.getProgramas());
    }

    /** Muestra las asignaciones vigentes, la lista de espera en orden y el historial del programa. */
    private void mostrarAsignaciones(ProgramaApoyo p) {
        String texto = "ASIGNACIONES DEL PROGRAMA «" + p.getNombre() + "» (código " + p.getCodigo() + ")\n"
                + "Vigentes: " + p.contarAsignacionesVigentes() + " de " + p.getCupos() + " cupos | En lista de espera: "
                + p.getListaEspera().size() + "\n";

        texto += "\nVIGENTES\n";
        for (AsignacionApoyo a : p.getAsignaciones()) {
            if (a.estaVigente()) {
                Estudiante e = a.getEstudiante();
                texto += "  " + e.getNombreCompleto() + " (" + e.getCedula() + ") – " + e.getCursoCompleto()
                        + " – riesgo " + e.getNivelRiesgo() + " – asignado el "
                        + UtilidadesInterfaz.formatear(a.getFechaAsignacion()) + "\n";
            }
        }

        texto += "\nLISTA DE ESPERA (en orden)\n";
        int posicion = 1;
        for (AsignacionApoyo a : p.getListaEspera()) {
            Estudiante e = a.getEstudiante();
            texto += "  " + posicion + ". " + e.getNombreCompleto() + " (" + e.getCedula() + ") – riesgo "
                    + e.getNivelRiesgo() + " – solicitó el " + UtilidadesInterfaz.formatear(a.getFechaSolicitud()) + "\n";
            posicion++;
        }

        texto += "\nHISTORIAL (finalizadas o canceladas)\n";
        for (AsignacionApoyo a : p.getAsignaciones()) {
            if (!a.estaVigente() && !a.estaEnEspera()) {
                texto += "  " + a.getEstudiante().getNombreCompleto() + " – " + a.getEstado() + "\n";
            }
        }
        mostrar(texto);
    }

    // Busca el programa con el código escrito arriba (lanza un error si no existe)
    private ProgramaApoyo leerPrograma() throws PuenteException {
        int codigo = UtilidadesInterfaz.leerEntero(campoCodigo.getText(), "El código del programa");
        return gestor.obtenerPrograma(codigo);
    }

    // Busca el estudiante con la cédula escrita arriba (lanza un error si no existe)
    private Estudiante leerEstudiante() throws PuenteException {
        return gestorEstudiantes.obtenerEstudiante(campoCedula.getText().trim());
    }

    private void verAsignaciones() {
        try {
            mostrarAsignaciones(leerPrograma());
        } catch (PuenteException | IllegalArgumentException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }

    private void nuevoPrograma() {
        FormularioPrograma formulario = new FormularioPrograma(gestor, null);
        formulario.setVisible(true);
        mostrarTodos();
    }

    private void editarPrograma() {
        try {
            ProgramaApoyo p = leerPrograma();
            if (!p.estaVigente()) {
                UtilidadesInterfaz.mostrarError(this, "No se puede modificar un programa cerrado.");
                return;
            }
            FormularioPrograma formulario = new FormularioPrograma(gestor, p);
            formulario.setVisible(true);
            mostrarTodos();
        } catch (PuenteException | IllegalArgumentException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }

    private void cerrarPrograma() {
        try {
            ProgramaApoyo p = leerPrograma();
            if (!UtilidadesInterfaz.confirmar(this, "¿Cerrar el programa «" + p.getNombre()
                    + "»? Las asignaciones vigentes pasarán a Finalizada y la lista de espera se cancelará.")) {
                return;
            }
            UtilidadesInterfaz.mostrarMensaje(this, gestor.cerrarPrograma(p.getCodigo()));
            mostrarTodos();
        } catch (PuenteException | IllegalArgumentException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }

    private void asignarEstudiante() {
        try {
            ProgramaApoyo p = leerPrograma();
            Estudiante e = leerEstudiante();
            UtilidadesInterfaz.mostrarMensaje(this, gestor.asignarApoyo(e, p, LocalDate.now()));
            mostrarAsignaciones(p);
        } catch (PuenteException | IllegalArgumentException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }

    private void finalizarAsignacion() {
        try {
            ProgramaApoyo p = leerPrograma();
            Estudiante e = leerEstudiante();
            AsignacionApoyo asignacion = gestor.buscarAsignacionActiva(e, p);
            if (asignacion == null) {
                UtilidadesInterfaz.mostrarError(this, e.getNombreCompleto() + " no tiene una asignación vigente en este programa.");
                return;
            }
            UtilidadesInterfaz.mostrarMensaje(this, gestor.finalizarAsignacion(asignacion));
            mostrarAsignaciones(p);
        } catch (PuenteException | IllegalArgumentException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }

    private void asignarPrimeroEnEspera() {
        try {
            ProgramaApoyo p = leerPrograma();
            UtilidadesInterfaz.mostrarMensaje(this, gestor.asignarPrimeroEnEspera(p, LocalDate.now()));
            mostrarAsignaciones(p);
        } catch (PuenteException | IllegalArgumentException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }
}
