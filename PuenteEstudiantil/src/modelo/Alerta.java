package modelo;

import java.time.LocalDate;

/**
 * Aviso de que un estudiante requiere atención del DECE (Módulos 4 y 5).
 */
public class Alerta {

    public static final String TIPO_INASISTENCIAS = "Inasistencias consecutivas";
    public static final String TIPO_SIN_APOYO = "Riesgo Alto o Crítico sin apoyo";
    public static final String TIPO_SIN_CASO = "Riesgo Alto o Crítico sin caso abierto";
    public static final String TIPO_CASO_SIN_INTERVENCION = "Caso sin intervención en 15 días";
    public static final String TIPO_BAJO_RENDIMIENTO = "Bajo rendimiento académico";

    private Estudiante estudiante;
    private String tipo;
    private LocalDate fechaGeneracion;
    private int diasTranscurridos;
    private String descripcion;

    public Alerta(Estudiante estudiante, String tipo, LocalDate fechaGeneracion, int diasTranscurridos,
                  String descripcion) {
        this.estudiante = estudiante;
        this.tipo = tipo;
        this.fechaGeneracion = fechaGeneracion;
        this.diasTranscurridos = diasTranscurridos;
        this.descripcion = descripcion;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public String getTipo() {
        return tipo;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public int getDiasTranscurridos() {
        return diasTranscurridos;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
