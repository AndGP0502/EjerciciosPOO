package modelo;

import java.util.ArrayList;

// Agregación: el firewall guarda reglas que se crean por fuera (en Main)
public class Firewall {
    private String nombre;
    private ArrayList<Regla> reglas;

    public Firewall(String nombre) {
        this.nombre = nombre;
        this.reglas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void agregarRegla(Regla regla) {
        reglas.add(regla);
    }

    public int contarReglas() {
        return reglas.size();
    }

    // Retorna el listado como String (la clase no imprime)
    public String listarReglas() {
        String lista = "";
        for (Regla r : reglas) {
            lista += r.mostrarInformacion() + "\n";
        }
        return lista;
    }

    // Ordena las reglas de mayor a menor prioridad (1 primero) con burbuja
    public void priorizarReglas() {
        for (int i = 0; i < reglas.size() - 1; i++) {
            for (int j = 0; j < reglas.size() - 1 - i; j++) {
                if (reglas.get(j).getPrioridad() > reglas.get(j + 1).getPrioridad()) {
                    Regla temporal = reglas.get(j);
                    reglas.set(j, reglas.get(j + 1));
                    reglas.set(j + 1, temporal);
                }
            }
        }
    }

    // Evalúa un tráfico simulado: decide la regla coincidente de mayor prioridad
    public String evaluarTrafico(String ipOrigen, int puerto, String protocolo) {
        Regla elegida = null;
        for (Regla r : reglas) {
            if (r.coincide(ipOrigen, puerto, protocolo)) {
                if (elegida == null || r.getPrioridad() < elegida.getPrioridad()) {
                    elegida = r;
                }
            }
        }
        String trafico = ipOrigen + ":" + puerto + " (" + protocolo + ")";
        if (elegida == null) {
            return "Trafico " + trafico + " -> BLOQUEAR (sin regla coincidente, politica por defecto)";
        }
        return "Trafico " + trafico + " -> " + elegida.getAccion() + " (regla: " + elegida.getNombre() + ")";
    }

    // Hay conflicto cuando dos reglas revisan lo mismo pero tienen acciones opuestas
    public String detectarConflictos() {
        String conflictos = "";
        for (int i = 0; i < reglas.size(); i++) {
            for (int j = i + 1; j < reglas.size(); j++) {
                Regla a = reglas.get(i);
                Regla b = reglas.get(j);
                if (a.tieneMismaCondicion(b) && !a.getAccion().equals(b.getAccion())) {
                    conflictos += "Conflicto: '" + a.getNombre() + "' (" + a.getAccion() + ") contra '"
                            + b.getNombre() + "' (" + b.getAccion() + ") sobre " + a.describirCondicion() + "\n";
                }
            }
        }
        if (conflictos.equals("")) {
            return "No se detectaron conflictos.\n";
        }
        return conflictos;
    }
}
