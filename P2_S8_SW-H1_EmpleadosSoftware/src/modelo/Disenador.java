package modelo;

public class Disenador extends Empleado {

    public Disenador(String identificacion, String nombre,
                     double salarioBase) {

        super(identificacion, nombre, salarioBase);
    }

    @Override
    public double calcularBono() {
        return getSalarioBase() * 0.10;
    }

    @Override
    public String mostrarInformacion() {
        return super.mostrarInformacion() +
                " | Cargo: Disenador" +
                " | Bono: $" + calcularBono();
    }
}

