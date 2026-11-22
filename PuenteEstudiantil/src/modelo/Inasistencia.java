package modelo;

import java.time.LocalDate;

/**
 * Día en que un estudiante no asistió al colegio (Módulo 4).
 */
public class Inasistencia {

    private Estudiante estudiante;
    private LocalDate fecha;
    private boolean justificada;
    private String motivo;

    public Inasistencia(Estudiante estudiante, LocalDate fecha, boolean justificada, String motivo) {
        this.estudiante = estudiante;
        this.fecha = fecha;
        this.justificada = justificada;
        this.motivo = motivo;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public boolean isJustificada() {
        return justificada;
    }

    public void setJustificada(boolean justificada) {
        this.justificada = justificada;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
