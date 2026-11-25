package modelo;

import java.util.ArrayList;

public class Sprint {

    private ArrayList<Tarea> tareas;

    public Sprint() {
        tareas = new ArrayList<>();
    }

    // Crear una tarea dentro del sprint
    public void crearTarea(String codigo, String titulo, double horas) {

        if (codigo != null && !codigo.isEmpty()
                && buscarTarea(codigo) == null) {

            Tarea nueva = new Tarea(codigo, titulo, horas);
            tareas.add(nueva);
        }
    }

    // Buscar una tarea por codigo
    public Tarea buscarTarea(String codigo) {

        for (Tarea tarea : tareas) {

            if (tarea.getCodigo().equals(codigo)) {
                return tarea;
            }
        }

        return null;
    }

    // Completar una tarea
    public boolean completarTarea(String codigo) {

        Tarea encontrada = buscarTarea(codigo);

        if (encontrada != null) {
            encontrada.completar();
            return true;
        }

        return false;
    }

    // Contar tareas completadas
    public int contarCompletadas() {

        int contador = 0;

        for (Tarea tarea : tareas) {

            if (tarea.isCompletada()) {
                contador++;
            }
        }

        return contador;
    }

    // Calcular horas pendientes
    public double calcularHorasPendientes() {

        double total = 0;

        for (Tarea tarea : tareas) {

            if (!tarea.isCompletada()) {
                total += tarea.getHoras();
            }
        }

        return total;
    }

    // Mostrar todas las tareas
    public String mostrarTareas() {

        String resultado = "";

        for (Tarea tarea : tareas) {
            resultado += tarea.mostrarInformacion() + "\n";
        }

        return resultado;
    }
}
