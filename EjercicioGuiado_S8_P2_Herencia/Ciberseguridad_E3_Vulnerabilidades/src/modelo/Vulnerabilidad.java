package modelo;

// Clase base de las vulnerabilidades
public class Vulnerabilidad {
    private String codigo;
    private String descripcion;
    private int severidad; // de 1 a 10
    private VectorAtaque vector;

    public Vulnerabilidad(String codigo, String descripcion, int severidad,
                          String tipoAcceso, String complejidad) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.severidad = severidad;
        // El vector se crea aquí adentro: no existe fuera de la vulnerabilidad (composición)
        this.vector = new VectorAtaque(tipoAcceso, complejidad);
    }

    public String getCodigo() {
        return codigo;
    }

    // Riesgo base: severidad multiplicada por la facilidad del ataque
    public int calcularRiesgo() {
        return severidad * vector.getFactor();
    }

    public String generarRecomendacion() {
        return "Revisar la vulnerabilidad y definir un plan de correccion.";
    }

    public String mostrarInformacion() {
        return "[" + codigo + "] " + descripcion
                + " | Severidad: " + severidad + "/10"
                + " | Vector: " + vector.describir()
                + " | Riesgo: " + calcularRiesgo();
    }
}
