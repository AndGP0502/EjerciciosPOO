package modelo;

public class Programador extends Empleado {
    private String lenguajePrincipal;
    private int proyectosEntregados;

    public Programador(String identificacion, String nombre, double salarioBase,
                       String lenguajePrincipal, int proyectosEntregados) {
        super(identificacion, nombre, salarioBase);
        this.lenguajePrincipal = lenguajePrincipal;
        this.proyectosEntregados = proyectosEntregados;
    }

    // 10% del salario base + 100 por cada proyecto entregado
    @Override
    public double calcularBono() {
        return getSalarioBase() * 10 / 100 + proyectosEntregados * 100;
    }

    @Override
    public String mostrarInformacion() {
        return "[Programador] " + super.mostrarInformacion()
                + " | Lenguaje: " + lenguajePrincipal
                + " | Proyectos entregados: " + proyectosEntregados;
    }
}
