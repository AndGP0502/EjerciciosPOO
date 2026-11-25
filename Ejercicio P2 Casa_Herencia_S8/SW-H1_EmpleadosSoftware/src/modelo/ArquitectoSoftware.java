package modelo;

// Desafío: se agrega esta clase sin modificar las demás
public class ArquitectoSoftware extends Empleado {
    private int sistemasDisenados;

    public ArquitectoSoftware(String identificacion, String nombre, double salarioBase, int sistemasDisenados) {
        super(identificacion, nombre, salarioBase);
        this.sistemasDisenados = sistemasDisenados;
    }

    // 15% del salario base + 300 por cada sistema diseñado
    @Override
    public double calcularBono() {
        return getSalarioBase() * 15 / 100 + sistemasDisenados * 300;
    }

    @Override
    public String mostrarInformacion() {
        return "[Arquitecto] " + super.mostrarInformacion()
                + " | Sistemas disenados: " + sistemasDisenados;
    }
}
