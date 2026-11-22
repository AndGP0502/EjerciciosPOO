package modelo;

/**
 * Situación económica y familiar del estudiante.
 * Sus datos son los factores que se usan para calcular el riesgo de abandono (RF-03).
 */
public class FichaSocioeconomica {

    public static final String ZONA_URBANA = "Urbana";
    public static final String ZONA_RURAL = "Rural";

    private String zona;
    private int miembrosHogar;
    private double ingresoMensualHogar;
    private boolean trabaja;
    private boolean tieneHijosOCuidaFamiliares;
    private boolean repitioAnio;

    public FichaSocioeconomica(String zona, int miembrosHogar, double ingresoMensualHogar, boolean trabaja,
                               boolean tieneHijosOCuidaFamiliares, boolean repitioAnio) {
        this.zona = zona;
        this.miembrosHogar = miembrosHogar;
        this.ingresoMensualHogar = ingresoMensualHogar;
        this.trabaja = trabaja;
        this.tieneHijosOCuidaFamiliares = tieneHijosOCuidaFamiliares;
        this.repitioAnio = repitioAnio;
    }

    // Ingreso del hogar dividido para el número de personas que viven en él
    public double calcularIngresoPorPersona() {
        if (miembrosHogar <= 0) {
            return 0;
        }
        return ingresoMensualHogar / miembrosHogar;
    }

    public boolean esRural() {
        return ZONA_RURAL.equals(zona);
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public int getMiembrosHogar() {
        return miembrosHogar;
    }

    public void setMiembrosHogar(int miembrosHogar) {
        this.miembrosHogar = miembrosHogar;
    }

    public double getIngresoMensualHogar() {
        return ingresoMensualHogar;
    }

    public void setIngresoMensualHogar(double ingresoMensualHogar) {
        this.ingresoMensualHogar = ingresoMensualHogar;
    }

    public boolean isTrabaja() {
        return trabaja;
    }

    public void setTrabaja(boolean trabaja) {
        this.trabaja = trabaja;
    }

    public boolean isTieneHijosOCuidaFamiliares() {
        return tieneHijosOCuidaFamiliares;
    }

    public void setTieneHijosOCuidaFamiliares(boolean tieneHijosOCuidaFamiliares) {
        this.tieneHijosOCuidaFamiliares = tieneHijosOCuidaFamiliares;
    }

    public boolean isRepitioAnio() {
        return repitioAnio;
    }

    public void setRepitioAnio(boolean repitioAnio) {
        this.repitioAnio = repitioAnio;
    }
}
