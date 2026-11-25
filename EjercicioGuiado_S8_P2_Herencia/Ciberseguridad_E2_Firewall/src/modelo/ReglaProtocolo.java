package modelo;

public class ReglaProtocolo extends Regla {
    private String protocolo;

    public ReglaProtocolo(String nombre, String accion, int prioridad, String protocolo) {
        super(nombre, accion, prioridad, "Protocolo", protocolo);
        this.protocolo = protocolo;
    }

    @Override
    public boolean coincide(String ipOrigen, int puerto, String protocoloTrafico) {
        return protocolo.equalsIgnoreCase(protocoloTrafico);
    }

    @Override
    public String mostrarInformacion() {
        return "[ReglaProtocolo] " + super.mostrarInformacion();
    }
}
