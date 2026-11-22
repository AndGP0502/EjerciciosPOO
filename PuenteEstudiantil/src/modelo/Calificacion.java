package modelo;

import java.time.LocalDate;

/**
 * Nota de un estudiante en una asignatura durante un periodo académico (Módulo 5).
 * La escala es sobre 10 y la nota mínima para aprobar es 7,00.
 */
public class Calificacion {

    public static final double NOTA_MINIMA_APROBACION = 7.0;
    public static final double NOTA_MAXIMA = 10.0;

    private Estudiante estudiante;
    private String anioLectivo;
    private int periodo;
    private String asignatura;
    private double nota;
    private LocalDate fechaRegistro;

    public Calificacion(Estudiante estudiante, String anioLectivo, int periodo, String asignatura, double nota,
                        LocalDate fechaRegistro) {
        this.estudiante = estudiante;
        this.anioLectivo = anioLectivo;
        this.periodo = periodo;
        this.asignatura = asignatura;
        this.nota = nota;
        this.fechaRegistro = fechaRegistro;
    }

    public boolean estaBajoMinimo() {
        return nota < NOTA_MINIMA_APROBACION;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public String getAnioLectivo() {
        return anioLectivo;
    }

    public int getPeriodo() {
        return periodo;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }
}
