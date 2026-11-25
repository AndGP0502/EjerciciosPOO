package modelo;

// Composición: esta clase NO es pública ni tiene constructor público,
// por eso solo puede existir dentro de una Vulnerabilidad
class VectorAtaque {
    private String tipoAcceso;   // Red, Local o Fisico
    private String complejidad;  // Baja, Media o Alta

    VectorAtaque(String tipoAcceso, String complejidad) {
        this.tipoAcceso = tipoAcceso;
        this.complejidad = complejidad;
    }

    // Mientras más fácil es el ataque, mayor es el factor
    int getFactor() {
        if (complejidad.equalsIgnoreCase("Baja")) {
            return 3;
        } else if (complejidad.equalsIgnoreCase("Media")) {
            return 2;
        }
        return 1;
    }

    String describir() {
        return "Acceso " + tipoAcceso + ", complejidad " + complejidad;
    }
}
