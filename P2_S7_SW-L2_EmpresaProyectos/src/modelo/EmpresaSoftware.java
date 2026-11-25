
package modelo;

import java.util.ArrayList;

public class EmpresaSoftware {

    private ArrayList<Proyecto> proyectos;

    public EmpresaSoftware() {
        proyectos = new ArrayList<>();
    }

    // Agregar un proyecto existente
    public void agregarProyecto(Proyecto proyecto) {

        if (proyecto != null) {
            proyectos.add(proyecto);
        }
    }

    // Buscar un proyecto por codigo
    public Proyecto buscarProyecto(String codigo) {

        for (Proyecto proyecto : proyectos) {

            if (proyecto.getCodigo().equals(codigo)) {
                return proyecto;
            }
        }

        return null;
    }

    // Eliminar un proyecto por codigo
    public boolean eliminarProyecto(String codigo) {

        Proyecto encontrado = buscarProyecto(codigo);

        if (encontrado != null) {
            proyectos.remove(encontrado);
            return true;
        }

        return false;
    }

    // Calcular presupuesto total
    public double calcularPresupuestoTotal() {

        double total = 0;

        for (Proyecto proyecto : proyectos) {
            total += proyecto.getPresupuesto();
        }

        return total;
    }

    // Mostrar todos los proyectos
    public String mostrarProyectos() {

        String resultado = "";

        for (Proyecto proyecto : proyectos) {
            resultado += proyecto.mostrarInformacion() + "\n";
        }

        return resultado;
    }

    // Desafio adicional
    public Proyecto obtenerProyectoMayorPresupuesto() {

        Proyecto mayor = null;

        for (Proyecto proyecto : proyectos) {

            if (mayor == null ||
                    proyecto.getPresupuesto() > mayor.getPresupuesto()) {

                mayor = proyecto;
            }
        }

        return mayor;
    }
}
