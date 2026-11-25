package modelo;

import java.util.ArrayList;

// Agregación: el gestor guarda notificaciones creadas por fuera (en Main)
public class GestorNotificaciones {
    private ArrayList<Notificacion> notificaciones;

    public GestorNotificaciones() {
        this.notificaciones = new ArrayList<>();
    }

    public void agregarNotificacion(Notificacion notificacion) {
        notificaciones.add(notificacion);
    }

    public int getCantidadNotificaciones() {
        return notificaciones.size();
    }

    // Retorna todos los mensajes generados como String (la clase no imprime)
    public String listarNotificaciones() {
        String lista = "";
        for (Notificacion n : notificaciones) {
            lista += n.generarMensaje() + "\n";
        }
        return lista;
    }
}
