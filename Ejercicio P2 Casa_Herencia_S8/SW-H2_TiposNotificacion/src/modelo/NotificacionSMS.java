package modelo;

public class NotificacionSMS extends Notificacion {
    private String numeroCelular;

    public NotificacionSMS(String destinatario, String mensaje, String numeroCelular) {
        super(destinatario, mensaje);
        this.numeroCelular = numeroCelular;
    }

    @Override
    public String generarMensaje() {
        return "[SMS] " + super.generarMensaje() + " | Celular: " + numeroCelular;
    }
}
