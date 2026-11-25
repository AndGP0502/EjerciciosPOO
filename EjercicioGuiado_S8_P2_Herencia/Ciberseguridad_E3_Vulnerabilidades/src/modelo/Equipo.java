package modelo;

import java.util.ArrayList;

// Agregación: el equipo guarda vulnerabilidades que se crean por fuera (en Main)
public class Equipo {
    private String codigo;
    private String nombre;
    private ArrayList<Vulnerabilidad> vulnerabilidades;

    public Equipo(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.vulnerabilidades = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void registrarVulnerabilidad(Vulnerabilidad vulnerabilidad) {
        vulnerabilidades.add(vulnerabilidad);
    }

    public int contarVulnerabilidades() {
        return vulnerabilidades.size();
    }

    // Retorna el listado como String (la clase no imprime)
    public String listarVulnerabilidades() {
        String lista = "";
        for (Vulnerabilidad v : vulnerabilidades) {
            lista += v.mostrarInformacion() + "\n";
        }
        return lista;
    }

    public int calcularRiesgoAcumulado() {
        int total = 0;
        for (Vulnerabilidad v : vulnerabilidades) {
            total += v.calcularRiesgo();
        }
        return total;
    }

    public String generarRecomendaciones() {
        String recomendaciones = "";
        for (Vulnerabilidad v : vulnerabilidades) {
            recomendaciones += v.getCodigo() + " -> " + v.generarRecomendacion() + "\n";
        }
        return recomendaciones;
    }
}
