package modelo;

public class EventoAcceso extends EventoSeguridad {
    private String usuario;
    private int intentosFallidos;

    public EventoAcceso(String codigo, String descripcion, int criticidad,
                        String usuario, int intentosFallidos) {
        super(codigo, descripcion, criticidad);
        this.usuario = usuario;
        this.intentosFallidos = intentosFallidos;
    }

    @Override
    public String obtenerDetalle() {
        return "Tipo: Acceso | Usuario: " + usuario + " | Intentos fallidos: " + intentosFallidos;
    }
}
