package modelo;

// Desafío: se agrega esta clase sin modificar las demás
public class NotificacionSlack extends Notificacion {
    private String canal;

    public NotificacionSlack(String destinatario, String mensaje, String canal) {
        super(destinatario, mensaje);
        this.canal = canal;
    }

    @Override
    public String generarMensaje() {
        return "[SLACK] " + super.generarMensaje() + " | Canal: " + canal;
    }
}
