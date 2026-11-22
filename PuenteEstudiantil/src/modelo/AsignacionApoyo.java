package modelo;

import java.time.LocalDate;

/**
 * Relación entre un estudiante y un programa de apoyo.
 * Puede estar vigente, en lista de espera, finalizada o cancelada.
 */
public class AsignacionApoyo {

    public static final String ESTADO_VIGENTE = "Vigente";
    public static final String ESTADO_EN_ESPERA = "En espera";
    public static final String ESTADO_FINALIZADA = "Finalizada";
    public static final String ESTADO_CANCELADA = "Cancelada";

    private static int contadorSolicitudes = 0;

    private Estudiante estudiante;
    private ProgramaApoyo programa;
    private LocalDate fechaSolicitud;
    private LocalDate fechaAsignacion;
    private String estado;
    private int ordenSolicitud;

    public AsignacionApoyo(Estudiante estudiante, ProgramaApoyo programa, LocalDate fechaSolicitud, String estado) {
        this.estudiante = estudiante;
        this.programa = programa;
        this.fechaSolicitud = fechaSolicitud;
        this.estado = estado;
        if (ESTADO_VIGENTE.equals(estado)) {
            this.fechaAsignacion = fechaSolicitud;
        }
        contadorSolicitudes++;
        this.ordenSolicitud = contadorSolicitudes;
    }

    // Pasa de la lista de espera a vigente cuando se libera un cupo
    public void activar(LocalDate fecha) {
        this.estado = ESTADO_VIGENTE;
        this.fechaAsignacion = fecha;
    }

    public void finalizar() {
        this.estado = ESTADO_FINALIZADA;
    }

    public void cancelar() {
        this.estado = ESTADO_CANCELADA;
    }

    public boolean estaVigente() {
        return ESTADO_VIGENTE.equals(estado);
    }

    public boolean estaEnEspera() {
        return ESTADO_EN_ESPERA.equals(estado);
    }

    // Regla de la lista de espera: mayor nivel de riesgo primero y, si empatan, quien solicitó antes
    public boolean debeIrAntesDe(AsignacionApoyo otra) {
        int miNivel = Estudiante.valorNivel(estudiante.getNivelRiesgo());
        int otroNivel = Estudiante.valorNivel(otra.getEstudiante().getNivelRiesgo());
        if (miNivel != otroNivel) {
            return miNivel > otroNivel;
        }
        if (!fechaSolicitud.equals(otra.getFechaSolicitud())) {
            return fechaSolicitud.isBefore(otra.getFechaSolicitud());
        }
        return ordenSolicitud < otra.ordenSolicitud;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public ProgramaApoyo getPrograma() {
        return programa;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    public LocalDate getFechaAsignacion() {
        return fechaAsignacion;
    }

    public String getEstado() {
        return estado;
    }
}
