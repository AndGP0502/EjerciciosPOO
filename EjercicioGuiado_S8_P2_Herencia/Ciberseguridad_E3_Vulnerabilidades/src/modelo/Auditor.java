package modelo;

import java.util.ArrayList;

// Asociación: el auditor evalúa equipos que recibe como parámetro,
// pero no los guarda ni le pertenecen
public class Auditor {
    private String nombre;

    public Auditor(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    private String clasificarRiesgo(int riesgo) {
        if (riesgo >= 60) {
            return "CRITICO";
        } else if (riesgo >= 40) {
            return "ALTO";
        } else if (riesgo >= 20) {
            return "MEDIO";
        }
        return "BAJO";
    }

    public String evaluarRiesgoEquipo(Equipo equipo) {
        int riesgo = equipo.calcularRiesgoAcumulado();
        return "Equipo " + equipo.getNombre() + " (" + equipo.getCodigo() + "): riesgo acumulado "
                + riesgo + " -> " + clasificarRiesgo(riesgo);
    }

    // Evalúa el riesgo total de todos los equipos y señala el más expuesto
    public String evaluarRiesgoTotal(ArrayList<Equipo> equipos) {
        int total = 0;
        Equipo masExpuesto = null;
        for (Equipo e : equipos) {
            int riesgo = e.calcularRiesgoAcumulado();
            total += riesgo;
            if (masExpuesto == null || riesgo > masExpuesto.calcularRiesgoAcumulado()) {
                masExpuesto = e;
            }
        }
        if (masExpuesto == null) {
            return "No hay equipos para evaluar.";
        }
        return "Riesgo total de " + equipos.size() + " equipos: " + total
                + " | Equipo mas expuesto: " + masExpuesto.getNombre()
                + " (" + masExpuesto.calcularRiesgoAcumulado() + ")";
    }
}
