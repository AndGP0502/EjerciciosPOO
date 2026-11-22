package negocio;

import modelo.Alerta;
import modelo.Estudiante;
import modelo.Inasistencia;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Módulo 4 (Juyani): inasistencias, alertas de riesgo de abandono y reporte de permanencia.
 * Cubre RF-10, RF-11 y RF-12.
 *
 * ESTADO: estructura base. Los atributos y la firma de los métodos ya están definidos según
 * el diagrama UML; falta programar la lógica de cada requerimiento (ver los TODO).
 * Para RF-11 ya se pueden usar gestorProgramas.tieneApoyoVigente(...) y gestorCasos.buscarCasoAbierto(...).
 */
public class GestorAlertas {

    public static final int DIAS_ALERTA_RIESGO_ALTO = 3;
    public static final int DIAS_ALERTA_RIESGO_BAJO = 5;
    public static final int DIAS_SIN_INTERVENCION = 15;

    private ArrayList<Inasistencia> inasistencias;
    private ArrayList<Alerta> alertas;
    private GestorEstudiantes gestorEstudiantes;
    private GestorProgramas gestorProgramas;
    private GestorCasos gestorCasos;

    public GestorAlertas(GestorEstudiantes gestorEstudiantes, GestorProgramas gestorProgramas, GestorCasos gestorCasos) {
        this.inasistencias = new ArrayList<>();
        this.alertas = new ArrayList<>();
        this.gestorEstudiantes = gestorEstudiantes;
        this.gestorProgramas = gestorProgramas;
        this.gestorCasos = gestorCasos;
    }

    /** RF-10: registra una inasistencia y genera alerta al llegar a 3 o 5 días seguidos sin justificar. */
    public String registrarInasistencia(Estudiante estudiante, LocalDate fecha, boolean justificada, String motivo)
            throws PuenteException {
        // TODO Módulo 4 (Juyani): validar, guardar la inasistencia y revisar contarDiasConsecutivos(...)
        throw new PuenteException("RF-10 en desarrollo (Módulo 4).");
    }

    /** RF-10: días consecutivos sin asistir y sin justificación, contando hacia atrás desde la última falta. */
    public int contarDiasConsecutivos(Estudiante estudiante) {
        // TODO Módulo 4 (Juyani)
        return 0;
    }

    /** RF-11: reúne a los estudiantes que requieren atención a una fecha de corte. */
    public ArrayList<Alerta> generarAlertas(LocalDate fechaCorte, String curso, String nivel) {
        // TODO Módulo 4 (Juyani): inasistencias, riesgo Alto/Crítico sin apoyo o sin caso, casos sin intervención
        return new ArrayList<>();
    }

    /** RF-12: arma el reporte de permanencia de un periodo (curso opcional). */
    public String generarReportePermanencia(LocalDate desde, LocalDate hasta, String curso) throws PuenteException {
        // TODO Módulo 4 (Juyani)
        throw new PuenteException("RF-12 en desarrollo (Módulo 4).");
    }

    public ArrayList<Inasistencia> getInasistencias() {
        return inasistencias;
    }

    public ArrayList<Alerta> getAlertas() {
        return alertas;
    }

    public GestorEstudiantes getGestorEstudiantes() {
        return gestorEstudiantes;
    }

    public GestorProgramas getGestorProgramas() {
        return gestorProgramas;
    }

    public GestorCasos getGestorCasos() {
        return gestorCasos;
    }
}
