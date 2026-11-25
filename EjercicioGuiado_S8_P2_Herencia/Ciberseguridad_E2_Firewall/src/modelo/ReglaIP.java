package modelo;

public class ReglaIP extends Regla {
    private String ip;

    public ReglaIP(String nombre, String accion, int prioridad, String ip) {
        super(nombre, accion, prioridad, "IP origen", ip);
        this.ip = ip;
    }

    @Override
    public boolean coincide(String ipOrigen, int puerto, String protocolo) {
        return ip.equals(ipOrigen);
    }

    @Override
    public String mostrarInformacion() {
        return "[ReglaIP] " + super.mostrarInformacion();
    }
}
