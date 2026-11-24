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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Pantalla del Módulo 1: búsqueda de estudiantes (RF-02), registro y edición (RF-01 y RF-02),
 * baja lógica (RF-02) y cálculo del riesgo con umbral configurable (RF-03).
 *
 * Distribución (BorderLayout): arriba los datos que usan los botones, en el centro
 * un área de texto con los resultados y abajo los botones.
 */
public class PanelEstudiantes extends JPanel {

    private GestorEstudiantes gestor;

    private JComboBox<String> comboCriterio;
    private JTextField campoBuscar;
    private JTextField campoCedula;
    private JComboBox<String> comboMotivo;
    private JTextArea area;

    public PanelEstudiantes(SistemaPuente sistema) {
        gestor = sistema.getGestorEstudiantes();
        setLayout(new BorderLayout());

        // ----- Arriba: fila 1 para buscar y fila 2 para elegir un estudiante por su cédula -----
        comboCriterio = new JComboBox<>(GestorEstudiantes.CRITERIOS_BUSQUEDA);
        campoBuscar = new JTextField(15);
        JButton botonBuscar = new JButton("Buscar");
        JButton botonTodos = new JButton("Mostrar todos");
        JPanel fila1 = new JPanel(new FlowLayout());
        fila1.add(new JLabel("Buscar por:"));
        fila1.add(comboCriterio);
        fila1.add(campoBuscar);
        fila1.add(botonBuscar);
        fila1.add(botonTodos);

        campoCedula = new JTextField(12);
        comboMotivo = new JComboBox<>(GestorEstudiantes.MOTIVOS_BAJA);
        JPanel fila2 = new JPanel(new FlowLayout());
        fila2.add(new JLabel("Cédula del estudiante:"));
        fila2.add(campoCedula);
        fila2.add(new JLabel("Motivo de baja:"));
        fila2.add(comboMotivo);

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

        // ----- Abajo: botones -----
        JButton botonNuevo = new JButton("Nuevo estudiante");
        JButton botonEditar = new JButton("Editar");
        JButton botonBaja = new JButton("Dar de baja");
        JButton botonFicha = new JButton("Ver ficha y riesgo");
        JButton botonRecalcular = new JButton("Recalcular riesgo");
        JButton botonUmbral = new JButton("Cambiar umbral");
        JPanel abajo = new JPanel(new FlowLayout());
        abajo.add(botonNuevo);
        abajo.add(botonEditar);
        abajo.add(botonBaja);
        abajo.add(botonFicha);
        abajo.add(botonRecalcular);
        abajo.add(botonUmbral);
        add(abajo, BorderLayout.SOUTH);

        // ----- Qué hace cada botón -----
        botonBuscar.addActionListener(e -> buscar());
        botonTodos.addActionListener(e -> mostrarTodos());
        botonNuevo.addActionListener(e -> nuevoEstudiante());
        botonEditar.addActionListener(e -> editarEstudiante());
        botonBaja.addActionListener(e -> darDeBaja());
        botonFicha.addActionListener(e -> verFicha());
        botonRecalcular.addActionListener(e -> recalcular());
        botonUmbral.addActionListener(e -> cambiarUmbral());

        mostrarTodos();
    }

    /** Escribe un texto en el área y vuelve al inicio para que se lea desde arriba. */
    private void mostrar(String texto) {
        area.setText(texto);
        area.setCaretPosition(0);
    }

    private void mostrarLista(ArrayList<Estudiante> lista) {
        String texto = "Umbral de ingreso por persona: USD "
                + String.format("%.2f", gestor.getUmbralIngresoPorPersona()) + "\n\n";
        int activos = 0;
        for (Estudiante e : lista) {
            texto += e.getCedula() + " | " + e.getNombreCompleto() + " | " + e.calcularEdad() + " años | "
                    + e.getCursoCompleto() + " | " + e.getFicha().getZona() + " | Ingreso por persona: $ "
                    + String.format("%.2f", e.getFicha().calcularIngresoPorPersona()) + " | Riesgo: "
                    + e.getNivelRiesgo() + " (" + e.getPuntajeRiesgo() + " puntos) | " + e.getEstado() + "\n";
            if (e.estaActivo()) {
                activos++;
            }
        }
        texto += "\nTotal: " + lista.size() + " estudiantes (" + activos + " activos).";
        mostrar(texto);
    }

    private void buscar() {
        mostrarLista(gestor.buscar((String) comboCriterio.getSelectedItem(), campoBuscar.getText()));
    }

    private void mostrarTodos() {
        campoBuscar.setText("");
        mostrarLista(gestor.getEstudiantes());
    }

    private void nuevoEstudiante() {
        FormularioEstudiante formulario = new FormularioEstudiante(gestor, null);
        formulario.setVisible(true);
        mostrarTodos();
    }

    private void editarEstudiante() {
        try {
            Estudiante e = gestor.obtenerEstudiante(campoCedula.getText().trim());
            if (!e.estaActivo()) {
                UtilidadesInterfaz.mostrarError(this, "No se puede modificar un estudiante inactivo.");
                return;
            }
            FormularioEstudiante formulario = new FormularioEstudiante(gestor, e);
            formulario.setVisible(true);
            mostrarTodos();
        } catch (PuenteException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }

    private void darDeBaja() {
        String motivo = (String) comboMotivo.getSelectedItem();
        try {
            Estudiante e = gestor.obtenerEstudiante(campoCedula.getText().trim());
            if (!UtilidadesInterfaz.confirmar(this, "¿Dar de baja a " + e.getNombreCompleto()
                    + "? Motivo: " + motivo + ".")) {
                return;
            }
            String mensaje = gestor.darDeBaja(e.getCedula(), motivo, LocalDate.now());
            UtilidadesInterfaz.mostrarMensaje(this, mensaje + "\nSu historial se conserva en el sistema.");
            mostrarTodos();
        } catch (PuenteException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }

    private void verFicha() {
        try {
            Estudiante e = gestor.obtenerEstudiante(campoCedula.getText().trim());
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
                texto += "\n\nBAJA\nFecha: " + UtilidadesInterfaz.formatear(e.getFechaBaja())
                        + " – Motivo: " + e.getMotivoBaja();
            }
            if (e.getObservaciones() != null && !e.getObservaciones().isEmpty()) {
                texto += "\n\nObservaciones: " + e.getObservaciones();
            }
            mostrar(texto);
        } catch (PuenteException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }

    private void recalcular() {
        UtilidadesInterfaz.mostrarMensaje(this, gestor.recalcularTodos());
        mostrarTodos();
    }

    private void cambiarUmbral() {
        String texto = JOptionPane.showInputDialog(this,
                "Nuevo umbral de ingreso mensual por persona (USD).\nSe recalculará el riesgo de todos los estudiantes:",
                String.format("%.2f", gestor.getUmbralIngresoPorPersona()));
        if (texto == null) {
            return;   // el usuario presionó Cancelar
        }
        try {
            String mensaje = gestor.cambiarUmbral(UtilidadesInterfaz.leerDecimal(texto, "El umbral"));
            UtilidadesInterfaz.mostrarMensaje(this, mensaje);
            mostrarTodos();
        } catch (PuenteException | IllegalArgumentException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }
}
