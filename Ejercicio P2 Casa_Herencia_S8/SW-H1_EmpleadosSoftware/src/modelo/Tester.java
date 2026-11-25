package modelo;

public class Tester extends Empleado {
    private int erroresDetectados;

    public Tester(String identificacion, String nombre, double salarioBase, int erroresDetectados) {
        super(identificacion, nombre, salarioBase);
        this.erroresDetectados = erroresDetectados;
    }

    // 8% del salario base + 5 por cada error detectado
    @Override
    public double calcularBono() {
        return getSalarioBase() * 8 / 100 + erroresDetectados * 5;
    }

    @Override
    public String mostrarInformacion() {
        return "[Tester] " + super.mostrarInformacion()
                + " | Errores detectados: " + erroresDetectados;
    }
}
