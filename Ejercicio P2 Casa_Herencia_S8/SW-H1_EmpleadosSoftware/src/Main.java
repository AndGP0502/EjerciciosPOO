import modelo.*;

public class Main {
    public static void main(String[] args) {
        EmpresaSoftware empresa = new EmpresaSoftware("TechSoft S.A.");

        // Referencias de tipo Empleado que apuntan a objetos de las subclases
        Empleado e1 = new Programador("0102030405", "Carlos Mena", 2000, "Java", 4);
        Empleado e2 = new Tester("0203040506", "Lucia Vera", 1500, 30);
        Empleado e3 = new Disenador("0304050607", "Diego Paredes", 1600, 5);
        Empleado e4 = new ArquitectoSoftware("0405060708", "Sofia Rojas", 3000, 2);

        empresa.agregarEmpleado(e1);
        empresa.agregarEmpleado(e2);
        empresa.agregarEmpleado(e3);
        empresa.agregarEmpleado(e4);

        System.out.println("===== PRUEBA 1: Bonos diferentes =====");
        System.out.println(e1.getNombre() + " (Programador) -> Bono: " + e1.calcularBono());
        System.out.println(e2.getNombre() + " (Tester) -> Bono: " + e2.calcularBono());
        System.out.println(e3.getNombre() + " (Disenador) -> Bono: " + e3.calcularBono());
        System.out.println(e4.getNombre() + " (Arquitecto) -> Bono: " + e4.calcularBono());

        System.out.println();
        System.out.println("===== PRUEBA 2: Listado polimorfico =====");
        System.out.print(empresa.listarEmpleados());

        System.out.println();
        System.out.println("===== PRUEBA 3: Total de bonos =====");
        System.out.println("Empleados en " + empresa.getNombre() + ": " + empresa.getCantidadEmpleados());
        System.out.println("Total de bonos: " + empresa.calcularTotalBonos());
    }
}
