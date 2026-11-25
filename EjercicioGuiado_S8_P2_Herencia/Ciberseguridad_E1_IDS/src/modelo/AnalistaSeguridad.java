package modelo;

import java.util.ArrayList;

// Asociación: el analista revisa eventos que recibe como parámetro,
// pero no los guarda ni le pertenecen
public class AnalistaSeguridad {
    private String nombre;
    private int eventosRevisados;

    public AnalistaSeguridad(String nombre) {
        this.nombre = nombre;
        this.eventosRevisados = 0;
    }

    public int getEventosRevisados() {
        return eventosRevisados;
    }

    // Revisa un evento y marca la acción sugerida según su criticidad
    public String revisarEvento(EventoSeguridad evento) {
        String accion;
        if (evento.getCriticidad() >= 5) {
            accion = "Aislar el equipo afectado y escalar al equipo de respuesta";
        } else if (evento.getCriticidad() >= 4) {
            accion = "Bloquear el origen y revisar los registros del sistema";
        } else if (evento.getCriticidad() >= 3) {
            accion = "Investigar el evento y aumentar el monitoreo";
        } else {
            accion = "Registrar el evento y mantener la vigilancia";
        }
        evento.setAccionSugerida(accion);
        eventosRevisados++;
        return nombre + " reviso " + evento.getCodigo() + " -> " + accion;
    }

    public String revisarEventos(ArrayList<EventoSeguridad> eventos) {
        String resumen = "";
        for (EventoSeguridad e : eventos) {
            resumen += revisarEvento(e) + "\n";
        }
        return resumen;
    }
}
