package modelo;

import java.time.LocalDate;
import java.time.Period;

/**
 * Estudiante de un colegio fiscal al que el DECE da seguimiento.
 * Guarda sus datos personales, su ficha socioeconómica, su representante
 * y el resultado del cálculo del riesgo de abandono.
 */
public class Estudiante extends Persona {

    public static final String ESTADO_ACTIVO = "Activo";
    public static final String ESTADO_INACTIVO = "Inactivo";

    public static final String NIVEL_BAJO = "Bajo";
    public static final String NIVEL_MEDIO = "Medio";
    public static final String NIVEL_ALTO = "Alto";
    public static final String NIVEL_CRITICO = "Crítico";
    public static final String[] NIVELES = {NIVEL_BAJO, NIVEL_MEDIO, NIVEL_ALTO, NIVEL_CRITICO};

    private LocalDate fechaNacimiento;
    private String curso;
    private String paralelo;
    private String direccion;
    private String sector;
    private String observaciones;
    private Representante representante;
    private FichaSocioeconomica ficha;

    private String estado;
    private LocalDate fechaBaja;
    private String motivoBaja;

    private int puntajeRiesgo;
    private String nivelRiesgo;
    private String detalleRiesgo;

    public Estudiante(String cedula, String nombres, String apellidos, String telefono, LocalDate fechaNacimiento,
                      String curso, String paralelo, String direccion, String sector, String observaciones,
                      Representante representante, FichaSocioeconomica ficha) {
        super(cedula, nombres, apellidos, telefono);
        this.fechaNacimiento = fechaNacimiento;
        this.curso = curso;
        this.paralelo = paralelo;
        this.direccion = direccion;
        this.sector = sector;
        this.observaciones = observaciones;
        this.representante = representante;
        this.ficha = ficha;
        this.estado = ESTADO_ACTIVO;
        this.puntajeRiesgo = 0;
        this.nivelRiesgo = NIVEL_BAJO;
        this.detalleRiesgo = "Sin calcular";
    }

    // Edad en años cumplidos a una fecha dada
    public int calcularEdad(LocalDate fechaReferencia) {
        return Period.between(fechaNacimiento, fechaReferencia).getYears();
    }

    // Sobrecarga: edad a la fecha de hoy
    public int calcularEdad() {
        return calcularEdad(LocalDate.now());
    }

    public boolean estaActivo() {
        return ESTADO_ACTIVO.equals(estado);
    }

    // Baja lógica: el estudiante no se elimina, solo pasa a Inactivo (RF-02)
    public void darDeBaja(String motivo, LocalDate fecha) {
        this.estado = ESTADO_INACTIVO;
        this.motivoBaja = motivo;
        this.fechaBaja = fecha;
    }

    public void actualizarRiesgo(int puntaje, String nivel, String detalle) {
        this.puntajeRiesgo = puntaje;
        this.nivelRiesgo = nivel;
        this.detalleRiesgo = detalle;
    }

    public boolean tieneRiesgoAltoOCritico() {
        return valorNivel(nivelRiesgo) >= valorNivel(NIVEL_ALTO);
    }

    public String getCursoCompleto() {
        return curso + " " + paralelo;
    }

    // Convierte el nivel en número para poder compararlos (Bajo = 1 ... Crítico = 4)
    public static int valorNivel(String nivel) {
        if (NIVEL_BAJO.equals(nivel)) {
            return 1;
        } else if (NIVEL_MEDIO.equals(nivel)) {
            return 2;
        } else if (NIVEL_ALTO.equals(nivel)) {
            return 3;
        } else if (NIVEL_CRITICO.equals(nivel)) {
            return 4;
        }
        return 0;
    }

    @Override
    public String mostrarDatos() {
        return super.mostrarDatos() + "\nCurso: " + getCursoCompleto() + "\nEstado: " + estado
                + "\nRiesgo: " + nivelRiesgo + " (" + puntajeRiesgo + " puntos)";
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getParalelo() {
        return paralelo;
    }

    public void setParalelo(String paralelo) {
        this.paralelo = paralelo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Representante getRepresentante() {
        return representante;
    }

    public void setRepresentante(Representante representante) {
        this.representante = representante;
    }

    public FichaSocioeconomica getFicha() {
        return ficha;
    }

    public void setFicha(FichaSocioeconomica ficha) {
        this.ficha = ficha;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    public String getMotivoBaja() {
        return motivoBaja;
    }

    public int getPuntajeRiesgo() {
        return puntajeRiesgo;
    }

    public String getNivelRiesgo() {
        return nivelRiesgo;
    }

    public String getDetalleRiesgo() {
        return detalleRiesgo;
    }
}
