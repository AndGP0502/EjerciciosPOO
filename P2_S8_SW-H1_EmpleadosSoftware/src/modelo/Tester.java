package modelo;

public class Tester extends Empleado {

    public Tester(String identificacion, String nombre,
                  double salarioBase) {

        super(identificacion, nombre, salarioBase);
    }

    @Override
    public double calcularBono() {
        return getSalarioBase() * 0.15;
    }

    @Override
    public String mostrarInformacion() {
        return super.mostrarInformacion() +
                " | Cargo: Tester" +
                " | Bono: $" + calcularBono();
    }
}
