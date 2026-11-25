package modelo;

// Clase base abstracta: no existe un evento "genérico",
// siempre es de red, de acceso o de malware
public abstract class EventoSeguridad {
    private String codigo;
    private String descripcion;
    private int criticidad; // de 1 (baja) a 5 (crítica)
    private String accionSugerida;

    public EventoSeguridad(String codigo, String descripcion, int criticidad) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.criticidad = criticidad;
        this.accionSugerida = "Sin revisar";
    }

    public String getCodigo() {
        return codigo;
    }

    public int getCriticidad() {
        return criticidad;
    }

    public String getAccionSugerida() {
        return accionSugerida;
    }

    public void setAccionSugerida(String accionSugerida) {
        this.accionSugerida = accionSugerida;
    }

    // Cada tipo de evento aporta su propio detalle
    public abstract String obtenerDetalle();

    public String mostrarInformacion() {
        return "[" + codigo + "] " + descripcion
                + " | Criticidad: " + criticidad + "/5"
                + " | " + obtenerDetalle()
                + " | Accion sugerida: " + accionSugerida;
    }
}
