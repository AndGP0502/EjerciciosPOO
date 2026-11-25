package modelo;

public class Programador extends Empleado {

    public Programador(String identificacion, String nombre,
                       double salarioBase) {

        super(identificacion, nombre, salarioBase);
    }

    @Override
    public double calcularBono() {
        return getSalarioBase() * 0.20;
    }

    @Override
    public String mostrarInformacion() {
        return super.mostrarInformacion() +
                " | Cargo: Programador" +
                " | Bono: $" + calcularBono();
    }
}

