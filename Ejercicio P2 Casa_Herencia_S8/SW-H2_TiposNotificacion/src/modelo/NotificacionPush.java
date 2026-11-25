package modelo;

public class NotificacionPush extends Notificacion {
    private String aplicacion;

    public NotificacionPush(String destinatario, String mensaje, String aplicacion) {
        super(destinatario, mensaje);
        this.aplicacion = aplicacion;
    }

    @Override
    public String generarMensaje() {
        return "[PUSH] " + super.generarMensaje() + " | App: " + aplicacion;
    }
}
