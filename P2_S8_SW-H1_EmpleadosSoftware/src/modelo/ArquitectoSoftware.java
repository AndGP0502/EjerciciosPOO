package modelo;

public class ArquitectoSoftware extends Empleado {

    public ArquitectoSoftware(String identificacion, String nombre,
                              double salarioBase) {

        super(identificacion, nombre, salarioBase);
    }

    @Override
    public double calcularBono() {
        return getSalarioBase() * 0.25;
    }

    @Override
    public String mostrarInformacion() {
        return super.mostrarInformacion() +
                " | Cargo: Arquitecto de Software" +
                " | Bono: $" + calcularBono();
    }
}

