package modelo;

import java.util.ArrayList;

// Agregación: la empresa guarda empleados que se crean por fuera (en Main)
public class EmpresaSoftware {
    private String nombre;
    private ArrayList<Empleado> empleados;

    public EmpresaSoftware(String nombre) {
        this.nombre = nombre;
        this.empleados = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void agregarEmpleado(Empleado empleado) {
        empleados.add(empleado);
    }

    public int getCantidadEmpleados() {
        return empleados.size();
    }

    // Retorna el listado completo como String (la clase no imprime)
    public String listarEmpleados() {
        String lista = "";
        for (Empleado e : empleados) {
            lista += e.mostrarInformacion() + "\n";
        }
        return lista;
    }

    public double calcularTotalBonos() {
        double total = 0;
        for (Empleado e : empleados) {
            total += e.calcularBono();
        }
        return total;
    }
}
