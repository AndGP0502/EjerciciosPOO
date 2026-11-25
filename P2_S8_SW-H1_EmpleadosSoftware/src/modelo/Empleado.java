package modelo;

public class Empleado {

    private String identificacion;
    private String nombre;
    private double salarioBase;

    public Empleado(String identificacion, String nombre,
                    double salarioBase) {

        this.identificacion = identificacion;
        this.nombre = nombre;

        if (salarioBase >= 0) {
            this.salarioBase = salarioBase;
        } else {
            this.salarioBase = 0;
        }
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

    public double calcularBono() {
        return 0;
    }

    public String mostrarInformacion() {
        return "ID: " + identificacion +
                " | Nombre: " + nombre +
                " | Salario base: $" + salarioBase;
    }
}
