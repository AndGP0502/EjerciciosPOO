package modelo;

public class NotificacionSlack extends Notificacion {

    private String canal;

    public NotificacionSlack(String destinatario,
                             String mensaje, String canal) {

        super(destinatario, mensaje);
        this.canal = canal;
    }

    @Override
    public String generarMensaje() {

        return "=== NOTIFICACION SLACK ===" +
                "\nDestinatario: " + getDestinatario() +
                "\nCanal: " + canal +
                "\nMensaje: " + getMensaje();
    }
}
