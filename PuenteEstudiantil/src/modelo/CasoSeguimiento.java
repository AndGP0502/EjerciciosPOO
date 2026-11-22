package modelo;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Caso que abre el DECE para acompañar a un estudiante en riesgo.
 * Guarda el profesional responsable, el estado y el historial de intervenciones.
 */
public class CasoSeguimiento {

    public static final String ESTADO_ABIERTO = "Abierto";
    public static final String ESTADO_EN_SEGUIMIENTO = "En seguimiento";
    public static final String ESTADO_REQUIERE_DERIVACION = "Requiere derivación";
    public static final String ESTADO_DERIVADO = "Derivado";
    public static final String ESTADO_RESUELTO = "Resuelto";
    public static final String ESTADO_ABANDONO = "Abandono confirmado";
    public static final String[] ESTADOS = {ESTADO_ABIERTO, ESTADO_EN_SEGUIMIENTO, ESTADO_REQUIERE_DERIVACION,
            ESTADO_DERIVADO, ESTADO_RESUELTO, ESTADO_ABANDONO};

    private int numero;
    private Estudiante estudiante;
    private ProfesionalDece profesional;
    private String motivoDeteccion;
    private LocalDate fechaApertura;
    private String observaciones;
    private String justificacion;
    private String estado;
    private String observacionFinal;
    private LocalDate fechaCierre;
    private ArrayList<Intervencion> intervenciones;

    public CasoSeguimiento(int numero, Estudiante estudiante, ProfesionalDece profesional, String motivoDeteccion,
                           LocalDate fechaApertura, String observaciones, String justificacion) {
        this.numero = numero;
        this.estudiante = estudiante;
        this.profesional = profesional;
        this.motivoDeteccion = motivoDeteccion;
        this.fechaApertura = fechaApertura;
        this.observaciones = observaciones;
        this.justificacion = justificacion;
        this.estado = ESTADO_ABIERTO;
        this.observacionFinal = "";
        this.intervenciones = new ArrayList<>();
    }

    public void agregarIntervencion(Intervencion intervencion) {
        intervenciones.add(intervencion);
    }

    // Un caso sigue abierto mientras no esté Resuelto ni con Abandono confirmado
    public boolean estaAbierto() {
        return !ESTADO_RESUELTO.equals(estado) && !ESTADO_ABANDONO.equals(estado);
    }

    public boolean tieneIntervencionEfectiva() {
        for (Intervencion i : intervenciones) {
            if (i.esEfectiva()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Cuenta los intentos seguidos de contacto con el representante que quedaron sin respuesta.
     * Si un contacto es efectivo, el contador vuelve a cero.
     */
    public int contarIntentosSinRespuesta() {
        int intentos = 0;
        for (Intervencion i : getIntervencionesOrdenadas()) {
            if (i.esContactoConRepresentante()) {
                if (i.esEfectiva()) {
                    intentos = 0;
                } else {
                    intentos++;
                }
            }
        }
        return intentos;
    }

    // Copia de las intervenciones ordenadas de la más antigua a la más reciente
    public ArrayList<Intervencion> getIntervencionesOrdenadas() {
        ArrayList<Intervencion> ordenadas = new ArrayList<>();
        for (Intervencion nueva : intervenciones) {
            int posicion = 0;
            while (posicion < ordenadas.size() && !nueva.getFecha().isBefore(ordenadas.get(posicion).getFecha())) {
                posicion++;
            }
            ordenadas.add(posicion, nueva);
        }
        return ordenadas;
    }

    public LocalDate getFechaUltimaIntervencion() {
        LocalDate ultima = null;
        for (Intervencion i : intervenciones) {
            if (ultima == null || i.getFecha().isAfter(ultima)) {
                ultima = i.getFecha();
            }
        }
        return ultima;
    }

    public void cerrar(String estadoFinal, String observacionFinal, LocalDate fecha) {
        this.estado = estadoFinal;
        this.observacionFinal = observacionFinal;
        this.fechaCierre = fecha;
    }

    public int getNumero() {
        return numero;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public ProfesionalDece getProfesional() {
        return profesional;
    }

    public String getMotivoDeteccion() {
        return motivoDeteccion;
    }

    public LocalDate getFechaApertura() {
        return fechaApertura;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacionFinal() {
        return observacionFinal;
    }

    public LocalDate getFechaCierre() {
        return fechaCierre;
    }

    public ArrayList<Intervencion> getIntervenciones() {
        return intervenciones;
    }
}
