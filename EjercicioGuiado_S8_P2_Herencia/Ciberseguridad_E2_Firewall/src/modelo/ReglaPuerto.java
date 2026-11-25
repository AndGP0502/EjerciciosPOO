package modelo;

public class ReglaPuerto extends Regla {
    private int puerto;

    public ReglaPuerto(String nombre, String accion, int prioridad, int puerto) {
        super(nombre, accion, prioridad, "Puerto destino", String.valueOf(puerto));
        this.puerto = puerto;
    }

    @Override
    public boolean coincide(String ipOrigen, int puertoTrafico, String protocolo) {
        return puerto == puertoTrafico;
    }

    @Override
    public String mostrarInformacion() {
        return "[ReglaPuerto] " + super.mostrarInformacion();
    }
}
