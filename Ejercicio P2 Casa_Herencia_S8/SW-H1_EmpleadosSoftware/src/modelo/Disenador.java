package modelo;

public class Disenador extends Empleado {
    private int disenosAprobados;

    public Disenador(String identificacion, String nombre, double salarioBase, int disenosAprobados) {
        super(identificacion, nombre, salarioBase);
        this.disenosAprobados = disenosAprobados;
    }

    // 6% del salario base + 20 por cada diseño aprobado
    @Override
    public double calcularBono() {
        return getSalarioBase() * 6 / 100 + disenosAprobados * 20;
    }

    @Override
    public String mostrarInformacion() {
        return "[Disenador] " + super.mostrarInformacion()
                + " | Disenos aprobados: " + disenosAprobados;
    }
}
