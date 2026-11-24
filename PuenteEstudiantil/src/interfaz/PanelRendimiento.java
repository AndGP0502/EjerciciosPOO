package interfaz;

import modelo.Calificacion;
import modelo.Estudiante;
import negocio.GestorEstudiantes;
import negocio.GestorRendimiento;
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
 * Pantalla del Módulo 5: registro de notas por periodo (RF-13), corrección y eliminación (RF-14)
 * y detección de bajo rendimiento con acciones sugeridas (RF-15).
 *
 * Distribución (BorderLayout): arriba el periodo y los datos de la nota, en el centro
 * un área de texto con los resultados y abajo los botones.
 */
public class PanelRendimiento extends JPanel {

    private GestorRendimiento gestor;
    private GestorEstudiantes gestorEstudiantes;

    private JTextField campoAnio;
    private JComboBox<String> comboPeriodo;
    private JComboBox<String> comboCurso;
    private JTextField campoCedula;
    private JComboBox<String> comboAsignatura;
    private JTextField campoNota;
    private JTextArea area;

    public PanelRendimiento(SistemaPuente sistema) {
        gestor = sistema.getGestorRendimiento();
        gestorEstudiantes = sistema.getGestorEstudiantes();
        setLayout(new BorderLayout());

        // ----- Arriba: fila 1 con el periodo y el curso, fila 2 con los datos de la nota -----
        campoAnio = new JTextField("2026-2027", 8);
        comboPeriodo = new JComboBox<>(new String[]{"1", "2", "3"});
        comboCurso = new JComboBox<>();
        comboCurso.addItem("Todos");
        for (String curso : UtilidadesInterfaz.CURSOS) {
            comboCurso.addItem(curso);
        }
        JButton botonTodos = new JButton("Ver todos");
        JButton botonDetectar = new JButton("Detectar bajo rendimiento");
        JPanel fila1 = new JPanel(new FlowLayout());
        fila1.add(new JLabel("Año lectivo:"));
        fila1.add(campoAnio);
        fila1.add(new JLabel("Periodo:"));
        fila1.add(comboPeriodo);
        fila1.add(new JLabel("Curso:"));
        fila1.add(comboCurso);
        fila1.add(botonTodos);
        fila1.add(botonDetectar);

        campoCedula = new JTextField(12);
        comboAsignatura = new JComboBox<>(GestorRendimiento.ASIGNATURAS);
        campoNota = new JTextField(5);
        JPanel fila2 = new JPanel(new FlowLayout());
        fila2.add(new JLabel("Cédula del estudiante:"));
        fila2.add(campoCedula);
        fila2.add(new JLabel("Asignatura:"));
        fila2.add(comboAsignatura);
        fila2.add(new JLabel("Nota (0 a 10):"));
        fila2.add(campoNota);

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
        JButton botonVerNotas = new JButton("Ver notas del estudiante");
        JButton botonRegistrar = new JButton("Registrar nota");
        JButton botonCorregir = new JButton("Corregir nota");
        JButton botonEliminar = new JButton("Eliminar nota");
        JPanel abajo = new JPanel(new FlowLayout());
        abajo.add(botonVerNotas);
        abajo.add(botonRegistrar);
        abajo.add(botonCorregir);
        abajo.add(botonEliminar);
        add(abajo, BorderLayout.SOUTH);

        // ----- Qué hace cada botón -----
        botonTodos.addActionListener(e -> verTodos());
        botonDetectar.addActionListener(e -> detectarBajoRendimiento());
        botonVerNotas.addActionListener(e -> verNotas());
        botonRegistrar.addActionListener(e -> registrarNota());
        botonCorregir.addActionListener(e -> corregirNota());
        botonEliminar.addActionListener(e -> eliminarNota());

        verTodos();
    }

    /** Escribe un texto en el área y vuelve al inicio para que se lea desde arriba. */
    private void mostrar(String texto) {
        area.setText(texto);
        area.setCaretPosition(0);
    }

    private String getAnio() {
        return campoAnio.getText().trim();
    }

    private int getPeriodo() {
        return comboPeriodo.getSelectedIndex() + 1;
    }

    /** "Todos" (posición 0) se convierte en texto vacío, que significa "sin filtrar por curso". */
    private String getCurso() {
        if (comboCurso.getSelectedIndex() <= 0) {
            return "";
        }
        return (String) comboCurso.getSelectedItem();
    }

    /** Escribe una línea por estudiante con su promedio, sus notas bajo 7 y su clasificación. */
    private void mostrarLista(String titulo, ArrayList<Estudiante> lista) {
        String texto = titulo + " – año " + getAnio() + ", periodo " + getPeriodo()
                + " (nota mínima para aprobar: 7,00)\n\n";
        int bajo = 0;
        int observacion = 0;
        for (Estudiante e : lista) {
            String clasificacion = gestor.clasificarRendimiento(e, getAnio(), getPeriodo());
            texto += e.getNombreCompleto() + " (" + e.getCedula() + ") | " + e.getCursoCompleto()
                    + " | Riesgo " + e.getNivelRiesgo();
            if (!GestorRendimiento.SIN_CALIFICACIONES.equals(clasificacion)) {
                texto += " | Promedio: " + String.format("%.2f", gestor.calcularPromedio(e, getAnio(), getPeriodo()))
                        + " | Notas bajo 7: " + gestor.contarAsignaturasBajoMinimo(e, getAnio(), getPeriodo());
            }
            texto += " | " + clasificacion + "\n";
            if (GestorRendimiento.RENDIMIENTO_BAJO.equals(clasificacion)) {
                bajo++;
            } else if (GestorRendimiento.RENDIMIENTO_EN_OBSERVACION.equals(clasificacion)) {
                observacion++;
            }
        }
        texto += "\nTotal: " + lista.size() + " | Bajo rendimiento: " + bajo + " | En observación: " + observacion;
        mostrar(texto);
    }

    private void verTodos() {
        ArrayList<Estudiante> lista = new ArrayList<>();
        for (Estudiante e : gestorEstudiantes.listarActivos()) {
            if (getCurso().isEmpty() || e.getCurso().equals(getCurso())) {
                lista.add(e);
            }
        }
        mostrarLista("ESTUDIANTES ACTIVOS", lista);
    }

    private void detectarBajoRendimiento() {
        mostrarLista("ESTUDIANTES QUE REQUIEREN ATENCIÓN (RF-15)",
                gestor.detectarBajoRendimiento(gestorEstudiantes.getEstudiantes(), getCurso(), getAnio(), getPeriodo()));
    }

    /** Muestra las notas del estudiante en el periodo elegido, su promedio y las acciones sugeridas. */
    private void mostrarNotas(Estudiante e) {
        String texto = "NOTAS DE " + e.getNombreCompleto() + " – año " + getAnio() + ", periodo " + getPeriodo() + "\n\n";
        for (Calificacion c : gestor.buscarCalificaciones(e, getAnio(), getPeriodo())) {
            String situacion = "Aprobada";
            if (c.estaBajoMinimo()) {
                situacion = "Alerta temprana";
            }
            texto += c.getAsignatura() + ": " + String.format("%.2f", c.getNota()) + " (" + situacion + ")\n";
        }
        String clasificacion = gestor.clasificarRendimiento(e, getAnio(), getPeriodo());
        if (!GestorRendimiento.SIN_CALIFICACIONES.equals(clasificacion)) {
            texto += "\nPromedio: " + String.format("%.2f", gestor.calcularPromedio(e, getAnio(), getPeriodo()));
        }
        texto += "\nRendimiento: " + clasificacion
                + "\n\nAcciones sugeridas: " + gestor.generarRecomendacion(e, getAnio(), getPeriodo());
        mostrar(texto);
    }

    // Busca el estudiante con la cédula escrita arriba (lanza un error si no existe)
    private Estudiante leerEstudiante() throws PuenteException {
        return gestorEstudiantes.obtenerEstudiante(campoCedula.getText().trim());
    }

    // Busca la nota de la asignatura elegida en el periodo elegido (lanza un error si no existe)
    private Calificacion leerCalificacion(Estudiante e) throws PuenteException {
        String asignatura = (String) comboAsignatura.getSelectedItem();
        for (Calificacion c : gestor.buscarCalificaciones(e, getAnio(), getPeriodo())) {
            if (c.getAsignatura().equals(asignatura)) {
                return c;
            }
        }
        throw new PuenteException(e.getNombreCompleto() + " no tiene nota de " + asignatura + " en ese periodo.");
    }

    private void verNotas() {
        try {
            mostrarNotas(leerEstudiante());
        } catch (PuenteException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }

    private void registrarNota() {
        try {
            Estudiante e = leerEstudiante();
            double nota = UtilidadesInterfaz.leerDecimal(campoNota.getText(), "La nota");
            String asignatura = (String) comboAsignatura.getSelectedItem();
            UtilidadesInterfaz.mostrarMensaje(this, gestor.registrarCalificacion(e, getAnio(), getPeriodo(), asignatura,
                    nota, LocalDate.now()));
            mostrarNotas(e);
        } catch (PuenteException | IllegalArgumentException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }

    private void corregirNota() {
        try {
            Estudiante e = leerEstudiante();
            Calificacion c = leerCalificacion(e);
            double nuevaNota = UtilidadesInterfaz.leerDecimal(campoNota.getText(), "La nota");
            UtilidadesInterfaz.mostrarMensaje(this, gestor.actualizarNota(c, nuevaNota));
            mostrarNotas(e);
        } catch (PuenteException | IllegalArgumentException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }

    private void eliminarNota() {
        try {
            Estudiante e = leerEstudiante();
            Calificacion c = leerCalificacion(e);
            if (!UtilidadesInterfaz.confirmar(this, "¿Eliminar la nota de " + c.getAsignatura() + " ("
                    + String.format("%.2f", c.getNota()) + ") de " + e.getNombreCompleto() + "?")) {
                return;
            }
            UtilidadesInterfaz.mostrarMensaje(this, gestor.eliminarCalificacion(c));
            mostrarNotas(e);
        } catch (PuenteException ex) {
            UtilidadesInterfaz.mostrarError(this, ex.getMessage());
        }
    }
}
