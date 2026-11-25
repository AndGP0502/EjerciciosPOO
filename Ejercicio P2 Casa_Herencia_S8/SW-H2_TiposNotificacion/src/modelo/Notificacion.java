package modelo;

// Clase padre: datos comunes de cualquier notificación
public class Notificacion {
    private String destinatario;
    private String mensaje;

    public Notificacion(String destinatario, String mensaje) {
        this.destinatario = destinatario;
        this.mensaje = mensaje;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getMensaje() {
        return mensaje;
    }

    // Mensaje general. Cada tipo de notificación lo sobrescribe
    public String generarMensaje() {
        return "Para: " + destinatario + " | Mensaje: " + mensaje;
    }
}
