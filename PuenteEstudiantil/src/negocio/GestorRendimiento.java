package negocio;

import modelo.Calificacion;
import modelo.Estudiante;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Módulo 5 (André Garzón): rendimiento académico.
 * Cubre RF-13 (registrar calificaciones), RF-14 (consultar, actualizar y eliminar)
 * y RF-15 (detectar bajo rendimiento).
 *
 * La escala es sobre 10 y se aprueba con 7,00. Una nota menor a 7,00 activa una alerta temprana:
 * el colegio debe notificar al representante y aplicar refuerzo pedagógico.
 */
public class GestorRendimiento {

    public static final int PERIODOS_POR_ANIO = 3;
    public static final int ASIGNATURAS_PARA_BAJO_RENDIMIENTO = 3;
    public static final double CAIDA_SIGNIFICATIVA = 1.0;

    public static final String RENDIMIENTO_ADECUADO = "Adecuado";
    public static final String RENDIMIENTO_EN_OBSERVACION = "En observación";
    public static final String RENDIMIENTO_BAJO = "Bajo rendimiento";
    public static final String SIN_CALIFICACIONES = "Sin calificaciones";

    public static final String[] ASIGNATURAS = {"Matemática", "Lengua y Literatura", "Ciencias Naturales",
            "Estudios Sociales", "Inglés", "Física", "Química", "Biología", "Historia", "Filosofía",
            "Educación Física", "Educación Cultural y Artística", "Emprendimiento y Gestión"};

    private ArrayList<Calificacion> calificaciones;
    private GestorCasos gestorCasos;

    public GestorRendimiento(GestorCasos gestorCasos) {
        this.calificaciones = new ArrayList<>();
        this.gestorCasos = gestorCasos;
    }

    // ------------------------------------------------------------------
    // RF-13: registrar calificaciones por periodo académico
    // ------------------------------------------------------------------

    /**
     * Registra la nota de una asignatura. Reglas: estudiante activo, año lectivo con formato 2026-2027,
     * periodo de 1 a 3, nota entre 0 y 10 (se guarda con dos decimales truncados) y sin repetir
     * la misma asignatura en el mismo periodo.
     */
    public String registrarCalificacion(Estudiante estudiante, String anioLectivo, int periodo, String asignatura,
                                        double nota, LocalDate fecha) throws PuenteException {
        if (estudiante == null) {
            throw new PuenteException("Seleccione un estudiante.");
        }
        if (!estudiante.estaActivo()) {
            throw new PuenteException("El estudiante no está activo.");
        }
        validarPeriodo(anioLectivo, periodo);
        if (asignatura == null || asignatura.trim().isEmpty()) {
            throw new PuenteException("Ingrese la asignatura.");
        }
        validarNota(nota);
        if (buscarCalificacion(estudiante, anioLectivo, periodo, asignatura) != null) {
            throw new PuenteException("Ya existe una nota de " + asignatura.trim() + " en ese periodo. "
                    + "Use la opción de actualizar.");
        }
        Calificacion c = new Calificacion(estudiante, anioLectivo.trim(), periodo, asignatura.trim(),
                truncarDosDecimales(nota), fecha == null ? LocalDate.now() : fecha);
        calificaciones.add(c);
        String texto = "Nota registrada. Promedio del periodo: "
                + String.format("%.2f", calcularPromedio(estudiante, anioLectivo, periodo)) + ".";
        if (c.estaBajoMinimo()) {
            texto += " La nota es menor a 7,00: se activa una alerta temprana (notificar al representante "
                    + "y aplicar refuerzo pedagógico).";
        }
        return texto;
    }

    // ------------------------------------------------------------------
    // RF-14: consultar, actualizar y eliminar calificaciones
    // ------------------------------------------------------------------

    /** Notas de un estudiante en un año lectivo. Con periodo = 0 devuelve todos los periodos. */
    public ArrayList<Calificacion> buscarCalificaciones(Estudiante estudiante, String anioLectivo, int periodo) {
        ArrayList<Calificacion> resultado = new ArrayList<>();
        for (Calificacion c : calificaciones) {
            if (c.getEstudiante() == estudiante && c.getAnioLectivo().equals(anioLectivo)
                    && (periodo == 0 || c.getPeriodo() == periodo)) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public String actualizarNota(Calificacion calificacion, double nuevaNota) throws PuenteException {
        if (calificacion == null || !calificaciones.contains(calificacion)) {
            throw new PuenteException("Seleccione una calificación registrada.");
        }
        validarNota(nuevaNota);
        calificacion.setNota(truncarDosDecimales(nuevaNota));
        return "Nota de " + calificacion.getAsignatura() + " actualizada. Nuevo promedio del periodo: "
                + String.format("%.2f", calcularPromedio(calificacion.getEstudiante(),
                calificacion.getAnioLectivo(), calificacion.getPeriodo())) + ".";
    }

    /** Elimina una nota mal ingresada (por ejemplo, asignatura equivocada). */
    public String eliminarCalificacion(Calificacion calificacion) throws PuenteException {
        if (calificacion == null || !calificaciones.remove(calificacion)) {
            throw new PuenteException("Seleccione una calificación registrada.");
        }
        return "Se eliminó la nota de " + calificacion.getAsignatura() + ".";
    }

    /** Promedio de las notas del periodo (0 si todavía no hay notas). */
    public double calcularPromedio(Estudiante estudiante, String anioLectivo, int periodo) {
        ArrayList<Calificacion> notas = buscarCalificaciones(estudiante, anioLectivo, periodo);
        if (notas.isEmpty()) {
            return 0;
        }
        double suma = 0;
        for (Calificacion c : notas) {
            suma += c.getNota();
        }
        return truncarDosDecimales(suma / notas.size());
    }

    // ------------------------------------------------------------------
    // RF-15: detectar bajo rendimiento académico [Regla de negocio]
    // ------------------------------------------------------------------

    public int contarAsignaturasBajoMinimo(Estudiante estudiante, String anioLectivo, int periodo) {
        int total = 0;
        for (Calificacion c : buscarCalificaciones(estudiante, anioLectivo, periodo)) {
            if (c.estaBajoMinimo()) {
                total++;
            }
        }
        return total;
    }

    /** Cuánto bajó el promedio respecto del periodo anterior (0 si no bajó o no hay periodo anterior). */
    public double calcularCaida(Estudiante estudiante, String anioLectivo, int periodo) {
        if (periodo <= 1 || buscarCalificaciones(estudiante, anioLectivo, periodo - 1).isEmpty()) {
            return 0;
        }
        double caida = calcularPromedio(estudiante, anioLectivo, periodo - 1)
                - calcularPromedio(estudiante, anioLectivo, periodo);
        return caida > 0 ? truncarDosDecimales(caida) : 0;
    }

    /**
     * Bajo rendimiento: promedio menor a 7,00 o 3 o más asignaturas bajo 7,00.
     * En observación: 1 o 2 asignaturas bajo 7,00, o el promedio cayó 1 punto o más.
     * Adecuado: ninguno de los casos anteriores.
     */
    public String clasificarRendimiento(Estudiante estudiante, String anioLectivo, int periodo) {
        if (buscarCalificaciones(estudiante, anioLectivo, periodo).isEmpty()) {
            return SIN_CALIFICACIONES;
        }
        double promedio = calcularPromedio(estudiante, anioLectivo, periodo);
        int bajoMinimo = contarAsignaturasBajoMinimo(estudiante, anioLectivo, periodo);
        if (promedio < Calificacion.NOTA_MINIMA_APROBACION || bajoMinimo >= ASIGNATURAS_PARA_BAJO_RENDIMIENTO) {
            return RENDIMIENTO_BAJO;
        }
        if (bajoMinimo > 0 || calcularCaida(estudiante, anioLectivo, periodo) >= CAIDA_SIGNIFICATIVA) {
            return RENDIMIENTO_EN_OBSERVACION;
        }
        return RENDIMIENTO_ADECUADO;
    }

    /**
     * Lista de estudiantes activos (de un curso o de todos) con bajo rendimiento o en observación.
     * Primero van los de bajo rendimiento y, dentro de cada grupo, el promedio más bajo.
     */
    public ArrayList<Estudiante> detectarBajoRendimiento(ArrayList<Estudiante> estudiantes, String curso,
                                                         String anioLectivo, int periodo) {
        ArrayList<Estudiante> resultado = new ArrayList<>();
        String cursoBuscado = curso == null ? "" : curso.trim().toLowerCase();
        for (Estudiante e : estudiantes) {
            boolean coincideCurso = cursoBuscado.isEmpty() || e.getCursoCompleto().toLowerCase().contains(cursoBuscado);
            String clasificacion = clasificarRendimiento(e, anioLectivo, periodo);
            boolean requiereAtencion = RENDIMIENTO_BAJO.equals(clasificacion)
                    || RENDIMIENTO_EN_OBSERVACION.equals(clasificacion);
            if (e.estaActivo() && coincideCurso && requiereAtencion) {
                int posicion = 0;
                while (posicion < resultado.size() && !vaAntes(e, resultado.get(posicion), anioLectivo, periodo)) {
                    posicion++;
                }
                resultado.add(posicion, e);
            }
        }
        return resultado;
    }

    /** Acciones sugeridas para el DECE según la clasificación (se conecta con los Módulos 2 y 3). */
    public String generarRecomendacion(Estudiante estudiante, String anioLectivo, int periodo) {
        String clasificacion = clasificarRendimiento(estudiante, anioLectivo, periodo);
        if (SIN_CALIFICACIONES.equals(clasificacion) || RENDIMIENTO_ADECUADO.equals(clasificacion)) {
            return "No se requieren acciones.";
        }
        String texto = "";
        String asignaturas = "";
        for (Calificacion c : buscarCalificaciones(estudiante, anioLectivo, periodo)) {
            if (c.estaBajoMinimo()) {
                asignaturas += (asignaturas.isEmpty() ? "" : ", ") + c.getAsignatura();
            }
        }
        if (!asignaturas.isEmpty()) {
            texto += "Notificar al representante y aplicar refuerzo pedagógico en: " + asignaturas + ". ";
        }
        if (calcularCaida(estudiante, anioLectivo, periodo) >= CAIDA_SIGNIFICATIVA) {
            texto += "El promedio bajó " + String.format("%.2f", calcularCaida(estudiante, anioLectivo, periodo))
                    + " puntos respecto del periodo anterior. ";
        }
        if (RENDIMIENTO_BAJO.equals(clasificacion)) {
            texto += "Considerar el programa de tutorías de refuerzo (Módulo 2). ";
            if (gestorCasos != null && gestorCasos.buscarCasoAbierto(estudiante) == null) {
                texto += "Se sugiere abrir un caso de seguimiento con el motivo «Bajo rendimiento académico» (Módulo 3).";
            } else {
                texto += "Registrar esta situación en su caso de seguimiento abierto (Módulo 3).";
            }
        }
        return texto.trim();
    }

    // ------------------------------------------------------------------
    // Métodos de apoyo
    // ------------------------------------------------------------------

    private Calificacion buscarCalificacion(Estudiante estudiante, String anioLectivo, int periodo, String asignatura) {
        for (Calificacion c : buscarCalificaciones(estudiante, anioLectivo, periodo)) {
            if (c.getAsignatura().equalsIgnoreCase(asignatura.trim())) {
                return c;
            }
        }
        return null;
    }

    private boolean vaAntes(Estudiante a, Estudiante b, String anioLectivo, int periodo) {
        boolean aBajo = RENDIMIENTO_BAJO.equals(clasificarRendimiento(a, anioLectivo, periodo));
        boolean bBajo = RENDIMIENTO_BAJO.equals(clasificarRendimiento(b, anioLectivo, periodo));
        if (aBajo != bBajo) {
            return aBajo;
        }
        return calcularPromedio(a, anioLectivo, periodo) < calcularPromedio(b, anioLectivo, periodo);
    }

    private void validarPeriodo(String anioLectivo, int periodo) throws PuenteException {
        if (anioLectivo == null || !anioLectivo.trim().matches("\\d{4}-\\d{4}")) {
            throw new PuenteException("El año lectivo debe tener el formato 2026-2027.");
        }
        if (periodo < 1 || periodo > PERIODOS_POR_ANIO) {
            throw new PuenteException("El periodo académico debe ser 1, 2 o 3.");
        }
    }

    private void validarNota(double nota) throws PuenteException {
        if (nota < 0 || nota > Calificacion.NOTA_MAXIMA) {
            throw new PuenteException("La nota debe estar entre 0 y 10.");
        }
    }

    // La normativa usa dos decimales sin redondear (truncados)
    private double truncarDosDecimales(double valor) {
        return Math.floor(valor * 100 + 1e-9) / 100.0;
    }

    public ArrayList<Calificacion> getCalificaciones() {
        return calificaciones;
    }

    public GestorCasos getGestorCasos() {
        return gestorCasos;
    }
}
