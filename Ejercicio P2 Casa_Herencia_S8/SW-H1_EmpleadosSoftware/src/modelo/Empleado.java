package modelo;

// Clase padre: guarda los datos que comparten todos los empleados
public class Empleado {
    private String identificacion;
    private String nombre;
    private double salarioBase;

    public Empleado(String identificacion, String nombre, double salarioBase) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.salarioBase = salarioBase;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public double getSalarioBase() {
        return salarioBase;
    }

    // Bono general: 5% del salario base. Las clases hijas lo sobrescriben
    public double calcularBono() {
        return salarioBase * 5 / 100;
    }

    public String mostrarInformacion() {
        return "ID: " + identificacion + " | Nombre: " + nombre
                + " | Salario base: " + salarioBase
                + " | Bono: " + calcularBono();
    }
}
