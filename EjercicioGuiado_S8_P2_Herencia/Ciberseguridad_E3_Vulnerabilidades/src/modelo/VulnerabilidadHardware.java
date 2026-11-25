package modelo;

public class VulnerabilidadHardware extends Vulnerabilidad {
    private String componente;

    public VulnerabilidadHardware(String codigo, String descripcion, int severidad,
                                  String tipoAcceso, String complejidad, String componente) {
        super(codigo, descripcion, severidad, tipoAcceso, complejidad);
        this.componente = componente;
    }

    // Corregir hardware es más difícil que un parche de software: suma 10 puntos
    @Override
    public int calcularRiesgo() {
        return super.calcularRiesgo() + 10;
    }

    @Override
    public String generarRecomendacion() {
        return "Actualizar el firmware o reemplazar el componente: " + componente + ".";
    }

    @Override
    public String mostrarInformacion() {
        return "[Hardware] " + super.mostrarInformacion() + " | Componente: " + componente;
    }
}
