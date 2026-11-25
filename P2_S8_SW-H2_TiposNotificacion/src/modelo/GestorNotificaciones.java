package modelo;

import java.util.ArrayList;

public class GestorNotificaciones {

    private ArrayList<Notificacion> notificaciones;

    public GestorNotificaciones() {
        notificaciones = new ArrayList<>();
    }

    // Agregar una notificacion existente
    public void agregarNotificacion(Notificacion notificacion) {

        if (notificacion != null) {
            notificaciones.add(notificacion);
        }
    }

    // Mostrar todas las notificaciones
    public String mostrarNotificaciones() {

        String resultado = "";

        for (Notificacion notificacion : notificaciones) {
            resultado += notificacion.generarMensaje() + "\n\n";
        }

        return resultado;
    }
}
