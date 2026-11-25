package modelo;

public class VulnerabilidadSoftware extends Vulnerabilidad {
    private String versionAfectada;
    private boolean parcheDisponible;

    public VulnerabilidadSoftware(String codigo, String descripcion, int severidad,
                                  String tipoAcceso, String complejidad,
                                  String versionAfectada, boolean parcheDisponible) {
        super(codigo, descripcion, severidad, tipoAcceso, complejidad);
        this.versionAfectada = versionAfectada;
        this.parcheDisponible = parcheDisponible;
    }

    // Si no existe parche, el riesgo aumenta 5 puntos
    @Override
    public int calcularRiesgo() {
        int riesgo = super.calcularRiesgo();
        if (!parcheDisponible) {
            riesgo += 5;
        }
        return riesgo;
    }

    @Override
    public String generarRecomendacion() {
        if (parcheDisponible) {
            return "Aplicar el parche disponible para la version " + versionAfectada + ".";
        }
        return "No hay parche: restringir el acceso y monitorear hasta que exista una correccion.";
    }

    @Override
    public String mostrarInformacion() {
        return "[Software] " + super.mostrarInformacion()
                + " | Version: " + versionAfectada
                + " | Parche: " + (parcheDisponible ? "Si" : "No");
    }
}
