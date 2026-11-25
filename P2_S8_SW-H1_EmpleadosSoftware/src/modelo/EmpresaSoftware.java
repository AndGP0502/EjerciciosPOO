package modelo;

import java.util.ArrayList;

public class EmpresaSoftware {

    private ArrayList<Empleado> empleados;

    public EmpresaSoftware() {
        empleados = new ArrayList<>();
    }

    // Agregar un empleado existente
    public void agregarEmpleado(Empleado empleado) {

        if (empleado != null) {
            empleados.add(empleado);
        }
    }

    // Mostrar los empleados
    public String mostrarEmpleados() {

        String resultado = "";

        for (Empleado empleado : empleados) {
            resultado += empleado.mostrarInformacion() + "\n";
        }

        return resultado;
    }

    // Calcular el total de bonos
    public double calcularTotalBonos() {

        double total = 0;

        for (Empleado empleado : empleados) {
            total += empleado.calcularBono();
        }

        return total;
    }
}


