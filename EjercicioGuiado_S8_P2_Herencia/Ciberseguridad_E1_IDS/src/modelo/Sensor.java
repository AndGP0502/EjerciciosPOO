package modelo;

import java.util.ArrayList;

// Agregación: el sensor guarda eventos que se crean por fuera (en Main)
public class Sensor {
    private String ubicacion;
    private ArrayList<EventoSeguridad> eventos;

    public Sensor(String ubicacion) {
        this.ubicacion = ubicacion;
        this.eventos = new ArrayList<>();
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void registrarEvento(EventoSeguridad evento) {
        eventos.add(evento);
    }

    public ArrayList<EventoSeguridad> getEventos() {
        return eventos;
    }

    public int contarEventos() {
        return eventos.size();
    }

    // Retorna todos los detalles como String (la clase no imprime)
    public String mostrarEventos() {
        String detalle = "";
        for (EventoSeguridad e : eventos) {
            detalle += e.mostrarInformacion() + "\n";
        }
        return detalle;
    }

    // Retorna los eventos cuya criticidad es igual o mayor a la indicada
    public ArrayList<EventoSeguridad> filtrarPorCriticidad(int criticidadMinima) {
        ArrayList<EventoSeguridad> filtrados = new ArrayList<>();
        for (EventoSeguridad e : eventos) {
            if (e.getCriticidad() >= criticidadMinima) {
                filtrados.add(e);
            }
        }
        return filtrados;
    }
}
