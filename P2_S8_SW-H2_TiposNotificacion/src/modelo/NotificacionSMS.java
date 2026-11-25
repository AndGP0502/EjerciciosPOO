package modelo;

public class NotificacionSMS extends Notificacion {

    private String operador;

    public NotificacionSMS(String destinatario,
                           String mensaje, String operador) {

        super(destinatario, mensaje);
        this.operador = operador;
    }

    @Override
    public String generarMensaje() {

        return "=== NOTIFICACION SMS ===" +
                "\nTelefono: " + getDestinatario() +
                "\nOperador: " + operador +
                "\nMensaje: " + getMensaje();
    }
}

