package modelo;

public class NotificacionEmail extends Notificacion {
    private String asunto;

    public NotificacionEmail(String destinatario, String mensaje, String asunto) {
        super(destinatario, mensaje);
        this.asunto = asunto;
    }

    @Override
    public String generarMensaje() {
        return "[EMAIL] " + super.generarMensaje() + " | Asunto: " + asunto;
    }
}
