package interfaz;

import modelo.Calificacion;
import modelo.Estudiante;
import negocio.DatosDeEjemplo;
import negocio.GestorEstudiantes;
import negocio.GestorRendimiento;
import negocio.PuenteException;
import negocio.SistemaPuente;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Menú del Módulo 5 (André Garzón): registro de calificaciones (RF-13), promedio y
 * clasificación del rendimiento (RF-14), detección de bajo rendimiento (RF-15)
 * y acciones sugeridas (RF-16).
 *
 * El año lectivo, el periodo y el curso elegidos se guardan como atributos
 * y los usan todas las opciones del menú.
 */
public class MenuRendimiento {

    private GestorRendimiento gestor;
    private GestorEstudiantes gestorEstudiantes;

    private String anio;
    private int periodo;
    private String curso;   // texto vacío significa "todos los cursos"

    public MenuRendimiento(SistemaPuente sistema) {
        gestor = sistema.getGestorRendimiento();
        gestorEstudiantes = sistema.getGestorEstudiantes();
        anio = DatosDeEjemplo.ANIO_LECTIVO;
        periodo = 1;
        curso = "";
    }

    public void mostrar() {
        int opcion;
        do {
            Consola.titulo("5. RENDIMIENTO ACADÉMICO");
            System.out.println("Año lectivo: " + anio + " | Periodo: " + periodo + " | Curso: " + getNombreCurso());
            System.out.println();
            System.out.println("1. Ver todos los estudiantes activos");
            System.out.println("2. Detectar bajo rendimiento");
            System.out.println("3. Ver notas de un estudiante");
            System.out.println("4. Registrar nota");
            System.out.println("5. Corregir nota");
            System.out.println("6. Eliminar nota");
            System.out.println("7. Cambiar año lectivo, periodo y curso");
            System.out.println("0. Volver al menú principal");
            opcion = Consola.leerEntero("Elija una opción");

            switch (opcion) {
                case 1:
                    verTodos();
                    break;
                case 2:
                    mostrarLista("ESTUDIANTES QUE REQUIEREN ATENCIÓN (RF-15)",
                            gestor.detectarBajoRendimiento(gestorEstudiantes.getEstudiantes(), curso, anio, periodo));
                    break;
                case 3:
                    verNotas();
                    break;
                case 4:
                    registrarNota();
                    break;
                case 5:
                    corregirNota();
                    break;
                case 6:
                    eliminarNota();
                    break;
                case 7:
                    cambiarPeriodo();
                    break;
                case 0:
                    break;
                default:
                    Consola.mostrarError("Opción no válida.");
            }
            if (opcion != 0) {
                Consola.pausar();
            }
        } while (opcion != 0);
    }

    private String getNombreCurso() {
        if (curso.isEmpty()) {
            return "Todos";
        }
        return curso;
    }

    private void cambiarPeriodo() {
        anio = Consola.leerTexto("Año lectivo", anio);
        periodo = Consola.leerEntero("Periodo (1 a " + GestorRendimiento.PERIODOS_POR_ANIO + ")", String.valueOf(periodo));
        while (periodo < 1 || periodo > GestorRendimiento.PERIODOS_POR_ANIO) {
            Consola.mostrarError("El periodo debe estar entre 1 y " + GestorRendimiento.PERIODOS_POR_ANIO + ".");
            periodo = Consola.leerEntero("Periodo (1 a " + GestorRendimiento.PERIODOS_POR_ANIO + ")");
        }
        // La opción 1 es "Todos"; las demás son los cursos
        String[] opciones = new String[Consola.CURSOS.length + 1];
        opciones[0] = "Todos";
        for (int i = 0; i < Consola.CURSOS.length; i++) {
            opciones[i + 1] = Consola.CURSOS[i];
        }
        String elegido = Consola.elegirOpcion("Curso", opciones, getNombreCurso());
        if (elegido.equals("Todos")) {
            curso = "";
        } else {
            curso = elegido;
        }
        System.out.println("Ahora se trabaja con: año " + anio + ", periodo " + periodo + ", curso " + getNombreCurso() + ".");
    }

    /** Escribe una línea por estudiante con su promedio, sus notas bajo 7 y su clasificación. */
    private void mostrarLista(String titulo, ArrayList<Estudiante> lista) {
        System.out.println();
        System.out.println(titulo + " – año " + anio + ", periodo " + periodo + " (nota mínima para aprobar: 7,00)");
        System.out.println();
        int bajo = 0;
        int observacion = 0;
        for (Estudiante e : lista) {
            String clasificacion = gestor.clasificarRendimiento(e, anio, periodo);
            String linea = e.getNombreCompleto() + " (" + e.getCedula() + ") | " + e.getCursoCompleto()
                    + " | Riesgo " + e.getNivelRiesgo();
            if (!GestorRendimiento.SIN_CALIFICACIONES.equals(clasificacion)) {
                linea += " | Promedio: " + String.format("%.2f", gestor.calcularPromedio(e, anio, periodo))
                        + " | Notas bajo 7: " + gestor.contarAsignaturasBajoMinimo(e, anio, periodo);
            }
            linea += " | " + clasificacion;
            System.out.println(linea);
            if (GestorRendimiento.RENDIMIENTO_BAJO.equals(clasificacion)) {
                bajo++;
            } else if (GestorRendimiento.RENDIMIENTO_EN_OBSERVACION.equals(clasificacion)) {
                observacion++;
            }
        }
        System.out.println();
        System.out.println("Total: " + lista.size() + " | Bajo rendimiento: " + bajo + " | En observación: " + observacion);
    }

    private void verTodos() {
        ArrayList<Estudiante> lista = new ArrayList<>();
        for (Estudiante e : gestorEstudiantes.listarActivos()) {
            if (curso.isEmpty() || e.getCurso().equals(curso)) {
                lista.add(e);
            }
        }
        mostrarLista("ESTUDIANTES ACTIVOS", lista);
    }

    /** Muestra las notas del estudiante en el periodo elegido, su promedio y las acciones sugeridas. */
    private void mostrarNotas(Estudiante e) {
        System.out.println();
        System.out.println("NOTAS DE " + e.getNombreCompleto() + " – año " + anio + ", periodo " + periodo);
        System.out.println();
        for (Calificacion c : gestor.buscarCalificaciones(e, anio, periodo)) {
            String situacion = "Aprobada";
            if (c.estaBajoMinimo()) {
                situacion = "Alerta temprana";
            }
            System.out.println(c.getAsignatura() + ": " + String.format("%.2f", c.getNota()) + " (" + situacion + ")");
        }
        String clasificacion = gestor.clasificarRendimiento(e, anio, periodo);
        System.out.println();
        if (!GestorRendimiento.SIN_CALIFICACIONES.equals(clasificacion)) {
            System.out.println("Promedio: " + String.format("%.2f", gestor.calcularPromedio(e, anio, periodo)));
        }
        System.out.println("Rendimiento: " + clasificacion);
        System.out.println();
        System.out.println("Acciones sugeridas: " + gestor.generarRecomendacion(e, anio, periodo));
    }

    // Pide la cédula del estudiante y lo busca (lanza un error si no existe)
    private Estudiante leerEstudiante() throws PuenteException {
        return gestorEstudiantes.obtenerEstudiante(Consola.leerTexto("Cédula del estudiante"));
    }

    // Pide la asignatura y busca su nota en el periodo elegido (lanza un error si no existe)
    private Calificacion leerCalificacion(Estudiante e) throws PuenteException {
        String asignatura = Consola.elegirOpcion("Asignatura", GestorRendimiento.ASIGNATURAS);
        for (Calificacion c : gestor.buscarCalificaciones(e, anio, periodo)) {
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
            Consola.mostrarError(ex.getMessage());
        }
    }

    private void registrarNota() {
        try {
            Estudiante e = leerEstudiante();
            String asignatura = Consola.elegirOpcion("Asignatura", GestorRendimiento.ASIGNATURAS);
            double nota = Consola.leerDecimal("Nota (0 a 10)");
            System.out.println(gestor.registrarCalificacion(e, anio, periodo, asignatura, nota, LocalDate.now()));
            mostrarNotas(e);
        } catch (PuenteException ex) {
            Consola.mostrarError(ex.getMessage());
        }
    }

    private void corregirNota() {
        try {
            Estudiante e = leerEstudiante();
            Calificacion c = leerCalificacion(e);
            System.out.println("Nota actual de " + c.getAsignatura() + ": " + String.format("%.2f", c.getNota()));
            double nuevaNota = Consola.leerDecimal("Nueva nota (0 a 10)");
            System.out.println(gestor.actualizarNota(c, nuevaNota));
            mostrarNotas(e);
        } catch (PuenteException ex) {
            Consola.mostrarError(ex.getMessage());
        }
    }

    private void eliminarNota() {
        try {
            Estudiante e = leerEstudiante();
            Calificacion c = leerCalificacion(e);
            if (!Consola.confirmar("¿Eliminar la nota de " + c.getAsignatura() + " ("
                    + String.format("%.2f", c.getNota()) + ") de " + e.getNombreCompleto() + "?")) {
                System.out.println("Operación cancelada.");
                return;
            }
            System.out.println(gestor.eliminarCalificacion(c));
            mostrarNotas(e);
        } catch (PuenteException ex) {
            Consola.mostrarError(ex.getMessage());
        }
    }
}
