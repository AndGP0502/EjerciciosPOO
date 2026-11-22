package modelo;

import java.time.LocalDate;

/**
 * Acción que realiza el DECE dentro de un caso de seguimiento
 * (entrevista, visita al hogar, llamada o derivación).
 */
public class Intervencion {

    public static final String TIPO_ENTREVISTA_ESTUDIANTE = "Entrevista con el estudiante";
    public static final String TIPO_ENTREVISTA_REPRESENTANTE = "Entrevista con el representante";
    public static final String TIPO_VISITA_HOGAR = "Visita al hogar";
    public static final String TIPO_LLAMADA = "Llamada al representante";
    public static final String TIPO_DERIVACION = "Derivación a otra entidad";
    public static final String[] TIPOS = {TIPO_ENTREVISTA_ESTUDIANTE, TIPO_ENTREVISTA_REPRESENTANTE,
            TIPO_VISITA_HOGAR, TIPO_LLAMADA, TIPO_DERIVACION};

    public static final String RESULTADO_EFECTIVA = "Efectiva";
    public static final String RESULTADO_SIN_RESPUESTA = "Sin respuesta";
    public static final String[] RESULTADOS = {RESULTADO_EFECTIVA, RESULTADO_SIN_RESPUESTA};

    private LocalDate fecha;
    private String tipo;
    private String resultado;
    private String observaciones;

    public Intervencion(LocalDate fecha, String tipo, String resultado, String observaciones) {
        this.fecha = fecha;
        this.tipo = tipo;
        this.resultado = resultado;
        this.observaciones = observaciones;
    }

    public boolean esEfectiva() {
        return RESULTADO_EFECTIVA.equals(resultado);
    }

    // Intentos de contacto con la familia: entrevista con el representante, visita o llamada
    public boolean esContactoConRepresentante() {
        return TIPO_ENTREVISTA_REPRESENTANTE.equals(tipo) || TIPO_VISITA_HOGAR.equals(tipo)
                || TIPO_LLAMADA.equals(tipo);
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public String getResultado() {
        return resultado;
    }

    public String getObservaciones() {
        return observaciones;
    }
}
