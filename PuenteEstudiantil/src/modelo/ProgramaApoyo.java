package modelo;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Programa de apoyo que ofrece el colegio (útiles, refrigerio, transporte, tutorías, etc.),
 * con un número limitado de cupos y una vigencia.
 */
public class ProgramaApoyo {

    public static final String ESTADO_VIGENTE = "Vigente";
    public static final String ESTADO_CERRADO = "Cerrado";
    public static final String[] TIPOS = {"Útiles escolares", "Uniforme", "Refrigerio", "Ayuda de transporte",
            "Tutorías de refuerzo", "Otro"};

    private int codigo;
    private String nombre;
    private String tipo;
    private String descripcion;
    private int cupos;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String nivelMinimo;
    private String estado;
    private ArrayList<AsignacionApoyo> asignaciones;

    public ProgramaApoyo(String nombre, String tipo, String descripcion, int cupos, LocalDate fechaInicio,
                         LocalDate fechaFin, String nivelMinimo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.cupos = cupos;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.nivelMinimo = nivelMinimo;
        this.estado = ESTADO_VIGENTE;
        this.asignaciones = new ArrayList<>();
    }

    public void agregarAsignacion(AsignacionApoyo asignacion) {
        asignaciones.add(asignacion);
    }

    public int contarAsignacionesVigentes() {
        int total = 0;
        for (AsignacionApoyo a : asignaciones) {
            if (a.estaVigente()) {
                total++;
            }
        }
        return total;
    }

    public int getCuposDisponibles() {
        int disponibles = cupos - contarAsignacionesVigentes();
        if (disponibles < 0) {
            return 0;
        }
        return disponibles;
    }

    public boolean estaVigente() {
        return ESTADO_VIGENTE.equals(estado);
    }

    public void cerrar() {
        this.estado = ESTADO_CERRADO;
    }

    // Indica si un rango de fechas se cruza con la vigencia de este programa
    public boolean seCruzaCon(LocalDate inicio, LocalDate fin) {
        return !inicio.isAfter(fechaFin) && !fin.isBefore(fechaInicio);
    }

    /**
     * Devuelve la lista de espera ordenada: primero el nivel de riesgo más alto (Crítico)
     * y, con el mismo nivel, el que lo solicitó antes.
     */
    public ArrayList<AsignacionApoyo> getListaEspera() {
        ArrayList<AsignacionApoyo> espera = new ArrayList<>();
        for (AsignacionApoyo a : asignaciones) {
            if (a.estaEnEspera()) {
                int posicion = 0;
                while (posicion < espera.size() && !a.debeIrAntesDe(espera.get(posicion))) {
                    posicion++;
                }
                espera.add(posicion, a);
            }
        }
        return espera;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCupos() {
        return cupos;
    }

    public void setCupos(int cupos) {
        this.cupos = cupos;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getNivelMinimo() {
        return nivelMinimo;
    }

    public void setNivelMinimo(String nivelMinimo) {
        this.nivelMinimo = nivelMinimo;
    }

    public String getEstado() {
        return estado;
    }

    public ArrayList<AsignacionApoyo> getAsignaciones() {
        return asignaciones;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
