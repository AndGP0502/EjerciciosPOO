package modelo;

public class EventoRed extends EventoSeguridad {
    private PaqueteInspeccionado paquete;

    public EventoRed(String codigo, String descripcion, int criticidad,
                     String ipOrigen, String ipDestino, int puerto, int tamanoBytes) {
        super(codigo, descripcion, criticidad);
        // El paquete se crea aquí adentro: no existe fuera del evento (composición)
        this.paquete = new PaqueteInspeccionado(ipOrigen, ipDestino, puerto, tamanoBytes);
    }

    @Override
    public String obtenerDetalle() {
        return "Tipo: Red | " + paquete.describir();
    }
}
